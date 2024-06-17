package Problems;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerProblem {
    final int MAX_CAPACITY = 10;
    Queue<Integer> queue = new LinkedList<>();
    void consume() {
        try {
            while (true) {
                synchronized (this) {
                    while (queue.isEmpty()) {
                        wait();
                    }
                    int front = queue.poll();
                    System.out.println(Thread.currentThread().getName() + " Consuming : " + front);
                    notifyAll();
                    Thread.sleep((long) (100));
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    void produce() {
        try {
            for (int i = 0; i < 20; i++) {
                Thread.sleep(10);
                synchronized (this) {
                    while (queue.size() == MAX_CAPACITY) {
                        wait();
                    }
                    final int val = (int) (Math.random() * 10);
                    queue.add(val);
                    System.out.println(Thread.currentThread().getName() + " Producing : " + val);
                    notifyAll();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args) {
        ProducerConsumerProblem pd = new ProducerConsumerProblem();

        for(int i = 0 ; i < 2; ++i)
            new Thread(() -> {pd.consume();}).start(); // spinning up 2 consumer threads

        new Thread(() -> {pd.produce();}).start();
    }
}
