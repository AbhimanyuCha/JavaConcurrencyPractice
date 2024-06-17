package Problems;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Description:
 * Implement a shared resource that uses read-write locks to manage concurrent read and write access.
 */
public class ReadWriteLockProblem {
    private int resource;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

    private void read(){
        try {
            var timeWaited = new Random().nextInt(4);
            Thread.sleep(timeWaited);
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + " : Reading the resource : " + resource + " at time " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
        }
    }

    private void write(int val){
        try {
            int timeWaited = new Random().nextInt(2);
            Thread.sleep(timeWaited);
            writeLock.lock();
            this.resource = val;
            System.out.println(Thread.currentThread().getName() + " : Wrote value : " + val + " at time " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockProblem obj = new ReadWriteLockProblem();
        Thread readerThreads = new Thread(()->{
            for(int i = 0 ; i < 5 ; i++) {
                Thread readerThread = new Thread(() -> {obj.read();});
                readerThread.start();
            }
        });

        Thread writerThreads = new Thread(()->{
            for(int i = 0 ; i < 5 ; i++) {
                int finalI = i;
                Thread writerThread = new Thread(() -> {obj.write(finalI);});
                writerThread.start();
            }
        });

        try {
            writerThreads.start();
            readerThreads.start();
        }finally {
//            readerThreads.join();
//            writerThreads.join();
        }

    }

}
