package Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import KnapsackProblem.ItemKnapsack;

public class KnapsackGenerator {
    public static List<ItemKnapsack> GenerateRandomList(int tamanho) {
        List<ItemKnapsack> itens = new ArrayList<ItemKnapsack>();

        for(int i = 0; i < tamanho; i++) {
            int weight = new Random().nextInt(1000);
            int value = new Random().nextInt(1000);

            ItemKnapsack item = new ItemKnapsack(weight, value);
            itens.add(item);
        }

        return itens;
    }
}
