package ForelesningerØving.F3_BubbleSort_OrdnetInnsetting;

public class Repetisjon1 {
    public static void main(String[] args) {
        int[] tiTabell = {1, 2, 3, 4, 5, 6, 8, 7, 6, 10};
        System.out.println(antallInversjoner(tiTabell));
    }

    public static int antallInversjoner(int[] tabell){
        int antall=0;
        for (int i = 0; i < tabell.length; i++){
            for (int j = i+1; j < tabell.length-1; j++){
                if (tabell[i]>tabell[j])
                    antall++;
            }
        }
        return antall;
    }
}
