package Problem.SubArrayProblem;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import Problem.Common.IExecutable;
import Conversion.Conversion;
import Problem.Common.ProblemExecutor;
import Scenario.MaximumSubarrayScenario;
import TimeCalculation.*;

public class MaximumSubarray extends ProblemExecutor implements IExecutable {
    private final ArrayList<Integer[]> scenarios;

    private final List<CalculationResult> resultsDefault = new ArrayList<>();
    private final List<CalculationResult> resultsDivideConquer = new ArrayList<>();
    private final List<CalculationResult> resultsDynamicProg = new ArrayList<>();

    private final TimeCalculation timeCalculation = new TimeCalculation();

    public MaximumSubarray() {
        super();
        scenarios = new ArrayList<>(MaximumSubarrayScenario.GetAll());
    }

    @Override
    public void ExecuteAllMethods() {
        System.out.println("\nPROBLEMA MAX SUBARRAY\n");

        ExecuteBruteForce();
        ExecuteDivideAndConquer();
        ExecuteDynamicProgramming();
    }

    @Override
    public void ShowResults() {
        System.out.println("\n\nRESULTADOS FORÇA BRUTA\n");
        PrintResults(resultsDefault);

        System.out.println("\n\nRESULTADOS DIVISÃO E CONQUISTA\n");
        PrintResults(resultsDivideConquer);

        System.out.println("\n\nRESULTADOS PROGRAMAÇÃO DINÂMICA\n");
        PrintResults(resultsDynamicProg);
    }

    private void PrintResults(List<CalculationResult> results) {
        int i = 0;
        for (CalculationResult result : results) {
            long operationResult = Conversion.toLong(result.getResult());
            double timeConsumptionInMs = result.getTimeDuration();

            System.out.println(MessageFormat.format("Duração: {0}", timeConsumptionInMs));
            System.out.println(
                    MessageFormat.format("SUBARRAY ({0} ELEMENTOS) => {1} ", scenarios.get(i).length, operationResult));

            i++;
        }
    }

    private void ExecuteBruteForce() {
        for (Integer[] scenario : scenarios) {
            Callable<CalculationResult> task = () -> timeCalculation.CalculateTimeConsuming(
                    args -> BruteForce(scenario), null);

            AddTaskToExecute(task);
        }

        Execute(scenarios.size(), resultsDefault);
    }

    private int BruteForce(Integer[] array) {
        if (array.length == 0) {
            return 0;
        }

        int maxSum = array[0];
        int i, j;

        for (i = 0; i < array.length; i++) {
            for (j = 0; j < array.length; j++) {
                int currentSum = 0;
                int k;

                for (k = i; k <= j; k++) {
                    currentSum += array[k];
                }

                maxSum = Math.max(currentSum, maxSum);
            }
        }

        return maxSum;
    }

    private void ExecuteDivideAndConquer() {
        for (Integer[] scenario : scenarios) {
            Callable<CalculationResult> task = () ->  timeCalculation.CalculateTimeConsuming(
                    args -> DivideAndConquer(scenario, 0, scenario.length - 1), null);

            AddTaskToExecute(task);
        }

        Execute(scenarios.size(), resultsDivideConquer);
    }

    private int DivideAndConquer(Integer[] array, int minIndex, int maxIndex) {
        if (maxIndex <= minIndex) {
            return array[minIndex];
        }

        int middleIndex = (maxIndex + minIndex) / 2;

        int maxSumLeft = Integer.MIN_VALUE;
        int sum = 0, i;

        for (i = middleIndex; i >= minIndex; i--) {
            sum += array[i];
            if (sum > maxSumLeft) {
                maxSumLeft = sum;
            }
        }

        int maxSumRight = Integer.MIN_VALUE;
        sum = 0;
        for (i = middleIndex + 1; i <= maxIndex; i++) {
            sum += array[i];
            if (sum > maxSumRight) {
                maxSumRight = sum;
            }
        }

        int sumFirstHalf = DivideAndConquer(array, minIndex, middleIndex);
        int sumSecondHalf = DivideAndConquer(array, middleIndex + 1, maxIndex);

        int maxSumBetweenHalf = Math.max(sumFirstHalf, sumSecondHalf);
        return Math.max(maxSumBetweenHalf, maxSumLeft + maxSumRight);
    }

    private void ExecuteDynamicProgramming() {
        for (Integer[] scenario : scenarios) {
            Callable<CalculationResult> task = () ->   timeCalculation.CalculateTimeConsuming(
                    args -> DynamicProgramming(scenario), null);

            AddTaskToExecute(task);
        }

        Execute(scenarios.size(), resultsDynamicProg);
    }

    private int DynamicProgramming(Integer[] array) {
        int maxSum = array[0];
        int currentSum = array[0];

        for (int i = 1; i < array.length; i++) {
            currentSum = Math.max(currentSum + array[i], array[i]);
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }
}
