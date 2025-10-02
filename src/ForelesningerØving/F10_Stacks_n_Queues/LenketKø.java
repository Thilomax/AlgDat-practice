package ForelesningerØving.F10_Stacks_n_Queues;

import java.util.NoSuchElementException;

public class LenketKø<T> implements Kø<T>{
    private Node<T> hode;
    private int antall;
    private Node<T> hale;

    private class Node<T> {
        T verdi;
        Node<T> neste;

        public Node(T verdi){
            this(verdi, null);
        }
        public Node(T verdi, Node<T> neste){
            this.verdi = verdi;
            this.neste = neste;
        }
    }

    @Override
    public void enqueue(T verdi) {
        if (verdi ==null) throw new NullPointerException("Null er ikke lov");
        Node<T> ny = new Node<>(verdi);
        if (antall == 0){
            hode = ny;
            hale = ny;
        } else{
            hale.neste = ny;
            hale= ny;
        }
        antall++;
    }

    @Override
    public T dequeue() {
        if (tom()) throw new NoSuchElementException("Køen er tom");
        T tmp = hode.verdi;
        hode.verdi = null;
        hode = hode.neste;
        if (hode== null) hale = null; //hvis køen er tom etter man fjerna hode, så må hale også nulles ut.
        antall--;
        return tmp;
    }

    @Override
    public T peek() {
        if (tom()) throw new NoSuchElementException("Køen er tom");
        return hode.verdi;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        Node<T> p = hode;
        while(p!=null){
            Node<T> neste = p.neste;
            p.verdi = null;
            p.neste = null;
            p = neste;
        }
        hode = null;
        hale = null;
        antall = 0;
    }
}
