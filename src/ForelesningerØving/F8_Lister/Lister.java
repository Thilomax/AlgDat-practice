package ForelesningerØving.F8_Lister;

public interface Liste<T> extends Beholder<T>{
    /*Så forskjellen på en beholder og en liste er at en liste er en beholder som også har informasjon om indeks for å ha rekkefølge. Derfor extender interfacet Liste Beholder. For å legge inn metodene, men utvide dem til å inneholde indeks*/


    /** Pre: t != null
    * Post: antall() øker med 1 hvis elementet ble lagt inn*/
    boolean leggInn(int indeks, T t);


    /** Pre: 0 <= indeks < antall()
    * Post: returnerer elementet på plass indeks, og listen er uendret*/
    T hent(int indeks); //det er en T fordi vi returnerer verdien av det som er på den lista. Altså, det kan være en int eller en string eller hva som helst, så vi bruker T


    /**Pre: t != null, 0 <= indeks < antall().
     * Post: elementet på posisjon indeks er t, antall() er uendret,
     * returverdien er elementet som ble erstattet.*/
    T oppdater(int indeks, T t); //dette kunne ha vært en boolean for å sjekke om det funket, men vi returnerer heller det elementet vi erstattet med det nye. klarere.

    /**Pre: 0 <= indeks < antall().
     * Post: antall() er redusert med 1, elementet som lå på posisjon indeks er fjernet,
     * og alle elementer etter denne er flyttet én posisjon til venstre. Returnerer true hvis fjerning skjedde.*/
    boolean fjern(int indeks);

    /** Pre: t != null.
     *Post: hvis t finnes, returneres minste i der 0 <= i < antall() og hent(i).equals(t),
     *ellers -1. Listen er uendret.*/
    int indeksTil(T t);
}

class EnkeltLenketListe<T> implements Liste<T> {

    // Dette er en enkelt node, som vi lager instanser av.
    // Den har en verdi og en peker videre til neste node i lista.
    private class Node<T> {
        T verdi;        // dataen vi lagrer i denne noden (av typen T)
        Node<T> neste;  // peker på neste node i kjeden, eller null hvis det er siste node

        //En konstruktør som kaller på konstruktøren med parameteret neste
        public Node(T verdi) {
            this(verdi, null);  // delegasjon: bruk hovedkonstruktøren og sett neste = null
        }
        //Konstruktør med parametere
        public Node(T verdi, Node<T> neste){
            this.verdi = verdi; // all faktisk initiering skjer her
            this.neste = neste;
        }
        /**Hvorfor ha to? I 90% av tilfellene lager du en ny node uten å kjenne “neste” enda. Da er new Node<>(verdi) kort og tydelig, og “neste” blir automatisk null.*/
    }


    // Variabel som peker på den første noden i lista.
    // Når lista er tom er hode = null.
    // Hvis lista har elementer, så peker hode på "hodenoden" (den første i kjeden).
    private Node<T> hode;

    // Variabel som peker på den siste noden i lista.
    // Når lista er tom er hale = null.
    // Når du legger inn bakerst, kan vi oppdatere hale direkte (i stedet for å traversere).
    private Node<T> hale;

    // Teller hvor mange elementer som ligger i lista.
    // Starter på 0, økes når vi legger inn, og reduseres når vi fjerner.
    private int antall;

    private void sjekkIndeks(int indeks, boolean leggInn) {
        // Hvis vi skal legge inn (leggInn == true):
        //    - indeks må være 0 eller større
        //    - indeks kan være helt opp til antall (== å legge inn bakerst)

        //Fordi legginn er en boolean, det bestemmer at hvis vi legger inn, bruker vi denne if-en som tillater høyere indeks, og hvis ikke, så bruker vi den under.
        if (leggInn && indeks >= 0 && indeks <= antall) {
            return; // gyldig indeks for leggInn
        }

        // Hvis vi IKKE skal legge inn (altså hente, fjerne eller oppdatere):
        //    - indeks må være 0 eller større
        //    - indeks må være mindre enn antall (må peke på et eksisterende element)
        if (indeks >= 0 && indeks < antall) {
            return; // gyldig indeks for hent/fjern/oppdater
        }

        // Hvis ingen av betingelsene over er oppfylt, er indeksen ugyldig
        throw new IndexOutOfBoundsException("Ulovlig indeks");
    }

