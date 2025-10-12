package ForelesningerØving.F14_F15Øving;

import ForelesningerØving.F8_Lister.Beholder;
import ForelesningerØving.F8_Lister.TabellListe;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

class Minimumshaug<T> implements Beholder<T>{

    private TabellListe<T> tl;
    private Comparator<? super T> cmp;

    public Minimumshaug(Comparator<? super T> cmp){
        this.cmp = cmp;
        this.tl = new TabellListe<>();
        tl.leggInn(0, null);
    }

    @Override
    public boolean leggInn(T t) {
        Objects.requireNonNull(t, "Null er ikke lov!");
        tl.leggInn(t); // sikrer at noden blir lagt inn i neste ledige plass, fordi den første ledige posisjonen i tabellista er den riktige posisjonen for en node å bli lagt inn i et komplett tre

        int k = antall(); // setter k til å være indeksen til posisjonen den nye noden ble lagt inn nå
        int forelderIndeks = k/2;
        T forelderVerdi = tl.hent(forelderIndeks);

        if (forelderIndeks==0) // hvis den nye nodens forelders indeks er 0, ble den lagt inn på rota. Da trenger vi ikke sammenligne noe fordi det er allerede min heap
            return true;
        while(k<1 && cmp.compare(t, forelderVerdi)<0){

            //bobler opp t til sin rette plass
            tl.oppdater(k, forelderVerdi); //setter k til å har forelderens verdi
            tl.oppdater(forelderIndeks,t); //setter forelderens verdi til å være t

            //nå må vi oppdatere k indeksen, så vi gjør k = k/2
            k = forelderIndeks;
            // som gjør at vi fortsetter dette frem til barn alltid er større enn forelder

            //Så oppdaterer vi forelderindeks til å peke på den nye forelderen til den nye k
            forelderIndeks= k/2;

            //OOG så oppdaterer vi forelderverdi
            forelderVerdi = tl.hent(forelderIndeks);
        }
        return true;
    }

    //denne metoden fjerner og returnerer det minste elementet (ROTA). Vi bytter rota med dne siste noden i tabellen, og så fjerner vi rotverdien.
    //Den nye rota må da motsatt-boble ned treet med DET MINSTE AV BARNA
    public T taUt(){
        if (tl.tom()) throw new NoSuchElementException("køen er tom");
        T tmp = tl.hent(1);
        int n = tl.antall();

        // 2) Hvis bare ett element: fjern det og returner
        if (n == 1) {
            // Forutsatt at TabellListe har en "fjern(pos)" som oppdaterer antall:
            tl.fjern(1); // alternativt tl.fjernSiste() om du har den
            return tmp;
         }

        T sisteVerdi = tl.hent(n);
        tl.oppdater(1,sisteVerdi);
        tl.fjern(n); // viktig: fjern siste fra tl! (ellers duplikat og feil antall)

        int antall = antall();
        int k = 1; //bruker denne til å holde styr over forelder. Setter til rot først.
        while (true){
            int venstre = k*2;
            int høyre = k*2+1;

            if (venstre> antall) break; //vis venstrebarnet har en større indeks enn antall noder i treet, da har noden ingen barn

            //så bestemmer vi et minstebarn
            int minsteBarn =  venstre;
            if (høyre<=antall) {//hvis høyre finnes
                T venstreVerdi = tl.hent(venstre);
                T høyreVerdi = tl.hent(høyre);
                int cmpv = cmp.compare(venstreVerdi,høyreVerdi);
                if (cmpv>0)
                    minsteBarn=høyre;
            }
            T forelderVerdi = tl.hent(k);
            T minstebarnVerdi = tl.hent(minsteBarn);

            if (cmp.compare(forelderVerdi,minstebarnVerdi)<=0) // hvis forelderens vedi er mindre eller lik minstebarnets verdi, trenger vi ikke gjøre noe mer.
                break;
            tl.oppdater(k, minstebarnVerdi);
            tl.oppdater(minsteBarn,forelderVerdi);

            k = minsteBarn; //setter k til å peke på noden den byttet med (bare hvis den fremdeles er mindre)
        }
        return tmp;
    }

    @Override
    public boolean fjern(T t) {
        return false;
    }


    @Override
    public int antall() {
        return tl.antall()-1;
    }

    @Override
    public boolean tom() {
        return false;
    }

    @Override
    public boolean inneholder(T t) {
        return false;
    }

    @Override
    public void nullstill() {

    }
}

public class MinimumshaugØving {
}
