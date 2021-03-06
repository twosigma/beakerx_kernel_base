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
package com.twosigma.beakerx.evaluator;

import com.twosigma.beakerx.jvm.object.ConfigurationFactoryImpl;
import com.twosigma.beakerx.jvm.object.EvaluationObject;
import com.twosigma.beakerx.jvm.object.SimpleEvaluationObject;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.message.Message;

public class SimpleEvaluationObjectFactory implements EvaluationObjectFactory {

  private static SimpleEvaluationObjectFactory INSTANCE = new SimpleEvaluationObjectFactory();

  public static SimpleEvaluationObjectFactory get() {
    return INSTANCE;
  }

  @Override
  public EvaluationObject createSeo(String code, KernelFunctionality kernel, Message message, int executionCount) {
    return new SimpleEvaluationObject(code, new ConfigurationFactoryImpl(kernel, message, executionCount));
  }
}
