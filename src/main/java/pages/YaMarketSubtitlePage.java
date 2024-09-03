package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static helpers.CustomWait.waitInvisibleIfLocated;
import static helpers.Properties.testsProperties;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

/**
 * @author Осюшкин Денис
 * Класс, характеризующий страницу подраздела сайта YandexMarket
 */
public class YaMarketSubtitlePage {

    private final WebDriver driver;

    private final WebDriverWait wait;

    private final Actions actions;

    /**
     * Универсальный селектор названия фильтра
     */
    private static final String TITLE_FILTER =
            "//div[@data-filter-type='%s']//h4[text()='%s']";

    /**
     * Селектор спиннера загрузки страницы при обновлении фильтров
     */
    private static final String SPINNER_PRELOAD =
            "//div[contains(@class, 'position_center')]//span[@data-auto='spinner']";

    /**
     * Селектор спиннера загрузки страницы при пролистывании страницы вниз
     */
    private static final String SPINNER_MORE =
            "//button[@data-auto='pager-more']//span[@data-auto='spinner']";

    /**
     * Селектор кнопки "Показать еще"
     */
    private static final String BUTTON_MORE =
            "//button[@data-auto='pager-more']";

    /**
     * Селектор веб-элемента блока продукта
     */
    private static final String PRODUCTS =
            "//div[@data-zone-name='productSnippet']";

    /**
     * Селектор поля поиска "Найти"
     */
    private static final String FIND_FIELD =
            "//input[@placeholder='Найти товары']";

    /**
     * Селектор кнопки поиска "Найти"
     */
    private static final String FIND_BUTTON =
            "//button[@data-auto='search-button']";

