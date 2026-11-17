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

package com.twosigma.beakerx.chart.actions;

import com.twosigma.beakerx.KernelTest;
import com.twosigma.beakerx.KernelTestFactory;
import com.twosigma.beakerx.chart.Chart;
import com.twosigma.beakerx.chart.xychart.Plot;
import com.twosigma.beakerx.kernel.KernelManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChartObjectManagerTest {

  private ChartObjectManager chartObjectManager;
  protected KernelTest kernel;

  @BeforeEach
  public void setUp() throws Exception {
    chartObjectManager = new ChartObjectManager();
    kernel = KernelTestFactory.getKernel();
    KernelManager.register(kernel);
  }

  @AfterEach
  public void tearDown() throws Exception {
    kernel.exit();
    KernelManager.register(null);
  }

  @Test
  public void registerChart_containsThatChart() {
    Chart chart = new Plot();
    //when
    chartObjectManager.registerChart("id1", chart);
    //then
    Assertions.assertThat(chartObjectManager.getChart("id1")).isEqualTo(chart);
  }

}
