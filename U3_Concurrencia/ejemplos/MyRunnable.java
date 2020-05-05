package U3_Concurrencia.ejemplos;

import java.util.concurrent.TimeUnit;

public class MyRunnable implements Runnable {

    private String parameter;
    private String name;

    public MyRunnable(String name) {
        this.name = name;
    }

    public void run() {
        while (!"exit".equals(parameter)) {
            System.out.println("thread" + this.name + ", parameter:" + parameter);
            pauseOneSecond();
        }
        System.out.println("thread " + this.name + ", parameter: " + parameter);
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private void pauseOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}