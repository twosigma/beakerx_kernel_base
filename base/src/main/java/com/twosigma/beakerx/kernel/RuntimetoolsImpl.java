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
package com.twosigma.beakerx.kernel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Arrays.asList;

public class RuntimetoolsImpl implements Runtimetools {

  public void configRuntimeJars(KernelFunctionality kernel) {
    Optional<String> runtimetools = getJar("runtimetools");
    if (runtimetools.isPresent()) {
      kernel.addJarsToClasspath(asList(new PathToJar(runtimetools.get())));
      kernel.addImport(new ImportPath("com.twosigma.beakerx.BxDriverManager"));
    }
  }

  private Optional<String> getJar(String name) {
    try {
      Path path = Paths.get(KernelFunctionality.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      Path location = path.getParent().getParent().getParent().resolve("kernel").resolve("ext");
      if (location.toFile().exists()) {
        Optional<String> jarFile = Arrays.stream(location.toFile().list()).filter(jar -> jar.contains(name)).findFirst();
        return jarFile.map(s -> location.resolve(s).toString());
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
