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

/**
 * @author Осюшкин Денис
 * Класс, характеризующий начальную страницу сайта YandexMarket
 */
public class YaMarketMainPage {

    private final WebDriver driver;

    private final WebDriverWait wait;

    private final Actions actions;

    /**
     * Селектор кнопки каталога
     */
    private static final String CATALOG_BUTTON =
            "//div[@data-zone-name='catalog']/button";

    /**
     * Селектор названий элементов каталога
     */
    private static final String CATALOG_ITEMS =
            "//div[@data-zone-name='catalog-content']//ul[@role='tablist']//descendant::span";

    /**
     * Селектор названий подэлементов каталога
     */
    private static final String CATALOG_SUBITEMS =
            "//div[@data-auto='category']//ancestor::div[@role='heading']/a";

    public YaMarketMainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
    }

    /**
     * @author Осюшкин Денис
     * Метод, в котором происходит выбор категории из каталога
     * @param titleCatalogItem название искомой категории в каталоге
     */
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

    /**
     * @author Осюшкин Денис
     * Метод, в котором происходит наведение на нужную категорию каталога
     * @param webElement элемент, в подкаталоге которого нужно продолжать поиск
     */
    @Step("Наводим курсор на категорию")
    private void correctCursorHover(WebElement webElement) {
        WebElement elementSubtitle;
        long startTime = System.currentTimeMillis();
        long endTime;
        do {
            actions.moveToElement(wait.until(visibilityOf(webElement))).perform();
            elementSubtitle = driver.findElement(By
                    .xpath(CATALOG_SUBITEMS));
            actions.moveToElement(wait.until(visibilityOf(elementSubtitle))).perform();
            endTime = System.currentTimeMillis();
        } while (!(elementSubtitle.getText()).contains(webElement.getText()) ||
                (endTime - startTime) > testsProperties.timeElapsed());
    }

    /**
     * @author Осюшкин Денис
     * Метод, в котором собрана логика поиска искомого раздела в подкаталоге
     * @param titleCatalogSubitem название искомой подкатегории в каталоге
     */
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

    /**
     * @author Осюшкин Денис
     * Метод, в котором происходит поиск нужной подкатегории в подкаталоге каталога
     * @param titleCatalogSubitem название искомой подкатегории в каталоге
     * @param categories список веб-элементов категорий
     * @param isClick логическая переменная, флаг, показывающий, кликнули мы подкатегорию или нет
     */
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

    /**
     * @author Осюшкин Денис
     * Метод, в котором открываются скрытые элементы подкатегорий путем нажатия кнопки "Ещё"
     * @param categories список веб-элементов категорий
     */
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

    /**
     * @author Осюшкин Денис
     * Метод, в котором происходит поиск нужной подкатегории в названиях подкатегорий
     * @param titleCatalogSubitem название искомой подкатегории в каталоге
     * @param categories список веб-элементов категорий
     * @param isClick логическая переменная, флаг, показывающий, кликнули мы подкатегорию или нет
     */
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
