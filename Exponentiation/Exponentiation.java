package Exponentiation;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import Contracts.IExecutable;
import Conversion.Conversion;
import Scenario.ExponentialScenario;
import TimeCalculation.*;

public class Exponentiation implements IExecutable {

    private final Integer[] bases;
    private final Integer[] exponents;

    private final List<CalculationResult> resultsDefault = new ArrayList<CalculationResult>();
    private final List<CalculationResult> resultsDivideConquer = new ArrayList<CalculationResult>();
    private final List<CalculationResult> resultsDynamicProg = new ArrayList<CalculationResult>();

    private final TimeCalculation timeCalculation = new TimeCalculation();

    public Exponentiation() {
        List<Integer[]> result = ExponentialScenario.GetAll();
        this.bases = result.get(0);
        this.exponents = result.get(1);
    }

    @Override
    public void ExecuteAllMethods() {
        System.out.println("\n\nPROBLEMA EXPONENCIAL\n");
        ExecuteBruteForce();
        ExecuteDivideConquer();
        ExecuteDynamicProgramming();
    }

    @Override
    public void ShowResults() {
        System.out.println("\n\nRESULTADOS DEFAULT\n");
        PrintResults(resultsDefault);

        System.out.println("\n\nRESULTADOS DIVISÃO E CONQUISTA\n");
        PrintResults(resultsDivideConquer);

        System.out.println("\n\nRESULTADOS PROGRAMAÇÃO DINÂMICA\n");
        PrintResults(resultsDivideConquer);
    }

    private void PrintResults(List<CalculationResult> results) {
        int i = 0;
        for(CalculationResult result : results) {
            long operationResult = Conversion.toLong(result.getResult());
            double timeConsumptionInMs = result.getTimeDuration();

            System.out.println(MessageFormat.format("Duração: {0}", timeConsumptionInMs));
            System.out.println(MessageFormat.format("{0} ^ {1} = {2}", bases[i], exponents[i], operationResult));

            i++;
        }
    }

    private void ExecuteBruteForce() {
        ExecutorService service = Executors.newFixedThreadPool(bases.length - 1);
        List<Callable<CalculationResult>> taskList = new ArrayList<>();

        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DefaultExecution(bases[index], exponents[index]);
            }, params);

            taskList.add(task);
        }
        waitAllFuturesToComplete(service, taskList, resultsDefault);
    }

    private long DefaultExecution(int base, int expoente) {
        long resultado = 1;
        for (int i = 0; i < expoente; i++) {
            resultado *= base;
        }
        return resultado;
    }

    private void ExecuteDivideConquer() {
        ExecutorService service = Executors.newFixedThreadPool(bases.length - 1);
        List<Callable<CalculationResult>> taskList = new ArrayList<>();

        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DivideConquer(bases[index], exponents[index]);
            }, params);

            taskList.add(task);
        }
        waitAllFuturesToComplete(service, taskList, resultsDivideConquer);
    }

    private long DivideConquer(int base, int exponent) {
        if (exponent == 0) {
            return 1;
        } else if (exponent % 2 == 0) {
            long result = DivideConquer(base, exponent / 2);
            return result * result;
        } else {
            long result = DivideConquer(base, (exponent - 1) / 2);
            return base * result * result;
        }
    }

    private void ExecuteDynamicProgramming() {
        ExecutorService service = Executors.newFixedThreadPool(bases.length - 1);
        List<Callable<CalculationResult>> taskList = new ArrayList<>();

        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DynamicProgramming(bases[index], exponents[index]);
            }, params);

            taskList.add(task);
        }

        waitAllFuturesToComplete(service, taskList, resultsDynamicProg);
    }

    private long DynamicProgramming(int base, int exponent) {
        long[] result = new long[exponent + 1];
        result[0] = 1;

        for (int i = 1; i <= exponent; i++) {
            result[i] = result[i / 2] * result[i / 2];
            if (i % 2 == 1) {
                result[i] *= base;
            }
        }

        return result[exponent];
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