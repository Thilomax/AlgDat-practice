package ForelesningerØving.F17_HuffmannTre;

import java.util.PriorityQueue;

class HuffmannTre{
    Node rot;
    char[] tegn; //dette er tegnene vi vil kode og dekode osv.
    private static final class Node implements Comparable<Node>{
        char verdi;
        Node venstre;
        Node høyre;
        int prioritet;


        //definerer hvordan vi sammenligner noder. Vi sammenligner det mha prioritet.
        @Override
        public int compareTo(Node o) {
            return this.prioritet - o.prioritet;
        }

        //lar oss lage en node med verdi og prioritet, (tegn og frekvens)
        public Node(char verdi, int prioritet){
            this.verdi = verdi;
            this.prioritet = prioritet;
            venstre=null;
            høyre=null;
        }

        private Node(){
            venstre=null; høyre=null;
            prioritet = 0;
            verdi = 0;
        }

        //VIKTIG: Dette er den konstruktøren som lar oss kombinere noder (du husker når vi må kombinere de to minste i listen, denne lar oss gjøre det.)
        public Node(Node venstre, Node høyre){

            //her blir de  to nodene som blir sendt inn satt som barna til den nye kombinerte noden!!!
            this.høyre = høyre; this.venstre = venstre;


            this.prioritet = venstre.prioritet+ høyre.prioritet;
        }

    }

    //Konstruktør til huffmanntreet. Denne bygger faktisk treet (ikke venstreorientert kanonisk, men start-treet)
    //Den tar inn lista med tegnene og frekvensene deres
    public HuffmannTre(char[] tegn, int[] frekvenser){
        if (tegn.length != frekvenser.length)
            throw new IllegalArgumentException("Antall tegn og antall frekvenser må være like");
        if (tegn.length<2)
            throw new IllegalArgumentException("Skal ha minst to tegn i et Huffmanntre");

        this.tegn = tegn;

        //viktig: sett pq til å ta inn bare noder
        PriorityQueue<Node> pq = new PriorityQueue();

        for (int i = 0; i < frekvenser.length; i++){
            //denne går gjennom lista med tegn og frekvenser, og legger inn alle bladnodene (nodene med verdiene) i PQ. Dette er da en liste over alle tegn, som blir bladnoder.
            //resultatet vil være en liste over tegn sortert fra minst prioritet til størst. (altså minst frekvens til størst)
            pq.add(new Node(tegn[i],frekvenser[i]));
        }

        // Etter denne løkken:
        // pq inneholder nå én node per tegn, alle bladnoder.
        // Neste steg i Huffman-algoritmen (som skjer etter denne løkken)
        // vil være å slå sammen de to laveste frekvensene gjentatte ganger
        // til bare én node (roten) står igjen – det ferdige Huffman-treet.

        //så vi må iterere frem til pq sin size er 1
        while(pq.size()>1){
            //fjerner det minste elementet i priorityqueuen.
            Node p = pq.remove();
            //fjerner det nest minste elemenetet i pq (nåværende minste)
            Node q = pq.remove();

            //legger inn en ny node i PQ med den kombinerte prioriteten til p og q.
            //denne nye noden blir forelder til p og q.

            //Så når du skriver dette, skjer dette:  (se konstruktøren. p blir satt til dets venstrebarn og q til høyre)
            // this.venstre = p;
            //this.høyre = q;
            //this.frekvens = p.frekvens + q.frekvens;
            pq.add(new Node(p,q));
        }
        //nå er det en node igjen i pq, og det er rota. Så vi setter roten til den og fjerner elementet.
        rot = pq.remove();
    }
    public void printKoder(){
        printKoder(rot, "");
    }

