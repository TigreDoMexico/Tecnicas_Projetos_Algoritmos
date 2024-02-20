package Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class MaximumSubarrayScenario {
    public static List<Integer[]> GetAll() {
        List<Integer> scenariosNumbers = List.of(1000, 5000, 10000, 15000);
        List<Integer[]> scenarios = new ArrayList<>();

        for(Integer scenarioNumber : scenariosNumbers) {
            Integer[] scenario = IntStream.generate(() -> new Random().nextInt(1000)).limit(scenarioNumber).boxed().toArray(Integer[]::new);
            scenarios.add(scenario);
        }

        return scenarios;
    }
}
