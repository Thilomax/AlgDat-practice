package ForelesningerØving.F10_Stacks_n_Queues;

import ForelesningerØving.F8_Lister.Liste;
import ForelesningerØving.F8_Lister.TabellListe;

import java.util.NoSuchElementException;
import java.util.Objects;

public class Oving2 {
    public static void main(String[] args) {

    }
}
interface Stabel2<T>{
    void push(T t);
    T pop();
    T peek();
    boolean tom();
}
interface kø2<T>{
    void enqueue(T t);
    T dequeue();
    T peek();
    boolean tom();
}

class TabellListeSomStabel<T> implements Stabel2<T>{
    private T[] tabell;
    private int antall, kapasitet;

    public TabellListeSomStabel(){
        this(10);
    }
    public TabellListeSomStabel(int kapasitet){
        this.kapasitet=kapasitet;
        tabell = (T[]) new Object[kapasitet];
        antall = 0;
    }

    @Override
    public void push(T t) {
        if (antall==kapasitet)
            utvid();
        tabell[antall++] = t;
    }
    private void utvid(){
        kapasitet*=2;
        T[] tmp = (T[]) new Object[kapasitet];
        System.arraycopy(tabell, 0, tmp, 0, antall);
        tabell = tmp;
    }

    @Override
    public T pop() {
        if (tom())
            throw new NoSuchElementException("Tom stabel");
        T tmp = tabell[--antall];
        tabell[antall]=null;
        return tmp;
    }

    @Override
    public T peek() {
        if (tom())
            throw new NoSuchElementException("tom stabel");
        return tabell[antall-1];
    }

    @Override
    public boolean tom() {
        return (antall==0);
    }
}
class LenketListeSomKø<T> implements kø2<T>{

    Node hode, hale;

    public LenketListeSomKø(){
        hode = null; hale = null;
    }

    private class Node{
        T verdi;
        Node neste;

        public Node(T verdi){
            this(verdi,null);
        }

        public Node(T verdi, Node neste){
            this.verdi = verdi;
            this.neste = neste;
        }
    }

    @Override
    public void enqueue(T t) {
        Objects.requireNonNull(t);
        Node ny = new Node(t);
        if (hode==null){
            hode = hale = ny;
        } else{
            hale.neste = ny;
            hale = ny;
        }
    }

    @Override
    public T dequeue() {
        if (tom())
            throw new NoSuchElementException("Tom kø");
        T tmp = hode.verdi;
        hode = hode.neste;
        if (tom())
            hale = null;
        return tmp;
    }

    @Override
    public T peek() {
        if (tom())
            throw new NoSuchElementException("tom kø");
        return hode.verdi;
    }

    @Override
    public boolean tom() {
        return hode==null;
    }
}
