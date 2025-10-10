package ForelesningerØving.F10_Stacks_n_Queues;

public interface Stabel<T>{
    void push(T verdi);
    T pop();
    T peek();
    int antall();
    boolean tom();
    void nullstill();
}
