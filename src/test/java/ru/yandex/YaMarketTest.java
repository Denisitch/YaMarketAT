package ru.yandex;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.YaMarketMainPage;

import java.time.Duration;
import java.util.List;

import static helpers.Properties.testsProperties;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Проверка результатов поиска c помощью PO")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({
//            "Электроника, Ноутбуки",
            "Оборудование, Инкубаторы"
    })
    public void test(String titleCatalogItem, String titleCatalogSubitem) {
        driver.get(testsProperties.yandexMarketUrl());
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(driver.getTitle().contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);
    }

    @Test
    public void test2() {
        driver.get("https://market.yandex.ru/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(By.xpath("//div[@data-zone-name='catalog']/button")).click();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        WebElement webElement = driver.findElement(By
                .xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//span[.='Оборудование']"));
        wait.until(presenceOfElementLocated(By
//                .xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//span[.='Оборудование']")));
                .xpath("//div[@role='dialog']//ul[@role='tablist']//span[.='Оборудование']")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        new Actions(driver)
                .scrollToElement(webElement)
                .moveToElement(wait.until(visibilityOf(webElement)))
                .pause(Duration.ofSeconds(5)).perform();

        driver.findElement(By
                .xpath("//div[@data-zone-name='catalog-content']//a[.='Холодильное оборудование']")).click();

        String titleCatalogSubitem = "Холодильное оборудование";
        assertTrue(driver.getTitle().contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);

    }
}
