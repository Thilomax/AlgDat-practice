package ForelesningerØving.F12_Binærtre;

import ForelesningerØving.F10_Stacks_n_Queues.LenketKø;

import java.util.NoSuchElementException;

class BinTre<T>{
    private Node<T> rot;
    private int antall;

    private static final class Node<T>{
        T verdi;
        Node<T> venstre;
        Node<T> høyre;

        public Node(T verdi, Node<T> venstre, Node<T> høyre){
            this.verdi = verdi;
            this.venstre = venstre;
            this.høyre=høyre;
        }
        public Node(T verdi){
            this(verdi,null,null);
        }
    }

    public BinTre(){
        this.rot = null;
        this.antall = 0;
    }

    /*Denne leggInnmetoden bruker bitshift som traversering, ikke iterasjon over et char array.*/

    public final void leggInn(int indeks, T verdi){
        //sjekker indeks
        if (indeks<=0) throw new IllegalArgumentException("Ugyldig indeks.");

        // 2) Spesialtilfelle: posisjon 1 er roten
        if (indeks == 1) {
            // Hvis det allerede finnes en rot, er plassen opptatt
            if (rot != null) throw new IllegalArgumentException("Roten er allerede opptatt");
            // Ellers opprett roten og øk antall
            rot = new Node<>(verdi);
            antall++;
            return;
        }

        // 3) Kan ikke sette inn under en tom rot. Forelder mangler for alle k > 1. Så hvis indeks er større enn 1, så ønskes det å sette på en høyere indeks enn rota, det går ikke!!!!
        if (indeks > 1 && rot == null) throw new IllegalArgumentException("Forelder mangler");

        // p brukes til å vandre i treet. Etter navigasjon stopper p på FORELDEREN
        Node<T> p = rot;


        // 4) Start masken på biten UNDER(fordi vi hopper over rota) MSB (Most significant Bit) i indeks
        // highestOneBit(indeks) er MSB. >> 1 hopper over rot-biten
        // >> 1 betyr bit-shift til høyre i Java.
        int neste = Integer.highestOneBit(indeks) >> 1;

        // 5) Gå nedover treet helt til vi står på forelderen
        // Vi stopper når neste == 1, siden siste bit (LSB) brukes til å velge venstre/høyre plass
        while (neste > 1) {

            // Bitvis test: 0 betyr venstre, ikke 0 betyr høyre
            // (indeks & neste) sjekker om den aktuelle biten i indeks er satt

            // indeks & neste betyr at vi itererer over bit-tallet, og får 1 når biten til indeks på den posisjonen stemmer
            // med biten til neste på den posisjonen, og 0 hvis ikke.
            // 110 & 101 = 100. 101 & 111 = 101.
            if ((indeks & neste) == 0) p = p.venstre;   // 0-bit: gå venstre
            else                      p = p.høyre;     // 1-bit: gå høyre

            // Hvis vi måtte gå til en forelder som ikke finnes, er strukturen hull i treet
            if (p == null) throw new IllegalArgumentException("Forelder mangler.");
            // Flytt masken én bit til høyre og fortsett
            neste >>= 1;
        }

        //nå etter while burde p peke på forelderen, fordi vi gikk til bit-nummeret >1, så vi stoppet et hakk før, altså der forelderen skal være
        //så dette går da og finner posisjonen der det nye tallet skal være.

        // 6) Nå står p på forelderen. Siste bit (LSB) i indeks avgjør om ny node skal til venstre eller høyre
        if ((indeks & 1) == 0) { // LSB = 0 → venstre plass
            if (p.venstre != null) throw new IllegalArgumentException("Plassen er allerede opptatt");
            p.venstre = new Node<>(verdi);
            antall++;
        } else {                 // LSB = 1 → høyre plass
            if (p.høyre != null) throw new IllegalArgumentException("Plassen er allerede opptatt");
            p.høyre = new Node<>(verdi);
            antall++;
        }
        /*Kort oppsummert i én setning:
        - vi hopper over MSB for å starte navigasjon fra roten,
        - går bit for bit nedover der 0 betyr venstre og 1 høyre,
        - stopper på forelderen før siste bit,
        - og lar LSB (least significant bit) bestemme om vi fester som venstre eller høyre barn.
        Dette kan også gjøres med en char array og iterere over den, så slipper man å tukle med mask og de rare greiene.*/
    }

