/*
 *  Copyright 2020 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beakerx.kernel.handler;

import com.twosigma.beakerx.handler.KernelHandler;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.kernel.msg.JupyterMessages;
import com.twosigma.beakerx.message.Header;
import com.twosigma.beakerx.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterruptMsgHandler extends KernelHandler<Message> {

  private final static Logger logger = LoggerFactory.getLogger(InterruptMsgHandler.class);

  public InterruptMsgHandler(final KernelFunctionality kernel) {
    super(kernel);
  }

  public void handle(Message message) {
    Message interruptReply = createInterruptReply(JupyterMessages.INTERRUPT_REPLY, message);
    kernel.send(interruptReply);
    kernel.interruptKernel();
    kernel.interruptKernelDone();
    logger.info("Interrupting done");
  }

  private static Message createInterruptReply(JupyterMessages type, Message message) {
    Message reply = new Message(new Header(type, message.getHeader().getSession()));
    reply.setParentHeader(message.getHeader());
    reply.setIdentities(message.getIdentities());
    return reply;
  }
}
