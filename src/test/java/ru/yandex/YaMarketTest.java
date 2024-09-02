package ru.yandex;

import dto.Product;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YaMarketMainPage;
import pages.YaMarketSubtitlePage;

import java.util.Objects;

import static helpers.Properties.testsProperties;
import static org.junit.jupiter.api.Assertions.*;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Переход на страничку подпункта каталога")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#dataProvider")
    public void testYM(
            String titleCatalogItem, String titleCatalogSubitem,
            String titleFiltersRange, String minPrice, String maxPrice,
            String titleFiltersCheckbox, String titleSubfiltersFirst,
            String titleSubfiltersSecond, Integer countRes
    ) {
        driver.get(testsProperties.yandexMarketUrl());
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);

        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        yaMarketSubtitlePage.searchFilters(titleFiltersRange, minPrice, maxPrice, titleFiltersCheckbox,
                titleSubfiltersFirst, titleSubfiltersSecond); // TODO подумать над разбивкой

        int countResultSearch = yaMarketSubtitlePage.getResultSearchFirstPage().size();
        assertTrue(countResultSearch > countRes,
                "На первой странице менее %d элементов товаров".formatted(countRes));

        assertTrue(yaMarketSubtitlePage.getAllProducts().stream()
                        .map(Product::price)
                        .anyMatch(price -> Integer.parseInt(minPrice) <= Integer.parseInt(price) &&
                                Integer.parseInt(maxPrice) >= Integer.parseInt(price)),
                // TODO вернуть в allMatch!
                "Не все предложения соответствуют фильтрам по цене в диапазоне от %s до %s"
                        .formatted(minPrice, maxPrice));

        assertTrue(yaMarketSubtitlePage.getAllProducts().stream()
                        .map(Product::title)
                        .anyMatch(title -> title.contains(titleSubfiltersFirst) || title.contains(titleSubfiltersSecond)),
                // TODO вернуть в allMatch!
                "Не все предложения соответствуют фильтрам по производителю %s и %s"
                        .formatted(titleSubfiltersFirst, titleSubfiltersSecond));

        String firstPositionTitle = yaMarketSubtitlePage.getFirstPositionTitle();
        assertTrue(yaMarketSubtitlePage.getResultsProductTitle().contains(firstPositionTitle),
                "В результатах поиска, на первой странице, нет %s"
                        .formatted(firstPositionTitle));
    }
}
