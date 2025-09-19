package ForelesningerØving.F9_Iterator;

/*Oppgave A - Forklar oversettelsen mellom enhanced for-loop for generiske elementer, og Iterator<T> sin iteratormetode
*
* enhanced for-loop:
* for (T t : b){
*
* }
* Blir oversatt av java til:
* Iterator<T> it = b.iterator();
* while(it.hasNext()){
*    T t = it.next();
* }
*
* Når man skriver en enhanced for-loop, så skjer det mer bak kulissene.
* START: Java henter en Iterator fra objektet ditt (b)
*  Men, b MÅ implementere Iterable-grensesnittet.
*   En iterator er som en pekepinne som peker på første element
*
* SJEKK: For hver runde spør Java til iteratoren: hasNext().
*   Har jeg et element å gi til deg, iterator?
*
* HENT: Hvis iteratoren returnerer ja, så kaller Java på next()-metoden
*   Da får du elementet til den neste posisjonen, og pekepinnen flytter til neste element
*
* GJENTA: Dette gjentas. Koden i blokken av for-loopen kjøres, og så starter SJEKKEN på nytt.
* */


import java.util.NoSuchElementException;
import java.util.Iterator;

class TabellListe<T> implements Iterable<T> { // Må implementere Iterable<T> for at for-each skal fungere.

    private T[] a;                 // Arrayet som holder elementene
    private int antall, kapasitet; // Antall elementer som faktisk er lagt inn, og kapasiteten

    // Konstruktør for å lage en ny liste med gitt kapasitet.
    public TabellListe(int kapasitet) {
        this.kapasitet = kapasitet;
        a = (T[]) new Object[kapasitet];
        antall = 0;
    }

    // Oppgave B: Standard iterator → starter alltid på indeks 0.
    @Override
    public Iterator<T> iterator() {
        return new TabellListeIterator(0);
    }

    // Oppgave C: Ny metode som gir en iterator som starter på valgt indeks.
    // F.eks. iterator(2) starter på elementet med indeks 2.
    public Iterator<T> iterator(int start) {
        sjekkIndeks(start);              // Validerer at start er innenfor [0, antall]
        return new TabellListeIterator(start); // Returnerer en iterator som starter på start
    }

    // Sjekker at indeksen er lovlig.
    private void sjekkIndeks(int indeks) {
        if (0 <= indeks && indeks <= antall) return;
        throw new IndexOutOfBoundsException("Ugyldig indeks");
    }

    // Indre klasse: Selve iteratoren.
    // Har sin egen posisjonsvariabel "denne", som holder styr på hvor vi er i arrayet.
    private class TabellListeIterator implements Iterator<T> {
        private int denne = 0;

        // Konstruktør for oppgave C → lar oss starte på en valgt posisjon.
        public TabellListeIterator(int start) {
            this.denne = start;
        }

        @Override
        public boolean hasNext() {
            // Sjekker om det finnes flere elementer å hente.
            return (denne < antall);
        }

        @Override
        public T next() {
            // Hvis ingen flere elementer, kast exception (Iterator-kontrakten).
            if (!hasNext()) throw new NoSuchElementException("Ikkeno neste element");

            // Hent element på nåværende posisjon
            T tmp = a[denne];

            // Flytt posisjonen ett steg frem
            denne++;

            // Returner elementet vi hentet
            return tmp;
        }
    }
}

class EnkeltLenketListe<T> implements Iterable<T>{

    private class Node<T> {
        T verdi;        // dataen vi lagrer i denne noden (av typen T)
        Node<T> neste;  // peker på neste node i kjeden, eller null hvis det er siste node

        //En konstruktør som kaller på konstruktøren med parameteret neste
        public Node(T verdi) {
            this(verdi, null);  // delegasjon: bruk hovedkonstruktøren og sett neste = null
        }
        //Konstruktør med parametere
        public Node(T verdi, Node<T> neste){
            this.verdi = verdi; // all faktisk initiering skjer her
            this.neste = neste;
        }
        /**Hvorfor ha to? I 90% av tilfellene lager du en ny node uten å kjenne “neste” enda. Da er new Node<>(verdi) kort og tydelig, og “neste” blir automatisk null.*/
    }


    // Variabel som peker på den første noden i lista.
    // Når lista er tom er hode = null.
    // Hvis lista har elementer, så peker hode på "hodenoden" (den første i kjeden).
    private Node<T> hode;
    private Node<T> hale;
    private int antall;


    @Override
    public Iterator<T> iterator() {
        return new EnkeltLenketListeIterator();
    }

    private class EnkeltLenketListeIterator implements Iterator<T>{

        private Node<T> p; //denne peker på neste node som skal leses, på en måte en erstatning til "denne"
        private boolean fjernOK; //denne sjekker om de er lov å kalle remove(). Fordi remove kan bare fjerne elementet som ble returnert av SISTE KALL PÅ NEXT(), og man kan ikke kalle  remove() to ganger på rad uten et nytt next() imellom
        private Node<T> q; //pekeren på noden FØR P
        public EnkeltLenketListeIterator(){
            p = hode;
            fjernOK = false;
        }
        @Override
        public boolean hasNext() {
            return p!=null; /*  Hvorfor er p alltid «neste node»?
                                Fordi next() er laget til å flytte p framover etter at du har hentet verdien.
                                Dermed står p hele tiden klar på den neste noden som skal leveres i et eventuelt nytt next().*/
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException("Det er ikke noe neste element");
            fjernOK = true; //fordi nå er det lov å fjerne siden vi har gjort next
            T tmp = p.verdi;
            q= p;           // flytter q til noden som kommer til å være før p
            p = p.neste; //flytter pekeren p til neste node og..
            return tmp; //returnerer den tidligere noden
        }


        @Override
        public void remove() {
            if (!fjernOK) throw new IllegalStateException("Ikke lov");
            if (q==hode){
                hode=p;
                if (p==null) hale=null;
                q.verdi=null;
                q.neste=null;
                q=null;
                antall--; //fordi vi fjerna en node
                fjernOK = false; // fordi man kan ikke kjøre remove to ganger på rad.
                return;

            } else {
                Node<T> r = hode;
                while (r!=null && r.neste!=q){
                    r=r.neste;
                }
                r.neste=p; //hopper over q, fordi vi vil fjerne q

                if (p==null) hale=r; //hvis p = null, betyr det at q var siste node

                q.verdi=null;
                q.neste=null;
                q=null;
                antall--;
                fjernOK = false; // fordi man kan ikke kjøre remove to ganger på rad.

            }
        }
    }

}

public class IteratorOppgave {
}
