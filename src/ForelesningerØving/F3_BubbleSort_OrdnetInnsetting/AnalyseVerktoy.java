package ForelesningerØving.F3_BubbleSort_OrdnetInnsetting;

public class AnalyseVerktoy {
    public static void main(String[] args) {
        int[] a = {1,3,54,4,6,3,7,9,2};
        System.out.println(antallInversjoner(a));
    }


    //Oppgave 1.a
    public static int antallInversjoner(int[] a){
        int inversjoner = 0;
        for (int i = 0; i < a.length; i++){
            for (int j = i+1; j < a.length; j++){
                if (a[i]>a[j])inversjoner++;
            }
        }
        return inversjoner;
    }

    //Oppgave 1.b
    public static boolean erSortert(int[] a){
        for (int i = 1; i < a.length-1; i++){
            if (a[i]> a[i+1]) return false;
        }
        return true;
    }

    //Oppgave 2
    public static int binærsøk(int[] a, int verdi){
        int v = 0;
        int h = a.length-1;
        int m;
        while (v<=h){
            //Når vi finner ut at verdien er på høyre side av lista, så OPPDATERER  VI m TIL Å VÆRE midten av den høyre delen av lista.
            m = (v+h)/2;
            if (verdi == a[m]) return m;
            if (verdi > a[m]) v = m+1;
            if (verdi < a[m]) h = m-1;
        }
        return v;
    }

    //Oppgave 3
    public static void utvalgssortering(int[] a){
        for (int i = 0; i < a.length-1; i++){
            int j = min(a, i, a.length);
            if (a[i]!= a[j]) bytt(a,i,j);
        }
    }

    public static void bytt(int[] a, int i, int j){
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static int min(int[] a, int fra, int til){
        int min = fra;
        for (int i = fra+1; i < til; i++){
            if (a[min] > a[i]) min= i;
        }
        return min;
    }
}


