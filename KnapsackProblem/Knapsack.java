package KnapsackProblem;

import java.util.*;
import Contracts.IExecutable;
import TimeCalculation.*;

public class Knapsack implements IExecutable {
    private List<CalculationResult> resultsDefault = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDivideConquer = new ArrayList<CalculationResult>();
    private List<CalculationResult> resultsDynamicProg = new ArrayList<CalculationResult>();

    private TimeCalculation timeCalculation = new TimeCalculation();

    @Override
    public void ExecuteAllMethods() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ExecuteAllMethods'");
    }

    @Override
    public void ShowResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ShowResults'");
    }

    private void ExecuteKnapsackDefault() {
        
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

    private int retornaMaior(int a, int b) {
        return a > b ? a : b;
    }
}
