package Problem.KnapsackProblem;

public class ItemKnapsack {
    private int weight;
    private int value;

    public ItemKnapsack(int weight, int value) {
        this.weight = weight;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }
}