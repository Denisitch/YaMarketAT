package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class DataProvider {

    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(
                        "Электроника", "Ноутбуки", "Цена, ₽", "10000", "30000",
                        "Производитель", "HP", "Lenovo", 12
                )
        );
    }
}
