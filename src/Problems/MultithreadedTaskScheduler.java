package Problems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
url :: https://enginebogie.com/public/charchaa/post/concurrent-task-scheduler-popular-multi-threading-interview-question/46
 Problem:

 You need to implement a concurrent task scheduler that can schedule and execute multiple tasks concurrently using multiple threads.
 The task scheduler should allow adding tasks with associated priorities, and the tasks with higher priority should be executed before the tasks with lower priority.
 The program should ensure efficient utilization of resources and proper synchronization among the threads.

 Requirements:

 The task scheduler should have a fixed number of worker threads that are responsible for executing the tasks.
 Each task should have an associated priority value (integer) indicating its priority. The lower the value, the higher the priority.
 Tasks with higher priority values should be executed before the tasks with lower priority values.
 The task scheduler should ensure that only one task is executed at a time by a worker thread.
 The task scheduler should provide a method to add tasks with their priority to the scheduler.
 The task scheduler should execute the tasks concurrently using multiple worker threads.
 The task scheduler should print the execution order of the tasks as they are completed.
 Example:

 TaskScheduler scheduler = new TaskScheduler(3);

 scheduler.addTask("Task A", 2);
 scheduler.addTask("Task B", 1);
 scheduler.addTask("Task C", 3);
 scheduler.addTask("Task D", 2);
 scheduler.addTask("Task E", 1);

 scheduler.start();
 Output:

 Executing Task B
 Executing Task E
 Executing Task A
 Executing Task D
 Executing Task C

 */
public class MultithreadedTaskScheduler {

    class Task{
        public String name;
        public int priority;

        Task(String name , int priority){
            this.name = name;
            this.priority = priority;
        }

        public int getPriority(){
            return priority;
        }
        public String getName(){
            return name;
        }
    }

    PriorityQueue<Task> taskQueue = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority).thenComparing(Task::getName));
    int capacity;
    static final int DEFAULT_CAPACITY = 100;
    int size;
    ReentrantLock lock = new ReentrantLock();
    Condition notEmpty = lock.newCondition();
    Condition notFull = lock.newCondition();
    ExecutorService executorService;
    public MultithreadedTaskScheduler(int threads) throws InterruptedException {
        init(DEFAULT_CAPACITY, threads);
    }
    public MultithreadedTaskScheduler(int threads , int capacity) throws InterruptedException {
        init(capacity, threads);
    }

    public void init(int capacity, int threads) throws InterruptedException {
        this.capacity = capacity;
        executorService =  Executors.newFixedThreadPool(threads);
    }

    /**
     *
     * @param taskName
     * @param priority
     *
     * adds a task to the queue if possible otherwise wait for the queue to have size < capacity
     */
    public void addTask(String taskName, int priority) throws InterruptedException {
        lock.lock();
        try {
            while(size >= capacity){
                notFull.await();
            }
            taskQueue.add(new Task(taskName,priority));
            size++;
            notEmpty.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void poll() throws InterruptedException  {
        lock.lock();
        try{
            while(size == 0){
                notEmpty.await();
            }
            size--;
//            Thread.sleep((long)Math.random() * 1000);
            System.out.println(taskQueue.poll().name + " Executing");
            notFull.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void start() throws InterruptedException {
        while(!taskQueue.isEmpty()){
            executorService.submit(() -> {
                try {
                    poll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        System.out.println("All tasks completed");
    }

    public static void main(String[] args) throws InterruptedException {
        MultithreadedTaskScheduler scheduler = new MultithreadedTaskScheduler(3);

        scheduler.addTask("Task A", 2);
        scheduler.addTask("Task B", 1);
        scheduler.addTask("Task C", 3);
        scheduler.addTask("Task D", 2);
        scheduler.addTask("Task E", 1);

        scheduler.start();
    }

}
