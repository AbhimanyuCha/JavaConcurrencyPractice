package Problems;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue {

    // Imp with synchronzied block and volatile
    class BoundedBlockingQueue1 {
        private final int[] queue;
        private volatile int size = 0;
        private int wp = 0, rp = 0;
        public BoundedBlockingQueue1(int capacity) {
            this.queue = new int[capacity];
        }

        public void enqueue(int element) throws InterruptedException {
            synchronized (this) {
                while(size == queue.length) {
                    wait();
                }
                queue[wp++] = element;
                size++;
                wp %= queue.length;
                notifyAll();
            }
        }

        public int dequeue() throws InterruptedException {
            synchronized (this) {
                while(size == 0) {
                    wait();
                }
                int res = queue[rp++];
                size--;
                rp %= queue.length;
                notifyAll();
                return res;
            }
        }

        public int size() {
            return size;
        }
    }

    // Imp with ReentrantLock and Condition variable
    class BoundedBlockingQueue2 {
        private final int[] queue;
        private volatile int size = 0;
        private int wp = 0, rp = 0;
        private ReentrantLock lock = new ReentrantLock();
        private Condition full = lock.newCondition();
        private Condition empty = lock.newCondition();

        public BoundedBlockingQueue2(int capacity) {
            this.queue = new int[capacity];
        }

        public void enqueue(int element) throws InterruptedException {
            lock.lock();

            try{
                while(size == queue.length) {
                    full.await();
                }
                queue[wp++] = element;
                size++;
                wp %= queue.length;

                empty.signalAll();
            }finally{
                lock.unlock();
            }
        }

        public int dequeue() throws InterruptedException {
            lock.lock();
            try{
                while(size == 0) {
                    empty.await();
                }
                int res = queue[rp++];
                size--;
                rp %= queue.length;
                full.signalAll();
                return res;
            }finally {
                lock.unlock();
            }
        }

        public int size() {
            return size;
        }
    }

    // Imp with Semaphore
    class BoundedBlockingQueue3 {
        private final int[] queue;
        private volatile int size = 0;
        private int wp = 0, rp = 0;
        Semaphore enqSem, deqSem, lockSem;

        public BoundedBlockingQueue3(int capacity) {
            this.queue = new int[capacity];
            enqSem = new Semaphore(capacity);
            deqSem = new Semaphore(0);
            lockSem = new Semaphore(1);
        }

        public void enqueue(int element) throws InterruptedException {
            enqSem.acquire();
            lockSem.acquire();

            queue[wp++] = element;
            size++;
            wp %= queue.length;

            lockSem.release();
            deqSem.release();
        }

        public int dequeue() throws InterruptedException {
            deqSem.acquire();
            lockSem.acquire();

            int res = queue[rp++];
            size--;
            rp %= queue.length;

            lockSem.release();
            enqSem.release();
            return res;
        }

        public int size() {
            return size;
        }
    }
}
