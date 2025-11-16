package ForelesningerØving.F13_BinærSøkeTre;

import ForelesningerØving.F10_Stacks_n_Queues.LenketStabel;
import ForelesningerØving.F10_Stacks_n_Queues.Stabel;
import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.StringJoiner;

public class BSToppgave2 {
    class Binærsøketre<T> implements Beholder<T>, Iterable<T> {

        @Override
        public Iterator<T> iterator() {
            return new InordenIterator();
        }

        private class InordenIterator implements Iterator<T> {
            Node<T> denne;
            Stabel<Node<T>> nodeStabel;
            public InordenIterator() {
                denne = rot;
                nodeStabel = new LenketStabel<>();
                while (denne.venstre != null) {
                    nodeStabel.push(denne);
                    denne = denne.venstre;
                }
            }
            @Override
            public boolean hasNext() {
                return (denne != null);
            }

            //denne metoden finner basically neste i inorden og derfor trenger den stabel, for å kunne komme seg opp igjen.
            @Override
            public T next() {
                T tmp = denne.verdi;
                if (denne.høyre != null) {
                    denne = denne.høyre;
                    while (denne.venstre != null) {
                        nodeStabel.push(denne);
                        denne = denne.venstre;
                    }
                } else {
                    if (!nodeStabel.tom())
                        denne = nodeStabel.pop();
                    else
                        denne = null;
                }
                return tmp;
            }
        }

        private static final class NodePar<T> {
            Node<T> current, forelder;
            private NodePar(Node<T> current, Node<T> forelder) {
                this.current = current;
                this.forelder = forelder;
            }
        } // Kunne brukt bare Node<T>[] med 2 verdier.

        private static final class Node<T> {
            Node<T> venstre, høyre;
            T verdi;

            private Node(T verdi, Node<T> venstre, Node<T> høyre) {
                this.verdi = verdi;
                this.venstre = venstre;
                this.høyre = høyre;
            }

            public Node(T verdi) {
                this(verdi, null, null);
            }
        }

        Node<T> rot;
        Comparator<? super T> cmp;

        public Binærsøketre(Comparator<? super T> cmp) {
            this.cmp = cmp;
            this.rot = null;
        }

        private boolean leggInnIterativ(T t) {
            Objects.requireNonNull(t, "Ikke lov å legge inn null-verdier");
            Node<T> p = rot;
            Node<T> q = null;

            while (p!=null){
                q = p;
                if (cmp.compare(t,p.verdi)<0) {
                    p = p.venstre;
                }
                else {
                    p = p.høyre;
                }
            }
            Node ny = new Node(t);
            if (q==null)
                rot = ny;
            else if (cmp.compare(t, q.verdi)<0)
                ny = q.venstre;
            else
                ny = q.høyre;
            return true;
        }

        private Node<T> leggInnRekursiv(Node<T> current, T t) {
            if (current == null)
                return new Node<>(t);
            if (cmp.compare(t, current.verdi)<0)
                current.venstre = leggInnRekursiv(current.venstre, t);
            else
                current.høyre = leggInnRekursiv(current.høyre, t);
            return current;
        }

        private boolean leggInnRekursiv(T t) {
            Objects.requireNonNull(t, "Ulovlig med null-verdier.");
            rot = leggInnRekursiv(rot, t);
            return true;
        }

        @Override
        public boolean leggInn(T t) {
            return leggInnRekursiv(t);
        }

        private void fjernNode(Node<T> current, Node<T> forelder) {
            if (current.venstre == null && current.høyre == null){
                if (forelder == null){
                    rot = null;
                }
                if (forelder.venstre == current)
                    forelder.venstre = null;
                else
                    forelder.høyre = null;
            }
            // et barn, venstrebarnet
            else if (current.høyre == null){
                if (forelder == null)
                    rot = current.venstre;
                else if (forelder.venstre == current)
                    forelder.venstre = current.venstre;
                else
                    forelder.høyre = current.venstre;
            }
            //høyrebarnet
            else if (current.venstre == null) {
                if (forelder == null)
                    rot = current.høyre;
                else if (forelder.venstre == current)
                    forelder.venstre = current.høyre;
                else
                    forelder.høyre = current.høyre;
            }
            //har to barn
            else{
                Node<T> p = current.høyre;
                Node<T> q = current;

                while (p.venstre!=null){
                    q=p;
                    p= p.venstre;
                }
                current.verdi = p.verdi;
                fjernNode(p,q);
            }
        }

        @Override
        public boolean fjern(T t) {
            NodePar<T> par = finnNode(t);
            if (par == null)
                return false;
            fjernNode(par.current, par.forelder);
            return true;
        }

        public int fjernAlle(T t) {
            // Skal fjerne alle kopier av en verdi.
            // Skal gi ut hvor mange kopier det var.
            Stabel<NodePar<T>> stabel = finnAlle(t);
            int teller = 0;
            while (!stabel.tom()) {
                NodePar<T> par = stabel.pop();
                fjernNode(par.current, par.forelder);
                teller++;
            }
            return teller;
        }

        private int antall(Node<T> current) {
            if (current == null)
                return 0;
            return 1 + antall(current.venstre) + antall(current.høyre);
        }

        @Override
        public int antall() {
            return antall(rot);
        }

        @Override
        public boolean tom() {
            return (rot == null);
        }

        private NodePar<T> finnNodeIterativ(T t) {
            Node<T> forelder = null;
            Node<T> current = rot;
            while (current != null) {
                int cmpv = cmp.compare(t, current.verdi);
                if (cmpv == 0)
                    return new NodePar<T>(current, forelder);
                else if (cmpv < 0) {
                    forelder = current;
                    current = current.venstre;
                } else {
                    forelder = current;
                    current = current.høyre;
                }
            }
            return null;
        }

        private Stabel<NodePar<T>> finnAlle(T t) {
            Stabel<NodePar<T>> stabel = new LenketStabel<>();
            Node<T> current = rot;
            Node<T> forelder = null;
            while (true) {
                NodePar<T> par = finnNodeRekursiv(current, forelder, t);
                if (par == null)
                    break;
                current = par.current.høyre;
                forelder = par.current;
                stabel.push(par);
            }
            return stabel;
        }

        private NodePar<T> finnNodeRekursiv(Node<T> current, Node<T> forelder, T t) {
            if (current == null)
                return null;
            int cmpv = cmp.compare(t, current.verdi);
            if (cmpv < 0)
                return finnNodeRekursiv(current.venstre, current, t);
            else if (cmpv > 0)
                return finnNodeRekursiv(current.høyre, current, t);
            else
                return new NodePar<>(current, forelder);
        }

        private NodePar<T> finnNodeRekursiv(T t) {
            return finnNodeRekursiv(rot, null, t);
        }

        private NodePar<T> finnNode(T t) {
            return finnNodeIterativ(t);
        }

        @Override
        public boolean inneholder(T t) {
            return (finnNode(t) != null);
        }

        @Override
        public void nullstill() {

        }

        // Jeg lagde to metoder som går gjennom treet i inorden og printer
        // det som en liste. Vi kan nå se at treet oppfører seg som ønsket.
        private void inordenSJ(Node<T> current, StringJoiner sj) {
            if (current == null)
                return;
            inordenSJ(current.venstre, sj);
            sj.add(current.verdi.toString());
            inordenSJ(current.høyre, sj);
        }

        public String toString() {
            StringJoiner sj = new StringJoiner(", ", "[", "]");
            inordenSJ(rot, sj);
            return sj.toString();
        }
    }
}
