package helpers;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static helpers.Properties.testsProperties;

public class CustomWait {

    private static int implicitlyWait = testsProperties.defaultTimeout();

    private static int pageLoadTimeout = testsProperties.defaultTimeout();

    private static int setScriptTimeout = testsProperties.defaultTimeout();

    public static void implicitlyWait(WebDriver driver, int defaultTimeout) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultTimeout));
        implicitlyWait = defaultTimeout;
    }

    public static void pageLoadTimeout(WebDriver driver, int defaultTimeout) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(defaultTimeout));
        pageLoadTimeout = defaultTimeout;
    }

    public static void setScriptTimeout(WebDriver driver, int defaultTimeout) {
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(defaultTimeout));
        setScriptTimeout = defaultTimeout;
    }

    private static void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitInvisibleIfLocated(WebDriver driver, String elementXpath,
                                              int timeWaitLocated, int timeWaitInvisible) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        for (int i = 0; i < timeWaitLocated; ++i) {
            if (!driver.findElements(By.xpath(elementXpath)).isEmpty()) {
                for (int j = 0; ; ++j) {
                    if (driver.findElements(By.xpath(elementXpath)).isEmpty())
                        break;
                    if (j + 1 == timeWaitInvisible)
                        Assertions.fail("Элемент " + elementXpath + " не исчез за " + timeWaitInvisible + "секунд");
                    sleep(testsProperties.timeSleep());
                }
            }
            sleep(testsProperties.timeSleep());
        }
        implicitlyWait(driver, implicitlyWait);
    }

    public static FluentWait<WebDriver> getFluentWait(WebDriver driver, int timeWaite, int frequency) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeWaite))
                .pollingEvery(Duration.ofMillis(frequency))
                .ignoreAll(List.of(
                        NoSuchElementException.class,
                        WebDriverException.class,
                        StaleElementReferenceException.class,
                        ElementClickInterceptedException.class,
                        TimeoutException.class)
                );
    }
}
