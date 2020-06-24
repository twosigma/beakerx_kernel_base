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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.twosigma.beakerx.table.serializer.TableSettings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class BxTableSettings implements TableSettings {

  private String path;
  private TableSettingsDefaults defaults;
  private ObjectMapper objectMapper;

  public BxTableSettings() {
    this(getFile("beakerx_tabledisplay.json"), new BxTableSettingsDefaults());
  }

  public BxTableSettings(String path, TableSettingsDefaults defaults) {
    this.path = path;
    this.defaults = defaults;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Override
  public Map<String, Object> options() {
    Map<String, Map> load = load();
    Map options;
    try {
      options = (Map) load.get("beakerx_tabledisplay").get("options");
    } catch (Exception e) {
      options = (Map)defaults.getDefault().get("beakerx_tabledisplay").get("options");
    }
    return options;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Map> load() {
    String jsonAsString = null;
    try {
      jsonAsString = Files.readString(Paths.get(this.path));
      LinkedHashMap linkedHashMap = fromJson(jsonAsString, LinkedHashMap.class);
      Map merge = mergeSettingsWithDefaults(linkedHashMap);
      return merge;
    } catch (Exception e) {
      Map<String, Map> defaultConfig = defaults.getDefault();
      save(defaultConfig);
      jsonAsString = toJson(defaultConfig);
      return fromJson(jsonAsString, LinkedHashMap.class);
    }
  }

  private Map mergeSettingsWithDefaults(Map<String, Map> current) {
    Map options = (Map) current.get("beakerx_tabledisplay").get("options");
    Map<String, Map> merge = defaults.getDefault();
    Map optionsFromDefault = (Map) merge.get("beakerx_tabledisplay").get("options");
    optionsFromDefault.putAll(options);
    return merge;
  }

  private <T> T fromJson(String json, Class<T> theClass) {
    T result = null;
    try {
      result = objectMapper.readValue(json, theClass);
    } catch (Exception e) {
      // Ignored.
    }

    return result;
  }

  private String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public void save(Map<String, Map> map) {
    try {
      String content = toJson(map);
      Files.write(Paths.get(this.path), content.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getFile(String fileName) {
    String path = (System.getenv("JUPYTER_CONFIG_DIR") != null
            ? System.getenv("JUPYTER_CONFIG_DIR")
            : (System.getProperty("user.home") + File.separator + ".jupyter")) + File.separator + fileName;
    return path;
  }


}
