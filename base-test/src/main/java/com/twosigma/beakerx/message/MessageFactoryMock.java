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
package com.twosigma.beakerx.message;

import com.twosigma.beakerx.kernel.msg.JupyterMessages;

import java.util.LinkedHashMap;

public class MessageFactoryMock {

  public static Message createMessage() {
    Header header = new Header(JupyterMessages.COMM_MSG, "session1");
    Message message = new Message(header);
    message.getIdentities().add("identityStr".getBytes());
    message.setParentHeader(header);
    message.setMetadata(new LinkedHashMap<>());
    message.setContent(new LinkedHashMap<>());
    return message;
  }

}