    //denne metoden printer ut koden den får så langt hver gang den treffer en node. Det betyr at hver ting den faktisk printer ut er koden til en node.
    private void printKoder(Node denne, String kodeTilNå){
        //hvis vi har funnet en bladnode, skriv ut koden vi har så langt.
        if (denne.verdi!=0) {
            System.out.println(kodeTilNå);
            //kodeTilNå + "0" skaper en ny streng, det endrer ikke den gamle.
            //Etter at den rekursive kallet er ferdig (altså når metoden “returnerer” og du går opp ett nivå igjen i treet), fortsetter du med den gamle kodeTilNå-verdien, uten "0" lagt til.
            return;
        }
        //hvis denne.verdi == 0, så har vi funnet en node som ikke er en av bladnodene. Da vet vi at den har barn? så vi kaller rekursivt på barna
        printKoder(denne.venstre, kodeTilNå + "0");
        printKoder(denne.høyre, kodeTilNå+"1");
        //I Huffman-treet er det slik at hver indre node alltid har to barn.
    }

    //Hva denne metoden skal gjøre:
    //Returnere en String[] der hver indeks svarer til samme indeks i tegn[]
    //koder[i] skal være Huffmann-koden til tegn[i]

    //denne metoden trenger hjelpemetoden private finnKoder
    public String[] finnKoder(){
        String[] koder = new String[tegn.length];
        finnKoder(rot, "", koder);
        return koder;
    }

    //denne metoden trenger hjelpemetoden private int finn(char c)
    private void finnKoder(Node denne, String kodeTilNå, String[] koder){
        if (denne == null) return;

        if (denne.verdi!= 0){
            //hvis vi har funnet en bladnode, finner vi posisjonen til dette tegnet i tegn arrayet
            int pos = finn(denne.verdi);
            //legg koden du har bygget opp (kodeTilNå) inn i koder[indeks].
            koder[pos] = kodeTilNå; //FORDI: Dette legger inn den tilsvarende koden til tegnet vi er på nå i kode arrayet. Så hvis vi er på posisjon 3 som er C f. eks, vil posisjon 3 i koder[] være koden til C.
            return; //stopper oss fra å gå videre rekursivt, vi har jo funnet en node. Kunne bli erstattet med else
        }
        finnKoder(denne.venstre, kodeTilNå+"0", koder);
        finnKoder(denne.høyre, kodeTilNå+"1", koder);
    }


    //denne finner posisjonen til et bestemt tegn i arrayet.
    private int finn(char c){
        for (int i = 0; i < tegn.length; i++){
            if (tegn[i]==c)
                return i;
        }
        throw new IllegalStateException("Noe har gått galt!");
    }

    //skal finne lengden på hver huffman-kode, (hvor mange biter til hvert tegn)
    public int[] finnLengder(){
        //ny array som samsvarer indeksene til tegn, koder og frekvenser til bitlengde
        int[] lengder = new int[tegn.length];
        finnLengder(rot,0,lengder);
        return lengder;
    }

    //fungerer nesten helt likt som finnKoder
    private void finnLengder(Node denne, int lengde, int[] lengder){
        if (denne==null)return;
        //funnet en bladnode
        if (denne.verdi!=0){
            int pos = finn(denne.verdi);
            //setter lengden vi har laget til nå til lengder[pos]
            lengder[pos] = lengde;
            return;
        }

        //Dette adder 1 til lengden hver gang vi går ned et nivå. og kaller rekursivt på metoden for å sette inn lengden i arrayet.
        finnLengder(denne.venstre, lengde+1, lengder); //Hver gang du går ett nivå ned i treet, legger du til +1 i lengden.
        finnLengder(denne.høyre, lengde+1, lengder);
        //Når du kommer til et blad, stopper du og lagrer den nåværende lengden i lengder[].
        //→ F.eks. lengder[pos] = lengde;
    }


    //vi trenger denne metoden for
    private int maks(int[] lengder){
        int maks = lengder[0];
        for (int i = 1; i < lengder.length; i++){
            if (lengder[i] > maks)
                maks = lengder[i];
        }
        return maks;
    }


    //finnLengder() → finner hvor lang hver kode skal være.
    //finnKanoniskeKoder() → bygger de faktiske kodene ut fra lengdene.

