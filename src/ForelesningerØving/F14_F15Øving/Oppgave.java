package ForelesningerØving.F14_F15Øving;

import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Objects;

class SBinTre<T> implements Beholder<T> {
    Node<T> rot;
    int antall;
    Comparator<? super T> cmp;

    public SBinTre(Comparator<? super T> cmp){
        this.cmp = cmp;
        this.rot = null;
        this.antall = 0;
    }

    @Override
    public boolean leggInn(T t) {
        Objects.requireNonNull(t, "null er ikke lov");
        Node<T> p = rot;
        Node<T> q =null;
        int cmpv = 0;
        while(p!=null){
            q=p;
            cmpv = cmp.compare(t,p.verdi);
            if (cmpv < 0)
                p=p.venstre;
             else
                p=p.høyre;
        }
        Node<T> ny = new Node<>(t);
        if(q==null) rot = ny;
        else if (cmpv<0){ // kan bruke cmpv fordi q ble oppdatert til p rett før p blir null så det funker.
            q.venstre = ny;
        } else
            q.høyre = ny;
        antall++;
        return true;
    }

    @Override
    public boolean fjern(T t) {
        return false;
    }

    private void fjernNode(Node<T> current, Node<T> forelder){
        if (current.venstre != null && current.høyre !=null){
            //nå må vi finne neste i inorden, det er etterfølgeren. Ett steg til høyre -> så langt til venstre som mulig
            // Vi finner neste i inorden fordi det er den minste verdien som fortsatt er større enn noden vi skal slette. Den kan trygt erstatte nodens verdi uten å bryte BST-regelen (venstre < rot < høyre).
            Node<T> p = current.høyre;
            Node<T> q = current;
            while(p.venstre!=null){
                q=p;
                p=p.venstre;
            }
            /*I denne blokken av koden gjør vi følgende:
            * - Vi setter current sin verdi (noden vi vil slette) til verdien til neste node i inorden
            *    - Nå er den neste noden i inorden den noden vi vil slette. Den har da ikke lenger  to barn
            * - Så sletter vi den
            *    - Hvis neste i inorden var direkte høyrebarn til current (q==current) så setter vi q.høyre til p.høyre (hopper over p)
            *    - Hvis neste i inorden var lengre ned, er p et venstrebarn av q, og da setter vi q.venstre til å peke på p.høyre
            *       (p kan ha høyrebarn (men ikke venstre), men den må ikke. Det betyr at enten blir q.venstre satt til høyrebarnet eller null. Begge funker */
            current.verdi = p.verdi; //dette erstatter verdien til noden vi vil slette med noden som er neste i inorden

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
            antall--;
        }
        //0 eller 1 barn
        else {
            /*Dette gjør vi i tilfellet 0 til 1 barn:
            * 1. Lager en pekernode som peker på barnet til noden som skal slettes
            * 2. Sjekker om current har et venstrebarn. Hvis ja -> sett b til å peke på den. Hvis nei -> sett b til å peke på potensiell høyrebarn eller null
            * 3. Hvis current er rota (forelder ==null) så setter vi rota til å være barnet til current.
            * 4. Hvis current er venstrebarnet til forelder, peker vi venstrepekeren til forelder til å peke på barnet til current
            * 5. Hvis current er HØYREBARNET, peker vi HØYREPEKEREN*/


            Node<T> b;

             /*Har noden bare venstre barn, behold venstrebarnet.
            Har den bare høyre barn, behold høyrebarnet.
            Har den ingen barn, sett peker til null.*/

            if (current.venstre!= null)
                b=current.venstre;
            else
                b=current.høyre; //denne kan være null

            //hvis noden vi vil fjerne er roten, så setter vi rota til å være barnet dets.
            if (forelder ==null)
                rot = b;

            //hvis noden vi vil slette er venstrebarnet til forelder, setter vi at venstrebarnet til forelder er barnet til current.
            if (forelder.venstre == current)
                forelder.venstre = b;
            else
                forelder.høyre = b;
        }
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall==0;
    }

    private NodePar<T> finnNode(T verdi){
        Objects.requireNonNull(verdi, "null er ikke lov");
        Node<T> p = rot;
        Node<T> q= null;
        int cmpv = 0;
        while(p!=null){
            cmpv = cmp.compare(verdi, p.verdi);
            if (cmpv<0) {
                q = p;
                p = p.venstre;
            }
            else if (cmpv>0) {
                q = p;
                p = p.høyre;
            }
            else
                //her, hvis du ikke har q=p i if-blokkene vil q være = p her fordi vi flytter ikke p nedover her
                return new NodePar<>(p,q);
        }
        return null;
    }

    @Override
    public boolean inneholder(T t) {
        return finnNode(t)!=null;
    }

    @Override
    public void nullstill() {

    }


    private static final class Node<T>{
        Node<T> venstre;
        Node<T> høyre;
        T verdi;

        public Node(T verdi){
            this.verdi = verdi;
            this.høyre = null;
            this.venstre = null;
        }

    }
    private static final class NodePar<T>{
        Node<T> current;
        Node<T> forelder;
        public NodePar(Node<T> current, Node<T> forelder){
            this.current = current;
            this.forelder = forelder;
        }
    }
}

public class Oppgave {
}
