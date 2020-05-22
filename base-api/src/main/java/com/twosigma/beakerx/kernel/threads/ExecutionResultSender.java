/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beakerx.kernel.threads;

import com.twosigma.beakerx.jvm.object.EvaluationObject;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.kernel.MessageCreatorService;
import com.twosigma.beakerx.kernel.SocketEnum;
import com.twosigma.beakerx.kernel.msg.MessageHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Collections.singletonList;

public class ExecutionResultSender implements ResultSender {

  public static Logger logger = LoggerFactory.getLogger(ExecutionResultSender.class);
  private KernelFunctionality kernel;
  private MessageCreatorService messageCreatorService;

  public ExecutionResultSender(KernelFunctionality kernel, MessageCreatorService messageCreatorService) {
    this.kernel = kernel;
    this.messageCreatorService = messageCreatorService;
  }

  @Override
  public void update(EvaluationObject seo) {
    if (seo != null) {
      List<MessageHolder> message = messageCreatorService.createMessage(seo);
      message.forEach(job -> {
        if (SocketEnum.IOPUB_SOCKET.equals(job.getSocketType())) {
          kernel.publish(singletonList(job.getMessage()));
        } else if (SocketEnum.SHELL_SOCKET.equals(job.getSocketType())) {
          kernel.send(job.getMessage());
        }
      });
    }
  }

  public void exit() {
  }

}