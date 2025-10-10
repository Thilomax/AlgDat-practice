package ForelesningerØving.F8_Lister;

public class TabellListe<T> implements Liste<T>{
    private T[] tabell;
    private int antall, kapasitet;

    public TabellListe(){
        /*Generisk konstruktør, default value = 10*/
        this(10); //Dette funker ikke uten den andre faktiske konstruktøren, fordi det kaller på den konstruktøren og sender inn 10 som parameter.

    }
    public TabellListe(int kapasitet){
        this.kapasitet = kapasitet;
        tabell = (T[]) new Object[kapasitet]; /**Siden man ikke kan lage et nytt array av en generisk type direkte, lager man et array av et objekt med størrelse på kapasitet-verdien, og caster det til T*/
        antall = 0;
    }

    private void utvidTabell(){
        /* - Når den gamle tabellen er full, lager vi et nytt array som er dobbelt så stort
        *  - Så kopierer vi over alle eksisterende elementer over til en ny generisk tmp-array
        *  - Så setter vi tabell = tmp*/
        kapasitet*=2;
        T[] tmp = (T[]) new Object[kapasitet];
        System.arraycopy(tabell, 0, tmp, 0, antall);
        /**Parameterne til System.arraycopy er respektivt:
         * - tabell (src) er kildetabellen, den vi henter fra.
         * - 0 (srcPos) = startposisjonen i kildetabellen
         * - tmp (dest) = destinasjonstabellen
         * - 0 (destPos) = startposisjonen i måltabellen
         * - antall (length) = antall elementer som skal kopieres.
         * Dette gjør essensielt det samme som en for løkke, bare raskere.*/
        tabell = tmp;
    }
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
        if (antall==kapasitet) { /**Hvis tabellen er full, så utvider vi*/
            utvidTabell();
        }
        tabell[antall++] = t; /**Vi plasserer verdien t på indeksen antall, og deretter øker antall med 1*/
        return true;
    }

    @Override
    public T hent(int indeks) {
        sjekkIndeks(indeks, false);
        return tabell[indeks];
    }

    @Override
    public T oppdater(int indeks, T t) {
        sjekkIndeks(indeks, false);
        T tmp = tabell[indeks];
        tabell[indeks] = t;
        return tmp; //returnerer den gamle verdien der
    }

    @Override
    public boolean fjern(int indeks) {
        sjekkIndeks(indeks, false);

        //Vi må finne hvor mange elementer vi flytter. Det er antall i tabellen minus indeks minus 1 (fordi tabellen er antall-1 stor)
        int antFlyttes = antall-indeks-1;

        if (antFlyttes>0){
            // Kopiér området [indeks+1 .. antall-1] til å starte på 'indeks'
            // src = tabell, srcPos = indeks+1
            // dest = tabell, destPos = indeks
            // length = antFlyttes
            System.arraycopy(tabell,indeks,tabell,indeks,antFlyttes);
        }
        //Nå er det siste elementet i lista duplisert, så vi må fjerne verdien.
        tabell[--antall] = null; //hvis vi hadde tatt -- etter antall, så hadde det vært out of bounds
        return true;
    }

    @Override
    public int indeksTil(T t) {
        if (t == null) throw new NullPointerException("Null er ikke tillatt");

        for (int i = 0; i<antall;i++){
            if (tabell[i].equals(t)) return i; //jeg brukte ==, men det er for primitive variabler.
        }
        return -1;
    }

    @Override
    public boolean leggInn(int indeks, T t) {
        sjekkIndeks(indeks, true);
        if (t == null) throw new NullPointerException("Null er ikke tillatt");
        if (antall == kapasitet){
            utvidTabell();
        }

        //flytte alle elementer over indeks ett steg opp
        for (int i = antall-1; i>= indeks; i--) {
            tabell[i+1] = tabell[i];
        }
        tabell[indeks] = t;
        antall++; //vi må oppdatere størrelsen på arrayet
        return true;
    }

    @Override
    public boolean fjern(T t) {
        if (t == null) throw new NullPointerException("Null er ikke tillatt");
        int fjernIndeks = indeksTil(t); //vi kan jo bare bruke metoden vår lol. Dette finner første forekomst.
        if (fjernIndeks == -1) return false;

        int antFlyttes = antall-fjernIndeks-1;

        if (antFlyttes>0){

            System.arraycopy(tabell,fjernIndeks+1,tabell,fjernIndeks,antFlyttes);
        }

        tabell[--antall] = null;
        return true;

    }

    @Override
    public int antall() {
        return antall;
    }
    public void reduserAntall(){
        antall--;
    }

    @Override
    public boolean tom() {
        return false;
    }

    @Override
    public boolean inneholder(T t) {
        if (t==null) throw new NullPointerException("Null er ikke tillatt");
        for (int i = 0; i<antall; i++){
            if (tabell[i].equals(t)) return true;
        }
        return false;
    }

    @Override
    public void nullstill() {
        // Setter kapasiteten tilbake til startverdien (10).
        // Dette betyr at vi ikke bare "tømmer" lista, men også
        // reduserer den tilbake til en standardstørrelse.
        kapasitet = 10;

        // Setter antall til 0 for å markere at lista nå er tom.
        // Ingen elementer skal lenger regnes som lagret.
        antall = 0;

        // Lager en ny tom tabell med kapasitet 10.
        // Den gamle tabellen (med eventuelle gamle objektreferanser)
        // blir erstattet, slik at alle gamle elementer blir frigjort
        // og kan ryddes opp av garbage collector.
        tabell = (T[]) new Object[kapasitet];
    }

}
