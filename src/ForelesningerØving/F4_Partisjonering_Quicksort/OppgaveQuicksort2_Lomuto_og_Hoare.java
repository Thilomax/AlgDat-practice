package ForelesningerØving.F4_Partisjonering_Quicksort;

import java.util.Arrays;

public class OppgaveQuicksort2_Lomuto_og_Hoare {
    public static void bytt(int[] a, int i, int j) {
        // Implementer kode her for å bytte a[i] og a[j].
        // Inspirasjon finnes i kilde [2].
        int tmp= a[i];
        a[i]= a[j];
        a[j]= tmp;
    }
    public static int hoarePartition(int[] a, int v, int h){
        int pivot = a[(v+h)/2];
        int i = v;
        int j = h;
        while(true){
            while (a[i] < pivot) i++; // finn første som er >= pivot
            while (a[j]> pivot)j--; // finn første som er <= pivot
            if (i>= j) return j;      // pekerne har krysset hverandre
            bytt(a,i,j);
            i++;
            j--;
        }
    }

    /*  i begynner på v - 1 (altså rett før første element).
        Tenk på i som "slutten av < pivot-sonen".
        I starten er det ingen < pivot elementer, så den sonen er tom.
        j begynner på v og går gjennom alle elementer til høyre for seg selv.
        Hver gang j finner et tall som er < pivot, så må vi flytte det inn i < pivot-sonen.
        Derfor øker vi i med én (sonen blir større), og bytter a[i] og a[j].*/

    public static int partition(int[] a, int v, int h){
        int pivotVerdi = a[h];
        int i = v-1; //i markerer "området" som er mindre enn pivot. Siden vi ikke har funnet noe enda, er dette området nå 0
        for (int j = v; j<h; j++){
            if (a[j]<pivotVerdi){ //j er en peker som sjekker om dette tallet nå er mindre enn pivotVerdien. Hvis den er det, blir det flyttet inn i området til i, altså < pivot området.
                i++; // derfor økes i med 1.
                bytt(a,i,j); //dette putter elementet vi fant med j inn i området i, som nå har økt med en ny plass.
            }
        }
        bytt(a, i+1, h); //dette er der pivotverdien opprinnelig lå, så vi putter den der v og j krysser. Putter pivoten på riktig plass.
        return i+1;
    }
    public static void quicksort (int[] a, int v, int h){
        if (v>=h) return; //dette er basistilfellet. Her skal rekursive metoden stoppe. Vi er da ferdig. Fordi: da har den funnet krysspunktet på høyre og venstre, og vi vil ikke at de skal krysse hverandre.
        int parisjoneringsIndeks = hoarePartition(a, v, h);
        quicksort(a, v, parisjoneringsIndeks-1); //dette sorterer venstre del, frem til krysspunktet.
        quicksort(a,parisjoneringsIndeks+1, h); //dette sorterer høyre del, fra krysspunktet. Dette fortsetter frem til v og h krysser hverandre.
    }
    //Metode for å enkelt putte inn arrayet sitt i en metode, så man slipper å putte inn v og h manuelt
    public static void quicksort (int[] a){
        if (a==null || a.length <2) return; //sjekk
        quicksort(a, 0, a.length-1);
    }
    public static void main(String[] args) {
        int[] minTabell = {5, 2, 8, 1, 9, 4, 7, 3, 6};
        System.out.println("Før:  " + Arrays.toString(minTabell));
        quicksort(minTabell);
        System.out.println("Etter:" + Arrays.toString(minTabell));

        int[] tom = {};
        quicksort(tom);
        System.out.println("Tom:  " + Arrays.toString(tom));

        int[] en = {42};
        quicksort(en);
        System.out.println("En:   " + Arrays.toString(en));

        int[] sortert = {1, 2, 3, 4, 5};
        quicksort(sortert);
        System.out.println("Sort: " + Arrays.toString(sortert));

        int[] revers = {5, 4, 3, 2, 1};
        System.out.println("Før:  " + Arrays.toString(revers));
        quicksort(revers);
        System.out.println("Rev:  " + Arrays.toString(revers));
    }
}


