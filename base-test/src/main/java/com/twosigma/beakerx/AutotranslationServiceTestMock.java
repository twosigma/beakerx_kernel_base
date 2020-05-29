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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AutotranslationServiceTestMock implements AutotranslationService {

  private ConcurrentMap<String, String> beakerx = new ConcurrentHashMap();

  @Override
  public String update(String name, String json) {
    return beakerx.put(name, json);
  }

  @Override
  public String get(String name) {
    return beakerx.get(name);
  }

  @Override
  public String close() {
    return null;
  }

  @Override
  public String getContextAsString() {
    return null;
  }
}
