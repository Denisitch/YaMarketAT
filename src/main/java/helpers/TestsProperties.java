package helpers;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:src/main/resources/tests.properties"
})
public interface TestsProperties extends Config {

    @Config.Key("yandex.market.url")
    String yandexMarketUrl();

    @Config.Key("default.timeout")
    int defaultTimeout();

    @Config.Key("time.wait.located")
    int timeWaitLocated();

    @Config.Key("time.wait.invisible")
    int timeWaitInvisible();

    @Config.Key("time.wait.for.sleep")
    int timeSleep();
}
