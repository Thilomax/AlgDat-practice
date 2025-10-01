package ForelesningerØving.F11_Prioritetskø;

class UsortertPrioKø<T>{
    private T[] a;
    private int antall;
    private int kapasitet;

    public UsortertPrioKø(int kapasitet) {
        this.kapasitet = kapasitet;
        a = (T[]) new Object[kapasitet];
        antall = 0;
    }
}

public class Prioritetskø {
    public static void main(String[] args) {

    }
}
