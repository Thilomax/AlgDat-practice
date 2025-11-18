package ForelesningerØving.F5_Merge_and_Mergesort;

import java.util.Arrays;

public class Dato_03_09_fletting {

    public static void main(String[] args) {
        // Vi lager to ferdig sorterte tabeller (a og b)
        int[] a = {2, 4, 5, 10, 23, 49};
        int[] b = {1, 7, 13, 14, 15, 35, 53, 56};

        // Vi fletter tabellene ved å kalle på sortertFlett()
        int[] resultat = sortertFlett(a, b);

        // Skriver ut den ferdig flettede tabellen
        System.out.println(Arrays.toString(resultat));

        int[] usortertTabell = {2,4,1,7,13,3,5,10};
        int[] sortertTabell = fletteSorter(usortertTabell);
        System.out.println(Arrays.toString(sortertTabell)+"sortert");
        System.out.println(Arrays.toString(usortertTabell)+"usortert");
    }

    // Metoden tar to sorterte tabeller (a og b) og returnerer en ny tabell som er sortert
    public static int[] sortertFlett(int[] a, int[] b) {
        // Total lengde blir summen av lengdene til a og b
        int n = a.length + b.length;
        int[] resultat = new int[n];

        // i = peker (indeks) i tabell a
        // j = peker (indeks) i tabell b
        // k = peker (indeks) i resultat-tabellen
        int i = 0, j = 0, k = 0;

        // Så lenge vi har elementer igjen i begge tabellene
        while (i < a.length && j < b.length) {
            // Sjekk hvilket av de "neste" elementene som er minst
            if (a[i] < b[j])
                // Hvis elementet i a er mindre, legges det til i resultat,
                // og vi flytter pekeren i a ett hakk videre
                resultat[k++] = a[i++];
            else
                // Ellers er elementet i b mindre eller likt,
                // så vi legger det inn og flytter pekeren i b
                resultat[k++] = b[j++];
        }

        // Når vi kommer ut av while-løkken, betyr det at
        // enten a eller b er "tømt" (vi har lagt til alle elementene derfra).
        // Da må vi bare legge til resten fra den andre tabellen.

        // Hvis det fortsatt finnes elementer i a, legges de på slutten
        while (i < a.length) resultat[k++] = a[i++];

        // Hvis det fortsatt finnes elementer i b, legges de på slutten
        while (j < b.length) resultat[k++] = b[j++];

        // Nå er resultat-tabellen ferdig fylt og sortert
        return resultat;
    }
    public static int[] fletteSorter(int[] tabell) {
        // Basis-tilfellet: Hvis tabellen har 0 eller 1 element,
        // er den allerede sortert. Vi returnerer en kopi.
        if (tabell.length <= 1)
            return tabell.clone();

        // Finn midtpunktet for å dele tabellen i to halvdeler
        int halve = tabell.length / 2;

        // Opprett nye tabeller for de to halvdelene
        int[] førsteHalvdel = new int[halve];
        int[] andreHalvdel = new int[tabell.length - halve];

        // Kopier elementene til første halvdel
        int k = 0;
        for (int i = 0; i < halve; i++) {
            førsteHalvdel[i] = tabell[k++];
        }

        // Kopier elementene til andre halvdel
        // NB: dette er det samme som å bruke k videre, men her brukes i+halve
        for (int i = 0; i < tabell.length - halve; i++) {
            andreHalvdel[i] = tabell[k++];
        }

        // Rekursiv kallesortering:
        // Sorterer hver halvdel hver for seg (rekursjon deler igjen i to osv.)
        int[] førsteHalvdelSortert = fletteSorter(førsteHalvdel);
        int[] andreHalvdelSortert = fletteSorter(andreHalvdel);

    /*
     Forklaring av kopieringsmåten ovenfor:

     for (int i = 0; i < tabell.length - halve; i++) {
         andreHalvdel[i] = tabell[k++];
     }

     er nøyaktig det samme som å gjøre:

     for (int i = 0; i < tabell.length - halve; i++) {
         andreHalvdel[i] = tabell[k];
         k++;
     }
     */

        // Til slutt flettes de to sorterte halvdelene sammen til en hel sortert tabell
        return sortertFlett(førsteHalvdelSortert, andreHalvdelSortert);
    }

}
