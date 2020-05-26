package com.twosigma.beakerx.autotests.groovy;

import com.twosigma.beakerx.autotests.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GroovyTest extends BaseTest {

    int cellIndex = 0;

    @BeforeClass
    public static void setupClass() {
        BaseTest.setupClass();
        System.out.println("groovy_________test");
        beakerxPO.runNotebookByUrl("/autotests/ipynb/groovy/GroovyTest.ipynb");
    }

    @Test(priority = 1, description = "Execute Results output contains 2.")
    public void setLocalVariable() {
        cellIndex = 0;
        WebElement codeCell = beakerxPO.runCodeCellByIndex(cellIndex);
        String txt = beakerxPO.getAllOutputsOfCodeCell(codeCell, beakerxPO.getAllOutputsExecuteResultsSelector())
                .get(0).getText();
        Assert.assertEquals(txt, "2");
    }

    @Test(priority = 5, description = "Stderr output contains \"groovy.lang.MissingPropertyException\"")
    public void callLocalVariableInAnotherCell() {
        cellIndex++;
        WebElement codeCell = beakerxPO.runCodeCellByIndex(cellIndex);
        String txt = beakerxPO.getAllOutputsOfCodeCell(codeCell, beakerxPO.getAllOutputsStderrSelector())
                .get(0).getText();
        Assert.assertTrue(txt.contains("groovy.lang.MissingPropertyException"));
    }

    @Test(priority = 10, description = "Execute Results output contains 2.")
    public void callGlobalVariableInAnotherCell() {
        cellIndex++;
        WebElement codeCell = beakerxPO.runCodeCellByIndex(cellIndex);
        String txt = beakerxPO.getAllOutputsOfCodeCell(codeCell, beakerxPO.getAllOutputsExecuteResultsSelector())
                .get(0).getText();
        Assert.assertEquals(txt, "2");
    }

}
