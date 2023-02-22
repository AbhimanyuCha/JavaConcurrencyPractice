import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    TYPES OF POOLS
    SHUTTING DOWN A SERVICE EXECUTOR
---------------------------------------------------------------------------------------------------
    Java provides 4 types of thread pools :
        1. FixedThreadPool
        2. CachedThreadPool :
            - Has a synchronous queue which holds only 1 task.
            - If no thread is available then it will create a new thread to do the task.
            - Has the ability to kill the thread if its idle for 60 seconds.
        3. ScheduledThreadPool
            - if you want to schedule tasks after some delay or some time interval.
            - can be used when you have to do some checks, lets say some security or auth check.
            - uses delay queue.
        4. SingleThreadedExecutor
            - uses a single thread in the thread pool
            - fetches next task from the blocking queue.
            - recreates thread if its killed.
            - sequential execution of threads.
            - same as fixed thread pool with size 1.



 */
public class ThreadPoolTypes {

    static class SomeTask implements Runnable{
        @Override
        public void run(){
            //Do something.
        }
    }

    public static void cachedThreadPool(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0 ; i < 100 ; i++)
            executorService.execute(new SomeTask());

    }


    public static void scheduledThreadPool() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        executorService.schedule(new SomeTask(),10, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new SomeTask(),15, 10, TimeUnit.SECONDS);
        executorService.scheduleWithFixedDelay(new SomeTask(), 10, 10, TimeUnit.SECONDS);

        executorService.shutdown(); // initiate shutdown, there can be threads which are running but after this they wont be assigned any other tasks.

        //will throw RejectionExecutionException
        //executorService.execute(new Task());

        //will return true
        executorService.isShutdown();


        //will return true if all the tasks are terminated
        executorService.isTerminated();

        //block all tasks which are completed or if timeout occurs.
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        //will initiate shutdown and return all the queued tasks
        List<Runnable> queuedTasks = executorService.shutdownNow();
    }

}
