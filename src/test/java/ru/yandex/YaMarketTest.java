package ru.yandex;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static helpers.Properties.testsProperties;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Переход на страницу подпункта каталога  и поиск ноутбука определенных параметров")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#dataProvider")
    public void testYM(
            String titleCatalogItem, String titleCatalogSubitem, String titleFiltersRange,
            String minPrice, String maxPrice, String titleFiltersCheckbox, String titleSubfiltersFirst,
            String titleSubfiltersSecond, Integer countRes
    ) {
        steps.openSite(testsProperties.yandexMarketUrl());
        steps.checkSearchInYMCatalog(titleCatalogItem, titleCatalogSubitem);
        steps.checkSearchInYMSubitem(titleFiltersRange, minPrice, maxPrice, titleFiltersCheckbox,
                titleSubfiltersFirst, titleSubfiltersSecond, countRes);
        steps.validatePriceFilter(minPrice, maxPrice);
        steps.validateTitleFilter(titleSubfiltersFirst, titleSubfiltersSecond);
        steps.validateSearchResult();
    }
}