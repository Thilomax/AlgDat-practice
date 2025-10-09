package ForelesningerØving.F15_Minimumshaug;

import ForelesningerØving.F8_Lister.Beholder;
import ForelesningerØving.F8_Lister.TabellListe;

import java.util.Comparator;
import java.util.Objects;

class Minimumshaug<T> implements Beholder<T>{

    private TabellListe<T> tl;
    private Comparator<? super T> cmp;

    //I konstruktøren, initialiser TabellListeen. Viktig: Bruk TabellListe.leggInn(0, null) (eller tilsvarende)
    // for å eksplisitt legge inn et null-element på plass 0, slik at roten starter på indeks 1. FORDI: Vi vil ha muligheten til å legge inn et element foran rota.

    public Minimumshaug(Comparator<? super T> cmp){
        this.cmp = cmp;
        this.tl = new TabellListe<>();
        tl.leggInn(0,null);
    }
    @Override
    public boolean leggInn(T t) {
        Objects.requireNonNull(t, "null er ikke lov");
        tl.leggInn(t); //Dette sikrer at den havner på første ledige plass. Fordi ved et komplett tre så må et nytt element alltid ligge på første ledige "indeks"

        int k = antall(); // setter startposisjonen k til den nye nodens indeks. (fordi antall() returnerer den siste indeksen)
        int forelderIndeks = k/2; //vil alltid være indeksen til forelder til noden vi legger til.
        T forelderVerdi = tl.hent(forelderIndeks); //henter verdien til forelder for å sammenligne med verdien vi legger inn

        if (forelderIndeks == 0)  //hvis den nye noden er helt øverst (altså rota), så er indeksen til dets forelder 0. Da trenger vi ikke boble opp.
            return true;

        while (k>1 && cmp.compare(t, forelderVerdi)<0){
            tl.oppdater(k, forelderVerdi); //Flytter forelder ned til noden som ble lagt inn
            tl.oppdater(forelderIndeks,t); //flytter t til å være på forelderens posisjon.
            //dette foregår frem til barnet endelig er større enn forelder.

            k= forelderIndeks; //nå oppdateres K til å være forelder, altså k blir igjen det nedeste elementet?
            // som gjør at vi fortsetter dette frem til barn alltid er større enn forelder

            forelderIndeks = k/2; //foreldervariablen settes til indeksen til forelder til den nederste, siste noden nå.

            forelderVerdi = tl.hent(forelderIndeks); //oppdaterer sammenligningsvariabelen for å fortsette loopen.
        }
        return true;
    }

    public T taUt(){
        if (tom()) throw new IllegalArgumentException("Heapen er tom");
        // 1) Ta vare på min (roten) og siste
        T min = tl.hent(1);
        int n = antall();            // siste reelle indeks
        T last = tl.hent(n);

        // 2) Fjern siste posisjon fra strukturen
        tl.reduserAntall();          // nå er n-1 siste reelle indeks

        // Hvis heapen ble tom (hadde bare ett element), er vi ferdige
        if (antall() == 0) return min;

        // 3) Flytt last(noden vi vil fjerne) til roten
        tl.oppdater(1, min);
        int k = 1; //k holder alltid indeksen til oppdatert forelder
        while (true){
            int venstre = k*2; //indeksen til potensielle venstrebarnet
            int høyre = k*2+1; // indeksen til potensielle høyrebarnet
            int antall = antall(); //dette er det oppdaterte antallet

            if (venstre>antall) break; //da har den ingen barn. Venstre indeks er større enn antall noder.

            //så antar vi at venstre er minst
            int minsteBarn = venstre;

            if (høyre <= antall){ //hvis høyrebarnet finnes
                T venstreVerdi = tl.hent(venstre);
                T høyreVerdi = tl.hent(høyre);
                if (cmp.compare(høyreVerdi, venstreVerdi)<0) //hvis høyre er mindre enn venstre, minstebarn = høyre
                    minsteBarn = høyre;
            }

            T forelderVerdi = tl.hent(k);
            T barnVerdi = tl.hent(minsteBarn);
            //hvis forelderVerdi er mindre ELLER LIK barn, så trenger vi ikke å gjøre noe
            if (cmp.compare(forelderVerdi,barnVerdi)<=0) break;

            //følgende skjer bare hvis treet ikke følger min heap reglene
            tl.oppdater(k, barnVerdi); //setter foreldernoden til å ha barnets verdi
            tl.oppdater(minsteBarn, forelderVerdi); // setter barnet til å ha forelders verdi

            //dette gjør at vi fortsetter nedover:
            k = minsteBarn;
        }
        return min;
    }

    @Override
    public boolean fjern(T t) {
        return false;
    }

    @Override
    public int antall() {
        return tl.antall()-1; //fordi vi må ta hensyn til det første null elementet.d
    }

    @Override
    public boolean tom() {
        return antall()==0;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i < tl.antall(); i++){
            sb.append(tl.hent(i));
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean inneholder(T t) {
        return false;
    }

    @Override
    public void nullstill() {

    }
}
public class Oppgave_1 {
}
