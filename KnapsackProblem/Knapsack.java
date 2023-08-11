package KnapsackProblem;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import Contracts.IExecutable;
import Conversion.Conversion;
import Generators.KnapsackGenerator;
import TimeCalculation.*;

public class Knapsack implements IExecutable {
    private List<List<ItemKnapsack>> itens = new ArrayList<List<ItemKnapsack>>();

    private List<CalculationResult> resultsDefault = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDivideConquer = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDynamicProg = new ArrayList<CalculationResult>();

    private TimeCalculation timeCalculation = new TimeCalculation();

    public Knapsack() {
        super();
        itens.add(KnapsackGenerator.GenerateRandomList(50));
        itens.add(KnapsackGenerator.GenerateRandomList(75));
        itens.add(KnapsackGenerator.GenerateRandomList(100));
        itens.add(KnapsackGenerator.GenerateRandomList(150));
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
        System.out.println("\n\nRESULTADOS DEFAULT\n");
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
                    MessageFormat.format("KNAPSACK ({0} ELEMENTOS) => {1} ", itens.get(i).size(), resultadoOperacao));

            i++;
        }
    }

    private void ExecuteKnapsackDefault() {
        ExecutorService service = Executors.newFixedThreadPool(itens.size());
        List<Callable<CalculationResult>> taskList = new ArrayList<Callable<CalculationResult>>();

        for (int i = 0; i < itens.size(); i++) {
            String[] params = { Integer.toString(i) };
            List<ItemKnapsack> copyItens = new ArrayList<ItemKnapsack>(itens.get(i));

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                return KnapsackDefault(copyItens, 1000);
            }, params);

            taskList.add(task);
        }

        waitAllFuturesToComplete(service, taskList, resultsDefault);
    }

    private int KnapsackDefault(List<ItemKnapsack> itens, int maxWeight) {
        if (itens.isEmpty() || maxWeight == 0)
            return 0;

        int lastIndex = itens.size() - 1;
        int lastItemWeight = itens.get(lastIndex).getWeight();
        int lastItemValue = itens.get(lastIndex).getValue();

        if (lastItemWeight > maxWeight) {
            itens.remove(lastIndex);
            return KnapsackDefault(itens, maxWeight);
        }

        itens.remove(lastIndex);
        return retornaMaior(
                lastItemValue + KnapsackDefault(itens, maxWeight - lastItemWeight),
                KnapsackDefault(itens, maxWeight));
    }

    private void ExecuteKnapsackDivideConquer() {
        ExecutorService service = Executors.newFixedThreadPool(itens.size());
        List<Callable<CalculationResult>> taskList = new ArrayList<Callable<CalculationResult>>();

        for (int i = 0; i < itens.size(); i++) {
            String[] params = { Integer.toString(i) };
            List<ItemKnapsack> copyItens = new ArrayList<ItemKnapsack>(itens.get(i));

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                return KnapsackDivideConquer(copyItens, 1000, copyItens.size() - 1);
            }, params);

            taskList.add(task);
        }

        waitAllFuturesToComplete(service, taskList, resultsDivideConquer);
    }

    private int KnapsackDivideConquer(List<ItemKnapsack> itens, int maxWeight, int index) {
        if (index < 0 || maxWeight <= 0) {
            return 0;
        }

        int pesoDoIndex = itens.get(index).getWeight();
        int valorDoIndex = itens.get(index).getValue();

        if (pesoDoIndex > maxWeight) {
            return KnapsackDivideConquer(itens, maxWeight, index - 1);
        } else {
            int withItem = valorDoIndex + KnapsackDivideConquer(itens, maxWeight - pesoDoIndex, index - 1);
            int withoutItem = KnapsackDivideConquer(itens, maxWeight, index - 1);
            return Math.max(withItem, withoutItem);
        }
    }

    private void ExecuteKnapsackDynamicProgramming() {
        ExecutorService service = Executors.newFixedThreadPool(itens.size());
        List<Callable<CalculationResult>> taskList = new ArrayList<Callable<CalculationResult>>();

        for (int i = 0; i < itens.size(); i++) {
            String[] params = { Integer.toString(i) };
            List<ItemKnapsack> copyItens = new ArrayList<ItemKnapsack>(itens.get(i));

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                return KnapsackDynamicProgramming(copyItens, 1000);
            }, params);

            taskList.add(task);
        }
        waitAllFuturesToComplete(service, taskList, resultsDynamicProg);
    }

    private int KnapsackDynamicProgramming(List<ItemKnapsack> itens, int maxWeight) {
        int n = itens.size();
        int[][] memo = new int[n + 1][maxWeight + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= maxWeight; w++) {
                int pesoIndex = itens.get(i - 1).getWeight();
                int valorIndex = itens.get(i - 1).getValue();

                if (pesoIndex > w) {
                    memo[i][w] = memo[i - 1][w];
                } else {
                    memo[i][w] = Math.max(memo[i - 1][w], valorIndex + memo[i - 1][w - pesoIndex]);
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
                    ex.printStackTrace();
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        service.shutdown();
    }

    private int retornaMaior(int a, int b) {
        return a > b ? a : b;
    }
}
