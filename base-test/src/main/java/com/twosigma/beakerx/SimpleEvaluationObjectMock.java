/*
 *  Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx;

import com.twosigma.beakerx.jvm.object.ConsoleOutput;
import com.twosigma.beakerx.jvm.object.EvaluationObject;
import com.twosigma.beakerx.jvm.object.EvaluationStatus;
import com.twosigma.beakerx.message.Message;

import java.util.List;
import java.util.Queue;

public class SimpleEvaluationObjectMock implements EvaluationObject {
  private String code;
  private KernelTest.SeoConfigurationFactoryMock seoConfigurationFactoryMock;

  public SimpleEvaluationObjectMock(String code, KernelTest.SeoConfigurationFactoryMock seoConfigurationFactoryMock) {
    this.code = code;
    this.seoConfigurationFactoryMock = seoConfigurationFactoryMock;
  }

  public static SimpleEvaluationObjectMock createSeo(String code) {
    return new SimpleEvaluationObjectMock(code, null);
  }

  @Override
  public boolean isShowResult() {
    return false;
  }

  @Override
  public void started() {

  }

  @Override
  public void finished(Object r) {

  }

  @Override
  public void error(Object r) {

  }

  @Override
  public void update(Object r) {

  }

  @Override
  public String getExpression() {
    return code;
  }

  @Override
  public EvaluationStatus getStatus() {
    return null;
  }

  @Override
  public Object getPayload() {
    return null;
  }

  @Override
  public void structuredUpdate(String message, int progress) {

  }

  @Override
  public void noResult() {

  }

  @Override
  public Message getJupyterMessage() {
    return seoConfigurationFactoryMock.getMessage();
  }

  @Override
  public Queue<ConsoleOutput> getConsoleOutput() {
    return null;
  }

  @Override
  public int getExecutionCount() {
    return 0;
  }

  @Override
  public List<Object> getOutputdata() {
    return null;
  }

  @Override
  public void setOutputHandler() {

  }

  @Override
  public void clrOutputHandler() {

  }
}
