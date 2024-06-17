package Problems;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class DiningPhilosophersProblem {
    static class Fork{
        volatile boolean inUse = false;
    }

    static class Philosopher{
        Fork left, right;
        Philosopher(Fork left, Fork right){
            this.left = left;
            this.right = right;
        }

        private void doAction(String action) throws InterruptedException {
            System.out.println(
                    Thread.currentThread().getName() + " " + action);
            Thread.sleep(((int) (Math.random() * 100)));
        }
        private void eat() throws InterruptedException {
            Thread.currentThread().sleep((long)Math.random() * 100);
            doAction(System.nanoTime()
                    + ": eating");
        }

        private void think() throws InterruptedException {
            Thread.currentThread().sleep((long)Math.random() * 10);
            doAction(System.nanoTime()
                    + ": Thinking");
        }

        private void act() throws InterruptedException {
            synchronized (left){
                synchronized (right){
                    doAction(System.nanoTime()
                            + ": Picked both the forks");
                    eat();
                    doAction(System.nanoTime()
                            + ": Put down both the forks");
                }
            }
        }
        public void start(){
            try{
                think();
                act();
            } catch (InterruptedException e) {
                System.out.println("Interrupted " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

    }

    public static Fork forks[] = new Fork[5];
    static {
        for(int i = 0 ; i < 5 ; i++){
            forks[i] = new Fork();
        }
    }
    public static Philosopher philosophers[] = new Philosopher[5];

    public static void main(String[] args) {
        Thread threads[] = new Thread[5];
        for(int i = 0 ; i < 5 ; i++){
            int finalI = i;
            threads[i] = new Thread(() -> {
                philosophers[finalI] = new Philosopher(forks[finalI] , forks[(finalI + 4) % 5]);
                philosophers[finalI].start();
            });
            threads[i].start();
        }
    }
}
