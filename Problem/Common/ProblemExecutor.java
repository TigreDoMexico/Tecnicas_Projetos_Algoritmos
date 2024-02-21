package Problem.Common;

import Contracts.ICallback;
import TimeCalculation.CalculationResult;
import TimeCalculation.TimeCalculation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class ProblemExecutor {
    private final List<Callable<CalculationResult>> taskList = new ArrayList<>();

    protected void AddTaskToExecute(Callable<CalculationResult> task) {
        taskList.add(task);
    }

    protected void Execute(int poolSize, List<CalculationResult> result) {
        ExecutorService service = Executors.newFixedThreadPool(poolSize);

        WaitAllFuturesToComplete(service, taskList, result);

        taskList.clear();
    }

    private void WaitAllFuturesToComplete(ExecutorService service,
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