    @Override
    public boolean leggInn(T t){
        // 1) null-sjekk
        if (t==null) throw new NullPointerException("Null er ikke lov");
        // 2) lag ny node
        Node ny = new Node<>(t);
        // 3) hvis tom liste: hode = hale = ny
        //    ellers: hale.neste = ny; hale = ny;
        if (hode==null) { //hvis hode == null, så er lista tom.
            hode = ny;
            hale = ny;
        } else {
            // lista er ikke tom (hode != null)
            hale.neste = ny; // 1) lenk den gamle halen sin peker til den nye noden, så kjeden blir lenger
            hale = ny;  // 2) oppdaterer hale til å peke på den nye noden, som nå er siste node i lista.
        }
        // 4) antall++
        antall++;
        // 5) return true
        return true;
    }

    @Override
    public boolean leggInn(int indeks, T t) {
        if (t==null) throw new NullPointerException("Null er ikke lov");
        sjekkIndeks(indeks, true);
        Node ny = new Node<>(t);

        //Legge inn foran? Da må vi endre hodet.
        if (indeks == 0){
            ny.neste = hode; // peker det nye hodet sin peker på det gamle hodet
            hode = ny; //gjør den nye noden om til hodet.
        }

        //hvis lista er tom, dvs hodet er null, så gjør vi hodet til ny og hale til ny.
        if (hode== null){
            hode = ny;
            hale = ny;
        }

        //Legge inn bakest? da må vi endre hale
        if (indeks == antall){
            hale.neste = ny; // peker hale sin peker på den nye noden, fordi den vil komme etter den daværende halen
            hale = ny; // gjør om hale til den nye noden.
        }

        //legge inn et sted i midten? Da må vi finne noden før der vi vil legge inn (fordi i enkeltlenkede lister så kan man bare gå fremover)
        if (0<indeks && indeks <antall){
            Node<T> p = hode; //vi begynner søket på hodet. p er en variabel som peker på en node.
            for (int i = 0; i<indeks-1; i++){
                p= p.neste; // vi hopper videre til neste node, indeks-1 ganger.
                // Når du skriver p = p.neste;, flytter du p til å peke på den neste noden i kjeden.
            }
            // nå peker p på noden rett før den posisjonen vi skal sette inn på

            ny.neste = p.neste; //nå peker ny sin peker på den gamle neste
            //Dette skjer når du skal sette inn en ny node.

            /*La oss si vi har:

                [A] -> [B] -> [C] -> null
                          ^
                          p (forrige node)


                Og vi skal sette inn X etter B.

                Først lager vi ny = [X].

                Så gjør vi ny.neste = p.neste;.

                p.neste peker på C, så da settes ny.neste til å peke på C.

                Resultat:

                [A] -> [B] -> [C] -> null
                          ^     ^
                          p     ny.neste*/

            p.neste = ny; // p peker på ny
            //p.neste pekte på C, men nå setter vi det til å peke på ny.
        }
        antall++;
        return true;
    }

    @Override
    public T hent(int indeks) {
        sjekkIndeks(indeks, false); // sjekker at 0 <= indeks < antall
        Node<T> p = hode; // Vi starter søket på hodet
        for (int i = 0; i < indeks; i++){ //går opp i lista indeks ganger
            p = p.neste; // dette er operasjonen som gjør at vi faktisk går oppover i lista
        }
        return p.verdi; // returnerer verdien til p, fordi etter for-loopen har vi funnet noden som vi vil finne, med  riktig indeks.
    }

    // YOOOO DU MEKKA DENNE FØRSTE FORSØK UTEN HJELPEMIDLER!!!!!!!!!!!!!
    // kommentarer er lagt inn etter for at du skal huske i fremtiden
    @Override
    public T oppdater(int indeks, T t) {
        sjekkIndeks(indeks, false);                                  // 1) sjekker indeks, må være 0 <= indeks < antall
        if (t==null) throw new NullPointerException("Null er ikke lov");    // 2) null-policy
        Node<T> p = hode;                                                   // 3) start på hodet
        for (int i = 0; i < indeks; i++){
            p = p.neste;                                                    // 4) gå 'indeks' steg framover
        }
        T gammel = p.verdi;                                                 // 5) lagre den gamle verdien
        p.verdi = t;                                                        // 6) sett inn den nye (t)
        return gammel;                                                      // 7) returner den gamle (kontrakten)
    }

    @Override
    public boolean fjern(int indeks) {
        sjekkIndeks(indeks, false);                      // sørger for at 0 <= indeks < antall

        if (indeks == 0){                                       //Tilfelle 1: fjerne første node (hodet)

            hode = hode.neste;                                  //flytt hode til neste node
            if (antall == 1) hale = null;                       //Hvis dette var det eneste elementet, må vi nullstille hale også.
        }

        else {                                                  //Tilfelle 2: fjerne en node i midten eller bakerst
            Node<T> p = hode;
            for (int i = 0; i<indeks-1; i++){
                p = p.neste;                                    //Vi går frem til noden RETT FØR noden vi vil fjerne
            }
            Node<T> slettes = p.neste;                          //Dette er noden som blir fjernet
            p.neste = slettes.neste;                            //hopp over slettes: koble p direkte til noden etter slettes


            if (slettes == hale){                               //Hvis siste node fjernes, oppdater hale til å peke på forrige
                hale = p;
            }
        }
        antall--;                                               // én node mindre i lista
        return true;                                            // signaliserer at fjerningen ble gjennomført
    }

