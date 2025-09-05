package Merge_and_Mergesort.OppgaverLM;

import java.util.Arrays;

/***Oppgave 2: Sortert Fletting av To Sorterte Tabeller**

 **Beskrivelse:** Lag en metode `public static int[] sortertFlett(int[] a, int[] b)` som tar inn to *allerede sorterte* heltallstabeller `a` og `b`, og returnerer en ny,
 * sortert tabell som inneholder alle elementene fra `a` og `b`**3...**. Metoden skal sikre at det nye resultatet er sortert, ikke bare flette annenhver verdi**5**.

 **Hint:** Du trenger tre indekser: én for `a`, én for `b`, og én for resultat-tabellen. Sammenlign elementene `a[i]` og `b[j]` og legg det minste inn i resultat-tabellen,
 *  før du øker den tilhørende indeksen**3...**. Når en av tabellene er tom, kopierer du resten av elementene fra den andre tabellen**37**.

 **Eksempel:**

 ```
 int[] a = {2, 4, 5, 10};
 int[] b = {1, 7, 13, 14};
 int[] resultat = sortertFlett(a, b);
 // Forventet utskrift: [1, 3, 4, 6, 9, 13-15]

 ```*/
public class Oppgave2 {
    public static void main(String[] args) {
        int[] a = {2, 4, 5, 10};
        int[] b = {1, 7, 13, 14};
        int[] resultat = sortertFlett(a, b);
        System.out.println(Arrays.toString(resultat));
    }
    public static int[] sortertFlett(int[] a, int[] b){
        int i = 0;
        int j = 0;
        int k = 0;
        int[] resultat = new int[a.length + b.length];

        while (i < a.length && j < b.length){
            if (a[i]< b[j]){
                resultat[k] = a[i];
                k++;
                i++;
            } else {
                resultat[k]= b[j];
                k++;
                j++;
            }
        }
        while (i<a.length){
            resultat[k]=a[i];
            k++;
            i++;
        }
        while (j<b.length){
            resultat[k]=b[j];
            k++;
            j++;
        }
        return resultat;
    }
}
