package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class YaMarketMainPage {

    private WebDriver driver;

    private WebDriverWait wait;

    private Actions actions;

    private static final By LOCATOR_CATALOG_BUTTON = By
            .xpath("//div[@data-zone-name='catalog']/button");

    private static final By LOCATOR_CATALOG_ITEMS = By
            .xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//descendant::span");

    public YaMarketMainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    public void chooseCatalogItem(String titleCatalogItem) {
        wait.until(presenceOfElementLocated(LOCATOR_CATALOG_BUTTON));
        driver.findElement(LOCATOR_CATALOG_BUTTON).click();

        wait.until(presenceOfElementLocated(LOCATOR_CATALOG_ITEMS));
        List<WebElement> catalogItems = driver.findElements(LOCATOR_CATALOG_ITEMS);
        System.out.println(catalogItems.size());
        catalogItems.stream()
                .filter(item -> item.getText().equals(titleCatalogItem))
                .findFirst()
                .ifPresentOrElse(
                        webElement -> actions.moveToElement(webElement).pause(Duration.ofSeconds(1)).perform(),
//                        WebElement::click,
                        () -> fail(titleCatalogItem + " отсутствует в каталоге")
                );

//        Thread.sleep(3000);
//        String title = driver.getTitle();
//        System.out.println(title);
//        assertTrue(title.contains(titleCatalogItem),
//                "Тайтл " + title + " на сайте не содержит " + titleCatalogItem);
    }

    public void chooseCatalogSubitem(String titleCatalogSubitem) {
        List<WebElement> categories = driver.findElements(By
                .xpath("//div[@data-auto='category']"));

        categories.stream()
                .map(webElement -> webElement.findElement(By
                        .xpath(".//div[@role='heading']//a")))
                .filter(titleSubitem -> titleSubitem.getText().equals(titleCatalogSubitem))
                .findFirst()
                .ifPresent(WebElement::click);

//        categories.stream()
//                .map(webElement -> webElement.findElement(By
//                        .xpath(".//span[text()='Ещё']")))
//                .forEach(WebElement::click);

        categories.stream()
                .map(webElement -> webElement.findElements(By
                        .xpath(".//span[text()='Ещё']")))
                .filter(Predicate.not(List::isEmpty))
                .flatMap(Collection::stream)
                .forEach(WebElement::click);

        categories.stream()
                .flatMap(webElement -> webElement.findElements(By
                        .xpath(".//span[text()='Ещё']")).stream())
                .forEach(WebElement::click);

        categories.stream()
                .flatMap(webElement -> webElement.findElements(By
                        .xpath(".//ul[@data-autotest-id='subItems']//a")).stream())
                .filter(subItem -> subItem.getText().equals(titleCatalogSubitem))
                .findFirst()
                .ifPresentOrElse(
                        WebElement::click,
                        () -> fail(titleCatalogSubitem + " отсутствует в каталоге")
                );
    }


    public void chooseAndClickCatalogItem(String titleCatalogItem) {

        driver.findElement(LOCATOR_CATALOG_ITEMS).click();
        WebElement iframe = driver.findElement(By.tagName("iframe"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", iframe);
        new Actions(driver)
                .scrollToElement(iframe)
                .perform();
    }
}
