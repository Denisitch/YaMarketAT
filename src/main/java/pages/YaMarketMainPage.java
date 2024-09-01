package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class YaMarketMainPage {

    private final WebDriver driver;

    private final WebDriverWait wait;

    private final Actions actions;

    private static final String CATALOG_BUTTON = "//div[@data-zone-name='catalog']/button";

    private static final String CATALOG_ITEMS =
            "//div[@data-zone-name='catalog-content']//ul[@role='tablist']//descendant::span";

    public YaMarketMainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    public void chooseCatalogItem(String titleCatalogItem) {
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(By.xpath(CATALOG_BUTTON)).click();

        wait.until(presenceOfElementLocated(By.xpath(CATALOG_ITEMS)));
        List<WebElement> catalogItems = driver.findElements(By.xpath(CATALOG_ITEMS));
        catalogItems.stream()
                .filter(item -> item.getText().equals(titleCatalogItem))
                .findFirst()
                .ifPresentOrElse(
                        webElement -> actions.moveToElement(wait.until(visibilityOf(webElement))).perform(),
                        () -> fail(titleCatalogItem + " отсутствует в заголовках каталога")
                );
    }

    public void chooseCatalogSubitem(String titleCatalogSubitem) {
        List<WebElement> categories = driver.findElements(By
                .xpath("//div[@data-auto='category']"));

        AtomicBoolean isClick = new AtomicBoolean(false);
        
        searchBySubtitle(titleCatalogSubitem, categories, isClick);

        if (!isClick.get()) {
            expandFieldsMore(categories);

            searchByElements(titleCatalogSubitem, categories);
        }
    }

    private void searchByElements(String titleCatalogSubitem, List<WebElement> categories) {
        categories.stream()
                .map(webElement -> webElement.findElements(By
                        .xpath(".//ul[@data-autotest-id='subItems']//a")))
                .flatMap(Collection::stream)
                .filter(subItem -> subItem.getText().equals(titleCatalogSubitem))
                .findFirst()
                .ifPresentOrElse(
                        webElement -> wait.until(visibilityOf(webElement)).click(),
                        () -> fail(titleCatalogSubitem + " отсутствует в подпунктах каталога")
                );
    }

    private void expandFieldsMore(List<WebElement> categories) {  // TODO сделать этот пункт необязательным
        categories.stream()
                .peek(webElement -> wait.until(visibilityOf(webElement)))
                .map(webElement -> webElement.findElements(By
                        .xpath(".//span[text()='Ещё']")))
                .flatMap(Collection::stream)
                .forEach(webElement -> actions.moveToElement(wait.until(visibilityOf(webElement))).click().perform());
    }

    private static void searchBySubtitle(String titleCatalogSubitem, List<WebElement> categories, AtomicBoolean isClick) {
        categories.stream()
                .map(webElement -> webElement.findElement(By
                        .xpath(".//div[@role='heading']//a")))
                .filter(titleSubitem -> titleSubitem.getText().equals(titleCatalogSubitem))
                .findFirst()
                .ifPresent(webElement -> {
                    webElement.click();
                    isClick.set(true);
                });
    }
}