    public String[] finnKanoniskeKoder(){
        //Oppretter et array med lengdene til hvert tegn
        int[] lengder = finnLengder();

        // Finner den største lengden (altså den lengste Huffman-koden).
        // Dette tilsvarer dybden til den dypeste bladnoden i treet,
        // eller "nivået nederst" i Huffman-treet.
        int n = maks(lengder); //finner den største lengden, altså det nederste nivået til treet.

        // Oppretter et array for å lagre de kanoniske kodene (i strengform)
        // til hvert tegn, i samme rekkefølge som tegn[]
        String[]posisjoner = new String[tegn.length]; //lagrer posisjonen til hvert tegn i et array



        // 1 << n betyr "1 flyttet n biter til venstre",
        // som er det samme som 2^n.

        // Hvis n = 4, får du f.eks.:
        // 1 << 4 = 16 (binært: 10000)

        // Denne brukes ofte som et utgangspunkt for å generere
        // de kanoniske kodene, fordi man kan "gå nedover" nivåene
        // (lengdene) fra den lengste til den korteste koden
        // ved å høyreskifte (dele på 2) senere.
        int posisjon = 1<< n; //hvorfor trenger vi 2^n? fordi det gir oss startpunktet til å finne koden til de lengste nodene. Siden de har n i lengde, gir dette oss et tall vi kan gjøre om til binær.
        //det betyr flytt 1, n biter til venstre, altså 2^n.

        //Eksempel:
        //1 << 4 = 16, som i binær form er 10000.

        //Men datamaskinen lagrer det som tallet 16, ikke som teksten "10000".


        //itererer over alle kodelengdene, begynner på den lengste (altså nederst i treet)
        //Vi starter på det lengste nivået (n= lengste Huffman-kodelengde)
        //og jobber oss OPP mot de kortere kodene (nivåene over i treet)
        for (int i = n; i >= 0; i--){

            //den indre for loopen går gjennom alle tegnene
            // Sjekker: har dette tegnet kodelengde lik i? (nivået vi er på nå.)
            //vi gjør dette for å få en oversikt over hvilke tegn som er på hvilket nivå
            for (int j = 0; j<tegn.length; j++){

                //Hvis tegnet har samme lengde om nivået vi er på:
                if (lengder[j] == i)

                    //Oppretter vi en binærstreng av det nåværende tallet i posisjon
                    //Integer.toBinaryString(posisjon) gjør for eksempel 18 -> "10010"
                    //Vi øker så "posisjon" med 1 for å klargjøre neste kode

                    // .substring(1) brukes for å "kutte bort" det første 1-tallet
                    // fordi vi startet posisjon på 1<<n (f.eks. 10000 for n=4),
                    // og vi vil bare ha de n bitene etter det (de faktiske kodene).
                    //FORDI: 10-talls-indeksen til en node i et binærtre, gjort om til binærtall er nøyaktig stegene vi må ta for å komme oss til den noden UNNTATT det første tallet.
                    // for eksempel 18 = 10010. Da fjerner vi den første -> 0010. Da må vi gå venstre, venstre, høyre, venstre.
                    posisjoner[j] = Integer.toBinaryString(posisjon++).substring(1);
            }

            //Nå er vi ferdig med alle tegn som har lengde i. Da må vi dele posisjon på 2 (som er det samme som å høyreskifte en bit)

            //hvorfor?
            //fordi vi skal gå OPP et nivå, så bit-koden blir en bit kortere. Derfor fjerner vi det siste bitnivået.
            //posisjon /=2 betyr basically fjern en bit fra lengden av koden
            //(eller mer presist: høyreskifter alle bitene med én plass, som betyr gå ett nivå opp i treet).
            posisjon /= 2;
        }
        //returnerer lista med kodene til hvert tegn
        return posisjoner;
    }
}

public class HuffmannTreMain {
    public static void main(String[] args) {
        char[] tegn = {'A', 'B', 'C', 'D', 'E'}; // Må være sortert alfabetisk
        int[] frekvens = {17, 9, 5, 2, 12};
        HuffmannTre hf = new HuffmannTre(tegn, frekvens);
        hf.printKoder();
    }
}
