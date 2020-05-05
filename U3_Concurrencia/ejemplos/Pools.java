package U3_Concurrencia.ejemplos;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pools {

    public static void main(String[] args) {

        //threadPool(Executors.newCachedThreadPool());
        //threadPool(Executors.newFixedThreadPool(2));
        // threadPool(Executors.newScheduledThreadPool(2));
        threadPool(Executors.newWorkStealingPool(2));

    }

    private static void threadPool(ExecutorService pool) {
        String[] names = {"One", "Two", "Three"};

        for (int i = 0; i < names.length; i++) {
            pool.execute(new MyPrivateRunnable(names[i]));
        }

        System.out.println("Before shutdown: isShutdown()=" + pool.isShutdown() + "\n isTerminated()=" + pool.isTerminated());

        pool.shutdown();  // New threads cannot be submitted
        // pool.execute(new MyRunnable("Four")); //java.util.concurrent.RejectedExecutionException
        System.out.println("After shutdown: isShutdown()=" + pool.isShutdown() + "\n isTerminated()=" + pool.isTerminated());

        try {
            long timeout = 100;
            TimeUnit timeunit = TimeUnit.MILLISECONDS;
            System.out.println("Waiting all threads completition for " + timeout + " " + timeunit + " ... ");
            // Blocks until timeout or all threads complete execution,
            // or the current thread is interrupted, whichever happens first.
            boolean isTerminated = pool.awaitTermination(timeout, timeunit);

            System.out.println("isTerminated() = " + isTerminated);
            if (!isTerminated) {
                System.out.println("Calling shutdown()...");
                List<Runnable> list = pool.shutdownNow();
                System.out.println(list.size() + " threads running");
                isTerminated = pool.awaitTermination(timeout, timeunit);
                if (!isTerminated) {
                    System.out.println("Some thread are still running");
                }
                System.out.println("Exiting");
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MyPrivateRunnable implements Runnable {
        
        private String name;

        public MyPrivateRunnable(String name) {
            this.name = name;
        }

        public void run() {
            try {
                while (true) {
                    System.out.println(this.name + " is working...");
                    TimeUnit.SECONDS.sleep(1);
                }
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.name + " was interrupted\n" + this.name + " Thread.currentThread().isInterrupted=" + Thread.currentThread().isInterrupted());
            }
        }
    }
}