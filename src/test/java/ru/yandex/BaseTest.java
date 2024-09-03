package ru.yandex;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import steps.StepsAll;

import static helpers.CustomWait.implicitlyWait;
import static helpers.Properties.testsProperties;

/**
 * @author Осюшкин Денис
 * Базовый класс для тестов
 */
public class BaseTest {

    /**
     * Веб-драйвер для браузера
     */
    protected WebDriver driver;

    /**
     * Экземпляр класса step'ов
     */
    protected StepsAll steps;

    /**
     * @author Осюшкин Денис
     * Метод, выполняющийся перед началом каждого теста
     */
    @BeforeEach
    public void setUp() {
        ChromeOptions driverOptions = new ChromeOptions();
        driverOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driver = new ChromeDriver(driverOptions);
        steps = new StepsAll(driver);
        driver.manage().window().maximize();
        implicitlyWait(driver, testsProperties.defaultTimeout());
    }

    /**
     * @author Осюшкин Денис
     * Метод, выполняющийся после окончания каждого теста
     */
    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}