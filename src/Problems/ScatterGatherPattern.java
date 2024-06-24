package Problems;


import java.util.concurrent.Callable;
import java.util.concurrent.*;
import java.util.*;

/**
 * Problem Statement : Retrieve data from multiple sources concurrently waiting for maximum 3 seconds
 *
 * Solution :
 *  1. Use CompletableFutures.allOf to combine multiple CompletableFutures, then use.get() to wait for all tasks to complete with timeouts
 *  2. Use CountDownLatch to keep track of how many tasks are still running
 */
public class ScatterGatherPattern {
    private class MyTask {
        String url;
        Integer productId;
        CountDownLatch latch;
        Set<Integer> set;

        public MyTask(String s, int i, Set<Integer> set) {
            this.url = s;
            this.productId = i;
            this.set = set;
            latch = new CountDownLatch(1); // signal that this task is running
        }

        public MyTask(String s, int id , CountDownLatch latch, Set<Integer> set) {
            this.latch = latch;
            this.url = s;
            this.set = set;
            this.productId = id;
        }


        public Integer call() {
            //make http calls to the url
            // Do some work here.
            try {
                Thread.sleep(1500); // simulate some work
                latch.countDown(); // signal that this task is done
                set.add(productId); // add productId to the set
                return productId;
            }catch (InterruptedException e) {

            }
            return null;
        }
    }

    public void main() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Set<Integer> set = Collections.synchronizedSet(new HashSet<>());
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> new MyTask("http://example.com/product1", 1 , set).call() , executorService);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> new MyTask("http://example.com/product2", 2, set).call() , executorService);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> new MyTask("http://example.com/product3", 3, set).call() , executorService);

        CompletableFuture<Void> futures = CompletableFuture.allOf(future1, future2, future3);
        try {
            futures.get(3, TimeUnit.SECONDS); // if all tasks complete within 3 seconds, get() will return null
            System.out.println(set.toString());
        }catch (InterruptedException | ExecutionException | TimeoutException e) {

        }
    }

    public void main2() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);
        Set<Integer> set = Collections.synchronizedSet(new HashSet<>());
        executorService.submit(() -> new MyTask("http://example.com/product1", 1, latch, set).call() , executorService);
        executorService.submit(() -> new MyTask("http://example.com/product2", 2, latch, set).call() , executorService);
        executorService.submit(() -> new MyTask("http://example.com/product3", 3, latch, set).call() , executorService);

        latch.await(3, TimeUnit.MILLISECONDS); // wait for the countdown latch to reach 0 or timeout whatever comes first
        System.out.println(set.toString());
    }

    public static void main(String[] args) throws InterruptedException {
        new ScatterGatherPattern().main2();
    }
}
