package ForelesningerØving.F17_HuffmannTre;

import java.util.PriorityQueue;

public class Huffmanntre2 {

    private class Node implements Comparable<Node>{

        Node venstre, høyre;
        int prioritet;
        int verdi;

        private Node(){
            venstre= null; høyre =null;
            prioritet = 0;
            verdi = 0;
        }

        public Node(char verdi, int prioritet){
            this.verdi = verdi;
            this.prioritet = prioritet;
            venstre = null; høyre = null;
        }

        public Node(Node venstre, Node høyre){
            //setter de to nodene denne noden ble lagd av til å bli pekt på av denne
            this.venstre = venstre; this.høyre= høyre;
            this.prioritet = venstre.prioritet + høyre.prioritet;
        }

        @Override
        public int compareTo(Node o) {
            return this.prioritet-o.prioritet;
        }
    }
    Node rot;
    char[] tegn;

    public Huffmanntre2(char[] tegn, int[] frekvenser){
        //den kan ikke ha flere tegn enn frekvenser
        if (tegn.length!=frekvenser.length)
            throw new IllegalArgumentException("Antall tegn og bbb skal være like");
        // den må ha minst to tegn
        if (tegn.length <2 )
            throw new IllegalArgumentException("neineinei");

        this.tegn = tegn;

        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (int i = 0; i< frekvenser.length; i++){
            //legger inn alle tegn og dets frekvenser som noder.
            pq.add(new Node(tegn[i],frekvenser[i]));
        }

        while(pq.size()>1){
            Node p = pq.remove();
            Node q = pq.remove();
            pq.add(new Node(p,q));
        }
        //nå har vi gått gjennom alle unntatt roten, fordi vi vil sette den siste til roten:
        rot = pq.remove();
    }

    private void printKoder(Node denne, String kodeTilNå){

    }

    public static void main(String[] args) {

    }
}
