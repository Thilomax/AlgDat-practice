package ForelesningerØving.F4_Partisjonering_Quicksort;

import java.util.Arrays;

public class OppgaverLM {
    public static void main(String[] args) {
//        char[] originalTabell = {'a', 'k', 'e', 'p', 'i', 'm', 'o', 't', 'u', 'b'};
//        char[][] resultat = partisjonerVokalerOgKonsonanter(originalTabell);
//        System.out.println(Arrays.deepToString(resultat));

        int[] tallTabell = {8, 3, 15, 13, 1, 9, 20, 3, 18, 2, 6, 25, 14, 8, 20, 16, 5, 21, 11, 14};
        int skilleverdi = 10;
        int skilletIndeks = partisjonerRundtSkilleverdi(tallTabell, skilleverdi);

        System.out.println("Skillet er ved indeks: " + skilletIndeks);
        System.out.println("Tabellen etter partisjonering: " + Arrays.toString(tallTabell));

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
    public static void bytt(int[] a, int i, int j) {
        // Implementer kode her for å bytte a[i] og a[j].
        // Inspirasjon finnes i kilde [2].
        int tmp= a[i];
        a[i]= a[j];
        a[j]= tmp;
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
    //Oppgave 2
    public static int partisjonerRundtSkilleverdi(int[] tabell, int skilleverdi) {
        int v = 0;
        int h= tabell.length-1;
        while (v<=h){
            while (v<=h && tabell[v]< skilleverdi) v++;
            while (v<=h &&tabell[h] >= skilleverdi) h--;
            //denne if setningen brukes bare for å sjekke at pekernes posisjon gir mening å bytte. Den faktiske sjekken er at vi endelig er ute av while løkkene som BETYR at vi har funnet et element på feil plass på begge pekerne
            if (v<h) {
                bytt(tabell,v,h);
                v++;
                h--;
            }

        }
        return v;
    }

}

