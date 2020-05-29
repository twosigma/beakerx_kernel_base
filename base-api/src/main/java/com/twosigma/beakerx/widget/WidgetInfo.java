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
package com.twosigma.beakerx.widget;

public interface WidgetInfo {
  String APPLICATION_VND_JUPYTER_WIDGET_VIEW_JSON = "application/vnd.jupyter.widget-view+json";
  String MODEL_ID = "model_id";

  String MODEL_MODULE = "_model_module";
  String MODEL_NAME = "_model_name";
  String VIEW_MODULE = "_view_module";
  String VIEW_NAME = "_view_name";
  String VIEW_MODULE_VERSION = "_view_module_version";
  String MODEL_MODULE_VERSION = "_model_module_version";

  String MODEL_MODULE_VALUE = "@jupyter-widgets/controls";
  String VIEW_MODULE_VALUE = "@jupyter-widgets/controls";
  String MODEL_MODULE_VERSION_VALUE = "*";
  String VIEW_MODULE_VERSION_VALUE = "*";

  String VALUE = "value";
  String DISABLED = "disabled";
  String VISIBLE = "visible";
  String DESCRIPTION = "description";
  String MSG_THROTTLE = "msg_throttle";

  String METHOD = "method";
  String DISPLAY = "display_data";

  String INDEX = "index";

}
