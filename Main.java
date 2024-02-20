import Exponentiation.Exponentiation;
import KnapsackProblem.Knapsack;
import SubArrayProblem.MaximumSubarray;

public class Main {
    public static void main (String[] args) {
        Exponentiation exponentiation = new Exponentiation();
        MaximumSubarray maximumSubarray = new MaximumSubarray();
        Knapsack knapsackProblem = new Knapsack();

        exponentiation.ExecuteAllMethods();
        exponentiation.ShowResults();

        maximumSubarray.ExecuteAllMethods();
        maximumSubarray.ShowResults();

        knapsackProblem.ExecuteAllMethods();
        knapsackProblem.ShowResults();
    }
}