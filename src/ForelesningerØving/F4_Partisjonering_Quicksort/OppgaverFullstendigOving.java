package ForelesningerØving.F4_Partisjonering_Quicksort;

import java.util.Arrays;

public class OppgaverFullstendigOving {
    public static void main(String[] args) {
        int[] a1 = {5, 2, 7, 3, 9};    // blandet rekkefølge, pivot midt inni
        int[] a2 = {4, 1, 6, 2, 9};    // pivot i starten
        int[] a3 = {5, 5, 2, 5, 1, 5}; // mange like pivotverdier
        int[] a4 = {-3, 7, -1, 0, 5, -2}; // blandet med negative tall

        System.out.println("a1:");
        int k1 = sParter(a1, 0, a1.length, 2); // pivot = 7
        System.out.println("k = " + k1 + ", " + Arrays.toString(a1));

        System.out.println("a2:");
        int k2 = sParter(a2, 0, a2.length, 0); // pivot = 4
        System.out.println("k = " + k2 + ", " + Arrays.toString(a2));

        System.out.println("a3:");
        int k3 = sParter(a3, 0, a3.length, 0); // pivot = 5
        System.out.println("k = " + k3 + ", " + Arrays.toString(a3));

        System.out.println("a4:");
        int k4 = sParter(a4, 0, a4.length, 3); // pivot = 0
        System.out.println("k = " + k4 + ", " + Arrays.toString(a4));

        int[] x = {5,2,7,3,9};
        kvikksortering0(x, 0, x.length);
// skal bli [2,3,5,7,9]
        System.out.println(Arrays.toString(x));

        int[] y = {5,5,2,5,1,5};
        kvikksortering0(y, 0, y.length);
// sjekk at sortert (duplikater ok)
        System.out.println(Arrays.toString(y));


    }
    public static void bytt(int[] a, int i, int j){
        int tmp;
        tmp = a[j];
        a[j] =  a[i];
        a[i] = tmp;
    }
    public static int parter(int[] a, int fra, int til){
        // PIVOT-VERSJON (Lomuto): velger pivot fra arrayet (a[til]) og jobber i [fra..til]
        int pivot = a[til];
        int i = fra - 1;
        for (int j = fra; j < til; j++){
            if (a[j] < pivot){
                i++;
                bytt(a, i, j);
            }
        }
        bytt(a, til, i + 1);               // slutt-bytte: plasserer pivot på sin endelige indeks
        System.out.println(Arrays.toString(a));
        return i + 1;                      // returnerer pivotens indeks
    }

    // Oppgave 1: generell to-delt partisjonering på halvåpent intervall [fra:til)
// rundt en gitt skilleverdi (ikke et pivot-element fra tabellen)
    public static int parter1(int[] a, int skilleverdi, int fra, int til){
        int i = fra - 1;                   // beholdt: i peker til siste plass i venstre del (< skilleverdi)

        // ENDRET: løkker i et halvåpent intervall [fra:til) ⇒ j < til
        // Hvorfor: oppgaveteksten spesifiserer a[fra:til), så siste gyldige indeks er til-1
        for (int j = fra; j < til; j++){
            // ENDRET: sammenligner mot "skilleverdi" i stedet for pivot fra a[til]
            // Hvorfor: oppgaven sier at vi får skilleverdi som parameter, ikke at vi velger pivot i tabellen
            if (a[j] < skilleverdi){
                i++;
                bytt(a, i, j);             // beholdt: utvider venstre del ved å flytte elementet på plass
            }
        }

        // FJERNET fra pivot-versjonen: "final swap" med pivot på a[til]
        // Hvorfor: vi har ingen pivot i arrayet å plassere. Vi deler bare i < skilleverdi og >= skilleverdi.

        System.out.println(Arrays.toString(a));

        // ENDRET: retur er første indeks i høyre del (partisjonsgrensen) m = i + 1
        // Hvorfor: kontrakten etter oppgaven er at a[fra:m) < skilleverdi og a[m:til) >= skilleverdi
        return i + 1;
    }

