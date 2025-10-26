package ForelesningerØving.F18_HashMap_HashSet;

import java.util.HashMap;
import java.util.Objects;

public class Øving2 {
    public static void main(String[] args) {

    }
}

interface Map2<N, V> {
    V leggInn(N nøkkel, V verdi);
    V fjern(N nøkkel);
    V hent(N nøkkel);
}

interface Set2<V>{
    V leggInn(V verdi);
    V fjern(V verdi);
    boolean inneholder(V verdi);
}

class Identitet2<T> {
    int id;
    String navn;


    @Override
    public boolean equals(Object o){
        if (this == o) return true;

        if (o==null || this.getClass() != o.getClass()) return false;


        //Caster objektet til å være identitet, fordi nå er de definitivt samme klasse.
        Identitet2<?> identitet = (Identitet2<?>) o;

        return id==identitet.id && Objects.equals(navn,identitet.navn);
    }
}

class HashMap2<N, V> implements Map2<N,V>{

    Node[] hash;
    int antall;
    int dimensjon;
    float tetthet =0.75f;
    int grense;

    private class Node{
        N nøkkel;
        V verdi;
        int hashverdi;
        Node neste;

        public Node(N nøkkel, V verdi, Node neste){
            this.nøkkel = nøkkel;
            this.verdi = verdi;
            this.neste = neste;
            //må bruke nøkkel sin hashcode, ikke noden
            this.hashverdi = nøkkel.hashCode() & 0x7fffffff;
        }

    }

    public HashMap2(){
        this.dimensjon=13;
        this.grense = (int) (tetthet*dimensjon);
        hash = (Node[]) new Object[dimensjon];
    }

    private int finnIndeks(N nøkkel){
        Objects.requireNonNull(nøkkel);
        int h = nøkkel.hashCode();
        h = h & 0x7fffffff;

        //dette er indeksen laget av hashcode
        int indeks = h % hash.length;
        return indeks;
    }

    @Override
    public V leggInn(N nøkkel, V verdi) {
        Objects.requireNonNull(nøkkel);
        if (antall>= grense) utvid();
        int indeks = finnIndeks(nøkkel);
        Node p = hash[indeks];
        while (p!=null){
            //hvis nøkkelen allerede finnes. Dette er ikke det samme som indeks. Dette er nøkkelen til den spesifikke noden.
            if (p.nøkkel.equals(nøkkel)){
                V tmp = p.verdi;
                p.verdi = verdi;
                return tmp;
            }
            p = p.neste;
        }
        Node ny = new Node(nøkkel, verdi, hash[indeks]); //setter neste til å være første noden på den indeksen.
        //Vi må sette hash[indeks] = ny fordi: Da kobles den riktig, den skal jo faktisk være den første noden på den indeksen, og dette gjør dette.
        hash[indeks] = ny;
        antall++;
        return null;
    }

    @Override
    public V hent(N nøkkel) {
        Objects.requireNonNull(nøkkel);
        int indeks = finnIndeks(nøkkel);

        Node p = hash[indeks];

        //går gjennom alle nodene på indeksen vi fant, og hvis den finner den med nøkkelen == den vi leter etter, returnerer den verdien.
        while (p!=null){
            if (p.nøkkel.equals(nøkkel))
                return p.verdi;
            p = p.neste;
        }
        return null;
    }

    @Override
    public V fjern(N nøkkel) {
        Objects.requireNonNull(nøkkel);
        int indeks = finnIndeks(nøkkel);

        Node p = hash[indeks];
        Node q = null;

        while (p!=null){
            if (p.nøkkel.equals(nøkkel)){
                V tmp = p.verdi;
                if (q==null)
                    hash[indeks] = p.neste;
                // Hvis noden vi vil slette IKKE er den første (q!=null), så flytter vi q.neste til å peke på p.neste. Hopper over p.
                else
                    q.neste = p.neste;
                antall--;
                return tmp;
            }
            q=p;
            p=p.neste;
        }
        return null;
    }

    private void utvid(){
        int nyDimensjon = 2*hash.length+1;
        Node[] nyHash = (Node[]) new Object[nyDimensjon];

        for (int i = 0; i<hash.length; i++){
            Node p = hash[i];
            while (p!=null){
                Node neste = p.neste;

                int nyIndeks = p.hashverdi % nyDimensjon;

                // legg p i front i ny bøtte
                p.neste = nyHash[nyIndeks];
                nyHash[nyIndeks] = p;
                p=neste;
            }
        }
        hash = nyHash;
        grense = (int)(tetthet*hash.length);
    }
}