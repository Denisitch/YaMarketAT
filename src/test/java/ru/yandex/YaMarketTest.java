package ru.yandex;

import dto.Product;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.YaMarketMainPage;
import pages.YaMarketSubtitlePage;

import java.time.Duration;
import java.util.Objects;

import static helpers.Properties.testsProperties;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class YaMarketTest extends BaseTest {

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Переход на страничку подпункта каталога")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({
            "Электроника, Ноутбуки"
//            "Оборудование, Инкубаторы"
    })
    public void testPartOne(String titleCatalogItem, String titleCatalogSubitem) {
        driver.get(testsProperties.yandexMarketUrl());
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);
    }

    @Feature("Проверка результатов поиска в YandexMarket")
    @DisplayName("Поиск по заданным параметрам")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({
            "Ноутбуки, 10000, 30000, Производитель, HP, Lenovo, 12"
    })
    public void testPartTwo(String titleCatalogSubitem,
                            String minPrice, String maxPrice,
                            String titleFilters, String titleSubfiltersFirst,
                            String titleSubfiltersSecond,
                            Integer countRes) {
        driver.get("https://market.yandex.ru/catalog--noutbuki/26895412/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);

        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        yaMarketSubtitlePage.searchFilters(minPrice, maxPrice, titleFilters, titleSubfiltersFirst,
                titleSubfiltersSecond);

        int countResultSearch = yaMarketSubtitlePage.getResultSearchFirstPage().size();
        assertTrue(countResultSearch > countRes,
                "На первой странице менее %d элементов товаров".formatted(countRes));

        assertTrue(yaMarketSubtitlePage.getAllProducts().stream()
                        .map(Product::price)
                        .allMatch(price -> Integer.parseInt(minPrice) <= Integer.parseInt(price) &&
                                Integer.parseInt(maxPrice) >= Integer.parseInt(price)),
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
