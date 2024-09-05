package steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.YaMarketMainPage;
import pages.YaMarketSubtitlePage;

import java.util.Objects;

import static helpers.Assertions.assertTrue;

/**
 * @author Осюшкин Денис
 * Класс, предоставляющий методы для тестов, разбивающие тест на шаги
 */
public class StepsAll {

    private final WebDriver driver;

    private final YaMarketMainPage mainPage;

    private final YaMarketSubtitlePage subtitlePage;

    public StepsAll(WebDriver currentDriver) {
        this.driver = currentDriver;
        this.mainPage = new YaMarketMainPage(driver);
        this.subtitlePage = new YaMarketSubtitlePage(driver);
    }

    /**
     * @param url ссылка url
     * @author Осюшкин Денис
     * Step, в котором осуществляется переход на сайт по ссылке url
     */
    @Step("Переходим на сайт: {url}")
    public void openSite(String url) {
        driver.get(url);
    }

    /**
     * @param titleCatalogItem    название искомой категории в каталоге
     * @param titleCatalogSubitem название искомой подкатегории в каталоге
     * @author Осюшкин Денис
     * Step, в котором производится проверка заголовка веб-страницы
     */
    @Step("Проверяем наличие тайтла: {titleCatalogSubitem} в результатах поиска YandexMarket")
    public void checkSearchInYMCatalog(String titleCatalogItem, String titleCatalogSubitem) {
        mainPage.chooseCatalogItem(titleCatalogItem);
        mainPage.chooseCatalogSubitem(titleCatalogSubitem);
        assertTrue(Objects.requireNonNull(driver.getTitle()).contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);
    }

    /**
     * @param titleFiltersRange     название фильтра по диапазону
     * @param minPrice              минимальная цена для ввода
     * @param maxPrice              максимальная цена для ввода
     * @param titleFiltersCheckbox  название фильтра с чекбокс
     * @param titleSubfiltersFirst  первый критерий фильтра чекбокс
     * @param titleSubfiltersSecond второй критерий фильтра чекбокс
     * @param countRes              количество ожидаемых результатов поиска
     * @author Осюшкин Денис
     * Step, в котором происходит проверка количества искомых товаров
     */
    @Step("Выставляем фильтры и проверяем, что на первой странице поиска более {countRes} элементов товаров")
    public void checkSearchInYMSubitem(String titleFiltersRange, String minPrice, String maxPrice,
                                       String titleFiltersCheckbox, String titleSubfiltersFirst,
                                       String titleSubfiltersSecond, Integer countRes) {
        subtitlePage.displaySettings();
        subtitlePage.searchFiltersInputRanges(titleFiltersRange, minPrice, maxPrice);
        subtitlePage.searchFiltersCheckbox(titleFiltersCheckbox, titleSubfiltersFirst, titleSubfiltersSecond);

        int countResultSearch = subtitlePage.getResultSearchFirstPage().size();
        assertTrue(countResultSearch > countRes,
                "На первой странице менее %d элементов товаров".formatted(countRes));
    }

    /**
     * @param minPrice минимальная цена для ввода
     * @param maxPrice максимальная цена для ввода
     * @author Осюшкин Денис
     * Step, в котором происходит проверка фильтра с диапазоном
     */
    @Step("Проверяем, что все предложения соответствуют фильтрам по цене в диапазоне от {minPrice} до {maxPrice}")
    public void validatePriceFilter(String minPrice, String maxPrice) {
        subtitlePage.scrollToEndPage();
        assertTrue(subtitlePage.getPriceProducts().stream()
                        .allMatch(price -> Integer.parseInt(minPrice) <= Integer.parseInt(price) &&
                                Integer.parseInt(maxPrice) >= Integer.parseInt(price)),
                "Не все предложения соответствуют фильтрам по цене в диапазоне от %s до %s"
                        .formatted(minPrice, maxPrice));
    }

    /**
     * @param titleSubfiltersFirst  первый критерий фильтра чекбокс
     * @param titleSubfiltersSecond второй критерий фильтра чекбокс
     * @author Осюшкин Денис
     * Step, в котором происходит проверка фильтра с чекбоксами
     */
    @Step("Проверяем, что все предложения соответствуют фильтрам по производителям " +
            "{titleSubfiltersFirst} и {titleSubfiltersSecond}")
    public void validateTitleFilter(String titleSubfiltersFirst, String titleSubfiltersSecond) {
        assertTrue(subtitlePage.getTitleProducts().stream()
                        .allMatch(title -> title.contains(titleSubfiltersFirst) ||
                                title.contains(titleSubfiltersSecond)),
                "Не все предложения соответствуют фильтрам по производителям %s и %s"
                        .formatted(titleSubfiltersFirst, titleSubfiltersSecond));
    }

    /**
     * @author Осюшкин Денис
     * Step, в котором осуществляется проверка поиска по названию товара
     */
    @Step("Проверяем, что в результатах поиска на первой странице есть данный производитель")
    public void validateSearchResult() {
        String firstPositionTitle = subtitlePage.getFirstPositionTitle();
        assertTrue(subtitlePage.getResultsProductTitle().contains(firstPositionTitle),
                "В результатах поиска на первой странице нет %s"
                        .formatted(firstPositionTitle));
    }


}
