package Boka.Kap132_134_136_lecture_3;

public class Tabell {
    public static void main(String[] args) {
        int [] a = {3,8,10,12,14,16,21,24,27,30,32,33,34,37,40};
        int verdi = 16;
        System.out.println(lineærsøk(a, verdi));
    }

    //1.3.5 oppgave 2

//    public static int lineærsøk(int[] a, int verdi) // legges i class Tabell
//    {
//        if (a.length == 0 || verdi > a[a.length-1])
//            return -(a.length + 1);  // verdi er større enn den største
//
//        int i = 0; for( ; a[i] < verdi; i++);  // siste verdi er vaktpost
//
//        return verdi == a[i] ? i : -(i + 1);   // sjekker innholdet i a[i]
//    }

    //1.3.5 oppgave 4
    public static int lineærsøk(int[] a, int fra, int til, int verdi){
        if (fra < 0 || til > a.length || fra > til) {
            throw new IllegalArgumentException("ulovlig interval");
        }
        if (fra == til){
            return -(fra + 1);
        }
        if (verdi > a[til-1]){
            return -(til+1);
        }
        int i = fra;
        for (;i<til&& a[i] < verdi; i++);
        return (verdi == a[i] ? i : -(i+1));
    }


//    public static void utvalgssortering(int[] a)
//    {
//        for (int i = 0; i < a.length - 1; i++)
//            bytt(a, i, min(a, i, a.length));  // to hjelpemetoder
//    }
//
//    public static void bytt(int[] tabell, int i, int j) {
//        int tmp = tabell[i]; // Lagrer verdien på indeks i midlertidig
//        tabell[i] = tabell[j]; // Setter verdien på indeks i til verdien på indeks j
//        tabell[j] = tmp; // Setter verdien på indeks j til den midlertidig lagrede verdien
//    } // [1]
//    public static int min(int[] tabell, int fra, int til) {
//        // Kontrollerer at intervallet er gyldig. Dette er en god praksis,
//        // og kommentaren i kilden antyder behovet for en slik sjekk [2].
//        if (fra < 0 || til > tabell.length || fra >= til) {
//            throw new IllegalArgumentException("Ulovlig intervall [" + fra + ":" + til + ">");
//        }
//
//        int laveste_posisjon = fra; // Antar at det første elementet er det minste
//        for (int i = fra + 1; i < til; i++) { // Går gjennom resten av intervallet
//            if (tabell[i] < tabell[laveste_posisjon]) { // Hvis et mindre element blir funnet
//                laveste_posisjon = i; // Oppdater posisjonen til det minste elementet
//            }
//        }
//        return laveste_posisjon; // Returnerer posisjonen til det minste elementet
//    } // [1, 2]
//
//    /**
//     * Snu-metoden snur rekkefølgen på elementene i en tabell.
//     * Beskrivelsen i kilden sier at man skal "rotere den om midten slik at første verdi kommer sist og siste verdi først, osv." [4].
//     * Dette er en standard implementasjon for å snu en tabell.
//     *
//     * @param a Tabellen som skal snus.
//     */
//    public static void snu(int[] a) {
//        // Går fra begge ender av tabellen og bytter elementer til midten er nådd.
//        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
//            bytt(a, i, j); // Bruker den eksisterende bytt-metoden
//        }
//    } // Basert på beskrivelse [4]
//
//    /**
//     * Skriver ut elementene i en tabell til konsollen, atskilt med mellomrom.
//     * Avslutter med et linjeskift.
//     * Denne metoden er laget for å matche utskriftsformatet i Programkode 1.3.4 b) [4].
//     *
//     * @param a Tabellen som skal skrives ut.
//     */
//    public static void skriv(int[] a) {
//        if (a.length == 0) {
//            System.out.println(); // Skriver ut en tom linje for en tom tabell
//            return;
//        }
//        for (int i = 0; i < a.length; i++) {
//            System.out.print(a[i] + (i == a.length - 1 ? "" : " ")); // Skriver ut elementet, med mellomrom etter alle unntatt det siste
//        }
//        System.out.println(); // Avslutter med et linjeskift
//    } // Basert på eksempel [4]
}
