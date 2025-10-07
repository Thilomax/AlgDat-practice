package ForelesningerØving.F10_Stacks_n_Queues;

import java.util.NoSuchElementException;

public class LenketStabel<T> implements Stabel<T>{

    private Node<T> hode;
    private int antall;

    private class Node<T> {
        T verdi;        // dataen vi lagrer i denne noden (av typen T)
        Node<T> neste;  // peker på neste node i kjeden, eller null hvis det er siste node

        public Node(T verdi){
           this(verdi, null);
        }
        public Node(T verdi, Node<T> neste){
            this.verdi = verdi;
            this.neste = neste;
        }

        /**Hvorfor ha to? I 90% av tilfellene lager du en ny node uten å kjenne “neste” enda. Da er new Node<>(verdi) kort og tydelig, og “neste” blir automatisk null.*/
    }
    @Override
    public void push(T verdi) {
        if (verdi == null) throw new NullPointerException("Null er ikke lov");
        Node<T> ny = new Node<>(verdi);
        ny.neste = hode;
        hode = ny;
        antall++;
    }

    @Override
    public T pop() {
        if (tom()) throw new NoSuchElementException("Lista er tom");
        T tmp = hode.verdi;
        hode = hode.neste;
        antall--;
        return tmp;

    }

    @Override
    public T peek() {
        if (tom()) throw new NoSuchElementException("Lista er tom");
        return hode.verdi;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return (antall == 0);
    }

    @Override
    public void nullstill() {
        while (hode != null) {
            Node<T> neste = hode.neste; // ta vare på pekeren videre
            hode.verdi = null;          // null ut verdien (hjelper GC)
            hode.neste = null;          // koble fra neste
            hode = neste;               // flytt hode videre
        }
        antall = 0;
    }
}
