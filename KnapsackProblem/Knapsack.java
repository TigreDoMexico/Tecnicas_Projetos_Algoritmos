package KnapsackProblem;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import Contracts.IExecutable;
import Conversion.Conversion;
import Scenario.KnapsackScenario;
import TimeCalculation.*;

public class Knapsack implements IExecutable {
    private final List<List<ItemKnapsack>> items;

    private final List<CalculationResult> resultsDefault = new ArrayList<>();
    private final List<CalculationResult> resultsDivideConquer = new ArrayList<>();
    private final List<CalculationResult> resultsDynamicProg = new ArrayList<>();

    private final TimeCalculation timeCalculation = new TimeCalculation();

    public Knapsack() {
        super();
        items = KnapsackScenario.GetAll();
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
                    MessageFormat.format("KNAPSACK ({0} ELEMENTOS) => {1} ", items.get(i).size(), resultadoOperacao));
            i++;
        }
    }

    private void ExecuteKnapsackDefault() {
        ExecutorService service = Executors.newFixedThreadPool(items.size());
        List<Callable<CalculationResult>> taskList = new ArrayList<>();

        for (List<ItemKnapsack> item : items) {
            List<ItemKnapsack> copyItems = new ArrayList<>(item);
            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> KnapsackDefault(copyItems, 1000), null);

            taskList.add(task);
        }

        waitAllFuturesToComplete(service, taskList, resultsDefault);
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
        ExecutorService service = Executors.newFixedThreadPool(items.size());
        List<Callable<CalculationResult>> taskList = new ArrayList<Callable<CalculationResult>>();

        for (List<ItemKnapsack> item : items) {
            List<ItemKnapsack> copyItems = new ArrayList<>(item);

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> KnapsackDivideConquer(copyItems, 1000, copyItems.size() - 1), null);
            taskList.add(task);
        }

        waitAllFuturesToComplete(service, taskList, resultsDivideConquer);
    }

    private int KnapsackDivideConquer(List<ItemKnapsack> items, int maxWeight, int index) {
        if (index < 0 || maxWeight <= 0) {
            return 0;
        }

        int indexWeight = items.get(index).getWeight();
        int indexValue = items.get(index).getValue();

        if (indexWeight > maxWeight) {
            return KnapsackDivideConquer(items, maxWeight, index - 1);
        } else {
            int withItem = indexValue + KnapsackDivideConquer(items, maxWeight - indexWeight, index - 1);
            int withoutItem = KnapsackDivideConquer(items, maxWeight, index - 1);
            return Math.max(withItem, withoutItem);
        }
    }

    private void ExecuteKnapsackDynamicProgramming() {
        ExecutorService service = Executors.newFixedThreadPool(items.size());
        List<Callable<CalculationResult>> taskList = new ArrayList<>();

        for (List<ItemKnapsack> item : items) {
            List<ItemKnapsack> copyItems = new ArrayList<>(item);

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> KnapsackDynamicProgramming(copyItems, 1000), null);
            taskList.add(task);
        }
        waitAllFuturesToComplete(service, taskList, resultsDynamicProg);
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

    private void waitAllFuturesToComplete(ExecutorService service,
            List<Callable<CalculationResult>> tasks,
            List<CalculationResult> resultList) {
        try {
            List<Future<CalculationResult>> futures = service.invokeAll(tasks);

            for (Future<CalculationResult> future : futures) {
                try {
                    CalculationResult result = future.get();
                    resultList.add(result);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(MessageFormat.format("Error: {0}", ex));
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(MessageFormat.format("Error: {0}", ex));
        }

        service.shutdown();
    }
}