    //oppgave 2
    public static int sParter(int[] a, int fra, int til, int indeks){
        int pivot = a[indeks];
        int i = fra - 1;

        // Endring: flytter pivot til slutten av intervallet (til-1).
        // Hvorfor: i et halvåpent intervall [fra:til) er 'til' utenfor rekkevidde.
        // Siste gyldige indeks er til-1, derfor kan vi aldri bruke a[til].
        bytt(a, indeks, til - 1);

        // Endring: looper bare frem til til-1.
        // Hvorfor: til-1 er siste element i intervallet, selve pivoten ligger der nå.
        // Vi må ikke ta med pivoten i sammenligningene.
        for (int j = fra; j < til - 1; j++){
            if (a[j] < pivot){
                i++;
                bytt(a, i, j);
            }
        }

        // Endring: sluttbytte skjer mot til-1, ikke til.
        // Hvorfor: pivot ligger på til-1 etter vi flyttet den dit i starten.
        bytt(a, til - 1, i + 1);

        return i + 1;
    }

    public static int sParter(int[] a){
        // Endring: pivot = siste element (a[a.length-1]).
        // Hvorfor: vi jobber på hele tabellen, og siste indeks er lovlig.
        int pivot = a[a.length - 1];

        // Endring: i starter på -1 når vi bruker hele tabellen (fra = 0).
        // Hvorfor: vi følger Lomuto-mønsteret, og "i" skal peke rett før venstre del.
        int i = -1;

        // Endring: løkker til nest siste element (a.length-1).
        // Hvorfor: pivot er allerede på siste plass, vi skal ikke sammenligne den med seg selv.
        for (int j = 0; j < a.length - 1; j++){
            if (a[j] < pivot){
                i++;
                bytt(a, i, j);
            }
        }

        // Endring: sluttbytte med siste element (a.length-1).
        // Hvorfor: pivot er flyttet dit, og skal nå på sin endelige posisjon i midten.
        bytt(a, a.length - 1, i + 1);

        System.out.println(Arrays.toString(a));
        return i + 1;
    }

    //oppgave 3
/*Quicksort egentlig bare gjør:

Partisjoner én gang → pivot havner på rett plass.

Rekursjon på venstre del.

Rekursjon på høyre del.

Ikke flere partisjoneringer i samme kall.*/
    private static void kvikksortering0(int[] a, int fra, int til){
        // Basis-tilfelle: hvis intervallet har 0 eller 1 element, er det allerede sortert.
        // Halvåpent intervall [fra:til), derfor stopper vi når fra >= til-1.
        if (fra >= til - 1) return;

        // Partisjonér intervallet [fra:til) ved å velge en pivot.
        // Her velger vi midt-indeksen (fra+til)/2 for å få en "midtverdi".
        // sParter plasserer pivot-elementet på sin endelige posisjon,
        // og returnerer indeksen pivot havner på.
        int pivot = sParter(a, fra, til, (fra + til) / 2); //kunne også hatt til-1 istedenfor (fra+til)/2

        // Rekursjon på venstre del [fra:pivot).
        // Alt til venstre for pivot skal sorteres videre.
        kvikksortering0(a, fra, pivot);

        // Rekursjon på høyre del [pivot+1:til).
        // Alt til høyre for pivot skal sorteres videre.
        kvikksortering0(a, pivot + 1, til);
    }
/*
Hvorfor lager vi egentlig flere varianter (kvikksortering0, kvikksortering(a, fra, til), kvikksortering(a))?


Det handler om struktur og gjenbruk:
kvikksortering0 (privat, rekursiv kjerne):
Denne gjør selve jobben: stopper når intervallet er lite nok, finner pivot, kaller seg selv på venstre og høyre del.
Den er privat fordi vi egentlig ikke vil at “brukeren” skal kalle den direkte (den er bare en byggestein).
kvikksortering(int[] a, int fra, int til) (offentlig):
Gir deg mulighet til å sortere bare et delintervall av tabellen.
Praktisk hvis du ikke alltid vil sortere alt, men bare en del (f.eks. [2:7) i en større array).
kvikksortering(int[] a) (offentlig):
Den “brukervennlige” varianten.
Ofte vil man bare si “sorter hele arrayet” uten å måtte huske å sende inn fra=0, til=a.length*/
    public static void kvikksortering(int[] a, int fra, int til){
        kvikksortering0(a, fra, til);
    }

