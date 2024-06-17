package Problems;

import java.util.concurrent.*;

/**
 Description:

 This problem involves performing an asynchronous computation using Java's Future and Callable interfaces.
 A Callable is similar to a Runnable but can return a result.
 A Future represents the result of an asynchronous computation.

 Objective:

 Implement an asynchronous task that performs a computation and returns the result.
 Use Callable to define the task and Future to retrieve the result once the computation is complete.
 Tasks:

 Create a Callable task that:
 Performs a computation (e.g., calculating a sum).
 Returns the result of the computation.
 Submit the task to an executor service.
 Use Future to get the result of the computation after the task is complete.

 */
public class FuturesAndCallables {
    static ExecutorService executorService = Executors.newFixedThreadPool(10);
    static class MyCallableTask implements Callable{

        @Override
        public Long call() throws Exception {
            long sum = 0;
            for(int i = 0 ; i < 1000000000L ; i++)
                sum += i;
            System.out.println("100 Sum : " + System.currentTimeMillis());
            return sum;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        Callable<Integer> task = () -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
                Thread.sleep(100);
            }
            System.out.println("10 sum : " + System.currentTimeMillis());
            return sum;
        };
        Future<Long> future = executorService.submit(new MyCallableTask());
        Future<Integer> futureCompact = executorService.submit(task);


        executorService.shutdown();
    }
}
