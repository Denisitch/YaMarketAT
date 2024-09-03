package helpers;

import io.qameta.allure.Step;

/**
 * @author Осюшкин Денис
 * Класс, переопределяющий Assertions
 */
public class Assertions {

    /**
     * @author Осюшкин Денис
     * Метод, переопределяющий assertTrue
     * @param condition условие
     * @param message сообщение
     */
    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    /**
     * @author Осюшкин Денис
     * Метод, переопределяющий fail
     * @param message сообщение
     */
    @Step("Проверяем что нет ошибки: {message}")
    public static void fail(String message) {
        org.junit.jupiter.api.Assertions.fail(message);
    }
}