    public static void kvikksortering(int[] a){

        kvikksortering(a, 0, a.length); //ikke a.length-1 fordi metodene våre er halvåpne, så de ignorer det siste elementet. hvis vi hadde tatt a.length-1 så hadde det siste elementet av listen ikke vært med
    }

    //oppgave 4

    /* Lag en forbedret versjon av kvikksortering0() der pivot ikke alltid er midt-elementet, men velges med median-of-three:
        Ta tre kandidater: første element i intervallet, midterste element, og siste element.
        Velg den verdien som ligger i midten av disse tre (altså medianen) som pivot.*/

    private static void kvikksortering2(int[]a, int fra, int til){
        if (fra >= til - 1) return;

        int v = fra;
        int m = (fra+til)/2;
        int h = til-1;

        if ((a[v]<=a[m] && a[m]<=a[h]) || (a[h]<=a[m] && a[m] <= a[v])){
            //median er m
        } else if ((a[m] <= a[v] && a[v] <= a[h]) || (a[h] <= a[v] && a[v] <= a[m])){
            //median er v
            m = v;
        } else {
            //median er h
            m = h;
        }
        int k = sParter(a, fra, til, m);
        kvikksortering2(a, fra, k);
        kvikksortering2(a, k+1, til);
    }

    //Opppgave 5
    /*
    * Lag en metode kvikksøk(int[] a, int m) som finner den m-te minste verdien i en usortert tabell ved hjelp av partisjonering (samme prinsipp som i Quicksort).

        Du skal altså ikke sortere hele tabellen, bare bruke partisjonering til å snevre inn søket til riktig del av tabellen til du finner verdien på plass nr. m.
        Lag en metode median(int[] a) som bruker kvikksøk() til å finne medianen:
        Hvis tabellen har odde lengde → medianen er verdien i midten.
        Hvis tabellen har partall lengde → medianen er gjennomsnittet av de to midterste verdiene.
        Test begge metodene godt, både på tabeller med odde og partall lengde. Husk at tabellen blir endret når du kjører disse metodene (siden de bruker partisjonering).*/

    public static int kvikksøk(int[] a, int m){
        // Input-sjekk: m må være en gyldig 0-basert rang (0..a.length-1)
        if (0 <= m && m < a.length) {
            int pivot;
            int fra = 0;
            int til = a.length;
            int k;
            while (true) {
                // Viktig: velg pivot-INDeks i det AKTUELLE intervallet [fra:til)
                // (før hadde jeg (a.length-1)/2 → ga samme pivot hver runde og ignorerte innsnevringen)
                pivot = (fra + til) / 2;

                // sParter partisjonerer [fra:til) rundt pivot-indeksen og
                // returnerer k = pivotens ENDELIGE indeks
                k = sParter(a, fra, til, pivot);

                // Quickselect-kjerne: sammenlign ønsket rang m med k (posisjon, ikke verdi)
                if (m == k) {
                    // Funnet: m-te minste ligger nå på indeks k
                    return a[k];
                } else if (m < k) {
                    // Søk venstre del: halvåpent intervall → [fra:k)
                    til = k;
                } else {
                    // Søk høyre del: halvåpent intervall → [k+1:til)
                    fra = k + 1;
                }
            }
        } else {
            throw new IllegalArgumentException("Ugyldig input");
        }
    }


}
