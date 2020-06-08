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

package com.twosigma.beakerx.autotests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    public static WebDriver driver;
    public static String baseURL;
    public static BasePageObject beakerxPO;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
//    options.addArguments("--headless");
//    options.addArguments("--disable-gpu");
        options.addArguments("window-size=1920,1080");
        driver = new ChromeDriver(options);
        if("lab".equalsIgnoreCase(System.getProperty("cur_app"))){
            System.out.println("current app is jupyter lab");
            beakerxPO = new LabPO(driver);
        }
        else {
            System.out.println("current app is jupyter notebook");
            beakerxPO = new NotebookPO(driver);
        }
    }

    public static void setBaseURL(String baseURL) {
        BaseTest.baseURL = baseURL;
    }

    @AfterClass
    public static void teardownClass() {
        beakerxPO.closeAndHaltNotebook();
        if (driver != null) {
            driver.quit();
        }
    }

}
