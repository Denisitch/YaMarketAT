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

    private WebDriver driver;

    private WebDriverWait wait;

    private Actions actions;

    private JavascriptExecutor js;

    private static final By LOCATOR_CATALOG_BUTTON = By
            .xpath("//div[@data-zone-name='catalog']/button");

    private static final By LOCATOR_CATALOG_ITEMS = By
            .xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//descendant::span");

    public YaMarketMainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    public void chooseCatalogItem(String titleCatalogItem) {
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(LOCATOR_CATALOG_BUTTON).click();

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        wait.until(presenceOfElementLocated(LOCATOR_CATALOG_ITEMS));
        List<WebElement> catalogItems = driver.findElements(LOCATOR_CATALOG_ITEMS);
        catalogItems.stream()
                .filter(item -> item.getText().equals(titleCatalogItem))
                .findFirst()
                .ifPresentOrElse(
                        webElement -> {
//                            js.executeScript("arguments[0].scrollIntoView(true);", webElement);
                            actions.scrollToElement(webElement).moveToElement(wait.until(visibilityOf(webElement)))
                                    .pause(Duration.ofSeconds(5)).perform();
                        },
//                        WebElement::click,
                        () -> fail(titleCatalogItem + " отсутствует в заголовках каталога")
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

        AtomicBoolean isClick = new AtomicBoolean(false);
        categories.stream()
                .map(webElement -> webElement.findElement(By
                        .xpath(".//div[@role='heading']//a")))
                .filter(titleSubitem -> titleSubitem.getText().equals(titleCatalogSubitem))
                .findFirst()
                .ifPresent(webElement -> {
                    webElement.click();
                    isClick.set(true);
                });

        if (!isClick.get()) {
            categories.stream()
                    .map(webElement -> webElement.findElements(By
                            .xpath(".//span[text()='Ещё']")))
                    .flatMap(Collection::stream)
                    .forEach(WebElement::click);

            categories.stream()
                    .map(webElement -> webElement.findElements(By
                            .xpath(".//ul[@data-autotest-id='subItems']//a")))
                    .flatMap(Collection::stream)
                    .filter(subItem -> subItem.getText().equals(titleCatalogSubitem))
                    .findFirst()
                    .ifPresentOrElse(
                            WebElement::click,
                            () -> fail(titleCatalogSubitem + " отсутствует в подпунктах каталога")
                    );
        }
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
