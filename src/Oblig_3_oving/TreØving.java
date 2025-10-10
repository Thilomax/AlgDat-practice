package Oblig_3_oving;

import java.util.*;

/**
 * Øve-rigg: komplett rammeverk, men oppgavene (traversering + postorden-hjelpere)
 * er bevisst IKKE implementert. Du fyller inn TODO-stedene.
 *
 * Inneholder:
 * - Node<T> med venstre/høyre/forelder
 * - Bygging av eksempel-tre (manuell) og rask BST-bygging (med Comparator)
 * - Utskriftsverktøy: nivåvis visning, konsise lister
 * - Test-harness i main() som viser hvor metoder kalles, men uten fasit
 */
public class TreØving<T> {

    // ------------------ Node ------------------
    public static class Node<T> {
        T verdi;
        Node<T> venstre, høyre, forelder;
        Node(T verdi) { this.verdi = verdi; }
        @Override public String toString() { return String.valueOf(verdi); }
    }

    // ------------------ Felt ------------------
    private Node<T> rot;
    private final Comparator<? super T> cmp;
    private int antall = 0;

    // ------------------ Konstruktører ------------------
    public TreØving(Comparator<? super T> cmp) { this.cmp = cmp; }
    public TreØving() { this.cmp = null; }

    // ------------------ Bygging ------------------
    /** Setter inn verdi som BST. Duplikater til høyre. Krever Comparator. */
    public Node<T> insertBST(T verdi) {
        if (cmp == null) throw new IllegalStateException("Trenger Comparator for insertBST");
        Node<T> n = new Node<>(verdi);
        if (rot == null) { rot = n; antall++; return n; }
        Node<T> p = rot, f = null; int c = 0;
        while (p != null) { f = p; c = cmp.compare(verdi, p.verdi); p = c < 0 ? p.venstre : p.høyre; }
        n.forelder = f; if (c < 0) f.venstre = n; else f.høyre = n; antall++; return n;
    }

    /** Manuelt eksempel-tre for øving.
     *
     *            6
     *           / \
     *          2   8
     *         / \   \
     *        1   4   9
     *           /
     *          3
     */
    public static TreØving<Integer> byggEksempelTre() {
        TreØving<Integer> t = new TreØving<>();
        Node<Integer> n6 = new Node<>(6);
        Node<Integer> n2 = new Node<>(2);
        Node<Integer> n8 = new Node<>(8);
        Node<Integer> n1 = new Node<>(1);
        Node<Integer> n4 = new Node<>(4);
        Node<Integer> n9 = new Node<>(9);
        Node<Integer> n3 = new Node<>(3);

        t.rot = n6;
        n6.venstre = n2; n2.forelder = n6;
        n6.høyre  = n8; n8.forelder = n6;
        n2.venstre = n1; n1.forelder = n2;
        n2.høyre   = n4; n4.forelder = n2;
        n4.venstre = n3; n3.forelder = n4;
        n8.høyre   = n9; n9.forelder = n8;
        t.antall = 7;
        return t;
    }

    // ------------------ Oppgavestubber: DU implementerer ------------------
    /** Preorden: N, V, H. Returner liste med besøksrekkefølgen. */

    //I preorden vil alltid forelderen bli "besøkt" først, deretter potensiell venstrebarn så potensielt høyrebarn
    public List<T> preorden() {
        List<T> result = new ArrayList<>();
        preordenRekursiv(rot, result);
        return result;
    }

    private void preordenRekursiv(Node<T>p, List<T> resultat){
        if (p==null) return;
        resultat.add(p.verdi);
        preordenRekursiv(p.venstre, resultat);
        preordenRekursiv(p.høyre, resultat);
    }

    public List<T> preordenIterativ(){
        Node<T> p = rot;
        List<T> result = new ArrayList<>();
        while(p!=null){
            result.add(p.verdi);
            if (p.venstre!=null)
                p = p.venstre;
            else if (p.høyre!=null)
                p=p.høyre;
        }
        return result;
    }

    /** Inorden: V, N, H. Returner liste med besøksrekkefølgen. */
    public List<T> inorden() {
        List<T> result = new ArrayList<>();
        inordenRekursiv(rot, result);
        return result;
    }

    private void inordenRekursiv(Node<T> p, List<T> resultat){
        if (p==null) return;
        inordenRekursiv(p.venstre,resultat);
        resultat.add(p.verdi);
        inordenRekursiv(p.høyre,resultat);
    }

    /** Postorden: V, H, N. Returner liste med besøksrekkefølgen. */
    public List<T> postorden() {
        List<T> resultat = new ArrayList<>();
        postordenRekursiv(rot,resultat);
        return resultat;
    }

    private void postordenRekursiv(Node<T> p, List<T> resultat){
        if (p==null) return;
        postordenRekursiv(p.venstre, resultat);
        postordenRekursiv(p.høyre, resultat);
        resultat.add(p.verdi);
    }

    /** Første node i postorden for deltre med rot p. */
    public Node<T> førstePostorden(Node<T> p) {
        if (p==null) return null;
        while(true){
            if(p.venstre!=null)
                p=p.venstre;
            else if (p.høyre!=null)
                p = p.høyre;
            else return p;
        }
    }

