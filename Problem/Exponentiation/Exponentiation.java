package Problem.Exponentiation;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import Problem.Common.IExecutable;
import Conversion.Conversion;
import Problem.Common.ProblemExecutor;
import Scenario.ExponentialScenario;
import TimeCalculation.*;

public class Exponentiation extends ProblemExecutor implements IExecutable {

    private final Integer[] bases;
    private final Integer[] exponents;

    private final List<CalculationResult> resultsDefault = new ArrayList<>();
    private final List<CalculationResult> resultsDivideConquer = new ArrayList<>();
    private final List<CalculationResult> resultsDynamicProg = new ArrayList<>();

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
        PrintResults(resultsDynamicProg);
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
       for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DefaultExecution(bases[index], exponents[index]);
            }, params);

            AddTaskToExecute(task);
       }

       Execute(bases.length, resultsDefault);
    }

    private long DefaultExecution(int base, int exponent) {
        long resultado = 1;
        for (int i = 0; i < exponent; i++) {
            resultado *= base;
        }
        return resultado;
    }

    private void ExecuteDivideConquer() {
        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DivideConquer(bases[index], exponents[index]);
            }, params);

            AddTaskToExecute(task);
        }

        Execute(bases.length, resultsDivideConquer);
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
        for(int i = 0; i < bases.length; i++) {
            String[] params = { Integer.toString(i) };

            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(args -> {
                int index = Integer.parseInt(args[0]);
                return DynamicProgramming(bases[index], exponents[index]);
            }, params);

            AddTaskToExecute(task);
        }

        Execute(bases.length, resultsDynamicProg);
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
}