package ru.yandex;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.YaMarketMainPage;

import java.util.Objects;

import static helpers.Properties.testsProperties;
import static org.junit.jupiter.api.Assertions.*;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Проверка результатов поиска c помощью PO")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({
            "Электроника, Ноутбуки"
//            "Оборудование, Инкубаторы"
    })
    public void test(String titleCatalogItem, String titleCatalogSubitem) {
        driver.get(testsProperties.yandexMarketUrl());
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);
    }
}
