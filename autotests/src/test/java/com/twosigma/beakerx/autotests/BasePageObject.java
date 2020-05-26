package com.twosigma.beakerx.autotests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class BasePageObject {

    public static String userDir = System.getProperties().getProperty("user.dir");
    public static String fileSeparator = System.getProperties().getProperty("file.separator");
    public static String imgDir = userDir + fileSeparator + "resources" + fileSeparator + "img";
    public static String imgFormat = "png";

    protected final WebDriver webDriver;
    public Actions action;

    protected BasePageObject(WebDriver webDriver) {
        this.webDriver = webDriver;
        action = new Actions(webDriver);
    }

    public abstract void runNotebookByUrl(String url);
    public abstract WebElement runCodeCellByIndex(int index);
    public abstract WebElement getCodeCellByIndex (int index);
    public abstract void waitKernelIdleIcon(int timeOutInSeconds);
    public abstract void closeAndHaltNotebook();
    public abstract By getAllOutputsExecuteResultsSelector();
    public abstract By getAllOutputsStdoutSelector();
    public abstract By getAllOutputsStderrSelector();

    public void doubleClick(By locator){
        //Double click the button to launch an alertbox
//        Actions action = new Actions(webDriver);
        WebElement element = webDriver.findElement(locator);
//        System.out.println("---------------------------------------");
//        System.out.println(element.getAttribute("innerHTML"));
        action.moveToElement(element).doubleClick().pause(500).perform();
//        action.doubleClick(element).perform();
    }

    public void pause(int mseconds){
        action.pause(mseconds).perform();
    }

    public Screenshot createActualScreenshot(WebElement element, String dirName, String fileName) throws IOException {
        String pathName = getPathNameForImage(dirName, fileName + "Act");
        Screenshot actualScreenshot =
                new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(webDriver, element);
        ImageIO.write(actualScreenshot.getImage(), imgFormat, new File(pathName));
        return actualScreenshot;
    }

    public int checkScreenshot(WebElement element, String dirName, String fileName) throws IOException {
        Screenshot actualScreenshot = createActualScreenshot(element, dirName, fileName);
        String expectedPathName = getPathNameForImage(dirName, fileName + "Exp");
        Screenshot expectedScreenshot = new Screenshot(ImageIO.read(new File(expectedPathName)));
        ImageDiff diff = new ImageDiffer().makeDiff(expectedScreenshot, actualScreenshot);
        if(diff.getDiffSize() > 0) {
            File diffFile = new File(getPathNameForImage(dirName, fileName + "Dif"));
            ImageIO.write(diff.getMarkedImage(), "png", diffFile);
        }
        return diff.getDiffSize();
    }

    private String getPathNameForImage(String dirName, String fileName){
        return imgDir + fileSeparator + dirName + fileSeparator + fileName  + "." + imgFormat;
    }

    public List<WebElement> getAllOutputsOfCodeCell(WebElement codeCell, By selector){
        FluentWait<WebElement> wait = new FluentWait<WebElement>(codeCell).withTimeout(10, TimeUnit.SECONDS);
        wait.until(new Function<WebElement, Boolean>() {
            public Boolean apply(WebElement codeCell) {
                return codeCell.findElements(selector).size() > 0;
            }
        });
        return codeCell.findElements(selector);
    }

}
