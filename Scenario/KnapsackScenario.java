package Scenario;

import KnapsackProblem.ItemKnapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KnapsackScenario {
    public static List<List<ItemKnapsack>> GetAll() {
        List<Integer> scenariosNumbers = List.of(50, 100, 150, 200);
        List<List<ItemKnapsack>> scenarios = new ArrayList<>();

        for(Integer scenarioNumber : scenariosNumbers) {
            List<ItemKnapsack> scenario = CreateScenario(scenarioNumber);
            scenarios.add(scenario);
        }

        return scenarios;
    }

    private static List<ItemKnapsack> CreateScenario(int size) {
        List<ItemKnapsack> items = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            int weight = new Random().nextInt(1000);
            int value = new Random().nextInt(1000);

            ItemKnapsack item = new ItemKnapsack(weight, value);
            items.add(item);
        }

        return items;
    }
}
