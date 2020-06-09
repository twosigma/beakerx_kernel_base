#!/usr/bin/env python
#
# Copyright 2020 TWO SIGMA OPEN SOURCE, LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import os
import sys
import argparse
import signal
import subprocess
import platform
import psutil
from subprocess import check_output, CalledProcessError

def kill_processes(name):
    try:
        pidlist = map(int, check_output(['pgrep', '-f', name]).split())
    except CalledProcessError:
        pidlist = []
    for pid in pidlist:
        os.kill(pid, signal.SIGKILL)

here = os.path.abspath(os.path.dirname(__file__))
beakerx_dir = os.path.abspath(os.path.join(here, ".."))
test_dir = here
cur_app = 'notebook'
conda_env = 'beakerx'
tst_templ = 'groovy.*'

argumentList = sys.argv[1:]
parser = argparse.ArgumentParser()
parser.add_argument("--app", help="define target application 'notebook' or 'lab'")
parser.add_argument("--env", help="define target conda environment where is jupyter notebook or lab")
parser.add_argument("--tst", help="define tests we need to run; for example: groovy.* ")
args = parser.parse_args()

if args.app:
    cur_app = args.app

if args.env:
    conda_env = args.env

if args.tst:
    tst_templ = args.tst

# start jupyter notebook
if platform.system() == 'Windows':
    nb_command = 'conda activate %(env)s && jupyter %(app)s --no-browser --notebook-dir="%(dir)s" --NotebookApp.token=""' % { "env": conda_env, "app" : cur_app, "dir" : beakerx_dir }
    beakerx = subprocess.Popen(nb_command, stderr=subprocess.STDOUT, stdout=subprocess.PIPE, shell=True)
else:
    nb_command = 'eval "$(conda shell.bash hook)" && conda activate %(env)s && jupyter %(app)s --no-browser --notebook-dir="%(dir)s" --NotebookApp.token=""' % { "env": conda_env, "app" : cur_app, "dir" : beakerx_dir }
    beakerx = subprocess.Popen(nb_command, shell=True, executable="/bin/bash", preexec_fn=os.setsid, stderr=subprocess.STDOUT, stdout=subprocess.PIPE)
# wait for notebook server to start up
while 1:
    line = beakerx.stdout.readline().decode('utf-8').strip()
    if not line:
        continue
    print(line)
    if 'Use Control-C to stop this server' in line:
        break

# run tests
tst_command = 'gradle cleanTest test -Dcur_app=%(app)s --tests "com.twosigma.beakerx.autotests.%(tst)s"' % { "app" : cur_app, "tst" : tst_templ }
result = subprocess.call(tst_command, shell=True)

# kill unused processes
if platform.system() == 'Windows':
    for proc in psutil.process_iter():
        if proc.name() in ["jupyter-lab.exe", "jupyter.exe", "jupyter-notebook.exe", "chromedriver.exe"]:
            print(proc)
            os.kill(proc.pid, signal.SIGTERM)
else:
    os.killpg(os.getpgid(beakerx.pid), signal.SIGKILL)
    kill_processes('java')
    kill_processes('jupyter')
    kill_processes('webdriver')