    @Override
    public int indeksTil(T t) {
        // Null-policy: vi tillater ikke å søke etter null
        if (t == null) throw new NullPointerException("Null er ikke lov");

        // Start på hodet og gå fremover node for node
        Node<T> p = hode;
        int indeks = 0;

        // Traverser lista til vi enten finner elementet eller kommer til slutten
        while (p != null) {
            if (p.verdi.equals(t)) {
                return indeks;   // første forekomst funnet → returner indeksen
            }
            p = p.neste;         // hopp videre til neste node
            indeks++;            // oppdater indeksen samtidig
        }

        // Hvis vi kommer hit, finnes ikke elementet i lista
        return -1;
    }


    @Override
    public boolean fjern(T t) {
        // Null-policy: vi tillater ikke å lete etter null
        if (t == null) throw new NullPointerException("Null er ikke lov");

        // Tom liste? Ingenting å fjerne.
        if (hode == null) return false;

        // Case 1: Første (hode) matcher
        // Sjekk hodet separat – både fordi det er enkelt,
        // og fordi vi må oppdatere hode (og ev. hale) spesielt.
        if (hode.verdi.equals(t)) {
            hode = hode.neste;          // flytt hode forbi første node
            if (hode == null) {         // hvis det var eneste element
                hale = null;            // lista er nå tom → nullstill hale også
            }
            antall--;                   // ett element mindre
            return true;                // fjernet første forekomst
        }

        // Case 2: Treffer et sted etter hodet (midten eller bakerst)
        // Bruk to pekere: 'forrige' peker på noden rett før 'p'.
        Node<T> forrige = hode;
        Node<T> p = hode.neste;

        // Traverser kjeden til vi finner første forekomst
        while (p != null) {
            if (p.verdi.equals(t)) {
                // Koble ut p: hopp over p ved å la 'forrige' peke forbi den
                forrige.neste = p.neste;

                // Hvis vi fjernet siste node (halen), må hale flyttes til 'forrige'
                if (p == hale) {
                    hale = forrige;
                }

                antall--;               // oppdater størrelse
                return true;            // første forekomst fjernet
            }

            // Flytt pekere videre i kjeden
            forrige = p;
            p = p.neste;
        }

        // Ikke funnet i lista
        return false;
    }


    @Override
    public int antall() {
        // Returnerer bare hvor mange elementer som er i lista
        return antall;
    }

    @Override
    public boolean tom() {
        // Lista er tom hvis vi ikke har noen elementer
        // Vi kan enten sjekke antall == 0 eller hode == null (begge er like riktige)
        return antall == 0;
    }

    @Override
    public boolean inneholder(T t) {
        // Null-policy: vi tillater ikke å lete etter null
        if (t == null) throw new NullPointerException("Null er ikke lov");

        // Bruker indeksTil for å gjenbruke logikken vi allerede har skrevet
        return indeksTil(t) != -1;
    }

    @Override
    public void nullstill() {
        // Nullstill pekerne, da mister vi hele kjeden
        hode = null;
        hale = null;
        antall = 0;

        // Når vi "slipper" alle noder, vil garbage collector ta seg av resten
    }

}

class DobbeltLenketListe<T> implements Liste<T>{
    private static final class Node<T>{
        T verdi;
        Node<T> forrige, neste;

        Node(T verdi) {
            this.verdi = verdi;
        }

        Node(T verdi, Node<T> forrige, Node<T> neste){
            this.verdi = verdi;
            this.neste = neste;
            this.forrige = forrige;
        }

    }

    //Siden disse feltene er lista sine felt, gjør vi dem private for å  beskytte dem
    //Disse feltene skal selvfølgelig være for lista, ikke for indre noden.
    private Node<T> hode;
    private Node<T> hale;
    private int antall;


