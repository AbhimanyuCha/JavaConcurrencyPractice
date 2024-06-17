import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantLocksBasics {
    private static Lock lock = new ReentrantLock();
    // Reentrant lock allows you to lock multiple times, useful in recursive method where the thread is locking
    // an already locked method.

    private static Lock fair_lock = new ReentrantLock(true);
    private static Lock unfair_lock = new ReentrantLock(false);


    // The thread which has waited the longest will be given the lock - thats the fairness if true is passed.
    // There is no thread starvation.
    // unfair lock is faster as there is no queue, but there may be some thread which is waiting for a long time.

    private static void accessResource2() throws InterruptedException {
        boolean isLocked = lock.tryLock(); // we can prevent the thread from waiting to take a lock and
        // rather do something else
        boolean isLocked2 = lock.tryLock(5, TimeUnit.SECONDS); // we can pass a timeout for which we need to try
        // locking

        if(isLocked){
            try {
            } finally {
                lock.unlock();
            }
        }else{
            //do something else.
        }
    }


    private static void accessResource(){
        lock.lock();
        //...... code
        lock.unlock();
    }
    //its very similar to synchronized keyword
    /*
      private static void accessResource(){
        synchronized(this){ //same as lock.lock()
            //... code
        }
       }                    //same as lock.unlock()
     */


    /*
        Difference :
            - Synchronized is implicit, locks are explicit.
            - locks allow locking and unlocking in any scope, allow lock in one method and remove lock in other method,
              and in any order.
            - ability to tryLock() and tryLock(timeout)
            - Other methods :
                - isHeldByCurrentThread() : tells if some other thread is holding this lock or not.
                - getQueueLength() : tells the queue length waiting to access the lock.
                - newCondition
     */


    /**
     * READ WRITE LOCKS

     - multiple threads can acquire the read lock and only a single thread can acquire the write lock
     - either read lock is being used (by n threads) or a write lock is being used (by 1 thread)
     */
    static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    static class MyTask implements Runnable{

        @Override
        public void run() {
            readLock.lock();
            System.out.println("Accessing resource");
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        t1.start();
        t2.start();
        t3.start();
    }

}
