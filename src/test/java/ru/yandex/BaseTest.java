package ru.yandex;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import static helpers.CustomWait.implicitlyWait;
import static helpers.Properties.testsProperties;

public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions driverOptions = new ChromeOptions();
        driverOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        driver = new ChromeDriver(driverOptions);
//        FirefoxOptions driverOptions = new FirefoxOptions();
//        driverOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
//        driver = new FirefoxDriver(driverOptions);
        driver.manage().window().maximize();
        implicitlyWait(driver, testsProperties.defaultTimeout());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}