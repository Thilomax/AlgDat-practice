package ForelesningerØving.F14_BinærSøkeTreEkstra;

import ForelesningerØving.F10_Stacks_n_Queues.LenketStabel;
import ForelesningerØving.F10_Stacks_n_Queues.Stabel;
import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

class BinærSøkeTre<T> implements Beholder<T>{

    Node<T> rot;
    int antall;
    Comparator<? super T> cmp;

    public BinærSøkeTre(Comparator<? super T> cmp){
        this.cmp = cmp;
        this.rot = null;
        this.antall = 0;
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
    private static final class NodePar<T>{
        Node<T> current;
        Node<T> forelder;

        public NodePar(Node<T> current, Node<T> forelder) {
            this.forelder = forelder;
            this.current = current;
        }
    }

    @Override
    public boolean leggInn(T t) {
        Objects.requireNonNull(t, "Null er ikke lov!");
        Node<T> p = rot;
        Node<T> q = null;
        //fortsetter til p er null, vi har funnet en tom plass
        while (p!=null){
            q = p;
            int comp = cmp.compare(t, p.verdi); // gir ut negativ hvis verdi 1 er mindre enn verdi 2
            if (comp < 0) //hvis t (verdien vi vil sette inn) er mindre enn p (verdien vi er på nå) så går vi til venstre
                p = p.venstre;
            else
                p= p.høyre;
        }
        Node<T> ny = new Node<>(t);
        if (q == null) // da er treet tomt, så den nye noden blir rota
            rot = ny;
        else if(cmp.compare(t,q.verdi)<0) //Hvis verdien vi vil legge inn er mindre enn foreldrenoden, legger vi den inn som venstrebarnet
            q.venstre = ny;
        else
            q.høyre = ny;
        antall++;
        return true;
    }

    private boolean leggInnRekursiv(T t){
        Objects.requireNonNull(t, "Ulovlig med null-verdier.");
        rot = leggInnRekursiv(rot, t);
        return true;
    }

    private Node<T> leggInnRekursiv(Node<T> current, T t){
        if (current == null)
            return new Node<>(t);
        if (cmp.compare(t, current.verdi)<0)
            current.venstre = leggInnRekursiv(current.venstre, t);
        /*Den rekursive metoden returnerer en node (enten den samme eller en ny).
        Du må lagre returverdien slik at forelderen peker riktig etter innsetting.
        Uten current.venstre = ..., mister du den nye noden, og treet oppdateres ikke.*/
        else
            current.høyre = leggInnRekursiv(current.høyre,t);
        return current;
    }

    //hjelpemetode til å finne node + forelder for sletting
    private NodePar<T> finnNode(T t){
        Objects.requireNonNull(t, "Null-verdi er ikke lov");
        Node<T> current = rot;
        Node<T> forelder = null;
        while(current!=null){
            int comp = cmp.compare(t, current.verdi);
            if (comp < 0) { //hvis t (verdien vi vil sette inn) er mindre enn p (verdien vi er på nå) så går vi til venstre
                forelder = current; /*SETTER FORELDER TIL CURRENT BARE HVIS VI SKAL FLYTTE ET HAKK NED, hvis man har det i starten vil forelder alltid være = current*/
                current = current.venstre;
            }
            else if (comp > 0) {
                forelder = current;
                current = current.høyre;
            }
            else
                return new NodePar<>(forelder,current);
        }
        return null;
    }

    /**Rekursiv variant av finnNode*/
    private NodePar<T> finnNodeRekursiv(Node<T> current, Node<T> forelder, T t){
        if (current == null)
            return null; // basistilfelle: vi har gått tom for noder uten å finne verdien

        int cmpv = cmp.compare(t, current.verdi);
        if (cmpv<0)
            return finnNodeRekursiv(current.venstre, current, t); //kaller rekursivt på venstre subtre. Current.venstre blir nye current, current blir nye forelder og vi sender med verdien
        else if (cmpv>0)
            return finnNodeRekursiv(current.høyre, current, t);
        else
            // cmpv == 0: verdien vi leter etter finnes i denne noden (current)
            // vi returnerer både denne noden og dens forelder
            return new NodePar<>(forelder, current);
    }

    /**
     * Steg 0: Ta inn en current node og en forelder node
     * TO BARN:
     * Steg 1: sjekk om noden har 2 barn
     * Steg 2: Opprett node p som er peker for traverseringen til neste node i inorden
     * Steg 2.1: Inorden = et steg til høyre og så mange steg som mulig til venstre, så initialiser p til å være current.høyre
     * Steg 3: Opprett node q som initialiseres til current. Denne skal alltid oppdateres til å være forelder til p
     * Steg 4: lag en while løkke while(p.venste!=null) for å gå helt til vi har funnet det nederste venstrebarnet.
     * Steg 4.1: Inn i while løkken, oppdater q = p, oppdater p = p.venstre
     * Steg 5: Etter while løkken, setter vi current.verdi til å være p.verdi(det nederste venstre barnet) for å bytte noden vi sletter med p (første inorden node)
     * Steg 6: Fjern noden rekursivt (fjernNode(p,q);). Kaller på metoden og seender inn p og q som current og forelder
     * Overview: Current = noden vi vil fjerne. P = neste node i inorden etter current. Q = forelder til P*/

    private void fjernNode(Node<T> current, Node<T> forelder) {
        // 2 barn
        if (current.venstre != null && current.høyre!= null){
            //vi må finne neste i inorden. -> et steg til høyre og så mange steg som mulig til venstre
            Node<T> p = current.høyre; //ett steg til høyre
            Node<T> q = current; // forelder
            while(p.venstre!=null){ // ikke p!= null fordi da går den for langt
                q=p; //holder styr over forelderen til p
                p=p.venstre; //går så mange steg som mulig til venstre
            }
            current.verdi = p.verdi; //FORDI: Se notater lecture 14. Det er for å bytte noden vi sletter med inorden noden som ikke har noen barn, så man lett kan slette verdien og gjøre at den ikke har barn.
            fjernNode(p,q); //vil rekursivt fjerne noden.
        } else {
            // 1) 0 eller 1 barn (b = barn)
            Node<T> b;
            if (current.venstre != null)
                b = current.venstre;
            else
                b = current.høyre;

            if (forelder == null) {  //hvis noden vi vil fjerne er roten, så setter vi rota til å være barnet dets.
                rot = b;

            } else {
                if (forelder.venstre == current) { //hvis noden vi vil slette (current) er venstrebarnet til forelder, setter vi venstrebarnet til å være b
                    forelder.venstre = b;

                    /*
                     *Du erstatter pekeren som tidligere pekte til noden som skal slettes (current)
                     *med pekeren til nodens barn (b).
                     *B ER BARNET TIL CURRENT
                     * */
                }
                else {
                    forelder.høyre = b;

                }
                // og pek den referansen til b
            }
        }

    }


    @Override
    public boolean fjern(T t) {
        // 1. Finn noden som skal fjernes, samt dens forelder.
        //    finnNode(t) returnerer et NodePar med (current, forelder)
        NodePar<T> par = finnNode(t);

        // 2. Hvis verdien ikke finnes i treet, returner false.
        //    (Det er ingenting å fjerne.)
        if (par == null)
            return false;

        // 3. Fjern noden ved å kalle hjelpemetoden fjernNode(),
        //    som håndterer både tilfellene 0, 1 og 2 barn.
        //    Vi sender inn både noden og dens forelder.
        fjernNode(par.current, par.forelder);

        antall--;

        // 5. Returner true for å indikere at en verdi ble fjernet.
        return true;
    }

    /**
     * Fjerner alle forekomster av verdien t.
     * Idé: Finn alle forekomster langs høyre-kjeden (fordi duplikater alltid går til høyre),
     * legg dem på en stabel, og fjern dem i FILO-rekkefølge (nederst først).
     * Nederste forekomster har maks ett barn, så vi slipper 2-barns-tilfellet.
     */
    public int fjernAlle(T t){
        Stabel<NodePar<T>> stabel = finnAlle(t);
        int teller = 0; // hvor mange vi fjernet

        // Fjern nederst-til-øverst for å unngå 2-barn-tilfeller underveis
        while (!stabel.tom()){ //iterer over listen i FILO. Altså første forekomst vi ser blir fjernet sist.
            NodePar<T> par = stabel.pop();  //(forelder, current) for én forekomst av t. Fjerner nåværende node fra stabelen og vi får nodeparet tilbake av pop-metoden,
                                            // som vi kan bruke i fjernNode (fra det faktiske binærsøketreet)

            fjernNode(par.current, par.forelder); //Fjerner nåværende node i while løkken fra binærsøketreet. Trygg fjerning (0/1-barn fordi vi går nedenfra)
            teller++; //Øker telleren
        }
        // VIKTIG: fjernNode() oppdaterer ikke antall.
        // Siden vi kalte fjernNode() direkte (ikke fjern(t)), må vi justere antall her:
        antall-=teller;
        return teller;
    }

    /**
     * Finn alle forekomster av t langs høyre-kjeden (duplikatregelen).
     * Returnerer en stabel med (forelder, current) for hver forekomst.
     * Vi fjerner senere fra bunnen (FILO), så pekerne "over" forblir gyldige.
     */
    private Stabel<NodePar<T>> finnAlle(T t){
        Objects.requireNonNull(t, "Null-verdi er ikke lov");
        Stabel<NodePar<T>> stabel = new LenketStabel<>();

        // Startpunkt for søk i (sub)treet vi fortsatt ikke har tømt for t-verdier
        Node<T> current = rot;
        Node<T> forelder = null;

        // Vi finner én forekomst om gangen (nærmest roten i dette (sub)treet),
        // skyver den på stabelen, og fortsetter videre i høyre subtre for å finne neste forekomst.
        while (true) {
            // Viktig: vi kaller rekursivt søk fra 'current' og lar metoden finne RIKTIG forelder underveis.
            NodePar<T> par = finnNodeRekursiv(current, forelder, t);

            if (par == null) // Ingen flere forekomster i dette (sub)treet
                break;

            stabel.push(par);       // Lagre (forelder, current) for senere fjerning FILO
            forelder = par.current; // Neste søk skal ha denne som forelder til roten i neste subtre
            current  = par.current.høyre; // Alle duplikater ligger langs høyre-grenen
        }
        return stabel;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return rot==null;
    }

    @Override
    public boolean inneholder(T t) {
        return (finnNode(t)!=null);
    }

    @Override
    public void nullstill() {

    }

    private void inordenSJ(Node current, StringJoiner sj) {
        if (current == null) return;
        // Basistilfelle: hvis noden er null (dvs. ingen gren her), avslutt rekursjonen

        inordenSJ(current.venstre, sj);
        // Først: gå rekursivt helt ned til venstre (minste verdi)

        sj.add(String.valueOf(current.verdi));
        // Deretter: legg til verdien til denne noden.
        // Denne kalles først for den venstre-mest noden, og deretter "bobler" den oppover i call stacken.
        // Derfor blir verdiene lagt til i stigende rekkefølge.

        inordenSJ(current.høyre, sj);
        // Til slutt: gå rekursivt ned til høyre gren (større verdier)
    }

    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        // Lager en StringJoiner som formaterer resultatet som en liste: [v1, v2, v3]

        inordenSJ(rot, sj);
        // Starter den rekursive traverseringen fra rot

        return sj.toString();
        // Returnerer resultatet som én formatert streng
    }

}

public class Oppgave1 {
}
