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

interface Toveiskø<T>{
    void leggInnFørst(T verdi);
    void leggInnSist(T verdi);
    T taUtFørst();
    T taUtSist();
    T kikkFørst();
    T kikkSist();
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
        til = (til+1) % kapasitet;
        antall ++;
    }

    @Override
    public T dequeue() {
        if (tom()) throw new NoSuchElementException("Lista er tom");
        T tmp = a[fra];
        a[fra]=null;
        fra= (fra+1) % kapasitet; // flytter fra en plass videre, fordi vi fjerner det første elementet
        antall--;
        return tmp;
    }

    @Override
    public T peek() {
        if (tom()) throw new NoSuchElementException("Lista er tom");
        return a[fra];
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall==0;
    }

    @Override
    public void nullstill() {
        for (int i = 0; i < antall; i++){ // går gjennom antall elementer i lista- ganger
            a[(fra+i) % kapasitet] = null; // indeksen wrappes når den når kapasiteten,
        }
        fra = 0;
        til = 0;
        antall = 0;
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

class LenketToveiskø<T> implements Toveiskø<T>{
    private Node<T> hode;
    private int antall;
    private Node<T> hale;


    private class Node<T>{
        T verdi;
        Node<T> neste;
        Node<T> forrige;

        public Node(T verdi){
            this(verdi, null, null);
        }
        public Node(T verdi, Node<T> neste, Node<T> forrige){
            this.verdi = verdi;
            this.neste = neste;
            this.forrige = forrige;
        }
    }
    @Override
    public void leggInnFørst(T verdi) {
        if (verdi==null) throw new NullPointerException("Null er ikke lov");
        Node<T> ny = new Node<>(verdi);
        if (antall == 0) {
            hode = ny;
            hale = ny;
        } else {
            ny.neste = hode;
            hode.forrige = ny;
            hode = ny;
        }
        antall++;
    }

    @Override
    public void leggInnSist(T verdi) {
        if (verdi==null) throw new NullPointerException("Null er ikke lov");
        Node<T> ny = new Node<>(verdi);
        if (antall == 0) {
            hode = ny;
            hale = ny;
        } else {
            ny.forrige = hale;
            hale.neste = ny;
            hale = ny;
        }
        antall++;
    }

    @Override
    public T taUtFørst() {
        if (antall==0) throw new NoSuchElementException("Lista er tom");
        T tmp = hode.verdi;
        Node<T> neste = hode.neste;

        hode.verdi=null;
        hode.neste=null;
        hode = neste;

        if (hode==null) hale=null;
        else hode.forrige=null;

        antall--;
        return tmp;
    }

    @Override
    public T taUtSist() {
        if (antall==0) throw new NoSuchElementException("Lista er tom");
        T tmp = hale.verdi;
        Node<T> forrige = hale.forrige;

        hale.verdi=null;
        hale.forrige=null;
        hale = forrige;

        if (hale == null) hode =null;
        else hale.neste =null;

        antall--;
        return tmp;
    }

    @Override
    public T kikkFørst() {
        if (antall==0) throw new NoSuchElementException("Lista er tom");
        return hode.verdi;
    }

    @Override
    public T kikkSist() {
        if (antall==0) throw new NoSuchElementException("Lista er tom");
        return hale.verdi;
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
        Node<T> p = hode;
        while (p!= null){
            Node<T> neste = p.neste;
            p.verdi = null;
            p.neste =null;
            p.forrige = null;
            p = neste;
        }
        hode = null;
        hale = null;
        antall = 0;
    }
}

class TabellToveiskø<T> implements Toveiskø<T>{

    T[] a;
    int fra;
    int til;
    int antall, kapasitet;

    public TabellToveiskø(int kapasitet){
        this.kapasitet = kapasitet;
        a = (T[]) new Object[kapasitet];
    }

    private void utvidTabell() {
        kapasitet *= 2; // (valgfritt) kan droppe dette og kun bruke a.length
        T[] tmp = (T[]) new Object[kapasitet];
        // Vi kan ikke bruke system.arraycopy fordi srcPos og destPos vil endre seg stadig vekk siden det er sirkulært
        for (int i = 0; i < antall; i++){
            tmp[i] = a[(fra+i) % kapasitet]; // % kapasitet sier basically at vi teller fra 0 igjen når den når kapasitet.
        }
        a = tmp;
    }

    @Override
    public void leggInnFørst(T verdi) {
        if (antall == kapasitet) utvidTabell();
        if (verdi == null) throw new NullPointerException("Null er ikke lov");
        fra = (fra-1+kapasitet) % kapasitet; // fra-1 + KAPASITET -> VIKTIG!!!! fordi ellers får man negative tall og det funker ikke ordentlig.
        a[fra] = verdi;
        antall++;
    }

    @Override
    public void leggInnSist(T verdi) {
        if (antall == kapasitet) utvidTabell();
        if (verdi == null) throw new NullPointerException("Null er ikke lov");
        a[til] = verdi; //til peker alltid på FØRSTE LEDIGE POSISJON, ikke siste element. Derfor setter vi verdien inn før vi;
        til = (til+1+kapasitet) % kapasitet;  // flytte til et hakk videre i sirkelen.
        antall++;
    }

    @Override
    public T taUtFørst() {
        if (tom()) throw new NoSuchElementException("Køen er tom");
        T tmp  = a[fra];
        a[fra] = null;
        fra = (fra+1) % kapasitet; //flytter fra et hakk videre, så vi tar + istedenfor -.
        antall--;
        return tmp;
    }

    @Override
    public T taUtSist() {
        if (tom()) throw new NoSuchElementException("Køen er tom");
        // Og, når vi har minus, så må vi alltid plusse på kapasitet fordi ellers så kan negative tall oppstå. Å plusse på kapasitet endrer ikke resultatet fordi det påvirker ikke modulus annet enn å hindre negative tall
        til = (til-1 + kapasitet) % kapasitet; // Flytter FØR vi henter verdien fordi til peker alltid på siste LEDIGE plass, ikke det siste elementet.
        T tmp = a[til];
        a[til] = null;
        antall--;
        return tmp;
    }

    @Override
    public T kikkFørst() {
        if (tom()) throw new NoSuchElementException("Køen er tom");
        return a[fra];
    }

    @Override
    public T kikkSist() {
        if (tom()) throw new NoSuchElementException("Køen er tom");
        return a[(til-1+kapasitet)% kapasitet]; // returnerer verdien til det siste faktiske elemetet (så vi må flytte det et hakk bakover)
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
        for (int i = 0; i < antall; i++){
            a[(fra+i)%kapasitet] = null; // iterer fordi vi plusser i på fra hver gang, men når den når kapasiteten så wrapper den.
        }
        fra= 0;
        til = 0;
        antall = 0;
    }
}
public class OppgaveGPT {
    public static void main(String[] args) {

    }
}
