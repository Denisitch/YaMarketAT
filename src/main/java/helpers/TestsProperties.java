package helpers;

import org.aeonbits.owner.Config;

/**
 * @author Осюшкин Денис
 * Интерфейс, для получения доступа к проперти файлу
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:src/main/resources/tests.properties"
})
public interface TestsProperties extends Config {

    /**
     * @return String
     * @author Осюшкин Денис
     * URL ссылка на страницу YandexMarket
     */
    @Config.Key("yandex.market.url")
    String yandexMarketUrl();

    /**
     * @return int
     * @author Осюшкин Денис
     * Дефолтный таймаут
     */
    @Config.Key("default.timeout")
    int defaultTimeout();

    /**
     * @return int
     * @author Осюшкин Денис
     * Время ожидания видимого элемента в сек
     */
    @Config.Key("time.wait.located")
    int timeWaitLocated();

    /**
     * @return int
     * @author Осюшкин Денис
     * Время ожидания невидимого элемента в сек
     */
    @Config.Key("time.wait.invisible")
    int timeWaitInvisible();

    /**
     * @return int
     * @author Осюшкин Денис
     * Время ожидания потока в сек
     */
    @Config.Key("time.wait.for.sleep")
    int timeSleep();

    /**
     * @return long
     * @author Осюшкин Денис
     * Время ожидаемой работы цикла в миллисек
     */
    @Config.Key("time.elapsed")
    long timeElapsed();
}
