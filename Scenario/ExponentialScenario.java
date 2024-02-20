package Scenario;

import java.util.*;
import java.util.stream.IntStream;

public class ExponentialScenario {
    public static List<Integer[]> GetAll() {
        Integer[] baseNumbers = IntStream.generate(() -> new Random().nextInt(1000)).limit(10).boxed().toArray(Integer[]::new);
        Integer[] exponentNumbers = IntStream.generate(() -> new Random().nextInt(1000)).limit(10).boxed().toArray(Integer[]::new);

        return List.of(baseNumbers, exponentNumbers);
    }
}
