package pages;

import dto.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static helpers.CustomWait.waitInvisibleIfLocated;
import static helpers.Properties.testsProperties;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class YaMarketSubtitlePage {

    private final WebDriver driver;

    private final WebDriverWait wait;

    private final Actions actions;

    private static final String TITLE_FILTER =
            "//div[@data-filter-type='%s']//h4[text()='%s']";

    private static final String SPINNER_PRELOAD =
            "//div[contains(@class, 'position_center')]//span[@data-auto='spinner']";

    private static final String SPINNER_MORE =
            "//button[@data-auto='pager-more']//span[@data-auto='spinner']";

    private static final String BUTTON_MORE =
            "//button[@data-auto='pager-more']";

    private static final String PRODUCTS =
            "//div[@data-zone-name='productSnippet']";

    private static final String FIND_FIELD =
            "//input[@placeholder='Найти товары']";

    private static final String FIND_BUTTON =
            "//button[@data-auto='search-button']";

    List<Product> products = new ArrayList<>();

    public YaMarketSubtitlePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
    }

    public void displaySettings() {
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(By.xpath("//input[@aria-label='в виде сетки']")).click();
    }

    public List<String> getResultSearchFirstPage() {
        List<WebElement> elements = driver.findElements(By
                .xpath(PRODUCTS + "//span[@data-auto='snippet-title']"));
        return elements.stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<Product> getAllProducts() {
        List<String> titleProducts = getTitleProducts();
        List<String> priceProducts = getPriceProducts();
        IntStream.range(0, titleProducts.size())
                .forEach(i -> products.add(new Product(titleProducts.get(i), priceProducts.get(i))));
        return products;
    }

    public List<String> getPriceProducts() {
        List<WebElement> elementsPriceProducts = driver.findElements(By
                .xpath(PRODUCTS + "//div[@data-auto='snippet-price-current']/div"));
        return elementsPriceProducts.stream()
                .map(webElement -> webElement.getText().replaceAll(" ", ""))
                .toList();
    }

    public List<String> getTitleProducts() {
        List<WebElement> elementsTitleProducts = driver.findElements(By
                .xpath(PRODUCTS + "//span[@data-auto='snippet-title']"));
        return elementsTitleProducts.stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getResultsProductTitle() {
        String firstPositionTitle = getFirstPositionTitle();

        WebElement findProducts = driver.findElement(By.xpath(FIND_FIELD));
        findProducts.click();
        findProducts.sendKeys(firstPositionTitle);
        driver.findElement(By.xpath(FIND_BUTTON)).click();

        return getResultSearchFirstPage();
    }

    public String getFirstPositionTitle() {
        actions.moveToElement(driver.findElement(By.xpath(PRODUCTS)));
        return driver.findElement(By
                .xpath(PRODUCTS + "//span[@data-auto='snippet-title']")).getText();
    }

    public void scrollToEndPage() {
        while (!driver.findElements(By.xpath(BUTTON_MORE)).isEmpty()) {
            actions.moveToElement(driver.findElement(By.xpath(BUTTON_MORE))).perform();
            waitInvisibleIfLocated(driver, SPINNER_MORE,
                    testsProperties.timeWaitLocated(), testsProperties.timeWaitInvisible());
        }
    }

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

    public void searchFiltersCheckbox(String titleFilters, String... titleSubfilters) {
        Arrays.stream(titleSubfilters).forEach(titleSubfilter -> searchFiltersCheckbox(titleFilters, titleSubfilter));
    }

    public void searchFiltersInputRanges(String titleFilters, String... input) {
        WebElement blockFilter = driver.findElement(By.xpath(TITLE_FILTER.formatted("range", titleFilters)));
        String[] minOrMax = new String[] {"min", "max"};
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
