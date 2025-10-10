package ForelesningerØving.F14_BinærSøkeTreEkstra;

import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Objects;

class SBinTre<T> implements Beholder<T> {

    Node<T> rot;
    Comparator <? super T> cmp;

    public SBinTre(Comparator<? super T> cmp){
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
            this.høyre = null;
        }
    }
    private static final class NodePar<T>{
        Node<T> current;
        Node<T> forelder;

        public NodePar(Node<T>current, Node<T> forelder){
            this.current = current;
            this.forelder = forelder;
        }
    }
    @Override
    public boolean leggInn(T t) {
        Objects.requireNonNull(t, "Null er ikke lov");
        Node<T> p = rot;
        Node<T> q = null;
        while(p!=null){
            q=p;
            int cmpv = cmp.compare(t, p.verdi);
            if (cmpv<0)
                p=p.venstre;
            else
                p=p.høyre;
        }
        Node<T> ny = new Node<>(t);
        if (q==null) //treet er tomt
            rot = ny;
        else if(cmp.compare(t, q.verdi)<0)
            q.venstre = ny;
        else
            q.høyre = ny;
        return true;
    }

    private Node<T> leggInnRekursivt(Node<T> current, T t){
        // Basistilfelle: har vi kommet til en "tom plass" (null)?
        // Da oppretter vi en ny node med verdien og returnerer den.
        if (current == null)
            return new Node<>(t);

        // Sammenlign verdien som skal inn med nodens verdi
        int cmpv = cmp.compare(t, current.verdi);

        if (cmpv < 0)
            // Gå rekursivt ned til venstre gren.
            // Resultatet (subtreet etter innsetting) kobles tilbake til current.venstre.
            current.venstre = leggInnRekursivt(current.venstre, t);
        else
            // Gå rekursivt ned til høyre gren.
            current.høyre = leggInnRekursivt(current.høyre, t);

        // Når vi er ferdig med å legge inn i venstre eller høyre subtre,
        // returnerer vi "current" — altså roten til dette deltreet.
        // Dette er viktig fordi når vi går ett nivå opp i rekursjonen,
        // må forelderen vite hvilken node som nå er roten i sitt venstre/høyre subtre.
        return current;
    }

    private boolean leggInnRekursivt(T t) {
        Objects.requireNonNull(t, "Null er ikke lov");

        // Vi kaller den rekursive hjelpefunksjonen og tilordner resultatet til rot.
        // Hvorfor? Fordi leggInnRekursivt(current, t) returnerer roten til det deltreet
        // der innsettingen skjedde. Det betyr at hvis treet opprinnelig var tomt (rot == null),
        // vil funksjonen returnere en helt ny node som nå er selve roten.
        //
        // Hvis treet ikke var tomt, vil funksjonen likevel returnere den samme rotnoden,
        // men poenget er at vi "sikrer" at rot alltid peker til det oppdaterte treet.
        //
        // Uten denne tilordningen (rot = ...), ville vi risikert at rot fortsatt peker
        // på null når vi legger inn første element.
        rot = leggInnRekursivt(rot, t);

        return true;
    }

    private NodePar<T> finnNode(T t) {
        Node<T> current = rot;     // Starter fra roten
        Node<T> forelder = null;   // Forelderen til current

        while (current != null) {
            int cmpv = cmp.compare(t, current.verdi);

            if (cmpv < 0) {
                // Verdien vi leter etter er mindre enn current.verdi → gå til venstre
                forelder = current;
                current = current.venstre;
            }
            else if (cmpv > 0) {
                // Verdien vi leter etter er større → gå til høyre
                forelder = current;
                current = current.høyre;
            }
            else {
                // Vi har funnet verdien!
                // Returnerer både noden og forelderen i en NodePar.
                // Denne MÅ stå inne i while-løkken fordi hvis vi hadde ventet til etterpå,
                // ville current blitt null etter løkken og vi mistet referansen.
                return new NodePar<>(current, forelder);
            }
        }

        // Hvis vi kom hit, betyr det at verdien ikke finnes i treet
        return null;
    }

    @Override
    public int antall() {
        return antall(rot);
    }
    public int antall(Node<T> current){
        if (current == null)
            return 0;

        //Returnerer 1(noden selv) pluss alle noder i venstre subtre pluss alle i høyre subtre
        return 1 + antall(current.venstre) + antall(current.høyre);
    }

    @Override
    public boolean tom() {
        return false;
    }

    @Override
    public boolean inneholder(T t) {
        Objects.requireNonNull(t, "Null er ikke lov");
        // Nullverdier er ikke tillatt i treet

        Node<T> p = rot;  // Starter søket fra roten

        while (p != null) {
            int cmpv = cmp.compare(t, p.verdi);
            // Sammenligner verdien vi leter etter med nodens verdi

            if (cmpv == 0)
                // Funnet verdien!
                return true;

            if (cmpv < 0)
                // Verdien vi søker etter er mindre → gå venstre
                p = p.venstre;
            else
                // Verdien er større → gå høyre
                p = p.høyre;
        }

        // Hvis vi har kommet ut av while-løkken, finnes ikke verdien i treet
        return false;
    }

    public boolean inneholderSimpel(T t){
        Objects.requireNonNull(t, "null er ikke lov");
        return finnNode(t) != null;
    }


    @Override
    public boolean fjern(T t) {
        Objects.requireNonNull(t, "Null er ikke lov");
        NodePar<T> par = finnNode(t);
        if (par==null) return false;
        fjernNode(par.current, par.forelder);
        return true;
    }

    private void fjernNode(Node current, Node forelder){
        if (current.høyre !=null && current.venstre !=null){
            //To barn
            Node<T> p = current.høyre;
            Node<T> q = null;
            while(p.venstre!= null){
                q=p;
                p = p.venstre;
            }
            //nå har vi funnet neste node i inorden så nå må vi bytte current(den vi vil fjerne) sin verdi og p(neste i inorden) sin verdi
            current.verdi = p.verdi;

            // fjern etterfølger-noden p:
            // p har aldri venstre barn. Den kan ha et høyrebarn.
            // To deltilfeller:
                // (i) etterfølgeren var direkte høyrebarn av current (q == current)
                // (ii) etterfølgeren ligger lenger ned (q != current)
            if (q == current) {
                // successor var current.høyre; koble forbi p med p.høyre
                q.høyre = p.høyre;
            } else {
                // successor lå som q.venstre; koble forbi p med p.høyre
                q.venstre = p.høyre;
            }

        } else{
            //0 eller 1 barn
            Node<T> b;
            if (current.venstre !=null) b = current .venstre;

            /*Har noden bare venstre barn, behold venstrebarnet.
            Har den bare høyre barn, behold høyrebarnet.
            Har den ingen barn, sett peker til null.*/
            else b=current.høyre; //denne kan være null

            if (forelder == null) rot = b;
            else {
                if (forelder.venstre == current)  //hvis noden vi vil slette (current) er foreldres venstrebarn, setter vi foreldres venstrepeker til å peke på barnet til current
                    forelder.venstre = b;
                else
                    forelder.høyre = b;
            }
        }
    }


    @Override
    public void nullstill() {

    }
}
public class Oppgave1_1 {
}
