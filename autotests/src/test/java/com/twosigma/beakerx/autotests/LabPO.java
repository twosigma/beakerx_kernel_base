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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LabPO extends BasePageObject {

    public LabPO(WebDriver webDriver) {
        super(webDriver);
    }

    public void runNotebookByUrl(String url){
        Actions action = new Actions(webDriver);
        webDriver.get("http://localhost:8888/lab");
        action.pause(3000).perform();

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("span[data-icon=\"folder\"]"), 0));
        webDriver.findElement(By.cssSelector("span.jp-BreadCrumbs-home")).click();
        action.pause(500).perform();

        String[] arr = url.split("/");
        int i = 0;
        while(i < arr.length){
            if(arr[i].isEmpty()){
                i++;
                continue;
            }
            doubleClick(By.cssSelector("li.jp-DirListing-item[title='" + arr[i] +"']"));
            i++;
        }
    }

    /* Close and Shutdown Notebook */
    public void closeAndHaltNotebook () {
        clickCellAllOutputClear();
        webDriver.findElement(By.xpath("//div[@class='p-MenuBar-itemLabel' and text()='File']")).click();

        WebElement closeAndCleanupMenuItem = webDriver.findElement(By.cssSelector("li[data-command='filemenu:close-and-cleanup']"));
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOf(closeAndCleanupMenuItem));
        closeAndCleanupMenuItem.click();

        WebElement acceptDialogButton = webDriver.findElement(By.cssSelector("button.jp-Dialog-button.jp-mod-accept.jp-mod-warn.jp-mod-styled"));
        wait.until(ExpectedConditions.visibilityOf(acceptDialogButton));
        acceptDialogButton.click();

        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.cssSelector("li[data-type='document-title']"),1));
        action.pause(5000).perform();
    };

    public void clickCellAllOutputClear () {
        webDriver.findElement(By.xpath("//div[@class='p-MenuBar-itemLabel' and text()='Edit']")).click();
        WebElement clearAllOutputsMenuItem = webDriver.findElement(By.cssSelector("li[data-command='editmenu:clear-all']"));
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOf(clearAllOutputsMenuItem));
        clearAllOutputsMenuItem.click();
    };

    public WebElement runCodeCellByIndex (int index) {
        WebElement codeCell = this.getCodeCellByIndex(index);
        action.moveToElement(codeCell).click().perform();
        this.clickRunCell();
        waitKernelIdleIcon(10);
        return codeCell;
    };

    private void clickRunCell () {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div.jp-NotebookPanel-toolbar > div.jp-ToolbarButton"), 1));

        WebElement buttonRunCell = webDriver.findElements(By.cssSelector("div.jp-NotebookPanel-toolbar > div.jp-ToolbarButton")).get(5);
        action.click(buttonRunCell).perform();
        waitKernelIdleIcon(10);
    };

    public void waitKernelIdleIcon (int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeOutInSeconds);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.jp-Toolbar-kernelStatus.jp-CircleIcon")));
    }

    public WebElement getCodeCellByIndex (int index) {
        return webDriver.findElements(By.cssSelector("div.jp-Cell.jp-CodeCell.jp-Notebook-cell")).get(index);
    }

    public By getAllOutputsExecuteResultsSelector() {
        return By.cssSelector("div.jp-OutputArea-child.jp-OutputArea-executeResult > div.jp-OutputArea-output");
    }

    public By getAllOutputsStdoutSelector() {
        return By.cssSelector("div.jp-OutputArea-child > div.jp-OutputArea-output[data-mime-type=\"application/vnd.jupyter.stdout\"]");
    }

    public By getAllOutputsStderrSelector() {
        return By.cssSelector("div.jp-OutputArea-child > div.jp-OutputArea-output[data-mime-type=\"application/vnd.jupyter.stderr\"]");
    }

}
