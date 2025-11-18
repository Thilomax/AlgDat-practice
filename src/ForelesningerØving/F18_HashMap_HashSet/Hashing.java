package ForelesningerØving.F18_HashMap_HashSet;

import java.util.Objects;

public class Hashing {

}

//Et map: Nøkkel og verdi, kalles derfor ofte for dictionary. Hver nøkkel kan maks dukke opp en gang.

//Hva er N og V? Det er bare et annet navn for generisk typeparameter. Det er akkurat som T. bruker bare N og V for lesbarhet.
interface Map<N, V>{
    //Legger inn en verdi med nøkkel og verdi, gir ut en eventuell verdi om den overskrev noe
    public V leggInn(N nøkkel, V verdi);

    //fjerner en verdi basert på nøkkel, og gir ut verdien den fjernet
    public V fjern(N nøkkel);

    //henter en verdi basert på en nøkkel
    public V hent(N nøkkel);
}


//Et set skal bare kunne legge inn et element, sjekke om et element er med, og fjerne element. Ikke hente spesifikke elementer elller bry seg om rekkefølge. Teller ikke et element flere ganger.
interface Set<V>{
    public V leggInn(V verdi);
    public V fjern(V verdi);
    public V inneholder(V verdi);
}

class Identitet<T>{
    int id;
    String navn;


    //overskriver .equals metoden, spesifikt til det vi trenger den til.
    //Standard-versjonen sjekker bare om to objekter er samme plass i minnet.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; //sjekker om det er nøyaktig samme objekt.
        /*F. eks:
        *   Test t1 = new Test("Thilo", 21);
            System.out.println(t1.equals(t1)); // true*/

        //Dette sjekker først om DUMMY == null, da kan den ikke være lik noe.
        //Så sjekker den om objektet er fra samme klasse som vårt, hvis ikke kan de ikke være like.
        if (obj==null || getClass() != obj.getClass()) return false;

        //Nå kan vi caste DUMMY til Identitet, fordi vi kan garantere at DUMMY faktisk er et Identitet-objekt.
        //Vi caster, fordi parameteret vårt er Object, ikke Identitet, så for å kunne gjøre DUMMY.id så må det castes til Identitet.
        Identitet<?> identitet = (Identitet<?>) obj; //Dette forteller java at DUMMY er av type Identitet.

        //Vi castet DUMMY til å bli objektet identitet av typen Identitet, så nå kan vi bruke dets id og navn til å sammenligne
        return id == identitet.id && Objects.equals(navn, identitet.navn);
    }
}

class HashMap<N, V> implements Map<N, V>{

    //Dette er en array av lenkede lister. Hver plass i tabellen peker til en Node som igjen kan peke videre til neste node. Det er sånn vi håndterer kollisjoner (at to ting er på samme indeks)
    Node[] hash;

    int antall; //hvor mange elementer som er lagret i hashmapen

    //tabellstørrelse. Hvor mange posisjoner arrayet kan ha. altså antall indekser. Imotsetning til antall som teller hver indeks + alle elementer som er lagret der.
    int dimensjon;

    //Dette er en variabel som forteller hvor full tabellen kan være før den utvides. (75% før vi utvider)
    //tetthet er lagret som en float fordi den representerer et forhold — altså en desimalverdi mellom 0 og 1, ikke et helt tall.
    float tetthet = 0.75f;

    //Dette er maksimalt antall elementer du kan ha før du må utvide tabellen. Når antal >= grense, kaller man på en utvid()-metode som oppretter en ny større tabell.
    int grense;


    private class Node{
        N nøkkel;
        V verdi;
        Node neste;

        public Node(N nøkkel, V verdi, Node neste) {
            this.nøkkel = nøkkel;
            this.verdi = verdi;
            this.neste = neste;
        }
    }

    public HashMap(){
        this.dimensjon = 13; //start størrelse på hash tabell, ofte et primtall som 13

        //Dette beregner antall elementer arrayet kan ha før den må utvides. Det er antall posisjoner (dimensjon) x prosenten vi har satt tettheten til.
        //DVS: hvis vi har en array på 100 plasser, kan den nå bli fylt opp til 75 før den må utvides (tetther= 0.75)
        this.grense = (int) (tetthet*dimensjon);

        //oppretter tabellen: Det var så mye kluss med dette, ingenting funket unntatt dette:
        //her oppretter vi en ny Object- tabell med størrelse på dimensjonen (antall posisjoner) som nå er 13.
        hash = (Node[]) new Object[dimensjon];;
    }


