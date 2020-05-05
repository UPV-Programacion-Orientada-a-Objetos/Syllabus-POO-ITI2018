
package U3_Concurrencia.ejemplos;

import java.util.concurrent.TimeUnit;

public class Threads {

    public static void main(String[] args) {
        //runExtendedThreads();
        runImplementsRunnable();
    }

    private static void runExtendedThreads() {
        MyThread thr1 = new MyThread("One");
        thr1.start();
        MyThread thr2 = new MyThread("Two");
        thr2.setDaemon(true);
        thr2.start();
        pauseOneSecond();
        thr1.setParameter("exit");
        pauseOneSecond();
        System.out.println("Main thread exists");
    }

    private static void runImplementsRunnable() {
        MyRunnable myRunnable1 = new MyRunnable("One");
        MyRunnable myRunnable2 = new MyRunnable("Two");

        Thread thr1 = new Thread(myRunnable1);
        thr1.start();
        Thread thr2 = new Thread(myRunnable2);
        thr2.setDaemon(true);
        pauseOneSecond();
        myRunnable1.setParameter("exit");
        pauseOneSecond();
        System.out.println("Main thread exists");

    }

    private static void pauseOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}