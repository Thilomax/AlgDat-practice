package F5_Merge_and_Mergesort.OppgaverLM;

import java.util.Arrays;

/***Oppgave 3: Forskyvning av Tabell til Høyre**

 **Beskrivelse:** Lag en metode `public static void forskyvHoyre(int[] tabell, int d)` som forskyver alle elementene i `tabell` `d` posisjoner til høyre. De `d` første posisjonene i tabellen skal fylles med `0` (eller en annen standardverdi), og elementer som "faller ut" fra høyre kant skal ignoreres**8**.

 **Hint:** For å unngå å overskrive verdier du trenger, er det lurt å starte flyttingen fra slutten av tabellen og jobbe bakover. Fyll deretter de første `d` elementene med `0`**8**.

 **Eksempel:**

 ```
 int[] arr = {1, 2, 3, 4, 5};
 forskyvHoyre(arr, 2); // For eksempel
 // Etter kall, skal 'arr' nå være: [3-5]
 ```*/
public class Oppgave3 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        forskyvHoyre(arr, 2);
    }
    public static void forskyvVenstre(int[] tabell, int d){
        int[] resultat = new int[tabell.length];
        for (int i = 0; i < tabell.length-d; i++){
            resultat[i] = tabell[i+d];
        }
        System.out.println(Arrays.toString(resultat));
    }
    public static void forskyvHoyre(int[] tabell, int d){
        int[] resultat = new int[tabell.length];
        for (int i = tabell.length-1; i>=d ; i--){ //hvorfor i>=d?: fordi man unngår å gå utenfor arrayet
            resultat[i] = tabell[i-d]; /*i er indeksen du fyller i resultat.
                                            i - d er indeksen du henter fra tabell.
                                            Hva skjer om i < d?
                                            Eksempel: d = 2, og vi prøver i = 1:
                                                        resultat[1] = tabell[1 - 2]; // tabell[-1] ❌*/
        }
        System.out.println(Arrays.toString(resultat));
    }
}
