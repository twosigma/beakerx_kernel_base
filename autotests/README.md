<!--
    Copyright 2020 TWO SIGMA OPEN SOURCE, LLC

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

## Dependencies

Running the e2e tests from its GitHub source code requires: 
* chrome browser (80 or higher) 
* [conda](https://www.anaconda.com/download/)
* java (1.8 or higher) and Gradle (5.2 or higher)
* [psutil](https://pypi.org/project/psutil/)
* any beakerx kernel

## Run the tests

To run tests use `python run_tests.py` with arguments:
```
optional arguments:
  -h, --help  show this help message and exit
  --app APP   define target application 'notebook' or 'lab'
  --env ENV   define target conda environment where is jupyter notebook or lab
  --tst TST   define tests we need to run; for example: groovy.*
```
If we run `run_tests.py` script without any arguments it'll use default values as there:
```
python run_tests.py --env=beakerx --app=notebook --tst=groovy.*
```

By default, a jupyter notebook server runs locally at 127.0.0.1:8888 and is accessible only from localhost. 
