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

import com.twosigma.beakerx.kernel.msg.JupyterMessages;
import com.twosigma.beakerx.message.Header;
import com.twosigma.beakerx.message.Message;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.beakerx.kernel.comm.Comm.COMM_ID;
import static com.twosigma.beakerx.kernel.comm.Comm.DATA;
import static com.twosigma.beakerx.kernel.comm.Comm.TARGET_MODULE;
import static com.twosigma.beakerx.kernel.comm.Comm.TARGET_NAME;

public class MessageFactoryTestMock {

  public static Message initCloseMessage() {
    Map<String, Serializable> content = new LinkedHashMap<>();
    content.put(DATA, new HashMap<>());
    content.put(COMM_ID, "commId");
    content.put(TARGET_NAME, "targetName");
    content.put(TARGET_MODULE, "targetModule");

    Message message = new Message(initHeader(JupyterMessages.COMM_CLOSE));
    message.setIdentities(Arrays.asList("identities".getBytes()));
    message.setContent(content);
    return message;
  }

  public static Message initOpenMessage() {
    Map<String, Serializable> content = new LinkedHashMap<>();
    content.put(DATA, new HashMap<>());
    content.put(COMM_ID, "commId");
    content.put(TARGET_NAME, "targetName");
    content.put(TARGET_MODULE, "targetModule");

    Message message = new Message(initHeader(JupyterMessages.COMM_OPEN));
    message.setIdentities(Arrays.asList("identities".getBytes()));
    message.setContent(content);
    return message;
  }

  public static Message initCommMessage() {
    Map<String, Serializable> content = new LinkedHashMap<>();
    content.put(DATA, new HashMap<>());
    content.put(COMM_ID, "commId");
    content.put(TARGET_NAME, "targetName");
    content.put(TARGET_MODULE, "targetModule");
    return initCommMessage(content);
  }

  public static Message initCommMessage(Map<String, Serializable> content) {
    Message message = new Message(initHeader(JupyterMessages.COMM_MSG));
    message.setIdentities(Arrays.asList("identities".getBytes()));
    message.setContent(content);
    return message;
  }

  public static Message createExecuteRequestMessage(String code) {
    Message message = executeRequestMessage();
    Map<String, Serializable> content = new LinkedHashMap<>();
    content.put("code", code);
    message.setContent(content);
    return message;
  }

  public static Message initExecuteRequestMessage() {
    Map<String, Serializable> content = new LinkedHashMap<>();
    content.put("allow_stdin", Boolean.TRUE);
    content.put("code", "new Plot() << new Line(x: (0..5), y: [0, 1, 6, 5, 2, 8])");
    content.put("stop_on_error", Boolean.TRUE);
    content.put("user_expressions", new LinkedHashMap<>());
    content.put("silent", Boolean.FALSE);
    content.put("store_history", Boolean.TRUE);

    Message message = executeRequestMessage();
    message.setContent(content);
    return message;
  }

  private static Message executeRequestMessage() {
    Message message = new Message(initHeader(JupyterMessages.EXECUTE_REQUEST));
    message.setIdentities(Arrays.asList("identities".getBytes()));
    message.setParentHeader(null);
    message.setMetadata(new LinkedHashMap<>());
    return message;
  }

  public static Message initInfoMessage() {
    Message message = new Message(initHeader(JupyterMessages.COMM_INFO_REQUEST));
    message.setIdentities(Arrays.asList("identities".getBytes()));
    return message;
  }

  public static Header initHeader(JupyterMessages jupyterMessages) {
    Header header = new Header(jupyterMessages, "sessionId" + jupyterMessages.getName());
    header.setId("messageId");
    header.setUsername("username");
    header.setVersion("5.0");
    return header;
  }
}
