package ForelesningerØving.EksamenØving;

import java.util.NoSuchElementException;
import java.util.Stack;

public class Lenket_Liste_2023E {
}
@FunctionalInterface
interface Test<T> {
    boolean test(T verdi);
}
class LenketListe2023<T> {
    private Node hode;
    private final class Node {
        Node neste; T verdi;
        private Node(T verdi, Node neste) {
            this.verdi = verdi; this.neste = neste;
        }
        private Node(T verdi) {
            this(verdi, null);
        }
    }
    public LenketListe2023() {
        hode = null;
    }
    public boolean tom() {
        return hode == null;
    }
    public void leggInn(T verdi) {
        if (hode == null) hode = new Node(verdi);
        else hode = new Node(verdi, hode);
    }
    public T taUt() {
        if (hode == null)
            throw new NoSuchElementException("Lista er tom");
        T verdi = hode.verdi; hode = hode.neste;
        return verdi;
    }
    public LenketListe2023<T> filtrer(Test<T> p) {
        Node peker = hode;
        LenketListe2023 nyListe = new LenketListe2023();

        //SIDEN LEGGINN ALLTID LEGGER INN BAKERST, MÅ DU LEGGE DEM INN I EN STACK OG SÅ TA UT HEHEHEHEH
        Stack stack = new Stack();
        while(peker!=null){
            if (p.test(peker.verdi)) {
                stack.push(peker.verdi);
            }
            peker = peker.neste;
        }
        while (!stack.empty()){
            nyListe.leggInn(stack.pop());
        }
        return nyListe;
    }

    public int indeks(Stack<T> s, T val){
        int indeks = 0;
        int funnet = -1;
        Stack<T> hjelp = new Stack<>();
        while(!s.empty()){
            T tmp = s.pop();
            if (tmp.equals(val) && funnet == -1) {
                funnet = indeks;
            }
            hjelp.push(tmp);
            indeks++;
        }

        // dette legger inn den reverserte hjelpestacken tilbake på s, så den blir riktig rekkefølge igjen
        while(!hjelp.empty()){
            s.push(hjelp.pop());
        }
        return funnet;
    }
}
