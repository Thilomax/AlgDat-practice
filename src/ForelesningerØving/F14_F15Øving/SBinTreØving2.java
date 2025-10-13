package ForelesningerØving.F14_F15Øving;

import ForelesningerØving.F10_Stacks_n_Queues.LenketStabel;
import ForelesningerØving.F10_Stacks_n_Queues.Stabel;
import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

class SBinTre2<T> implements Beholder<T>, Iterable<T>{
    Node<T> rot;
    int antall;
    Comparator<? super T> cmp;

    public SBinTre2(Comparator<? super T> cmp){
        this.rot = null;
        this.antall = 0;
        this.cmp = cmp;
    }

    private static final class Node<T>{
        Node<T> venstre;
        Node<T> høyre;
        T verdi;

        public Node(T verdi){
            this.venstre = null;
            this.høyre = null;
            this.verdi = verdi;
        }
    }

    private static final class NodePar<T>{
        Node<T> current;
        Node<T> forelder;

        public NodePar(Node<T> current, Node<T> forelder){
            this.current=current;
            this.forelder=forelder;
        }
    }

    private final class InordenIterator2 implements Iterator<T>{
        Node<T> denne;
        Stabel<T> nodeStabel;

        //må initialisere variablene OG sette denne til å være første i inorden. som er helt nederst til venstre
        public InordenIterator2(){
            denne = rot;
            nodeStabel = new LenketStabel<>();

            while (denne.venstre!=null){
                nodeStabel.push(denne.verdi); //lagrer alle verdiene i nodeStabel så man kan gå oppover (som man må når man går opp igjen)
                denne = denne.venstre;
            }
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new InordenIterator2();
    }

    @Override
    public boolean leggInn(T t) {
        Objects.requireNonNull(t, "null er ikke lov");
        Node<T> p = rot;
        Node<T> q = null;
        int cmpv = 0;
        while(p!=null){
            q=p;
            cmpv = cmp.compare(t,p.verdi);
            if (cmpv<0)
                p=p.venstre;
            else
                p=p.høyre;

        }
        Node<T> ny = new Node<>(t);
        //hvis lista var tom, så er q null og ny blir rot.
        if (q==null) ny = rot;
        else if (cmpv<0)
            q.venstre = ny;
        else
            q.høyre = ny;
        antall++;
        return true;
    }

    /*🌳 Når noden har to barn:

Tenk: Jeg kan ikke bare fjerne denne, for den har to grener.

Gå derfor ett steg til høyre, og så helt venstre — der finner du den neste verdien i rekkefølge.

Kopiér den verdien opp til noden du skal slette.

Nå er den nederste noden du fant “duplikat”, og den har høyst ett barn — fjern den på vanlig måte.

Ferdig: treet beholder rekkefølgen, ingen hull.

🌿 Når noden har ett eller ingen barn:

Tenk: Jeg skal bare koble forelderen forbi denne noden.

Hvis den har ett barn → la forelderen peke rett på barnet.

Hvis den ikke har barn → la forelderen peke til null (blad fjernes).

Hvis det var roten som ble slettet → la roten bli barnet i stedet.

💡 Husketips:
To barn → kopier og fjern etterfølgeren.
Ett eller ingen barn → koble forelderen rett videre.*/

    @Override
    public boolean fjern(T t) {
        NodePar<T> par = finnNode(t);
        if (par==null) return false;
        Node<T> current = par.current;
        Node<T> forelder = par.forelder;

        //TO BARN:
        if (current.høyre!= null && current.venstre != null){
            Node<T> neste = current.høyre;
            Node<T> q = current;
            while (neste.venstre!=null) {
                q = neste;
                neste = neste.venstre;
            }
            //overskriver verdien til current
            current.verdi = neste.verdi;

            //sletter etterfølgeren neste ved å flytte pekeren til forelder
            //to tilfeller: etterfølgeren var bare høyrebarnet (hadde ingen venstrebarn) da er q==current
            if (q == current){
                q.høyre = neste.høyre;
            } else {
                q.venstre = neste.høyre;
            }
            antall--;
        }
        //0 eller 1 barn

        //1 barn først:
        else{
            Node<T> b;
            if (current.venstre!=null) b= current.venstre;

            else b=current.høyre; //hvorfor sette b=current.høyre hvis current.venstre ikke finnes? fordi hvis den ene finnes tar vi det, hvis den ikke finnes tar vi den andre (som kan være null, men det funker).

            if (forelder==null) rot=b;

            //her setter vi bare (den riktige) pekeren til forelder til å peke på barnet til current istedenfor current.
            else {
                if (forelder.venstre == current)
                    forelder.venstre=b;
                else
                    forelder.høyre=b;
            }
            antall--;
        }
        return true;
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
    public boolean inneholder(T t) {
        return finnNode(t)!=null;
    }

    private NodePar<T> finnNode(T verdi){
        Objects.requireNonNull(verdi, "null er ikke lov");
        Node<T> p = rot;
        Node<T> q = null;
        int cmpv =0;
        while (p!=null){
            cmpv = cmp.compare(verdi, p.verdi);
            if (cmpv<0) {
                q = p;
                p = p.venstre;
            }
            else if (cmpv>0){
                q=p;
                p=p.høyre;
            } else{
                return new NodePar<>(p,q);
            }
        }
        return null;
    }

    @Override
    public void nullstill() {

    }
}

public class SBinTreØving2 {
}
