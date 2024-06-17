package Problems;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
public class ProducerConsumerBlockingQueue {

    class Producer implements Runnable {
        private final BlockingQueue<Integer> sharedQueue;

        public Producer(BlockingQueue<Integer> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    System.out.println("Produced: " + i);
                    sharedQueue.put(i);
                    Thread.sleep(100); // Simulating some time taken to produce an item
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    class Consumer implements Runnable {
        private final BlockingQueue<Integer> sharedQueue;

        public Consumer(BlockingQueue<Integer> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Integer item = sharedQueue.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(200); // Simulating some time taken to consume an item
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void run(){
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Consumer(blockingQueue)).start();
    }
    public static void main(String[] args) {
        new Thread(() -> new ProducerConsumerBlockingQueue().run()).start();
    }

}
