package ForelesningerØving.F14_BinærSøkeTreEkstra;

import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Objects;

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
}

public class Oppgave1 {
}