    private void sjekkIndeks(int indeks, boolean leggInn) {
        // Hvis vi skal legge inn (leggInn == true):
        //    - indeks må være 0 eller større
        //    - indeks kan være helt opp til antall (== å legge inn bakerst)

        //Fordi legginn er en boolean, det bestemmer at hvis vi legger inn, bruker vi denne if-en som tillater høyere indeks, og hvis ikke, så bruker vi den under.
        if (leggInn && indeks >= 0 && indeks <= antall) {
            return; // gyldig indeks for leggInn
        }

        // Hvis vi IKKE skal legge inn (altså hente, fjerne eller oppdatere):
        //    - indeks må være 0 eller større
        //    - indeks må være mindre enn antall (må peke på et eksisterende element)
        if (indeks >= 0 && indeks < antall) {
            return; // gyldig indeks for hent/fjern/oppdater
        }

        // Hvis ingen av betingelsene over er oppfylt, er indeksen ugyldig
        throw new IndexOutOfBoundsException("Ulovlig indeks");
    }

    @Override
    public boolean leggInn(T t) {
        if (t==null) throw new NullPointerException("Null er ikke lov");
        Node<T> ny = new Node<>(t); //oppretter en ny node med konstruktøren og putter inn t som verdi.
        if (hode == null){ //hvis lista er tom
            hode= ny;
            hale = ny;
        } else { // ellers setter  vi inn noden bakerst
            hale.neste = ny; //setter ny ETTER hale
            ny.forrige = hale;  //setter nys forrige peker til å peke på forrige hale
            hale = ny; //setter hale variablen til å inneholde informasjonen til ny, altså ny er nå halen
        }
        antall++; // fordi en node ble lagt inn
        return true;
    }

    @Override
    public boolean leggInn(int indeks, T t) {
        if (t == null) throw new NullPointerException("Null er ikke lov");
        sjekkIndeks(indeks, true);
        Node<T> ny = new Node<>(t);

        if (indeks == 0) {
            if (hode == null) {           //lista er tom
                hode = ny;
                hale = ny;
            } else {
                ny.neste = hode;        //"ny" peker fremover til gamle hodet
                hode.forrige = ny;      // det gamle hodet peker nå bakover til ny
                hode = ny;              // oppdaterer hode-variablen til å være ny

            }
            antall++;
            return true;
        }

        //Sette inn bakerst
        else if (indeks == antall){
            ny.forrige = hale;          // siden ny kommer bakerst, vil vi at ny sin forrige er hale


            if (hale ==null){           //hvis lista er tom
                hode = ny;
                hale = ny;
            } else{
                hale.neste = ny;        //hvis lista ikke er tom, gjør vi at den gamle halen sin neste peker på ny.
                hale = ny;              //og hale blir ny
            }
            antall++;
            return true;
        }
        else {

        //Sette inn i midten
        Node<T> p = finnNode(indeks);   // Kan ikke bruke hent metoden, fordi hent returnerer verdien T og ikke noden
        Node<T> før = p.forrige;        //før er noden rett FØR p

        ny.neste = p;                   //gjør at ny peker på den noden den lander før til basically
        ny.forrige = før;               // ny sin forrige peker er nå den noden før ny
        før.neste = ny;                 // før sin neste peker peker nå på ny
        p.forrige = ny;                 // p sin forrige node peker nå på ny.

        antall++;
        return true;
        }

    }


    private Node<T> finnNode(int indeks){

        //Hvis indeks er nær hode, altså første halvdel av lista: gå forfra
        if (indeks < antall/2) {
            Node<T> p = hode;           // vi begynner søket fra hodet
            for (int i = 0; i < indeks; i++){
                p = p.neste;
            }
            return p;
        }

        //Hvis indeks er i andre halvdel av lista: gå bakfra
        else {
            Node<T> p = hale;           // vi begynner søket fra halen
            for (int i = antall-1; i > indeks; i--){
                p = p.forrige;
            }
            return p;
        }
    }

    @Override
    public T hent(int indeks) {
        //Hvis indeks er nær hode, altså første halvdel av lista: gå forfra
        if (indeks < antall/2) {
            Node<T> p = hode;           // vi begynner søket fra hodet
            for (int i = 0; i < indeks; i++){
                p = p.neste;
            }
            return p.verdi;
        }

        //Hvis indeks er i andre halvdel av lista: gå bakfra
        else {
            Node<T> p = hale;           // vi begynner søket fra halen
            for (int i = antall-1; i > indeks; i--){
                p = p.forrige;
            }
            return p.verdi;
        }
    }

    @Override
    public T oppdater(int indeks, T t) {
        return null;
    }

    @Override
    public boolean fjern(int indeks) {
        return false;
    }

    @Override
    public int indeksTil(T t) {
        return 0;
    }


    @Override
    public boolean fjern(T t) {
        return false;
    }

    @Override
    public int antall() {
        return 0;
    }

    @Override
    public boolean tom() {
        return false;
    }

    @Override
    public boolean inneholder(T t) {
        return false;
    }

    @Override
    public void nullstill() {

    }
}

public class Lister {
}
