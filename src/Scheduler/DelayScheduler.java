package Scheduler;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class DelayScheduler {
    private final DelayQueue<DelayTask> queue;
//    private final PriorityBlockingQueue
    public DelayScheduler(){
        this.queue = new DelayQueue<>();

    }
}
