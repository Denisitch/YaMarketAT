package helpers;

import org.aeonbits.owner.ConfigFactory;

/**
 * @author Осюшкин Денис
 * Класс, предоставляющий доступ к проперти файлам
 */
public class Properties {

    public static TestsProperties testsProperties = ConfigFactory.create(TestsProperties.class);
}