    public YaMarketSubtitlePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
    }

    /**
     * @author Осюшкин Денис
     * Метод, настраивающий отображение результатов поиска в виде сетки
     */
    @Step("Настраиваем отображение результатов поиска в виде сетки")
    public void displaySettings() {
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(By.xpath("//input[@aria-label='в виде сетки']")).click();
    }

    /**
     * @author Осюшкин Денис
     * Метод, возвращающий список названий товаров с первой страницы
     * @return List<String>
     */
    @Step("Получаем список названий товаров с первой страницы по результатам поиска")
    public List<String> getResultSearchFirstPage() {
        List<WebElement> elements = driver.findElements(By
                .xpath(PRODUCTS + "//span[@data-auto='snippet-title']"));
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * @author Осюшкин Денис
     * Метод, возвращающий список с ценами продуктов
     * @return List<String>
     */
    @Step("Получаем список с ценами всех продуктов")
    public List<String> getPriceProducts() {
        List<WebElement> elementsPriceProducts = driver.findElements(By
                .xpath(PRODUCTS + "//div[@data-auto='snippet-price-current']/div"));
        return elementsPriceProducts.stream()
                .map(webElement -> webElement.getText().replaceAll(" ", ""))
                .toList();
    }

    /**
     * @author Осюшкин Денис
     * Метод, возвращающий список с названиями продуктов
     * @return List<String>
     */
    @Step("Получаем список с названиями всех продуктов")
    public List<String> getTitleProducts() {
        List<WebElement> elementsTitleProducts = driver.findElements(By
                .xpath(PRODUCTS + "//span[@data-auto='snippet-title']"));
        return elementsTitleProducts.stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * @author Осюшкин Денис
     * Метод, возвращающий список с названиями продуктов при поиске по запомненному значению
     * @return List<String>
     */
    @Step("Получаем названия товаров с первой страницы результатов поиска")
    public List<String> getResultsProductTitle() {
        String firstPositionTitle = getFirstPositionTitle();
        inputSearchTitle(firstPositionTitle);
        return getResultSearchFirstPage();
    }

    /**
     * @author Осюшкин Денис
     * Метод, отвечающий за введение в посковое поле и нажатие кнопки "Найти"
     * @param firstPositionTitle название первого с текущей страницы товара
     */
    @Step("Вводим в поисковое поле название первого с текущей страницы товара: {firstPositionTitle}")
    private void inputSearchTitle(String firstPositionTitle) {
        WebElement findProducts = driver.findElement(By.xpath(FIND_FIELD));
        findProducts.click();
        findProducts.sendKeys(firstPositionTitle);
        driver.findElement(By.xpath(FIND_BUTTON)).click();
    }

    /**
     * @author Осюшкин Денис
     * Метод, который получает название первого товара по результатам поиска
     * @return String
     */
    @Step("Получаем название первого товара по результатам поиска")
    public String getFirstPositionTitle() {
        actions.moveToElement(driver.findElement(By.xpath(PRODUCTS)));
        return driver.findElement(By
                .xpath(PRODUCTS + "//span[@data-auto='snippet-title']")).getText();
    }

    /**
     * @author Осюшкин Денис
     * Метод, пролистывающий страницу с товарами вниз
     */
    @Step("Открываем весь список товаров, листая страницу до конца вниз")
    public void scrollToEndPage() {
        while (!driver.findElements(By.xpath(BUTTON_MORE)).isEmpty()) {
            actions.moveToElement(driver.findElement(By.xpath(BUTTON_MORE))).perform();
            waitInvisibleIfLocated(driver, SPINNER_MORE,
                    testsProperties.timeWaitLocated(), testsProperties.timeWaitInvisible());
        }
    }

    /**
     * @author Осюшкин Денис
     * Метод, фильтрующий чекбокс по названию фильтра
     * @param titleFilters название фильтра
     * @param titleSubfilters критерий фильтра
     */
    @Step("Выставляем фильтр {titleFilters} по критерию: {titleSubfilters}")
    private void searchFiltersCheckbox(String titleFilters, String titleSubfilters) {
        WebElement itemBlockFilter = driver.findElement(By.xpath(TITLE_FILTER.formatted("enum", titleFilters)));

        wait.until(visibilityOf(itemBlockFilter.findElement(By
                .xpath("./ancestor::fieldset//button")))).click();

        WebElement fieldInput = itemBlockFilter.findElement(By
                .xpath("./ancestor::fieldset//input"));
        fieldInput.click();
        fieldInput.sendKeys(titleSubfilters);
        itemBlockFilter.findElement(By
                .xpath("./ancestor::fieldset//span[text()='%s']/preceding-sibling::*".formatted(titleSubfilters))).click();
        waitInvisibleIfLocated(driver, SPINNER_PRELOAD,
                testsProperties.timeWaitLocated(), testsProperties.timeWaitInvisible());
    }

    /**
     * @author Осюшкин Денис
     * Метод, фильтрующий чекбокс по названию фильтра с множественными критериями
     * Перегруженный метод searchFiltersCheckbox(String titleFilters, String titleSubfilters)
     * @param titleFilters название фильтра
     * @param titleSubfilters критерии фильтра
     */
    @Step("Выставляем фильтр {titleFilters} по критериям: {titleSubfilters}")
    public void searchFiltersCheckbox(String titleFilters, String... titleSubfilters) {
        Arrays.stream(titleSubfilters).forEach(titleSubfilter -> searchFiltersCheckbox(titleFilters, titleSubfilter));
    }

    /**
     * @author Осюшкин Денис
     * Метод, фильтрующий по диапазону
     * @param titleFilters название фильтра
     * @param input диапазон фильтра
     */
    @Step("Выставляем фильтр {titleFilters} в диапазоне: {input}")
    public void searchFiltersInputRanges(String titleFilters, String... input) {
        WebElement blockFilter = driver.findElement(By.xpath(TITLE_FILTER.formatted("range", titleFilters)));
        String[] minOrMax = new String[]{"min", "max"};
        for (int i = 0; i < minOrMax.length; i++) {
            if (!input[i].isEmpty()) {
                WebElement fieldInput = blockFilter.findElement(By
                        .xpath("./ancestor::fieldset//span[@data-auto='filter-range-%s']//input".formatted(minOrMax[i])));
                fieldInput.click();
                fieldInput.sendKeys(input[i]);

                waitInvisibleIfLocated(driver, SPINNER_PRELOAD,
                        testsProperties.timeWaitLocated(), testsProperties.timeWaitInvisible());
            }
        }
    }
}