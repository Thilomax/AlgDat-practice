package ForelesningerØving.F4_Partisjonering_Quicksort;

import static ForelesningerØving.F4_Partisjonering_Quicksort.OppgaverLM.bytt;

public class Repetisjon1 {
    public static void main(String[] args) {

    }

    public static int parterPåVerdi(int[] tabell, int fra, int til){
        int venstre = fra;
        int høyre = til -1;
        int pivot = tabell[til];

        while (true){
            //rar grunn til hvorfor det er <= og ikke <, men det er ikke viktig
            //men disse while løkkene flyttes frem til den finner en verdi som ikke skal være der, og hvis den gjør det, så enten breaker den eller så bytter vi
            while(venstre <= høyre && tabell[venstre]<pivot)
                venstre++;
            while(venstre<=høyre && tabell[høyre]>pivot)
                høyre--;
            if (venstre>høyre)
                break;
            //her øker vi venstre etter byttet fordi vi vil gå videre etter vi har byttet for å sjekke videre
            bytt(tabell, venstre++,høyre--);
        }
        //Fordi pivot-elementet (tabell[til]) må plasseres på sin riktige posisjon i den sorterte delen. Etter løkka ligger venstre på FØRSTE ELEMENT SOM ER STØRRE ENN PIVOT, så vi bytter pivot inn der
        bytt(tabell, venstre, til);
        return venstre;
    }

    public static void kvikkSorter(int[] tabell, int fra, int til){
        if (til-fra<=0) return;
        int k = parterPåVerdi(tabell, fra, til);
        kvikkSorter(tabell, fra, k-1);
        kvikkSorter(tabell, k+1, til);
    }

    public static void kvikkSorter(int[] tabell){
        kvikkSorter(tabell, 0, tabell.length-1);

    }
}

