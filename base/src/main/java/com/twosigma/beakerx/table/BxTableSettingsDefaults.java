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
package com.twosigma.beakerx.table;

import java.util.HashMap;
import java.util.Map;

public class BxTableSettingsDefaults implements TableSettingsDefaults {
  @Override
  public Map<String, Map> getDefault() {
    Map<String, Object> btd = new HashMap<>();
    btd.put("version", 1);
    HashMap<String, Object> options = new HashMap<>();
    options.put("auto_link_table_links", false);
    options.put("show_publication", true);
    btd.put("options", options);
    HashMap<String, Map> df = new HashMap<>();
    df.put("beakerx_tabledisplay", btd);
    return df;

  }
}
