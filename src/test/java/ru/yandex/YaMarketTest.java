package ru.yandex;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.YaMarketMainPage;

import java.time.Duration;
import java.util.List;

import static helpers.Properties.testsProperties;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Проверка результатов поиска c помощью PO")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"Электроника, Ноутбуки", "Оборудование, Инкубаторы"})
    public void test(String titleCatalogItem, String titleCatalogSubitem) {
        driver.get(testsProperties.yandexMarketUrl());
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(driver.getTitle().contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogItem);
    }

//    @Test
//    public void test2() throws InterruptedException {
//        driver.get("https://market.yandex.ru/");
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(visibilityOfElementLocated(By.xpath("//div[@data-zone-name='catalog']/button")));
//        driver.findElement(By.xpath("//div[@data-zone-name='catalog']/button")).click();
//
//        wait.until(visibilityOfElementLocated(By.xpath("//body/div[@role='dialog']")));
//
//        String titleCatalogItem = "Электроника";
//        List<WebElement> catalogItems = driver.findElements(By.xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//descendant::span"));
//        catalogItems.stream().filter(item -> item.getText().equals(titleCatalogItem))
//                .findFirst().ifPresentOrElse(
//                        WebElement::click,
//                        () -> fail(titleCatalogItem + " отсутствует в каталоге")
//                );
//
//        Thread.sleep(3000);
//        String title = driver.getTitle();
//        assertTrue(title.contains(titleCatalogItem),
//                "Тайтл " + title + " на сайте не содержит " + titleCatalogItem);
//    }
}
