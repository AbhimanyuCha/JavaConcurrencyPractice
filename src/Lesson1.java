/*

we will cover 3 things :

    1. Visibility Problem : use of "volatile" keyword
    2. Synchronization Problem : user of "synchronized" keyword
    3. How to create basic threads.

 */


public class Lesson1 {

    //How to create a thread ?
    /*
        - By manually creating a thread and passing a class object which implements Runnable interface.
     */

    public static void createThreadExample(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Total available processors : " + Runtime.getRuntime().availableProcessors());
            }
        });

        t1.start();
    }

    //Visibility Problem :
    /*
        - using volatile keyword avoids visibility problem.
     */
    static volatile boolean flag = true; // volatile can only be used with variables in the heap, not in stack memory.
    public static void visibilityDemo(){
         Thread t1 = new Thread(new Runnable() {
             @Override
             public void run() {
                 System.out.println("Thread-1 read the value of flag as " + flag);
                 flag = false;
                 System.out.println("Thread-1 changed the value of flag to " + flag);
             }
         });

         //T1 has pushed the value of flag from thread local cache to shared cache so thread2 should also read the value
         //as false.
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(flag) {
                    System.out.println("Thread-2 read the value of flag as " + flag);
                }
                System.out.println("Thread-2 read the value of flag as " + flag);
            }
        });

        t1.start();
        t2.start();
    }

    /*
        Synchronization problem :
            - It happens when more than one thread try to change the variable value,
            - synchronize keyword is used to not allow more than 1 thread to change the value of the variable.

        The main thread spawns 2 thread below, T1 and T2, both of which want to increment the value of count.
     */

    static volatile int count = 0;
    synchronized static void increment(int val){
        count += val;
    }
    static void synchroDemo() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                increment(1);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                increment(2);
            }
        });

        t1.start();
        t2.start();
        t1.join(); // join means wait for the thread to complete
        t2.join();
        System.out.println("final value of count = " + count);
    }

    public static void main(String[] args) throws InterruptedException {
//        createThreadExample();
//        visibilityDemo();
        for(int i = 0 ; i < 100 ; i++)
            synchroDemo();
    }
}
