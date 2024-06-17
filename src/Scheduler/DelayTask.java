package Scheduler;

import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class DelayTask extends FutureTask implements Delayed {

    private final long startTime;
    private final Runnable task;
    DelayTask(long startTime, Runnable task){
        super(task , null);
        this.startTime = startTime;
        this.task = task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = this.startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS) ,
                o.getDelay(TimeUnit.MILLISECONDS));
    }
}