    /*FORKLARING AV HASHCODE -> INDEKS:
    * 💡 1. Hvorfor nøkkelen ikke = indeksen
    * Når du skriver:
    * map.leggInn(23, "Thilo");
    * så betyr ikke det at verdien havner på hash[23].
    * Grunnen er at nøkler i en HashMap kan være hva som helst — ikke bare tall:
    * Strings, Objekter, Andre klasser du lager selv (f.eks. Identitet)
    *
    * …og Java aner jo ikke hvor i tabellen "Thilo" eller {id=7, navn="Per"} skal ligge.
    * Derfor må vi oversette nøkkelen til et tall — og det er hashverdien som gjør det.
    *
    * Når du kaller nøkkel.hashCode(), så sier du egentlig:
    * “Hei Java, gi meg et tall som representerer dette objektet.”
    */


    /*Hva gjør finnIndeks?
    * - Den tar inn en nøkkel og regner ut posisjonen til nøkkelen i tabellen hash[].
    * - Alle andre metoder trenger indeksen for å fungere.
    * Basically, denne metoden tar nøkkelen til et objekt og henter HASH-VERDIEN til nøkkelen og finner dets riktige posisjon i arrayet.*/
    private int finnIndeks(N nøkkel){
        Objects.requireNonNull(nøkkel,"Null er ikke lov");
        int hash = nøkkel.hashCode();

        //hash KAN være negativ, gjør den positiv:

        //0x7fffffff er bare et tall i heksadesimal.
        //I binær er det:
        //
        //0111 1111 1111 1111 1111 1111 1111 1111
        hash = hash & 0x7fffffff; //BETYR: “Behold alle bitene unntatt den første (fortegnsbiten), sett den til 0.”

        //Tar modulo hashkoden med tabellens lengde, og får da en indeks:
        int indeks = hash%dimensjon;

        return indeks;
    }


    @Override
    public V leggInn(N nøkkel, V verdi) {
        Objects.requireNonNull(nøkkel, "Null er ikke lov");

        //sjekke om tabellen må utvides
        if (antall>= grense) utvid();
        //hente indeksen
        int indeks = finnIndeks(nøkkel);

        //finner første node på indeksen vi fant
        Node p = hash[indeks];

        //går gjennom alle nodene på den posisjonen:
        while (p!=null) {

            //SJEKKER OM DEN FINNES FRA FØR: hvis vi finner en matchende verdi, så sjekker vi om nøkkelen er lik (om det er samme innhold i objektet), og da må vi overskrive
            if (p.nøkkel.equals(nøkkel)){
                V tmp = p.verdi;
                p.verdi = verdi;
                return tmp;
            }
            p = p.neste;
        }
        //Nå er vi på slutten av den lenkede listen på posisjon indeks
        //vi oppretter en ny node med nøkkelen og verdien OG setter den forrerst, altså neste noden er den tidligste forreste noden (fordi det er ingen forrige peker og ellers måtte vi endre pekeren til den siste noden til å peke på den nye, mer jobb?)
        Node ny = new Node(nøkkel, verdi, hash[indeks]);

        // Vi setter den nye noden først i kjeden på denne posisjonen
        // slik at den peker på forrige hode (hash[indeks]) og blir ny start.
        hash[indeks] = ny;
        antall++;
        return null;
    }

    private void utvid() {

        //størrelsen på det nye arrayet
        int nyDimensjon = 2*hash.length+1;

        //oppretter et nytt array
        Node[] nyHash = (Node[]) new Object[nyDimensjon];

        //vi må nå gå gjennom hele lista, OG alle nodene per indeks, så vi trenger en pekernode P
        Node p;
        for (int i = 0; i < hash.length; i++){

            //setter P til å være første node på den posisjonen
            p = hash[i];

            //itererer ned langs alle nodene
            while(p!= null){

                //først finner vi indeksen denne noden skal ha i det nye arrayet. Det gjør vi vet å ta hashcoden, gjøre den posistiv, og så ta modulo NY DIMENSJONE for å gjøre det om til indeksen.

                int hash = p.nøkkel.hashCode(); //Vi trenger p.NØKKEL sin hashcode, ikke p, fordi vi vil finne indeksen hvor den skal ligge. p.hashcode gir oss hashcoden til den spesifikke noden, men vi vil ha nøkkelen.
                hash = hash & 0x7fffffff;       //gjør den positiv

                int indeks = hash % nyDimensjon;//gjør den om til en indeks.
                //grunnen til nyDimensjon: Vi må “flytte” elementene (rehashing) fordi hele matematikken for hvordan nøkler mapper til indekser endres når tabellen vokser.
                // Hvis vi ikke hadde gjort det: Resultatet blir: alle kolliderer fortsatt i de første 13 plassene, mens alle de nye posisjonene blir nesten tomme.

                Node neste = p.neste;// ta vare på neste før vi rører p
                p.neste = nyHash[indeks]; // link p inn foran
                nyHash[indeks] = p;  // p blir nytt hode
                p= neste;           // videre i gammel kjede
            }
        }

        //setter hash til å peke på nyHash
        hash = nyHash;
        //endrer dimensjonen til å bli den nye dimensjonen
        dimensjon = nyDimensjon;

        //definerer en ny grense ved å gange tettheten (0.75) med størrelsen
        grense = (int) (tetthet * nyDimensjon);

    }

