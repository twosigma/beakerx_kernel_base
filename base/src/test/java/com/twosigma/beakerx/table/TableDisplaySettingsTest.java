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

import com.twosigma.beakerx.KernelTestFactory;
import com.twosigma.beakerx.ResourceLoaderTest;
import com.twosigma.beakerx.kernel.KernelManager;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.twosigma.beakerx.table.serializer.TableDisplaySerializer.AUTO_LINK_TABLE_LINKS;
import static com.twosigma.beakerx.table.serializer.TableDisplaySerializer.SHOW_PUBLICATION;
import static org.assertj.core.api.Assertions.assertThat;

public class TableDisplaySettingsTest {

  private TableDisplay tableDisplay;

  @Before
  public void setUp() throws Exception {
    KernelManager.register(KernelTestFactory.getKernel());
    tableDisplay = new TableDisplay(ObservableTableDisplayTest.getListOfMapsData());
  }

  @After
  public void tearDown() throws Exception {
    KernelManager.register(null);
  }

  @Test
  public void options() throws Exception {
    //given
    //when
    Map actualObj = serializeTableDisplay();
    //then
    assertThat(actualObj.get(AUTO_LINK_TABLE_LINKS)).isEqualTo(true);
    assertThat(actualObj.get(SHOW_PUBLICATION)).isEqualTo(true);
  }

  @Test
  public void default_options() throws Exception {
    //given
    //when
    try {
      Map actualObj = serializeTableDisplayWithNoSettings();
      //then
      assertThat(actualObj.get(AUTO_LINK_TABLE_LINKS)).isEqualTo(false);
      assertThat(actualObj.get(SHOW_PUBLICATION)).isEqualTo(true);
    } finally {
      Files.deleteIfExists(Paths.get(fileNoExists()));
    }
  }

  @Test
  public void merge_with_new_default_options() throws Exception {
    //given
    try {
      Map actualObj = serializeTableDisplay();
      assertThat(actualObj.get(AUTO_LINK_TABLE_LINKS)).isEqualTo(true);
      assertThat(actualObj.get(SHOW_PUBLICATION)).isEqualTo(true);
      //when
      Map newObj = serializeTableDisplayWithNewDefaults();
      //then
      assertThat(newObj.get("NEW_DEFAULT_OPTION_1")).isEqualTo(123);
      assertThat(newObj.get(AUTO_LINK_TABLE_LINKS)).isEqualTo(true);
      assertThat(newObj.get(SHOW_PUBLICATION)).isEqualTo(true);
    } finally {
      Files.deleteIfExists(Paths.get(fileNoExists()));
    }
  }

  private Map serializeTableDisplayWithNewDefaults() throws Exception {
    String osAppropriatePath =
            ResourceLoaderTest.getOsAppropriatePath(this.getClass().getClassLoader().getResource("beakerx_tabledisplay_mock.json").toURI());
    return new TableDisplayToJson(new BxTableSettings(osAppropriatePath, new BxTableSettingsDefaultsWithNewOption())).toJson(tableDisplay);
  }

  private Map serializeTableDisplayWithNoSettings() throws Exception {
    String path = fileNoExists();
    return new TableDisplayToJson(new BxTableSettings(path, new BxTableSettingsDefaults())).toJson(tableDisplay);
  }

  @NotNull
  private String fileNoExists() throws Exception{
    String osAppropriatePath = ResourceLoaderTest.getOsAppropriatePath(this.getClass().getClassLoader().getResource("").toURI());
    return osAppropriatePath + "beakerx_tabledisplay_no_exist.json";
  }

  private Map serializeTableDisplay() throws Exception {
    String osAppropriatePath =
            ResourceLoaderTest.getOsAppropriatePath(this.getClass().getClassLoader().getResource("beakerx_tabledisplay_mock.json").toURI());
    return new TableDisplayToJson(new BxTableSettings(osAppropriatePath, new BxTableSettingsDefaults())).toJson(tableDisplay);
  }

  class BxTableSettingsDefaultsWithNewOption implements TableSettingsDefaults {

    @Override
    public HashMap<String, Map> getDefault() {
      HashMap<String, Object> btd = new HashMap<>();
      btd.put("version", 1);
      HashMap<String, Object> options = new HashMap<>();
      options.put("auto_link_table_links", false);
      options.put("show_publication", false);
      options.put("NEW_DEFAULT_OPTION_1", 123);
      btd.put("options", options);
      HashMap<String, Map> df = new HashMap<>();
      df.put("beakerx_tabledisplay", btd);
      return df;
    }
  }

}
