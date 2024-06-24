package Problems;


import java.util.concurrent.*;

/**
 * The problem is to stop a thread when a specific condition is met.
 */
public class StoppingThread {
    /**
     * Way to stop a thread using volatile boolean flag.
     */
    static volatile boolean stop = false;
    void method1(){
        Thread t = new Thread(() -> {
            // some task
            while(!stop){
                //keep doing the work
            }

            System.out.println("Thread stopped");
        });

        t.start();
        stop = true;
    }

    /**
     * Way to stop a thread in executors service
     */
    void method2() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            while(!Thread.currentThread().isInterrupted()){
                // do some task
                System.out.println("Thread is running");
            }
            System.out.println("Thread stopped");
        });
        Thread.sleep(5);
        executorService.shutdown();
        executorService.shutdownNow(); // internally calls the interrupt() method for the threads
    }

    /**
     * Way to stop a thread in futures
     */
    void method3(){
        Future<?> future = Executors.newFixedThreadPool(1).submit(() -> {
            while(!Thread.currentThread().isInterrupted()){
                // do some task
                System.out.println("Thread is running");
            }
            System.out.println("Thread stopped");
        });
        try {
            Thread.sleep(5);
            future.cancel(true); // calls thread.interrupt() internally
        }catch (InterruptedException e) {

        }
    }

    /**
     * Way to stop a thread in completable future : use cancel(true) method to interrupt the thread and wait for it to complete
     */

    void method4(){

        CompletableFuture<?> completableFuture = CompletableFuture.runAsync(() -> {
            while(!Thread.currentThread().isInterrupted()){
                //do some task
                System.out.println("Thread is running");
            }
            System.out.println("Thread stopped");
        });


        try {
            Thread.sleep(5);
            completableFuture.cancel(true); // calls thread.interrupt() internally
//            completableFuture.get(2 , TimeUnit.MILLISECONDS); // waits for the thread to complete
        }catch (InterruptedException e) {

        }
    }

    /**
     * Stopping a thread after a specific time
     * 1. Sleep the thread and then interrupt it
     * 2. Use a ScheduledExecutorService to schedule a task to interrupt the thread after a specific time
     * 3. For Futures and CompletableFutures, use .get(time, TimeUnit) to wait for the thread to complete, and then interrupt it in the exception handler
     */

    void scheduledStop(){
        CompletableFuture<?> completableFuture = CompletableFuture.runAsync(() -> {
            while(!Thread.currentThread().isInterrupted()){
                //do some task
                System.out.println("Thread is running");
            }
            System.out.println("Thread stopped");
        });


        try {
            completableFuture.get(2 , TimeUnit.MILLISECONDS); // waits for the thread to complete
//            completableFuture.get(2 , TimeUnit.MILLISECONDS); // waits for the thread to complete
        }catch (InterruptedException e) {
            completableFuture.cancel(true);
            System.out.println("Thread stopped after 2 seconds");
        } catch (ExecutionException e) {
            completableFuture.cancel(true);
            System.out.println("Thread stopped after 2 seconds");
        } catch (TimeoutException e) {
            completableFuture.cancel(true);
            System.out.println("Thread stopped after 2 seconds");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new StoppingThread().scheduledStop();
    }
}
