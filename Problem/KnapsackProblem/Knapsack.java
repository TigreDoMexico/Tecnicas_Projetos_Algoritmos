package Problem.KnapsackProblem;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import Problem.Common.IExecutable;
import Conversion.Conversion;
import Problem.Common.ProblemExecutor;
import Scenario.KnapsackScenario;
import TimeCalculation.*;

public class Knapsack extends ProblemExecutor implements IExecutable {
    private final List<List<ItemKnapsack>> scenarios;

    private final List<CalculationResult> resultsDefault = new ArrayList<>();
    private final List<CalculationResult> resultsDivideConquer = new ArrayList<>();
    private final List<CalculationResult> resultsDynamicProg = new ArrayList<>();

    private final TimeCalculation timeCalculation = new TimeCalculation();

    public Knapsack() {
        super();
        scenarios = KnapsackScenario.GetAll();
    }

    @Override
    public void ExecuteAllMethods() {
        System.out.println("\n\nPROBLEMA KNAPSACK\n");
        ExecuteKnapsackDefault();
        ExecuteKnapsackDivideConquer();
        ExecuteKnapsackDynamicProgramming();
    }

    @Override
    public void ShowResults() {
        System.out.println("\n\nRESULTADOS DEFAULT (GREEDY SEARCH)\n");
        PrintResults(resultsDefault);

        System.out.println("\n\nRESULTADOS DIVISÃO E CONQUISTA\n");
        PrintResults(resultsDivideConquer);

        System.out.println("\n\nRESULTADOS PROGRAMAÇÃO DINAMICA\n");
        PrintResults(resultsDynamicProg);
    }

    private void PrintResults(List<CalculationResult> results) {
        int i = 0;
        for (CalculationResult result : results) {
            long resultadoOperacao = Conversion.toLong(result.getResult());
            double duracaoEmMs = result.getTimeDuration();

            System.out.println(MessageFormat.format("Duração: {0}", duracaoEmMs));
            System.out.println(
                    MessageFormat.format("KNAPSACK ({0} ELEMENTOS) => {1} ", scenarios.get(i).size(), resultadoOperacao));
            i++;
        }
    }

    private void ExecuteKnapsackDefault() {
        for (List<ItemKnapsack> item : scenarios) {
            List<ItemKnapsack> copyItems = new ArrayList<>(item);
            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> KnapsackDefault(copyItems, 1000), null);

            AddTaskToExecute(task);
        }

        Execute(scenarios.size(), resultsDefault);
    }

    private int KnapsackDefault(List<ItemKnapsack> items, int maxWeight) {
        if (items.isEmpty() || maxWeight == 0)
            return 0;

        int lastIndex = items.size() - 1;
        int lastItemWeight = items.get(lastIndex).getWeight();
        int lastItemValue = items.get(lastIndex).getValue();

        if (lastItemWeight > maxWeight) {
            items.remove(lastIndex);
            return KnapsackDefault(items, maxWeight);
        }

        items.remove(lastIndex);
        return Math.max(
                lastItemValue + KnapsackDefault(items, maxWeight - lastItemWeight),
                KnapsackDefault(items, maxWeight));
    }

    private void ExecuteKnapsackDivideConquer() {
        for (List<ItemKnapsack> item : scenarios) {
            List<ItemKnapsack> copyItems = new ArrayList<>(item);

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> KnapsackDivideConquer(copyItems, 1000, copyItems.size() - 1), null);

            AddTaskToExecute(task);
        }

        Execute(scenarios.size(), resultsDivideConquer);
    }

    private int KnapsackDivideConquer(List<ItemKnapsack> items, int maxWeight, int index) {
        if (index < 0 || maxWeight <= 0) return 0;

        int indexWeight = items.get(index).getWeight();
        int indexValue = items.get(index).getValue();

        if (indexWeight > maxWeight) return KnapsackDivideConquer(items, maxWeight, index - 1);
        else {
            int withItem = indexValue + KnapsackDivideConquer(items, maxWeight - indexWeight, index - 1);
            int withoutItem = KnapsackDivideConquer(items, maxWeight, index - 1);
            return Math.max(withItem, withoutItem);
        }
    }

    private void ExecuteKnapsackDynamicProgramming() {
        for (List<ItemKnapsack> item : scenarios) {
            List<ItemKnapsack> copyItems = new ArrayList<>(item);

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> KnapsackDynamicProgramming(copyItems, 1000), null);

            AddTaskToExecute(task);
        }

        Execute(scenarios.size(), resultsDynamicProg);
    }

    private int KnapsackDynamicProgramming(List<ItemKnapsack> items, int maxWeight) {
        int n = items.size();
        int[][] memo = new int[n + 1][maxWeight + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= maxWeight; w++) {
                int indexWeight = items.get(i - 1).getWeight();
                int indexValue = items.get(i - 1).getValue();

                if (indexWeight > w) {
                    memo[i][w] = memo[i - 1][w];
                } else {
                    memo[i][w] = Math.max(memo[i - 1][w], indexValue + memo[i - 1][w - indexWeight]);
                }
            }
        }

        return memo[n][maxWeight];
    }
}