    /** Neste node i postorden etter p. Se reglene i oppgaveteksten. */
    public Node<T> nestePostorden(Node<T> p) {
        // TODO: implementer "neste postorden" iht. reglene

        if (p==rot) return null; //rot er alltid sist i postorden
        Node<T> f = p.forelder;

        //De eneste alternativene er Null (p er roten), høyrebarnet (p er venstrebarn) eller forelder!(p er høyrebarn eller forelder har ikke høyrebarn)

        //hvis p er høyrebarnet til forelderen sin eller om forelderen ikke har noe barn, er forelder neste!
        if (f.høyre == p || f.høyre==null)
            return f;

        //hvis p er venstrebarnet og forelder har et høyrebarn så er høyrebarnets SUBTRE neste. Det betyr at hvis høyrebarnet har venstrebarn, må vi gå ned hele treet.
        //SÅ vi må finne  første i postorden i f.høyres subtre!!
        else
            return førstePostorden(f.høyre);
    }

    /** Bygg hele postorden ved å bruke førstePostorden + nestePostorden. */
    public List<T> postordenIterasjon() {
        // TODO: bruk førstePostorden(rot) og iterer med nestePostorden(...)
        List<T> liste = new ArrayList<>();
        if(rot== null) return liste;

        // setter p til å være første node i postorden (det er jo seff ikke rot)
        Node<T> p = førstePostorden(rot);

        //Går gjennom lista og legger inn p sin verdi, og så flytter p til å være neste i postorden
        while(p!=null){
            liste.add(p.verdi);
            p = nestePostorden(p);
        }
        return liste;
    }

    // ------------------ Verktøy: søk og printing ------------------
    /** Finn node via BST-søk hvis mulig, ellers lineært. */
    public Node<T> finnNode(T verdi) {
        if (verdi == null) return null;
        if (cmp != null) {
            Node<T> p = rot;
            while (p != null) {
                int c = cmp.compare(verdi, p.verdi);
                if (c == 0) return p;
                p = c < 0 ? p.venstre : p.høyre;
            }
            return null;
        } else {
            return finnNodeLinear(rot, verdi);
        }
    }
    private Node<T> finnNodeLinear(Node<T> p, T verdi) {
        if (p == null) return null;
        if (Objects.equals(p.verdi, verdi)) return p;
        Node<T> v = finnNodeLinear(p.venstre, verdi);
        return v != null ? v : finnNodeLinear(p.høyre, verdi);
    }

    /** Nivåvis utskrift (BFS) for å “se” treet i konsollen. */
    public void printNivåvis() {
        if (rot == null) { System.out.println("(tomt tre)"); return; }
        Queue<Node<T>> q = new ArrayDeque<>();
        q.add(rot);
        while (!q.isEmpty()) {
            int sz = q.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sz; i++) {
                Node<T> p = q.remove();
                sb.append(p.verdi).append("  ");
                if (p.venstre != null) q.add(p.venstre);
                if (p.høyre  != null) q.add(p.høyre);
            }
            System.out.println(sb.toString().trim());
        }
    }

    /** Hjelper for å printe lister pent. */
    public static <T> void printListe(String tittel, List<T> liste) {
        System.out.println(tittel + ": " + liste);
    }

    // ------------------ Demo-harness ------------------
    public static void main(String[] args) {
        TreØving<Integer> tre = TreØving.byggEksempelTre();

        System.out.println("Nivåvis visning av eksempel-tre:");
        tre.printNivåvis();
        System.out.println();

        // Kallene under vil feile med UnsupportedOperationException helt til du implementerer dem.
        // Fjern kommentarer etter hvert som du løser TODO-ene.

        try {
            // printListe("Preorden", tre.preorden());
        } catch (UnsupportedOperationException e) {
            System.out.println("[Hint] Implementer preorden() først.");
        }

        try {
            // printListe("Inorden", tre.inorden());
        } catch (UnsupportedOperationException e) {
            System.out.println("[Hint] Implementer inorden() neste.");
        }

        try {
            // printListe("Postorden (rekursiv)", tre.postorden());
        } catch (UnsupportedOperationException e) {
            System.out.println("[Hint] Implementer postorden() deretter.");
        }

        try {
            // TreØving<Integer>.Node<Integer> start = tre.førstePostorden(tre.rot);
            // System.out.println("Første i postorden: " + start);
            // System.out.print("Trinnvis via nestePostorden: ");
            // for (var p = start; p != null; p = tre.nestePostorden(p)) System.out.print(p + " ");
            // System.out.println();
        } catch (UnsupportedOperationException e) {
            System.out.println("[Hint] Implementer førstePostorden() og nestePostorden().");
        }

        try {
            // printListe("Postorden (første/neste)", tre.postordenIterasjon());
        } catch (UnsupportedOperationException e) {
            System.out.println("[Hint] Når første/neste fungerer, implementer postordenIterasjon().");
        }

        // Ekstra: rask bygging av BST hvis du vil lage egne trær
        TreØving<Integer> bst = new TreØving<>(Integer::compareTo);
        for (int v : new int[]{5, 2, 1, 4, 8, 7, 12}) bst.insertBST(v);
        System.out.println("\nNivåvis visning av rask BST:");
        bst.printNivåvis();
        // Her kan du bruke bst.*-metodene når du har implementert dem.
    }
}