    @Override
    public V fjern(N nøkkel) {
        Objects.requireNonNull(nøkkel, "Null er ikke lov");
        int indeks = finnIndeks(nøkkel);

        //peker node som begynner på første node på den posisjonen
        Node p = hash[indeks];

        //"forrige"-node for å fjerne p.
        Node q = null;


        while(p!= null){

            //Vi har funnet noden vi vil slette
            if (p.nøkkel.equals(nøkkel)){
                V tmp = p.verdi; //lagrer verdien

                //hvis det var første node, altså forrige er null, da tar vi at den første noden (hash[indeks]) blir p.neste, altså vi hopper over første node.
                if (q==null) hash[indeks] = p.neste;

                //hvis noden ikke var første node, peker vi forrige node sin neste til p.neste, hopper altså over p.
                else {
                    q.neste = p.neste;
                }
                //reduserer antall og returnerer verdien
                antall--;
                return tmp;
            }
            //går videre
            else {
                q = p;
                p = p.neste;
            }
        }
        //har ikke funnet noden
        return null;
    }

    @Override
    public V hent(N nøkkel) {
        Objects.requireNonNull(nøkkel, "Null er ikke lov");
        int indeks = finnIndeks(nøkkel);
        Node p = hash[indeks];

        //vi må gå gjennom alle nodene på denne indeksen, fordi denne indeksen har flere nøkler, så vi må sikre at vi sender tilbake verdien til den riktige noden.
        while (p!=null){
            if (p.nøkkel.equals(nøkkel))
                return p.verdi;
            p=p.neste;
        }
        return null;
    }
}

class HashSet<V> implements Set<V>{

    // Vi bruker vår egen HashMap i bakgrunnen.
    // Nøkkelen = verdien i settet, Verdien = et "dummy" objekt som bare fungerer som plassholder.
    private HashMap<V, Object> hm;



    // Et konstant objekt som vi bruker som "verdi" for alle nøkler i hashmappen.
    // Selve objektet har ingen betydning — det er bare for å fylle map'en.
    private static final Object DUMMY = new Object();

    // Konstruktør
    public HashSet(HashMap hm) {
        this.hm = hm;
    }

    // Legger inn et element i settet.
    // Hvis verdien finnes fra før, returner verdien (som da "ble overskrevet").
    // Hvis den ikke fantes, legg den inn og returner null.
    @Override
    public V leggInn(V verdi) {
        Objects.requireNonNull(verdi, "Null er ikke lov i HashSet");

        // Bruker HashMap sin leggInn(). Den returnerer gammel verdi (DUMMY) hvis nøkkelen fantes.
        Object gammel = hm.leggInn(verdi, DUMMY);

        // Hvis gammel != null → elementet fantes fra før (overskriving)
        // Returner verdien for å indikere at den ble "overskrevet"
        if (gammel != null) return verdi;

        // Hvis den ikke fantes fra før, returner null
        return null;
    }

    // Fjerner en verdi hvis den finnes, returnerer verdien som ble fjernet.
    // Hvis den ikke fantes, returner null.
    @Override
    public V fjern(V verdi) {
        Objects.requireNonNull(verdi, "Null er ikke lov i HashSet");

        Object resultat = hm.fjern(verdi);
        return (resultat != null) ? verdi : null;
    }

    // Returnerer verdien hvis den finnes i settet, ellers null.
    @Override
    public V inneholder(V verdi) {
        Objects.requireNonNull(verdi, "Null er ikke lov i HashSet");

        Object resultat = hm.hent(verdi);
        if  (resultat == null)
            return null;
        else
            return verdi;
    }
}