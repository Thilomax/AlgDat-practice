package F4_Partisjonering_Quicksort;

import java.util.Arrays;

public class OppgaverLM {
    public static void main(String[] args) {
        char[] originalTabell = {'a', 'k', 'e', 'p', 'i', 'm', 'o', 't', 'u', 'b'};
        char[][] resultat = partisjonerVokalerOgKonsonanter(originalTabell);
        System.out.println(Arrays.deepToString(resultat));

// Vokaler: ['a', 'e', 'i', 'o', 'u']
// Konsonanter: ['k', 'p', 'm', 't', 'b']
    }
    public static boolean erVokal(char a) {
        char[] vokaler = {'a', 'e', 'i', 'o', 'u', 'y', 'æ', 'ø', 'å'};
        for (int i = 0; i < vokaler.length; i++) {
            if (a == vokaler[i])
                return true;
        }
        return false;
    }

    //Oppgave 1
    /*Oppgave: Skriv en metode partisjonerVokalerOgKonsonanter(char[] tabell) som tar inn en array av karakterer (bokstaver). Metoden skal returnere to nye char[] tabeller: én som inneholder alle vokalene fra den originale tabellen, og én som inneholder alle konsonantene. Rekkefølgen internt i vokal- og konsonanttabellene trenger ikke å være sortert.
Du kan bruke en eksisterende hjelpemetode erVokal(char c) for å sjekke om en karakter er en vokal. Den er definert i kildene.*/
    public static char[][] partisjonerVokalerOgKonsonanter(char[] tabell){
        int antallVokaler =0;
        int antallKonsonanter = 0;

        for (int i = 0; i < tabell.length; i++){
            if (erVokal(tabell[i])){
                antallVokaler++;
            } else antallKonsonanter++;
        }
        char[] vokalArray = new char[antallVokaler];
        char[] konsonantArray = new char[antallKonsonanter];
        int j=0;
        int k =0;
        for (int i = 0; i < tabell.length; i++){

            if (erVokal(tabell[i])){
                vokalArray[j] = tabell[i];
                        j++;
            } else {
                konsonantArray[k] = tabell[i];
                k++;
            }

        }
        return new char[][]{vokalArray,konsonantArray};
    }

}
