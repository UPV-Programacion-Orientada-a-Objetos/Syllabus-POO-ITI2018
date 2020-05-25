package U1_ManejodeErroresyExcepciones.ejemplos;

import java.io.FileNotFoundException;
import java.util.Vector;

public class ThrowExample {

    public static void main(String[] args) {
        int evenNumbers[] = { 3, 4, 7, 8, 9 };
        Vector<EvenInteger> vei = new Vector<EvenInteger>();

        for (int element : evenNumbers) {
            try {
                vei.add(boxInteger(element));
            } catch (NotEvenIntegerException e) {
                // se maneja realiza el flujo alternativo
                e.printStackTrace();
             }
        }

        vei.forEach(vecItem->System.out.println(vecItem));
        
    }

    public static EvenInteger boxInteger(int n) throws NotEvenIntegerException {
        EvenInteger ei = new EvenInteger(n);

        return ei;
    }
}