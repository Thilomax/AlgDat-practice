package ForelesningerØving.F12_Binærtre;

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
}


public class BinærtreØving {
}
