package ForelesningerØving.F3_BubbleSort_OrdnetInnsetting.OppgaverFullstendig;

public class OppgaverFullstendig {
    public static void main(String[] args) {
            int[] t1 = {1, 2, 3, 4};
            int[] t2 = {3, 2, 1};
            int[] t3 = {3, 1, 2};

            System.out.println(inversjoner(t1)); // forventer 0
            System.out.println(inversjoner(t2)); // forventer 3
            System.out.println(inversjoner(t3)); // forventer 2

            System.out.println(erSortert(t1)); // forventer true
            System.out.println(erSortert(t2)); // forventer false

    }
    public static int inversjoner(int[] a){
        int inversjoner = 0;
        for (int i= 0; i < a.length -1; i++){
            for (int j = i+1; j<a.length; j++){
                if (a[i]> a[j]) inversjoner++;
            }
        }
        return inversjoner;
    }
    public static boolean erSortert(int[]a){
        for (int i = 0; i< a.length-1; i++){
            if (a[i]>a[i+1]) return false;
        }
        return true;
    }

    public static void bytt(int[] a, int i, int j){
        int tmp;
        tmp = a[j];
        a[j] =  a[i];
        a[i] = tmp;
    }

    //Oppgave 2 BOBLESORTERING

    /*Indre løkke passer på naboene.

Ytre løkke gjentar prosessen nok ganger til alle har “boblet” på plass.

-i på slutten gjør at du slipper å sjekke det som allerede er sortert bakerst.*/
    public static void boblesortering(int[] a){
        for (int i= 0; i < a.length-1; i++){
            for (int j = 0; j<a.length-1-i; j++) {
                if (a[j] > a[j+1]) bytt(a, j, j+1);
            }
        }
    }

    // Oppgave 3 SELECTION SORT
    // Utvalgssortering (Selection Sort)
    public static void utvalgssortering(int[] a) {
        // Gå gjennom alle posisjoner unntatt den siste
        // (når bare ett element står igjen, er det automatisk riktig plassert)
        for (int i = 0; i < a.length - 1; i++) {

            // Finn indeksen til minste element i intervallet [i : a.length)
            int j = min(a, i, a.length);

            // Hvis minste element ikke allerede står på plass i, bytt dem
            if (j != i) bytt(a, i, j);
        }
    }

    // Hjelpemetode som finner indeksen til minste element i et gitt intervall [fra : til)
    public static int min(int[] a, int fra, int til) {
        int min = fra;  // Start med å anta at elementet på 'fra' er minst

        // Gå gjennom resten av intervallet
        for (int i = fra + 1; i < til; i++) {
            // Hvis vi finner et element som er mindre, oppdater min-indeksen
            if (a[i] < a[min]) min = i;
        }

        // Returner indeksen til det minste elementet
        return min;
    }


}
