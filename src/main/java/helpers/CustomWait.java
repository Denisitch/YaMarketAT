package helpers;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;

import java.time.Duration;

import static helpers.Properties.testsProperties;

/**
 * @author Осюшкин Денис
 * Класс пользовательских ожиданий
 */
public class CustomWait {

    /**
     * Установка кастомного неявного ожидания в значение, взятое из свойств
     */
    private static int implicitlyWait = testsProperties.defaultTimeout();

    /**
     * @author Осюшкин Денис
     * Метод-обертка над дефолтным неявным ожиданием
     * @param driver веб-драйвер
     * @param defaultTimeout дефолтное время ожидания
     */
    public static void implicitlyWait(WebDriver driver, int defaultTimeout) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(defaultTimeout));
        implicitlyWait = defaultTimeout;
    }

    /**
     * @author Осюшкин Денис
     * Вспомогательный метод, останавливающий поток на одну секунду
     * @param sec секунда
     */
    private static void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Осюшкин Денис
     * Метод ожидания для веб-элемента, который появляется при обновлении данных на странице
     * и исчезает через неопределенное время
     * @param driver веб-драйвер
     * @param elementXpath селектор элемента
     * @param timeWaitLocated время ожидания видимого элемента
     * @param timeWaitInvisible время ожидания невидимого элемента
     */
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
}
