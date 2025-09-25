package ForelesningerØving.F10_Stacks_n_Queues;

import java.util.NoSuchElementException;

interface Stabel<T>{
    void push(T verdi);
    T pop();
    T peek();
    int antall();
    boolean tom();
    void nullstill();
}

interface Kø<T>{
    void enqueue(T verdi);
    T dequeue();
    T peek();
    int antall();
    boolean tom();
    void nullstill();
}

interface Deque<T>{
    void leggInnførst(T verdi);
    void leggInnSist(T verdi);
    T taUtførst();
    T taUtSist();
    T peekFørst();
    T peekSist();
    int antall();
    boolean tom();
    void nullstill();
}

//STABLER / FILO

class TabellStabel<T> implements Stabel<T> {
    // a: underliggende tabell som lagrer elementene i stabelen
    // Invariant: gyldige elementer ligger i a[0 .. antall-1]
    private T[] a;

    // antall: hvor mange elementer som faktisk ligger i stabelen nå
    // kapasitet: nåværende lengde på a (kan alternativt hente via a.length)
    private int antall, kapasitet;

    public TabellStabel() {
        this(10); // fornuftig standardstart
    }

    public TabellStabel(int kapasitet) {
        // (valgfritt) valider at kapasitet >= 1 for å unngå ubrukelige tilstander
        this.kapasitet = kapasitet;
        // generisk array-opprettelse krever cast i Java
        a = (T[]) new Object[kapasitet];
        antall = 0; // starter tom
    }

    // Dobler størrelsen når tabellen er full.
    // Dette gir amortisert O(1) innsetting (push) over mange operasjoner.
    private void utvidTabell() {
        kapasitet *= 2; // (valgfritt) kan droppe dette og kun bruke a.length
        T[] tmp = (T[]) new Object[kapasitet];
        // rask kopi av de antall gyldige elementene. srcPos og destPos er begge null, fordi det er source Start position og destination Start Position
        System.arraycopy(a, 0, tmp, 0, antall);
        a = tmp;
    }

    @Override
    public void push(T verdi) {
        if (antall==kapasitet) { /**Hvis tabellen er full, så utvider vi*/
            utvidTabell();
        }
        a[antall++] = verdi; /**Vi plasserer verdien t på indeksen antall, og deretter øker antall med 1*/
    }

    @Override
    public T pop() {
        if (antall==0) throw new NoSuchElementException("Lista er tom");
        T tmp = a[antall-1]; //lagrer verdien av det siste elementet for å returnere
        a[--antall] = null;  //minker antall først (fordi posisjonen av det siste elementet er antall -1) og så setter det til null. Det gjør at vi slipper å minke antall i en ny linje
        return tmp;
    }

    @Override
    public T peek() {
        if (antall==0) throw new NoSuchElementException("Lista er tom");
        return a[antall-1];
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return (antall==0);
    }

    @Override
    public void nullstill() {
        antall = 0;
    }
}

class LenketStabel<T> implements Stabel<T>{

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


//KØER / FIFO

class TabellKø<T> implements Kø<T>{

    T[] a;
    int fra;
    int til;
    int antall, kapasitet;

    public TabellKø(int kapasitet) {
        this.kapasitet = kapasitet;
        a = (T[]) new Object[kapasitet];
    }

    public TabellKø() {
        this(10);
    }

    private void utvidTabell() {
        kapasitet *= 2; // (valgfritt) kan droppe dette og kun bruke a.length
        T[] tmp = (T[]) new Object[kapasitet];
        // Vi kan ikke bruke system.arraycopy fordi srcPos og destPos vil endre seg stadig vekk siden det er sirkulært
        for (int i = 0; i < antall; i++){
            tmp[i] = a[(fra+i) % kapasitet];
        }
        a = tmp;
    }

    @Override
    public void enqueue(T verdi) {
        if (a.length == antall) utvidTabell();
        a[til] = verdi;
        til = (til+1) % antall;
        antall ++;
    }

    @Override
    public T dequeue() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public int antall() {
        return 0;
    }

    @Override
    public boolean tom() {
        return antall==0;
    }

    @Override
    public void nullstill() {

    }
}

class LenketKø<T> implements Kø<T>{
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
        hale.neste = ny;
        hale= ny;
        antall++;
    }

    @Override
    public T dequeue() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public int antall() {
        return 0;
    }

    @Override
    public boolean tom() {
        return false;
    }

    @Override
    public void nullstill() {

    }
}

public class OppgaveGPT {
    public static void main(String[] args) {

    }
}
