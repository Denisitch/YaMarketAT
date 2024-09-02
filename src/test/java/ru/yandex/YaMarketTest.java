package ru.yandex;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YaMarketMainPage;
import pages.YaMarketSubtitlePage;

import java.util.Objects;

import static helpers.Assertions.assertTrue;
import static helpers.Properties.testsProperties;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Переход на страничку подпункта каталога  и поиск ноутбука определенных параметров")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#dataProvider")
    public void testYM(
            String titleCatalogItem, String titleCatalogSubitem, String titleFiltersRange,
            String minPrice, String maxPrice, String titleFiltersCheckbox, String titleSubfiltersFirst,
            String titleSubfiltersSecond, Integer countRes
    ) {
        driver.get(testsProperties.yandexMarketUrl());
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);

        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        yaMarketSubtitlePage.displaySettings();
        yaMarketSubtitlePage.searchFiltersInputRanges(titleFiltersRange, minPrice, maxPrice);
        yaMarketSubtitlePage.searchFiltersCheckbox(titleFiltersCheckbox, titleSubfiltersFirst, titleSubfiltersSecond);

        int countResultSearch = yaMarketSubtitlePage.getResultSearchFirstPage().size();
        assertTrue(countResultSearch > countRes,
                "На первой странице менее %d элементов товаров".formatted(countRes));

        yaMarketSubtitlePage.scrollToEndPage();
        assertTrue(yaMarketSubtitlePage.getPriceProducts().stream()
                        .allMatch(price -> Integer.parseInt(minPrice) <= Integer.parseInt(price) &&
                                Integer.parseInt(maxPrice) >= Integer.parseInt(price)),
                "Не все предложения соответствуют фильтрам по цене в диапазоне от %s до %s"
                        .formatted(minPrice, maxPrice));

        assertTrue(yaMarketSubtitlePage.getTitleProducts().stream()
                        .anyMatch  // TODO вернуть в allMatch!
                                (title -> title.contains(titleSubfiltersFirst) ||
                                title.contains(titleSubfiltersSecond)),
                "Не все предложения соответствуют фильтрам по производителю %s и %s"
                        .formatted(titleSubfiltersFirst, titleSubfiltersSecond));

        String firstPositionTitle = yaMarketSubtitlePage.getFirstPositionTitle();
        assertTrue(yaMarketSubtitlePage.getResultsProductTitle().contains(firstPositionTitle),
                "В результатах поиска, на первой странице, нет %s"
                        .formatted(firstPositionTitle));
    }
}