    public final void leggInnCharArray(int indeks, T verdi){
        if (verdi == null) throw new IllegalArgumentException("Null-verdi ikke tillatt.");

        if (indeks<=0) throw new IllegalArgumentException("Ugyldig indeks.");

        // 2) Spesialtilfelle: posisjon 1 er roten
        if (indeks == 1) {
            // Hvis det allerede finnes en rot, er plassen opptatt
            if (rot != null) throw new IllegalArgumentException("Roten er allerede opptatt");
            // Ellers opprett roten og øk antall
            rot = new Node<>(verdi);
            antall++;
            return;
        }

        // 3) Kan ikke sette inn under en tom rot. Forelder mangler for alle k > 1. Så hvis indeks er større enn 1, så ønskes det å sette på en høyere indeks enn rota, det går ikke!!!!
        if (indeks > 1 && rot == null) throw new IllegalArgumentException("Forelder mangler");

        // p brukes til å vandre i treet. Etter navigasjon stopper p på FORELDEREN
        Node<T> p = rot;

        String binærIndeks = Integer.toBinaryString(indeks);
        char[] binærarray = binærIndeks.toCharArray();

        //vi må iterere til binærarray.length -1 for å komme til posisjonen der forelder skal være
        for (int i = 1; i<binærarray.length-1; i++){
            if (binærarray[i]=='1') p=p.høyre; //må sammenligne med '1' fordi det er en char
            if (binærarray[i]=='0')p = p.venstre;

            //hvis pekeren p er på null nå, så hadde vi trengt å en forelder som ikke eksisterer for å legge inn noden vår, så det går ikke.
            if (p == null) throw new IllegalArgumentException("Forelder mangler.");
        }

        //nå er vi på plassen til forelderen til noden vår
        //Vi stopper på forelderen, fordi hvis vi ville legge inn i loopen hadde vi måttet sjekke hver gang "er dette siste bit? hvis ja, legg til" men her bare gjør vi det når vi har nådd riktig posisjon

        if (binærarray[binærarray.length-1] == '0'){
            if (p.venstre != null) throw new IllegalArgumentException("Plassen er allerede opptatt.");
            p.venstre = new Node<>(verdi);
            antall++;
        }
        if (binærarray[binærarray.length-1] == '1'){
            if (p.høyre != null) throw new IllegalArgumentException("Plassen er allerede opptatt.");
            p.høyre = new Node<>(verdi);
            antall++;
        }
    }

    /*Del 3: Preorden, inorden, postorden og nivåorden*/

    public void preorden(){
        if (rot != null)
            preorden(rot);

    }

    public void inorden(){
        if (rot != null)
            inorden(rot);

    }

    public void postorden(){
        if (rot != null)
            postorden(rot);
    }

    /*PREORDEN: Først seg selv, så venstre, så høyre*/
    private void preorden(Node<T> p){
        if (p==null) return;
        System.out.println(p.verdi);  //printer seg selv
        preorden(p.venstre);        // kaller metoden rekursivt på venstrebarn. Dette gjør: print først venstre barnet, så gå til venstrebarnet til det og print deg og fortsett til det ikke er noe venstrebarn, så går den opp grenene igjen og printer venstrebarna helt ned og så begynner den på høyrebarna
        preorden(p.høyre);          // printer høyre
    }

    private void inorden(Node<T> p){
        if (p==null) return;
        inorden(p.venstre);            //behandle først venstre barna
        System.out.println(p.verdi);
        inorden(p.høyre);
    }

    private void postorden(Node<T> p){
        if (p==null) return;
        postorden(p.venstre);
        postorden(p.høyre);
        System.out.println(p.verdi);
    }

    /*Bruke en kø for å printe ut FIFO. */
    public void nivåorden(){
        if (rot == null) return;
        LenketKø lk = new LenketKø();               //Oppretter en ny lenket kø
        lk.enqueue(rot);                            //legger til roten i den lenkede køen
        Node<T> p = rot;
        while(!lk.tom()){                           //bruker tom()-metoden jeg lagde hehe
            p = (Node<T>) lk.dequeue();
            System.out.println(p.verdi);       // fjerner og printer ut det første elementet i køen

            if (p.venstre!=null) lk.enqueue(p.venstre); //legger til eventuelle venstrebarn i køen (som blir fjerna neste runde)
            if (p.høyre!=null) lk.enqueue(p.høyre);     //legger til eventuelle høyrebarn
        }
    }
}


public class BinærtreØving {
}
