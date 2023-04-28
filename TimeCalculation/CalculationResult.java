package TimeCalculation;

public class CalculationResult {
    private double timeDuration;
    private Object result;

    public CalculationResult(double timeDuration, Object result) {
        this.timeDuration = timeDuration;
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public double getTimeDuration() {
        return timeDuration;
    }
}
