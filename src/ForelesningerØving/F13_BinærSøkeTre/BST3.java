package ForelesningerØving.F13_BinærSøkeTre;

import ForelesningerØving.F8_Lister.Beholder;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public class BST3 {
    class BinærSøkeTre<T> implements Beholder<T>, Iterable<T>{
        Node<T> rot;

        int antall;
        Comparator<?super T> cmp;

        public BinærSøkeTre(Comparator<? super T> cmp) {
            rot = null;
            this.cmp = cmp;
        }



        @Override
        public boolean leggInn(T t) {
            return false;
        }

        private boolean leggInnIterativ(T t){
            Objects.requireNonNull(t);
            Node<T> p = rot;
            Node<T> q = null;
            while(p!=null){
                int cmpv = cmp.compare(t, p.verdi);
                if (cmpv <0){
                    q=p;
                    p=p.venstre;
                } else{
                    q=p;
                    p=p.høyre;
                }
            }
            Node<T> ny = new Node<>(t);
            if (q==null){
                rot = ny;
            } else if (cmp.compare(t, q.verdi)<0)
                q.venstre = ny;
            else
                q.høyre =ny;
            return true;
        }

        private Node<T> leggInnRekursiv(Node<T> current, T t){
            if (current == null)
                return new Node<>(t);
            if (cmp.compare(t,current.verdi)<0)
                current.venstre = leggInnRekursiv(current.venstre, t);
            else
                current.høyre = leggInnRekursiv(current.høyre,t);
            return current;
        }

        private boolean leggInnRekursiv(T t){
            Objects.requireNonNull(t);
            leggInnRekursiv(rot, t);
            return true;
        }


        private NodePar<T> finnNodeIterativ(T t){
            Objects.requireNonNull(t);
            Node<T> p = rot;
            Node<T> q = null;

            while(p!=null){
                int cmpv = cmp.compare(t, p.verdi);
                if (cmpv==0){
                    return new NodePar<>(p,q);
                }
                if (cmpv < 0){
                    q = p;
                    p = p.venstre;
                } else if (cmpv>0){
                    q = p;
                    p = p.høyre;
                }
            }
            return null;
        }
        @Override
        public boolean fjern(T t) {
            Objects.requireNonNull(t);
            NodePar<T> slettes = finnNodeIterativ(t);
            if (slettes == null || slettes.denne == null) return false;
            fjernNode(slettes.denne,slettes.forelder);
            antall--;

            return true;
        }

        private void fjernNode(Node<T> p, Node<T> q){
            /*
             * Først finn noden du skal slette med finnNode
             * Så er det tre tilfeller:
             * 0 barn
             * 1 barn
             * 2 barn
             *
             * 0 barn: sett den korrekte foreldrepekeren til å være null
             * 1 barn: sett den KORREKTE foreldrepekern til å peke på det riktige barne (sjekk hvilket barn p er og hvilket barn p HAR
             * 2 barn: finn neste node i inorden, kopier verdien til den til noden du skal slette, så slett neste node i inorden, enten manuelt eller rekursivt
             * */
            if (p.høyre == null && p.venstre == null){
                if (q==null) rot = null; //da er p roten, og den har ingen barn, så vi setter roten til å være null
                else if (p==q.venstre){
                    q.venstre = null;
                } else{
                    q.høyre = null;
                }
                antall--;
            }

            //1 barnstilfellet
            //hvis den bare har et venstrebarn
            else if (p.høyre == null){
                if (q==null)
                    rot = p.venstre;
                else if(p==q.venstre)
                    q.venstre = p.venstre;
                else
                    q.høyre = p.venstre;
                antall--;
            }
            //hvis den bare har et høyrebarn
            else if (p.venstre ==null){
                if (q==null)
                    rot = p.høyre;
                else if (p==q.venstre){
                    q.venstre = p.høyre;
                } else
                    q.høyre = p.høyre;
                antall--;
            }

            // to barn
            else{
                Node<T> current = p.høyre;
                Node<T> forelder = p;

                while (current.venstre!=null){
                    forelder=current;
                    current=current.venstre;
                }

                p.verdi = current.verdi;

                fjernNode(current,forelder);
            }
        }

        @Override
        public int antall() {
            return antall;
        }

        @Override
        public boolean tom() {
            return antall == 0;
        }

        @Override
        public boolean inneholder(T t) {
            Objects.requireNonNull(t);
            Node<T> p = rot;
            while(p!=null){
                int cmpv = cmp.compare(t, p.verdi);
                if (cmpv==0)
                    return true;
                if (cmpv<0)
                    p = p.venstre;
                else
                    p= p.høyre;
            }
            return false;
        }

        @Override
        public void nullstill() {
            if (rot == null ) return;
            nullstillRekursivt(rot);
            rot = null;
            antall = 0;
        }

        private void nullstillRekursivt(Node<T> p){
            if (p==null) return;
            nullstillRekursivt(p.venstre);
            nullstillRekursivt(p.høyre);

            p.venstre= null;
            p.høyre = null;
        }

        @Override
        public Iterator<T> iterator() {
            return new InordenIterator();
        }
        private class InordenIterator implements Iterator<T>{
            Node<T> denne;

            @Override
            public boolean hasNext() {
                return denne !=null;
            }
            @Override
            public T next() {
                return null;
            }

        }
        private static final class NodePar<T>{
            Node<T> denne;

            Node<T> forelder;
            public NodePar(Node<T> denne, Node<T> forelder){
                this.denne = denne;
                this.forelder = forelder;
            }
        }
        private static final class Node<T>{
            Node<T> venstre; Node<T> høyre;

            T verdi;
            public Node(T verdi){
                this.verdi = verdi;
                this.venstre =null;
                this.høyre = null;
            }

        }
    }
}
