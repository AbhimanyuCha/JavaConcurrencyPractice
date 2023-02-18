/*
    EXECUTOR SERVICE FRAMEWORK
    IDEAL POOL SIZE
----------------------------------------------------------------------------------------

    - Java Thread Pool Framework.


    - 1 java thread corresponds to 1 OS thread.

    lets create a task :
        static class Task implements Runnable{
            public void run(){
                System.out.println("Thread name is " + Thread.currentThread().getName());
            }
        }

     we can run this any number of tasks using the threads we have in the OS.
     for(int i = 0 ; i < 10 ; i++){
        Thread t = new Thread(new Task());
        t.start();
     }


     it will create thread and when the operations are done by the threads, then JVM will kill those tasks.

     but what if I create 1000 threads ? by running the for loop 1000 times.
     creating a thread is an expensive operation.

     By best prac, we need a fixed number of threads - we call it a thread pool and then we submit 1000 tasks
     to them, this is done using an executor service which keeps a thread pool and a blocking queue.
     in the blocking queue it will keep storing the tasks and the threads will fetch the next task from the queue
     and execute it.

     The queue which holds the task should be thread safe and hence it is called as a blocking queue.


     Ideal pool size ?
        - Depends on the type of task you want to execute.
        - Lets say if its CPU intensive task then, and if you have a large number of threads then it will do a time split
          scheduling and do frequent thread context switches.
        - In this case the ideal size of the thread pool is to have the same number of cores.
        - If the tasks are IO intensive, then the size of the pool can be large, as the context switches will be better
          as we dont want the current set of threads to be occupied in waiting, so meanwhile they are in waiting state,
          we can run other task and want a context switch.

 */

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lesson2 {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for(int i = 0 ; i < 100 ; i++){
            executorService.execute(new IOTask());
        }
    }

    static class IOTask implements Runnable{
        @Override
        public void run(){
            //** do something.
        }
    }
}
