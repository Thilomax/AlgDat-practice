package SorteringF3.OppgaverLM;

import java.util.Arrays;

public class Oppgaver {
    public static void main(String[] args) {
        int[] a = {2,6,8,4,2,6,3};
        bobleEnGjennomgang(a, 7);
    }
    //felles hjelpemetode
    public static void bytt(int[] a, int i, int j) {
        // Implementer kode her for å bytte a[i] og a[j].
        // Inspirasjon finnes i kilde [2].
        int tmp= a[i];
        a[i]= a[j];
        a[j]= tmp;
    }
/***Kjerneidé:** Gå gjennom tabellen gjentatte ganger, sammenlign naboverdier og bytt dem hvis de er i feil rekkefølge. Større verdier "bobler opp" til slutten av tabellen**3...**.

 **Superenkel kodeoppgave:** Lag en metode som utfører én enkelt gjennomgang (én "pass") av boblesortering.
 * Etter denne gjennomgangen skal det største elementet i den undersøkte delen av tabellen ha "boblet opp"
 * til sin korrekte posisjon bakerst i denne delen**4**.*/


    public static void bobleEnGjennomgang(int[] a, int n) {
        // Sammenlign naboverdier fra indeks 0 opp til n-2.
        // Hvis a[i] er større enn a[i+1], bytt plass på dem.
        // Denne metoden skal flytte det største elementet i a[0...n-1] til a[n-1].
        // Du kan ta inspirasjon fra den indre løkken i Programkode 1.3.3 e) [6]
        // (men tilpass for å kun utføre én enkelt gjennomgang i et spesifikt intervall 'n').

        for (int i = 0; i < n-1; i++){
            if (a[i]>a[i+1]) bytt(a,i,i+1); // må sende indeksene, ikke verdiene
        }
        System.out.println(Arrays.toString(a));
    }

    /***2. Utvalgssortering (Selection Sort)**

     **Kjerneidé:** Finn den minste verdien i den usorterte delen av tabellen og bytt den med den første verdien i den usorterte delen**6...**.

     **Superenkel kodeoppgave:** Lag en metode som finner posisjonen (indeksen) til den minste verdien innenfor et spesifikt intervall av en heltallstabell.*/

    public static int finnMinstePosisjon(int[] a, int fra, int til) {
        // Finn og returner indeksen til det minste elementet i intervallet a[fra:til-1].
        // Anta at 'fra' er inkludert og 'til' er ekskludert (a[fra:til>).
        // Du kan bruke logikken fra metoden 'min' i kilde [10].
    }


}
