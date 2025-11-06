package ForelesningerØving.F8_Lister;

import java.util.Objects;

public class øving2 {

}

interface Beholder2<T>{
    boolean leggInn(int indeks, T t);
    T hent(int indeks);
    T oppdater(int indeks, T t);
    T fjern(int indeks);
    int indeksTil(T t);
}

class TabellListe2<T> implements Beholder2<T>{
    T[] tabell;
    int antall;
    int kapasitet;

    public TabellListe2(int kapasitet){
        this.kapasitet = kapasitet;
        this.antall=0;
        tabell = (T[]) new Object[kapasitet];
    }

    public TabellListe2(){
        this(10);
    }
    @Override
    public boolean leggInn(int indeks, T t) {
        Objects.requireNonNull(t);
        if (antall==kapasitet) utvid();

        //begynner bakfra og  flytter alle elementer opp
        for (int i = antall-1; i >= indeks; i--){
            tabell[i+1]= tabell[i];
        }

        tabell[indeks] = t;
        antall++;
        return true;
    }

    private void utvid(){
        kapasitet*=2;
        T[] tmp = (T[]) new Object[kapasitet];
        System.arraycopy(tabell, 0, tmp, 0, antall);
        tabell = tmp;
    }

    @Override
    public T hent(int indeks) {
        if (indeks > 0 && indeks < antall)
            return tabell[indeks];
        else throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public void sjekkIndeks(int indeks, boolean erLeggInn){
        if (erLeggInn && indeks >= 0 && indeks <= antall) return ;

        if (indeks >= 0 && indeks <antall)
            return ;
        else
            throw new IndexOutOfBoundsException("indeks out of bounds");
    }

    @Override
    public T oppdater(int indeks, T t) {
        Objects.requireNonNull(t);
        sjekkIndeks(indeks, false);
        T tmp = tabell[indeks];
        tabell[indeks] = t;
        return tmp;
    }

    @Override
    public T fjern(int indeks) {
        sjekkIndeks(indeks, false);
        // vi må flytte alle elementer til venstre som er høyere enn elementet som ble flyttet.
        return null;
    }
    @Override
    public int indeksTil(T t) {
        Objects.requireNonNull(t);
        for (int i = 0; i < tabell.length; i++){
            if (tabell[i].equals(t))
                return i;
        }
        return -1;
    }

    class LenketListe2<T> implements Beholder2<T>{

        Node<T> hode;
        Node<T> hale;
        int antall;
        private final class Node<T>{
            T verdi;
            Node<T> neste;

            public Node(T verdi){
                this.verdi = verdi;
                this.neste = null;
            }
        }

        @Override
        public boolean leggInn(int indeks, T t) {
            Objects.requireNonNull(t);
            Node<T> p;
            Node<T> ny = new Node<>(t);
            p = hode;

            if (antall==0)
                hode = hale = ny;
            else if (indeks == 0){
                ny.neste = hode;
                hode = ny;
            }
            else if (indeks == antall){
                hale.neste= ny;
                hale = ny;
            }

            else if (indeks>0 && indeks < antall) {
                for (int i = 0; i < indeks-1; i++) {
                    p = p.neste;
                }
                // nå er vi rett før posisjonen vi vil sette inn på
                //p.neste er noden som blir noden etter ny
                ny.neste = p.neste;

                //p.neste er der den nye noden skal inn.
                p.neste = ny;
            }
            antall++;
            return true;
        }

        public boolean leggInn(T t){
            return false;
        }

        @Override
        public T hent(int indeks) {
            return null;
        }

        @Override
        public T oppdater(int indeks, T t) {
            return null;
        }

        @Override
        public T fjern(int indeks) {
            return null;
        }

        @Override
        public int indeksTil(T t) {
            return 0;
        }
    }
}