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
package com.twosigma.beakerx.widget.strings;

import com.twosigma.beakerx.jupyter.SearchMessages;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.message.Message;

import java.util.List;
import java.util.Map;

import static com.twosigma.beakerx.kernel.msg.JupyterMessages.COMM_OPEN;
import static com.twosigma.beakerx.widget.LayoutInfo.IPY_MODEL;
import static com.twosigma.beakerx.widget.LayoutInfo.LAYOUT;
import static com.twosigma.beakerx.widget.TestWidgetUtils.getState;
import static com.twosigma.beakerx.widget.TestWidgetUtils.verifyTypeMsg;
import static com.twosigma.beakerx.widget.WidgetInfo.MODEL_MODULE;
import static com.twosigma.beakerx.widget.WidgetInfo.MODEL_NAME;
import static com.twosigma.beakerx.widget.WidgetInfo.VIEW_MODULE;
import static com.twosigma.beakerx.widget.WidgetInfo.VIEW_NAME;
import static org.assertj.core.api.Assertions.assertThat;

public class TextFieldChecker {
  public static void verifyTextField(
          List<Message> messages,
          String modelNameValue,
          String modelModuleValue,
          String viewNameValue,
          String viewModuleValue
  ) {
    Message widget = SearchMessages.getListWidgetsByViewName(messages, viewNameValue).get(0);
    Message layout = SearchMessages.getLayoutForWidget(messages, widget);

    verifyTypeMsg(widget, COMM_OPEN);
    Map data = getState(widget);
    assertThat(data.get(LAYOUT)).isEqualTo(IPY_MODEL + layout.getContent().get(Comm.COMM_ID));
    assertThat(data.get(MODEL_NAME)).isEqualTo(modelNameValue);
    assertThat(data.get(MODEL_MODULE)).isEqualTo(modelModuleValue);
    assertThat(data.get(VIEW_NAME)).isEqualTo(viewNameValue);
    assertThat(data.get(VIEW_MODULE)).isEqualTo(viewModuleValue);
  }
}
