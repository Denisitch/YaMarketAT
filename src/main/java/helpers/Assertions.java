package helpers;

import io.qameta.allure.Step;

/**
 * @author Осюшкин Денис
 * Класс, переопределяющий Assertions
 */
public class Assertions {

    /**
     * @param condition условие
     * @param message   сообщение
     * @author Осюшкин Денис
     * Метод, переопределяющий assertTrue
     */
    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    /**
     * @param message сообщение
     * @author Осюшкин Денис
     * Метод, переопределяющий fail
     */
    @Step("Проверяем что нет ошибки: {message}")
    public static void fail(String message) {
        org.junit.jupiter.api.Assertions.fail(message);
    }
}
