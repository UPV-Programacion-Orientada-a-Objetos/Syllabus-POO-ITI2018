package U1_ManejodeErroresyExcepciones.ejemplos;



/**
 * clase que almacena sólo números pares
 */
public class EvenInteger {

    Integer intValue;

    
    //public EvenInteger(int n) throws NotEvenIntegerException {
    public EvenInteger(int n) {
        if (n%2 != 0) {
                // throw new NotEvenIntegerException("Sólo se pueden almacenar números pares");                 
                throw new NumberFormatException("Sólo se puede almacenar números pares");
        }

        this.intValue = n;
    }

    @Override
    public String toString() {
        return intValue.toString();
    }
}
