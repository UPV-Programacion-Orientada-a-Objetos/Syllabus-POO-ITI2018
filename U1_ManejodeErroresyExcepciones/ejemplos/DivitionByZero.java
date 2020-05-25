package U1_ManejodeErroresyExcepciones.ejemplos;

public class DivitionByZero {
    public static void main(String[] args) {
        int a = 12;
        int b = 0;
        double c;

        try {
           c = a / b;
        }
        catch(ArithmeticException e) {
            e.printStackTrace();
            c = 0;
        }
        

        System.out.println(c);
    }
}