package ForelesningerØving.F5_Merge_and_Mergesort.OppgaverLM;

import java.util.Arrays;

/*Oppgave 1: Enkel Fletting av Ulike Tabeller
Beskrivelse: Lag en metode public static int[] enkelFlett(int[] a, int[] b) som fletter sammen to heltallstabeller a og b ved å ta annenhver verdi fra hver tabell. Hvis den ene tabellen er lengre enn den andre, skal de gjenværende elementene fra den lengste tabellen legges til sist i resultatet.
Hint: Bruk while-løkker og indekser for å holde styr på posisjonen i hver tabell og i resultat-tabellen. Tenk på hvordan du håndterer de resterende elementene etter at en av tabellene er tom.
Eksempel:
int[] a = {1, 3, 5};
int[] b = {2, 4, 6, 8};
int[] resultat = enkelFlett(a, b);
// Forventet utskrift: [1, 3-8]*/
public class Oppgave1 {
    public static void main(String[] args) {
        int[] a = {1, 3, 5};
        int[] b = {2, 4, 6, 8};
        int[] resultat = enkelFlett(a, b);
        System.out.println(Arrays.toString(resultat));
    }
    public static int[] enkelFlett(int[] a, int[] b){
        int i = 0;
        int j = 0;
        int k = 0;
        int n = a.length + b.length;
        int[] resultat = new int[n];
        while (i < a.length && j < b.length){
                resultat[k]=a[i];
                k++;
                i++;

                resultat[k]=b[j];
                k++;
                j++;

        }
        while (i < a.length){
            resultat[k] = a[i];
            k++;
            i++;
        }

        // Hvis det fortsatt finnes elementer i b, legges de på slutten
        while (j < b.length) {
            resultat[k] = b[j];
            k++;
            j++;
        }
        return resultat;
    }
}
