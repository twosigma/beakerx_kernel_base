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

import com.twosigma.beakerx.handler.Handler;
import com.twosigma.beakerx.kernel.KernelFunctionality;
import com.twosigma.beakerx.kernel.KernelManager;
import com.twosigma.beakerx.kernel.comm.Buffer;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.kernel.comm.Data;
import com.twosigma.beakerx.kernel.msg.JupyterMessages;
import com.twosigma.beakerx.message.Header;
import com.twosigma.beakerx.message.Message;
import com.twosigma.beakerx.widget.ChangeItem;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeakerXCommRepositoryMock implements CommRepository {
  private Map<String, Comm> comms = new HashMap<>();

  @Override
  public Comm getCommByTargetName(String targetName) {
    return null;
  }

  @Override
  public Comm getOrCreateAutotranslationComm() {
    return new Comm() {
      @Override
      public void addMsgCallbackList(Handler<Message>... handlers) {

      }

      @Override
      public void setData(HashMap<?, ?> data) {

      }

      @Override
      public void open() {

      }

      @Override
      public void open(Buffer buffer) {

      }

      @Override
      public void open(Message parentMessage) {

      }

      @Override
      public void close() {

      }

      @Override
      public String getCommId() {
        return null;
      }

      @Override
      public void send(Buffer buffer, Data data) {

      }

      @Override
      public void send(JupyterMessages type, Data data) {

      }

      @Override
      public void send(JupyterMessages type, Buffer buffer, Data data) {
        KernelFunctionality kernelFunctionality = KernelManager.get();
        kernelFunctionality.publish(Arrays.asList(new Message(new Header(type, "id"))));
      }

      @Override
      public void sendUpdate(Buffer buffer) {

      }

      @Override
      public void sendUpdate(List<ChangeItem> changes) {

      }

      @Override
      public void sendUpdate(List<ChangeItem> changes, Message parent) {

      }

      @Override
      public Message createUpdateMessage(List<ChangeItem> changes, Message parent) {
        return null;
      }

      @Override
      public Message createUpdateMessage(List<ChangeItem> changes, HashMap<String, Object> state) {
        return null;
      }

      @Override
      public Message createOutputContent(Map<String, Serializable> content) {
        return null;
      }

      @Override
      public Message getParentMessage() {
        return null;
      }

      @Override
      public void publish(List<Message> list) {

      }

      @Override
      public Message createMessage(JupyterMessages type, Buffer buffer, Data data, Message parent) {
        return null;
      }

      @Override
      public Message createMessage(JupyterMessages type, Buffer buffer, Data data) {
        return null;
      }

      @Override
      public String getTargetName() {
        return null;
      }

      @Override
      public void handleMsg(Message parentMessage) {

      }

      @Override
      public String getTargetModule() {
        return null;
      }

      @Override
      public void setTargetModule(String targetModule) {

      }

      @Override
      public Data getData() {
        return null;
      }

      @Override
      public Comm createNewComm() {
        return null;
      }

      @Override
      public void sendData(String event, HashMap<String, String> payload) {

      }
    };
  }

  @Override
  public void closeComms() {

  }

  @Override
  public Set<String> getCommHashSet() {
    return this.comms.keySet();
  }

  @Override
  public void addComm(String hash, Comm commObject) {
    this.comms.put(hash, commObject);

  }

  @Override
  public Comm getComm(String hash) {
    return this.comms.get(hash);
  }

  @Override
  public void removeComm(String hash) {
    this.comms.remove(hash);
  }

  @Override
  public boolean isCommPresent(String hash) {
    return this.comms.get(hash) != null;
  }
}
