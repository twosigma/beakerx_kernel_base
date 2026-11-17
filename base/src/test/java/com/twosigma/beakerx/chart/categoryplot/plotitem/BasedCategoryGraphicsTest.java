/*
 * Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.beakerx.chart.categoryplot.plotitem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BasedCategoryGraphicsTest {
  protected BasedCategoryGraphics graphics;
  protected List<Object> numberList = Arrays.asList(1, 2, 3);
  protected List<Object> listOfNumberLists = Arrays.asList(
      Arrays.asList(1, 2, 3),
      Arrays.asList(4, 5, 6)
  );
  protected List<Object> listOfNumbersAndListsOfNumbers = Arrays.asList(
      1, 2, Arrays.asList(3, 4, 5)
  );
  protected List<Object> numberAndNonNumberList = Arrays.asList(1, "2", 3);
  protected List<Object> numberAndListWithNonNumbersList = Arrays.asList(1, 2, Arrays.asList(3, "4", 5));

  @BeforeEach
  public void setup() {
    graphics = new BasedCategoryGraphics() {};
  }

  @Test
  public void setBaseNumber() throws Exception {
    graphics.setBase(3);

    assertEquals(3.0, graphics.getBase().doubleValue(), 0.01);
  }

  @Test
  public void setBaseListOfNumbers() throws Exception {
    graphics.setBase(numberList);

    assertEquals(numberList, graphics.getBases());
  }

  @Test
  public void setBaseListOfList() throws Exception {
    graphics.setBase(listOfNumberLists);

    assertEquals(listOfNumberLists, graphics.getBases());
  }

  @Test
  public void setBaseListOfMixed() throws Exception {
    graphics.setBase(listOfNumbersAndListsOfNumbers);

    assertEquals(listOfNumbersAndListsOfNumbers, graphics.getBases());
  }

  @Test
  public void setBaseListOfNumbersAndNonNumbers() throws Exception {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> graphics.setBase(numberAndNonNumberList));
  }

  @Test
  public void setBaseListofListsWithNonNumbers() throws Exception {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> graphics.setBase(numberAndListWithNonNumbersList));
  }
}
