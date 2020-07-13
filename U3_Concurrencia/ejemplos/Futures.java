package U3_Concurrencia.ejemplos;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Futures {

    public static void main(String[] args) {
        // futureSubmitRunnable1();
        // futureSubmitRunnable2();
        // futureSubmitRunnable3();
        futureSubmitCallable();
    }
    

    private static class MyPrivateRunnable implements Runnable {
        private String name;

        public MyPrivateRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(this.name + " is working...");
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(this.name + " is done");
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(this.name + " was interrupted\n" + this.name + " Thread.currentThread().isInterrupted()=" + Thread.currentThread().isInterrupted());
            }
        }
    }

    private static void shutdownAndTerminate(ExecutorService pool) {
        try {
            long timeout = 100;
            TimeUnit timeunit = TimeUnit.MILLISECONDS;
            System.out.println("Waiting all threads completion for " + timeout + " " + timeunit + " ... ");
            // Blocks until timeout or all threads complete execution,
            // or the current thread is interrupted, whichever happens first.
            boolean isTerminated = pool.awaitTermination(timeout, timeunit);
            System.out.println("isTerminated=" + isTerminated);
            if (!isTerminated) {
                System.out.println("Calling shutdownNow()...");
                List<Runnable> list = pool.shutdownNow();
                System.out.println(list.size() + " threads running");
                isTerminated = pool.awaitTermination(timeout, timeunit);
                if (!isTerminated) {
                    System.out.println("Some threads are still running");
                }
                System.out.println("Exiting");
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void futureSubmitRunnable1() {
        System.out.println("\nfutureSubmitRunnable1():\n");

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Future future = pool.submit(new MyPrivateRunnable("one"));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        try {
            System.out.println(future.get());
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }
    }

    private static void futureSubmitRunnable2() {
        System.out.println("\nfutureSubmitRunnable2():");

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Future future = pool.submit(new MyPrivateRunnable("One"));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        try {
            System.out.println(future.get(1, TimeUnit.SECONDS));
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }
    }

    private static class MyPrivateResult {

        private String name;
        private double result;

        public MyPrivateResult(String name, double result) {
            this.name = name;
            this.result = result;
        }

        @Override
        public String toString() {
            return "Result{name=" + name + ", result=" + result + "}";
        }

    }

    private static void futureSubmitRunnable3() {
        System.out.println("\nfutureSubmitRunnable3():");

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Future<MyPrivateResult> future = pool.submit(new MyPrivateRunnable("Two"), new MyPrivateResult("Two", 42.0));

        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        try {
            System.out.println(future.get());
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }
    }

    private static class MyPrivateCallable implements Callable<MyPrivateResult> {

        private String name;

        public MyPrivateCallable(String name) {
            this.name = name;
        }

        public MyPrivateResult call() {
            try {
                int i = 0;
                while (i < 10) {
                    System.out.println(this.name + " is working...");
                    TimeUnit.MILLISECONDS.sleep(100);
                    i++;
                    if (i < 9) i = 0;
                }

                System.out.println(this.name + " is done");
                return new MyPrivateResult(name, 42.42);
            }
            catch(InterruptedException e) {
                System.out.println(this.name + " was interruped\n" + this.name + "Thread.currentThread().isInterrupted()=" + Thread.currentThread().isInterrupted());
            }
            return null;
        }
    }

    private static void futureSubmitCallable() {
        System.out.println("\nfutureSubmitCallable():");

        ExecutorService pool = Executors.newSingleThreadExecutor();

        Future<MyPrivateResult> future = pool.submit(new MyPrivateCallable("Three"));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        try {
            System.out.println(future.get());
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
        }
    }


}