import Exponentiation.Exponentiation;
import SubArrayProblem.MaximumSubarray;

public class Main {
    public static void main (String[] args) {
        Exponentiation exponentiation = new Exponentiation();
        MaximumSubarray maximumSubarray = new MaximumSubarray();

        exponentiation.ExecuteAllMethods();
        exponentiation.ShowResults();

        maximumSubarray.ExecuteAllMethods();
        maximumSubarray.ShowResults();
    }
}