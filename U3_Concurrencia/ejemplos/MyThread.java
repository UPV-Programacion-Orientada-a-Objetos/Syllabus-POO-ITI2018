package U3_Concurrencia.ejemplos;

import java.util.concurrent.TimeUnit;

public class MyThread extends Thread {

    private String parameter;

    public MyThread(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        while (!"exit".equals(this.parameter)) {
            System.out.println((isDaemon() ? "daemon" : "user") + " thread " + this.getName() + "(id=" + this.getId() + ") parameter: " + this.parameter);
            pauseOneSecond();
        }

        System.out.println((isDaemon() ? "daemon" : " user") + " thread " + this.getName() + "(id=" + this.getId() + ") parameter: " + this.parameter);
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
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