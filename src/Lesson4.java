/*

 FUTURES
 -------------------------------------------------------------------------------
 Use case :
    We have created the tasks by implementing Runnable interface and passed the tasks to the executor service.

    Suppose we want some value to be returned from the task.
    We cannot change the run method to return some value.

    So now we need to implement Callable inorder to achieve this functionality.
    Callable takes the generic parameter of the type of response which needs to be returned.

    When we submit a callable task, we get a return type of "Future" which is a placeholder for that value
    which will be returned when the task is executed.

 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Lesson4 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //response is the placeholder which will have the value from the task once its completed.
        Future<Integer> response = executorService.submit(new Task());


        //meanwhile you can perform some other unrelated operation

        //to get the value. (1s)
        System.out.println(response.get()); // if by this time the future does not have the value then it will wait here,
        //thus making it as a blocking operation.


        //Similarly you can submit 100 tasks and store the placeholder future in an arraylist.
        List<Future> futureList = new ArrayList<Future>();
        for(int i = 0 ; i < 100 ; i++)
            futureList.add(executorService.submit(new Task()));

        //The method get has overloaded implementation which has a timeout time argument, if the task takes time more
        //than the specified timeout time, then it will throw timeout exception.
        Integer responseValue = response.get(100, TimeUnit.MILLISECONDS);
        System.out.println(responseValue);

        executorService.shutdownNow();
    }

    static class Task implements Callable<Integer>{
        @Override
        public Integer call() throws Exception{
            Thread.sleep(1000);
            return 12;
        }
    }

}
