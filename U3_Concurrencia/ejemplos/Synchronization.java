package U3_Concurrencia.ejemplos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Synchronization {

    public static void main(String[] args) {
        invokeAllCallables(new PrivateCalculatorNoSync());
        // invokeAllCallables(new CalculatorAtomicRef());
        // invokeAllCallables(new CalculatorSyncMethod());
        // invokeAllCallables(new CalculatorSyncBlock());
    }

    private interface Calculator {
        String getDescription();
        double calculate(int i);
    }

    private static class PrivateCalculatorNoSync implements Calculator {
        private double prop;
        private String description = "Without synchronization";

        public String getDescription() {
            return description;
        }

        public double calculate(int i) {
            try {
                this.prop = 2.0 * i;
                TimeUnit.MICROSECONDS.sleep(i);
                return Math.sqrt(this.prop);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }

            return 0.0;
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
            return "MyPrivateResult{name=" + name + ", result=" + result + "}";
        }
    }

    private static class MyCallable implements Callable<MyPrivateResult> {
        private String name;
        private Calculator calculator;

        public MyCallable(String name, Calculator calculator) {
            this.name = name;
            this.calculator = calculator;
        }

        public MyPrivateResult call() {
            double sum = 0.0;
            for (int i = 1; i < 20; i++) {
                sum += calculator.calculate(i);
            }

            return new MyPrivateResult(name, sum);
        }
    }

    private static void invokeAllCallables(Calculator c) {
        System.out.println("\n" + c.getDescription() + ":");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Callable<MyPrivateResult>> tasks = List.of(new MyCallable("One", c), new MyCallable("Two", c));

        try {
            List<Future<MyPrivateResult>> futures = pool.invokeAll(tasks);
            List<MyPrivateResult> results = new ArrayList<>();
            
            while(results.size() < futures.size()) {
                TimeUnit.MILLISECONDS.sleep(5);
                for(Future future: futures) {
                    if (future.isDone()) {
                        results.add((MyPrivateResult) future.get());
                    }
                }
            }
            for (MyPrivateResult result: results) {
                System.out.println(result);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            shutdownAndTerminate(pool);
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
            System.out.println("isTerminated()=" + isTerminated);

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

    private static class CalculatorAtomicRef implements Calculator {
        private AtomicReference<Double> prop = new AtomicReference<>(0.0);
        private String description = "Using AtomicReference";
        
        public String getDescription() {
            return description;
        }

        public double calculate(int i) {
            try {
                Double currentValue = prop.get();
                TimeUnit.MILLISECONDS.sleep(i);
                boolean b = this.prop.compareAndSet(currentValue, 2.0 * i);
                // System.out.println(b);   // Prints: true for one thread
                                            // and false for another thread
                return Math.sqrt(this.prop.get());
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }
            return 0.0;
        }
    }

    private static class CalculatorSyncMethod implements Calculator {

        private double prop;
        private String description = "Using synchronized method";

        public String getDescription() {
            return description;
        }

        synchronized public double calculate(int i) {
            try {
                this.prop = 2.0 * i;
                TimeUnit.MILLISECONDS.sleep(i);
                return Math.sqrt(this.prop);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }

            return 0.0;
        }
    }

    private static class CalculatorSyncBlock implements Calculator {
        private double prop;
        private String description = "Using synchronized block";

        public String getDescription() {
            return description;
        }

        public double calculate(int i) {
            try {
                // there maybe some other code here
                synchronized (this) {
                    this.prop = 2.0 * i;
                    TimeUnit.MILLISECONDS.sleep(i);
                    return Math.sqrt(this.prop);
                }
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Calculator was interrupted");
            }
            return 0.0;
        }
    }
    
}