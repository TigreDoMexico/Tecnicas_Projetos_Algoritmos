import Exponentiation.Exponentiation;
import KnapsackProblem.Knapsack;
import SubArrayProblem.MaximumSubarray;

public class Main {
    public static void main (String[] args) {
        Exponentiation exponentiation = new Exponentiation();
        MaximumSubarray maximumSubarray = new MaximumSubarray();
        Knapsack knapsackProblem = new Knapsack();

        knapsackProblem.ExecuteAllMethods();
        knapsackProblem.ShowResults();

        exponentiation.ExecuteAllMethods();
        exponentiation.ShowResults();

        maximumSubarray.ExecuteAllMethods();
        maximumSubarray.ShowResults();

    }
}