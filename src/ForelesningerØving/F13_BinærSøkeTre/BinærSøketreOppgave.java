package ForelesningerØving.F13_BinærSøkeTre;

import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

class BinærSøketre<T> implements Beholder<T> {
    Node<T> rot;
    Comparator<? super T> cmp; //Som en variabel for binærsøketre, i form av Comparator


    public BinærSøketre(Comparator<? super T> cmp) {
        this.cmp = cmp;
        this.rot = null;
    }

    private static final class Node<T>{

        T verdi;
        Node<T> venstre;
        Node<T> høyre;
        public Node(T verdi){
            this.verdi = verdi;
            this.venstre = null;
            this.høyre= null;
        }

    }

    //DEL 2: LEGG INN
    @Override
    public boolean leggInn(T t) {
        return leggInnIterativt(t);
    }


    //REKURSIV VARIANT

    private Node<T> leggInnRekursivt(Node<T> current, T t){
        if (current == null){
            return new Node<>(t);
        }
            if (cmp.compare(t, current.verdi)<0) {  // hvis t er MINDRE ENN p.verdi, så gir cmp.compare ut en negativ verdi.
                current.venstre = leggInnRekursivt(current.venstre, t); //da kaller vi på leggInnRekursivt på venstre barnet.
                // Dette sikrer at vi går riktig retning hvor mindre enn forelder er til venstre og større eller == er på høyre
                // , og vi gjør det frem til vi når en tom node.
            } else
                current.høyre = leggInnRekursivt(current.høyre, t); // Vi setter resultatet av den rekursive metoden til å være current.høyre fordi:
                                                                    // det rekursive kallet returnerer en ny node hvis vi har funnet et tomt sted i treet.
                                                                    // Dermed kobler vi denne nye noden inn som høyre barn på current.
        return current;
    }

    /*så boolean leggInnRekursvit gjør bare at man ikke må sende inn roten når man kaller på metoden*/
    private boolean leggInnRekursivt(T t){
        Objects.requireNonNull(t, "Ulovlig med null");

        /*Starter den rekursive innsettingen fra roten:
            Her skjer magien:
            Hvis treet er tomt (rot == null), returnerer den rekursive metoden en ny node, og derfor må vi sette rot = denne noden.
            Hvis treet allerede har en rot, vil metoden returnere samme rot tilbake (bare med en ny gren koblet på et sted), så rot beholder verdien sin.*/
        rot = leggInnRekursivt(rot, t);
        return true;
    }

    //ITERATIV VARIANT

    private boolean leggInnIterativt(T t){
        Objects.requireNonNull(t, "Ulovlig med null");
        Node<T> p = rot;
        Node<T> prev = null;
        while (p!=null){
            prev = p;
            if (cmp.compare(t, p.verdi)<0)
                p=p.venstre;
            else
                p=p.høyre;
        }
        p = new Node<>(t);
        if (prev == null)
            rot = p;
        else if (cmp.compare(t, prev.verdi)<0){
//            if (prev.venstre == null) TRENGER IKKE DENNE IF SETNINGEN, Løkka stopper når p blir null, og da vet vi at vi kom dit via prev.venstre eller prev.høyre som var null
                  prev.venstre = p;
        } else
//            if (prev.høyre == null)
                prev.høyre = p;

        return true;
    }

    @Override
    public boolean fjern(T t) {
        return false;
    }

    @Override
    public int antall() {
        // return antall; VANLIGVIS HADDE VI GJORT DET SÅNN, MEN JEG VIL PRØVE Å TELLE ANTALL REKURSIVT
        return antall(rot);
    }

    public int antall(Node<T> current){
        if (current == null)
            return 0; // basistilfelle

        // Teller 1 for denne noden (current selv).
        //Plusser på antall noder i venstre subtre (antall(current.venstre)).
        //Plusser på antall noder i høyre subtre (antall(current.høyre)).
        return 1 + antall(current.venstre) + antall(current.høyre);
    }

    @Override
    public boolean tom() {
        return rot == null;
    }

    public String toString(){
        StringJoiner sj = new StringJoiner(",","[","]");

        // Kaller den rekursive hjelpemetoden fra roten av treet,
        // så hele treet blir traversert i inorden og lagt inn i sj

        inorden(rot, sj);                 // 1) start fra roten og fyll sj

        // Når traverseringen er ferdig har sj alle verdiene i riktig rekkefølge,
        // og vi returnerer strengen som StringJoiner har bygd
        return sj.toString();             // 2) returner joineren som tekst
    }

    private void inorden(Node<T> p, StringJoiner sj){
        if (p==null) return;                // basistilfelle

        inorden(p.venstre, sj);             // 1) venstre subtre

        // 2) Legg til selve nodens verdi i StringJoiner
        //    String.valueOf(...) gjør om verdien til tekst
        sj.add(String.valueOf(p.verdi));    // 2) node (legg til i sj)


        inorden(p.høyre, sj);                // 3) høyre subtre
    }

    @Override
    public boolean inneholder(T t) {
        return false;
    }

    @Override
    public void nullstill() {

    }
}
public class BinærSøketreOppgave {
    public static void main(String[] args) {
        // 1) Lag tre for Integer med naturlig sortering
        BinærSøketre<Integer> bst = new BinærSøketre<>(Comparator.naturalOrder()); //sender med comparator i konstruktøren

        // 2) Legg inn tallene
        int[] verdier = {10, 6, 15, 8, 3, 17, 7, 20, 25};
        for (int v : verdier) {
            bst.leggInn(v);        // bruker din rekursive leggInn
            // evt: bst.leggInnIterativt(v); // hvis du vil teste den i stedet
        }

        // 3) Print antall og inorden-strengen
        System.out.println("Antall noder: " + bst.antall());
        System.out.println("Inorden:      " + bst.toString());
    }

}
