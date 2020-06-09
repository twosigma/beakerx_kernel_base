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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotebookPO  extends BasePageObject  {

    WebDriverWait wait = new WebDriverWait(webDriver, 20);

    protected NotebookPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void runNotebookByUrl(String url) {
        webDriver.get("http://127.0.0.1:8888/notebooks" + url);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='notification_kernel']//span[text()='Kernel ready']")));
        waitKernelIdleIcon(10);
    }

    @Override
    public WebElement runCodeCellByIndex(int index) {
        WebElement codeCell = this.getCodeCellByIndex(index);
        action.moveToElement(codeCell).click().perform();
        this.clickRunCell();
        waitKernelIdleIcon(10);
        return codeCell;
    }

    private void clickRunCell(){
        By runButtonSelector = By.cssSelector("button[data-jupyter-action=\"jupyter-notebook:run-cell-and-select-next\"]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(runButtonSelector));

        action.click(webDriver.findElement(runButtonSelector)).perform();
        this.waitKernelIdleIcon(10);
    }

    @Override
    public WebElement getCodeCellByIndex(int index) {
        return webDriver.findElements(By.cssSelector("div.code_cell")).get(index);
    }

    @Override
    public void waitKernelIdleIcon(int timeOutInSeconds) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("i.kernel_idle_icon")));
    }

    @Override
    public void closeAndHaltNotebook() {
        this.clickCellAllOutputClear();
        webDriver.findElement(By.partialLinkText("File")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li#close_and_halt")));
        action.moveToElement(webDriver.findElement(By.cssSelector("li#close_and_halt"))).click().perform();
    }

    private void clickCellAllOutputClear(){
        webDriver.findElement(By.partialLinkText("Cell")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li#all_outputs")));
        action.moveToElement(webDriver.findElement(By.cssSelector("li#all_outputs"))).perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li#toggle_all_output")));
        action.moveToElement(webDriver.findElement(By.cssSelector("li#toggle_all_output"))).perform();
        action.moveToElement(webDriver.findElement(By.cssSelector("li#clear_all_output"))).click().perform();
    }

    @Override
    public By getAllOutputsExecuteResultsSelector() {
        return By.cssSelector("div.output_subarea.output_result");
    }

    @Override
    public By getAllOutputsStdoutSelector() {
        return By.cssSelector("div.output_subarea.output_stdout");
    }

    @Override
    public By getAllOutputsStderrSelector() {
        return By.cssSelector("div.output_subarea.output_error");
    }
}
