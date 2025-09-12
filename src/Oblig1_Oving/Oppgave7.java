package Oblig1_Oving;

public class Oppgave7 {
    public static String flett(String... s) { //String... s betyr “valgfritt antall strenger”. Inne i metoden oppfører s seg som et String[]
        StringBuilder sb = new StringBuilder(); // Vi bygger resultatet trinnvis i en StringBuilder, som er laget for å sette sammen tekst effektivt.
        int maks = 0 ;
        for (String string : s){
            if (string.length()> maks) maks = string.length(); /*Vi finner lengden på den lengste strengen.
                                                                 Hvorfor? Fordi vi skal hente “første tegn fra alle, så andre tegn fra alle, …” og vi må vite hvor mange runder vi trenger. Antall runder = lengden til den lengste strengen.*/
        }
        /*
        * Tradisjonell for loop:
        * int maks = 0;
           for (int i = 0; i < s.length; i++) {
                if (s[i].length() > maks) {
                maks = s[i].length();
            }
        }*/

        for (int j = 0; j < maks; j++){ // Ytterløkken går over tegnposisjon, altså enkeltbokstavene, j i strengene, altså “runde 1” (j=0), “runde 2” (j=1), osv., helt til den lengste er brukt opp.
            for (int i = 0; i < s.length; i++){ // Innerløkken går over hvilken streng vi er på, s[0], s[1], s[2], ....
                if (j < s[i].length()) {  /*    Ikke alle strenger er like lange. Sjekken j < s[i].length() gjør at vi hopper over strenger som ikke har flere tegn i denne runden.
                                                Hvis strengen har et tegn på posisjon j, så henter vi det med charAt(j) og legger det til resultatet.
                                                Basically: bokstavindeksen i denne strengen må være mindre enn antall totale bokstaver i strengen */
                    sb.append(s[i].charAt(j));
                }
            }
        }
        return sb.toString();
    }

}
