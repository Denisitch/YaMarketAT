package ru.yandex;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static helpers.Properties.testsProperties;

/**
 * @author Осюшкин Денис
 * Класс тестов, тесты разбиты на step'ы
 */
public class YaMarketTest extends BaseTest {

    /**
     * @param titleCatalogItem      название искомой категории в каталоге
     * @param titleCatalogSubitem   название искомой подкатегории в каталоге
     * @param titleFiltersRange     название фильтра по диапазону
     * @param minPrice              минимальная цена для ввода
     * @param maxPrice              максимальная цена для ввода
     * @param titleFiltersCheckbox  название фильтра с чекбокс
     * @param titleSubfiltersFirst  первый критерий фильтра чекбокс
     * @param titleSubfiltersSecond второй критерий фильтра чекбокс
     * @param countRes              количество ожидаемых результатов поиска
     * @author Осюшкин Денис
     * Тест для проверки результатов поиска в YandexMarket c помощью паттерна Page Object
     */
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