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

package com.twosigma.beakerx.jupyter.handler;

import com.twosigma.beakerx.KernelTest;
import com.twosigma.beakerx.KernelTestFactory;
import com.twosigma.beakerx.kernel.comm.BxComm;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.kernel.msg.JupyterMessages;
import com.twosigma.beakerx.kernel.handler.CommCloseHandler;
import com.twosigma.beakerx.kernel.handler.CommInfoHandler;
import com.twosigma.beakerx.kernel.handler.CommMsgHandler;
import com.twosigma.beakerx.kernel.handler.CommOpenHandler;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import com.twosigma.beakerx.handler.Handler;
import com.twosigma.beakerx.message.Message;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.beakerx.MessageFactoryTestMock.initCloseMessage;
import static com.twosigma.beakerx.MessageFactoryTestMock.initHeader;
import static com.twosigma.beakerx.MessageFactoryTestMock.initInfoMessage;
import static com.twosigma.beakerx.MessageFactoryTestMock.initOpenMessage;
import static com.twosigma.beakerx.kernel.comm.Comm.COMMS;
import static com.twosigma.beakerx.kernel.comm.Comm.COMM_ID;
import static com.twosigma.beakerx.kernel.comm.Comm.TARGET_NAME;

public class JupyterHandlerTest {

  private static KernelTest kernel;
  private CommOpenHandler commOpenHandler;
  private CommCloseHandler commCloseHandler;
  private CommInfoHandler commInfoHandler;
  private CommMsgHandler commMsgHandler;


  public static String initKernelCommMapWithOneComm(KernelTest kernelTest) {
    Message openMessage = initOpenMessage();
    String commId = (String) openMessage.getContent().get(COMM_ID);
    String targetName = (String) openMessage.getContent().get(TARGET_NAME);
    Comm comm = new BxComm(commId, targetName) {
      @Override
      public void handleMsg(Message parentMessage) {
      }
    };
    kernelTest.addComm(commId, comm);
    return commId;
  }

  @BeforeClass
  public static void setUpClass() {
    kernel = KernelTestFactory.getKernel();
  }


  @Before
  public void setUp() {
    commOpenHandler = new CommOpenHandler(kernel) {
      @Override
      public Handler<Message>[] getKernelControlChanelHandlers(String targetName) {
        return (Handler<Message>[]) new Handler<?>[0];
      }
    };
    commCloseHandler = new CommCloseHandler(kernel);
    commInfoHandler = new CommInfoHandler(kernel);
    commMsgHandler = new CommMsgHandler(kernel);
  }

  @After
  public void tearDown() throws Exception {
    kernel.clearMessages();
  }

  @Test
  public void handleOpenCommMessage_shouldAddCommMessageToStorageMap() throws Exception {
    //given
    Message message = initCloseMessage();
    String commId = (String) message.getContent().get(COMM_ID);
    //when
    commOpenHandler.handle(message);
    //then
    Assertions.assertThat(kernel.isCommPresent(commId)).isTrue();
  }

  @Test
  public void handleCloseCommMessage_shouldRemoveCommMessageFromStorageMap() throws Exception {
    //given
    String commId = initKernelCommMapWithOneComm(kernel);
    //when
    commCloseHandler.handle(initCloseMessage());
    //then
    Assertions.assertThat(kernel.isCommPresent(commId)).isFalse();
  }

  @Test
  public void handleOpenThenCloseCommMessages_shouldRemoveCommMessageFromStorageMap()
          throws Exception {
    //given
    Message openMessage = initOpenMessage();
    String commId = (String) openMessage.getContent().get(COMM_ID);
    //when
    commOpenHandler.handle(openMessage);
    commCloseHandler.handle(initCloseMessage());
    //then
    Assertions.assertThat(kernel.isCommPresent(commId)).isFalse();
  }

  @Test
  public void handleInfoCommMessages_replyCommMessageHasCommsInfoContent() throws Exception {
    //given
    initKernelCommMapWithOneComm(kernel);
    //when
    commInfoHandler.handle(initInfoMessage());
    //then
    Assertions.assertThat(kernel.getSentMessages()).isNotEmpty();
    Message sendMessage = kernel.getSentMessages().get(0);
    Assertions.assertThat((Map) sendMessage.getContent().get(COMMS)).isNotEmpty();
  }

  @Test
  public void commInfoHandlerHandleEmptyMessage_dontThrowNullPointerException() throws Exception {
    //given
    Message message = new Message(initHeader(JupyterMessages.COMM_MSG));
    //when
    commInfoHandler.handle(message);
  }

  @Test
  public void commOpenHandlerHandleEmptyMessage_dontThrowNullPointerException() throws Exception {
    //given
    Message message = new Message(initHeader(JupyterMessages.COMM_MSG));
    initMessage(message);
    //wnen
    commOpenHandler.handle(message);
  }

  public static void initMessage(Message message) {
    message.getIdentities().add("identityStr".getBytes());
    message.setParentHeader(message.getHeader());
    message.setMetadata(new LinkedHashMap<>());
    message.setContent(new LinkedHashMap<>());
  }

  @Test
  public void commMsgHandlerHandleEmptyMessage_dontThrowNullPointerException() throws Exception {
    //given
    Message message = new Message(initHeader(JupyterMessages.COMM_MSG));
    initMessage(message);
    //when
    commMsgHandler.handle(message);
  }

  @Test
  public void commCloseHandlerHandleEmptyMessage_dontThrowNullPointerException() throws Exception {
    //given
    Message message = new Message(initHeader(JupyterMessages.COMM_MSG));
    initMessage(message);
    //when
    commCloseHandler.handle(message);
  }
}
