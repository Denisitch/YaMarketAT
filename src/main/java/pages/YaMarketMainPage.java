package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static helpers.Properties.testsProperties;
import static helpers.Assertions.fail;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
    }

    @Step("Выбираем категорию {titleCatalogItem} в каталоге товаров")
    public void chooseCatalogItem(String titleCatalogItem) {
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(By.xpath(CATALOG_BUTTON)).click();

        wait.until(visibilityOfElementLocated(By.xpath(CATALOG_ITEMS)));
        List<WebElement> catalogItems = driver.findElements(By.xpath(CATALOG_ITEMS));
        catalogItems.stream()
                .filter(item -> item.getText().equals(titleCatalogItem))
                .findFirst()
                .ifPresentOrElse(
                        this::correctCursorHover,
                        () -> fail(titleCatalogItem + " отсутствует в заголовках каталога")
                );
    }

    @Step("Наводим курсор на категорию")
    private void correctCursorHover(WebElement webElement) {
        WebElement elementSubtitle;
        long startTime = System.currentTimeMillis();
        long endTime;
        do {
            actions.moveToElement(wait.until(visibilityOf(webElement))).perform();
            elementSubtitle = driver.findElement(By
                    .xpath("//div[@data-auto='category']//ancestor::div[@role='heading']/a"));
            actions.moveToElement(wait.until(visibilityOf(elementSubtitle))).perform();
            endTime = System.currentTimeMillis();
        } while (!(elementSubtitle.getText()).contains(webElement.getText()) ||
                (endTime - startTime) > testsProperties.timeElapsed());
    }

    @Step("Выбираем подкатегорию {titleCatalogSubitem} в каталоге товаров")
    public void chooseCatalogSubitem(String titleCatalogSubitem) {
        List<WebElement> categories = driver.findElements(By
                .xpath("//div[@data-auto='category']"));
        AtomicBoolean isClick = new AtomicBoolean(false);
        searchByElements(titleCatalogSubitem, categories, isClick);

        if (!isClick.get()) {
            searchBySubtitle(titleCatalogSubitem, categories, isClick);
            if (!isClick.get()) {
                expandFieldsMore(categories);
                searchByElements(titleCatalogSubitem, categories, isClick);
                if (!isClick.get()) {
                    fail(titleCatalogSubitem + " отсутствует в подпунктах каталога");
                }
            }
        }
    }

    @Step("Ищем подкатегорию {titleCatalogSubitem} в списке подкатегорий")
    private void searchByElements(String titleCatalogSubitem, List<WebElement> categories, AtomicBoolean isClick) {
        categories.stream()
                .map(webElement -> webElement.findElements(By
                        .xpath(".//ul[@data-autotest-id='subItems']//a")))
                .flatMap(Collection::stream)
                .filter(subItem -> subItem.getText().equals(titleCatalogSubitem))
                .findFirst()
                .ifPresent(
                        webElement -> {
                            actions.moveToElement(wait.until(visibilityOf(webElement))).click().perform();
                            isClick.set(true);
                        }
                );
    }

    @Step("Открытие скрытых элементов подкатегорий")
    private void expandFieldsMore(List<WebElement> categories) {
        categories.stream()
                .map(webElement -> {
                    wait.until(visibilityOf(webElement));
                    return webElement.findElements(By.xpath(".//span[text()='Ещё']"));
                })
                .flatMap(Collection::stream)
                .forEach(webElement -> actions.moveToElement(wait.until(visibilityOf(webElement))).click().perform());
    }

    @Step("Ищем подкатегорию {titleCatalogSubitem} в списке названий подкатегорий")
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
