package ru.yandex;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TestForTest extends BaseTest {

    @Test
    public void test2() {
        driver.get("https://market.yandex.ru/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(presenceOfElementLocated(By.xpath("//body/script[@type]")));
        driver.findElement(By.xpath("//div[@data-zone-name='catalog']/button")).click();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        WebElement webElement = driver.findElement(By
                .xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//span[.='Оборудование']"));
        wait.until(presenceOfElementLocated(By
//                .xpath("//div[@data-zone-name='catalog-content']//ul[@role='tablist']//span[.='Оборудование']")));
                .xpath("//div[@role='dialog']//ul[@role='tablist']//span[.='Оборудование']")));

//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        Actions actions = new Actions(driver);

        actions.moveToElement(wait.until(visibilityOf(webElement)))
//                .scrollToElement(webElement)
//                .pause(Duration.ofSeconds(5))
                .perform();

        actions.moveToElement(wait.until(visibilityOfElementLocated(By
                .xpath("//div[@data-zone-name='catalog-content']//a[.='Холодильное оборудование']")))).perform();
        driver.findElement(By
                .xpath("//div[@data-zone-name='catalog-content']//a[.='Холодильное оборудование']")).click();

        String titleCatalogSubitem = "Холодильное оборудование";
        assertTrue(driver.getTitle().contains(titleCatalogSubitem),
                "Тайтл " + driver.getTitle() + " на сайте не содержит " + titleCatalogSubitem);
    }
}
