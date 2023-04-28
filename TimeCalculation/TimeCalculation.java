package TimeCalculation;

import Contracts.ICallback;

public class TimeCalculation {
    public CalculationResult CalculateTimeConsuming(ICallback callback, String[] args) {
        long initialT = System.currentTimeMillis();

        Object result = callback.call(args);

        long finalT = System.currentTimeMillis();
        double totalTime = ((finalT - initialT)/1000.0);
        return new CalculationResult(totalTime, result);
    }
}
