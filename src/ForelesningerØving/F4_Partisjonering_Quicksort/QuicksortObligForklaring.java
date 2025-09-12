package ForelesningerØving.F4_Partisjonering_Quicksort;

public class QuicksortObligForklaring {
}
// Hovedklassen som inneholder alle metodene for obligatorisk oppgave 1.
class Oblig1 {

    public static void bytt(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    // Oppgave 4: delsortering
    // Denne metoden deler et heltallsarray i to deler:
    // alle oddetallene til venstre, og alle partallene til høyre.
    // Deretter sorteres hver av disse delene for seg.
    public static void delsortering(int[] a) {

        // Sjekker om arrayet er tomt. Hvis det er, er det ingenting å gjøre.
        if (a.length == 0 ) return;

        // Disse pekerne (v og h) brukes til å flytte oddetall og partall til riktig side.
        // v starter på venstre side (indeks 0).
        // h starter på høyre side (siste indeks).
        int v = 0;
        int h = a.length-1;

        // Denne store løkken kjører så lenge 'v' og 'h' ikke har krysset hverandre.
        // Hver iterasjon jobber med å flytte ett oddetall og ett partall til riktig side.
        while (v < h) {

            // Første indre løkke: 'v' flytter seg mot høyre så lenge den finner oddetall.
            // Oddetallene skal jo være til venstre, så vi lar pekeren passere dem.
            while (v <= h && a[v] % 2 != 0) v++;

            // Andre indre løkke: 'h' flytter seg mot venstre så lenge den finner partall.
            // Partallene skal være til høyre, så vi lar pekeren passere dem.
            while (v <= h && a[h] % 2 == 0) h--;

            // Nå har 'v' funnet et partall og 'h' har funnet et oddetall.
            // Hvis 'v' og 'h' ikke har krysset hverandre, betyr det at tallene er "feilplassert".
            // Vi bytter plass på dem for å få dem på riktig side.
            if (v < h) {
                bytt(a, v, h);
                // Flytter pekerne for å unngå en uendelig løkke og forberede neste iterasjon.
                v++;
                h--;
            }
        }

        // Nå er alle oddetallene til venstre og alle partallene til høyre.
        // Neste steg er å finne grensen mellom disse to delene.
        // 'k' er indeksen til det første partallet i arrayet.
        int k = 0;
        while (k < a.length && a[k] % 2 != 0) {
            k++;
        }

        // Nå kaller vi Quicksort for å sortere de to delene hver for seg.
        // Første kall: Sorterer oddetallene (fra indeks 0 til k-1).
        quicksort(a, 0, k - 1);

        // Andre kall: Sorterer partallene (fra indeks k til slutten av arrayet).
        quicksort(a, k, a.length - 1);

    }

    //---------------------------------------------------------------------------------

    // Quicksort del 1: Hjelpemetoden 'partition'
    // Denne metoden er kjernen i Quicksort. Den plasserer et pivot-element
    // på sin endelige sorterte posisjon og flytter alle mindre elementer til venstre
    // og alle større elementer til høyre for det.
    // Metoden returnerer indeksen til pivotet etter at det er plassert.
    public static int partition(int[] a, int v, int h) {

        // Velger det siste elementet i underarrayet som pivot.
        int pivotVerdi = a[h];

        // 'i' er en peker som holder styr på den siste posisjonen til et element
        // som er mindre enn pivotet. Den starter utenfor arrayet.
        int i = v - 1;

        // Denne løkken går gjennom alle elementene i underarrayet (unntatt pivotet).
        for (int j = v; j < h; j++) {

            // Hvis elementet på indeks 'j' er mindre enn pivotet:
            if (a[j] < pivotVerdi) {
                // Flytt 'i' ett steg fremover...
                i++;
                // ... og bytt elementene.
                // Dette flytter elementet mindre enn pivotet til venstre side.
                bytt(a, i, j);
            }
        }

        // Nå er løkken ferdig. Alle elementer mindre enn pivotet er til venstre for 'i'.
        // Vi bytter pivot-elementet (som var på 'h') med elementet på 'i+1'.
        // Nå er pivotet på sin riktige, sorterte posisjon.
        bytt(a, i + 1, h);

        // Vi returnerer indeksen til pivotet, slik at Quicksort kan fortsette sorteringen.
        return i + 1;
    }

    //---------------------------------------------------------------------------------

    // Quicksort del 2: Den rekursive metoden 'quicksort'
    // Denne metoden kaller 'partition' for å dele arrayet, og kaller deretter seg selv
    // på de to nye underdelene.
    public static void quicksort(int[] a, int v, int h) {

        // Dette er "base case" for rekursjonen. Hvis startindeksen (v)
        // er lik eller større enn sluttindeksen (h), er underarrayet sortert (0 eller 1 elementer).
        // Da stopper rekursjonen for denne grenen.
        if (v < h) {

            // Første steg i rekursjonen: Del arrayet i to og få indeksen til pivotet.
            int partisjonsIndeks = partition(a, v, h);

            // Andre steg: Sorter den venstre underlisten rekursivt.
            // Denne delen inneholder alle elementer som var mindre enn pivotet.
            quicksort(a, v, partisjonsIndeks - 1);

            // Tredje steg: Sorter den høyre underlisten rekursivt.
            // Denne delen inneholder alle elementer som var større enn pivotet.
            quicksort(a, partisjonsIndeks + 1, h);
        }
    }
}