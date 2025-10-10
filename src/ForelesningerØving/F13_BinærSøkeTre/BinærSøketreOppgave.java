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
            prev = p; //alltid en node bak p
            if (cmp.compare(t, p.verdi)<0)
                p=p.venstre; //hvis verdien vi vil legge inn er mindre enn p sin verdi gå til venstre
            else
                p=p.høyre;  //hvis verdien vi vil legge inn er større enn p sin verdi gå til høyre
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
        Node<T> current = rot; //begynner på rota
        while(current != null){
            // Sjekk rota om verdien vi leter etter er mindre eller større enn verdien i rota
            if (cmp.compare(t, current.verdi)==0)
                return true; //Da er current sin verdi lik den vi leter etter, da har vi funnet den så den finnes.
            else if (cmp.compare(t, current.verdi)<0)
                current = current.venstre; //Mindre → gå til venstre
            else
                current = current.høyre; //Større → gå til høyre
        }
        return false;
    }

    //inneholder() kan også gjøres som en finnNode() som returnerer en node

    public Node<T> finnNode(T t) {
        Node<T> current = rot; //begynner på rota
        while(current != null){
            // Sjekk rota om verdien vi leter etter er mindre eller større enn verdien i rota
            if (cmp.compare(t, current.verdi)==0)
                return current; //Da er current sin verdi lik den vi leter etter, da har vi funnet den så den finnes.
            else if (cmp.compare(t, current.verdi)<0)
                current = current.venstre; //Mindre → gå til venstre
            else
                current = current.høyre; //Større → gå til høyre
        }
        return null;
    }

    private Node<T> finnNodeRekursiv(Node<T> current, T t){
        if (current == null)
            return null;
        int cmpv = cmp.compare(t, current.verdi); //lagrer sjekken i en int
        if (cmpv == 0)
            return current;
        else if (cmpv <0)
            return finnNodeRekursiv(current.venstre, t); //kaller rekursivt og sender inn venstre barnet som current
        else return finnNodeRekursiv(current.høyre, t); //samme men med høyre
    }

    private Node<T> finnNodeRekursiv(T t) {
        return finnNodeRekursiv(rot, t);
    }

    //TODO: Trenger informasjon om forelder, lag klasse nodepara
    private void fjernNode(Node<T> current, Node<T> forelder){
        if (current.venstre == null && current.høyre ==null){ // hvis jeg ikke har noen barn
            //ingen barn
            if (forelder==null) //da er jeg rotnoden, har ingen forelder
                rot = null;
            else if (forelder.venstre == current)
                forelder.venstre = null; //fjerner forelders venstrepeker hvis jeg er venstrebarnet
            else
                forelder.høyre=null; //fjerner forelders høyrepeker hvis jeg er høyrebarnet
        } else if (current.høyre == null) {
            //hvis jeg bare har ett barn: Må sette forelderens peker til å hoppe over meg, har venstrebarn men ikke høyrebarn
            if (forelder == null)
                rot = current.venstre;
            else if(forelder.venstre == current)
                forelder.venstre = current.venstre;
            else
                forelder.høyre = current.venstre;
        } else if (current.venstre == null) {
            //Har høyrebarn men ikke venstrebarn
            if (forelder == null)
                rot = current.høyre;
            else if(forelder.venstre == current)
                forelder.venstre = current.høyre;
            else
                forelder.høyre = current.høyre;
        } else {
            //har to barn
            Node<T> p = current.høyre; //p betyr current
            Node<T> q = current; // q betyr forelder
            while (p.venstre != null){
                q = p;
                p = p.venstre;
            }
            current.verdi = p.verdi;
            fjernNode(p, q); //vil rekursivt fjerne denne verdien
            //kunne slette direkte, ville vært å sette forelders peker til p.høyre.
        }

    }
//    public boolean fjern(T t){
//        NodePar<T> par = finnNode(t);
//        if (par == null)
//            return false;
//        fjern(par.current, par.forelder);
//        return true;
//    }

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
