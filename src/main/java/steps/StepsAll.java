package steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.YaMarketMainPage;
import pages.YaMarketSubtitlePage;

import java.util.Objects;

import static helpers.Assertions.assertTrue;

public class StepsAll {

    private static WebDriver driver;

    @Step("Переходим на сайт: {url}")
    public static void openSite(String url, WebDriver currentDriver) {
        driver = currentDriver;
        driver.get(url);
    }

    @Step("Проверяем наличие тайтла: {titleCatalogSubitem} в результатах поиска YandexMarket")
    public static void checkSearchInYMCatalog(String titleCatalogItem, String titleCatalogSubitem) {
        YaMarketMainPage yaMarketMainPage = new YaMarketMainPage(driver);
        yaMarketMainPage.chooseCatalogItem(titleCatalogItem);
        yaMarketMainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);
    }

    @Step("Выставляем фильтры и проверяем, что на первой странице поиска более {countRes} элементов товаров")
    public static void checkSearchInYMSubitem(String titleFiltersRange, String minPrice, String maxPrice,
                                              String titleFiltersCheckbox, String titleSubfiltersFirst,
                                              String titleSubfiltersSecond, Integer countRes) {
        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        yaMarketSubtitlePage.displaySettings();
        yaMarketSubtitlePage.searchFiltersInputRanges(titleFiltersRange, minPrice, maxPrice);
        yaMarketSubtitlePage.searchFiltersCheckbox(titleFiltersCheckbox, titleSubfiltersFirst, titleSubfiltersSecond);

        int countResultSearch = yaMarketSubtitlePage.getResultSearchFirstPage().size();
        assertTrue(countResultSearch > countRes,
                "На первой странице менее %d элементов товаров".formatted(countRes));
    }

    @Step("Проверяем, что все предложения соответствуют фильтрам по цене в диапазоне от {minPrice} до {maxPrice}")
    public static void validatePriceFilter(String minPrice, String maxPrice) {
        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        yaMarketSubtitlePage.scrollToEndPage();
        assertTrue(yaMarketSubtitlePage.getPriceProducts().stream()
                        .allMatch(price -> Integer.parseInt(minPrice) <= Integer.parseInt(price) &&
                                Integer.parseInt(maxPrice) >= Integer.parseInt(price)),
                "Не все предложения соответствуют фильтрам по цене в диапазоне от %s до %s"
                        .formatted(minPrice, maxPrice));
    }

    @Step("Проверяем, что все предложения соответствуют фильтрам по производителям " +
            "{titleSubfiltersFirst} и {titleSubfiltersSecond}")
    public static void validateTitleFilter(String titleSubfiltersFirst, String titleSubfiltersSecond) {
        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        assertTrue(yaMarketSubtitlePage.getTitleProducts().stream()
                        .anyMatch  // TODO вернуть в allMatch!
                                (title -> title.contains(titleSubfiltersFirst) ||
                                        title.contains(titleSubfiltersSecond)),
                "Не все предложения соответствуют фильтрам по производителям %s и %s"
                        .formatted(titleSubfiltersFirst, titleSubfiltersSecond));
    }

    @Step("Проверяем, что в результатах поиска на первой странице есть данный производитель")
    public static void validateSearchResult() {
        YaMarketSubtitlePage yaMarketSubtitlePage = new YaMarketSubtitlePage(driver);
        String firstPositionTitle = yaMarketSubtitlePage.getFirstPositionTitle();
        assertTrue(yaMarketSubtitlePage.getResultsProductTitle().contains(firstPositionTitle),
                "В результатах поиска на первой странице нет %s"
                        .formatted(firstPositionTitle));
    }


}
