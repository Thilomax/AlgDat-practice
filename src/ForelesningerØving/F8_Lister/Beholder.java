package ForelesningerØving.F8_Lister;

/**
 * En enkel beholder for elementer av typen T.
 *
 * Null-policy: null er ikke tillatt som argument. Metoder som tar T vil kaste NullPointerException ved null.
 * Duplikat-policy: duplikater er tillatt i denne basiskontrakten.
 *   Implementasjoner som velger å avvise duplikater skal indikere dette ved at leggInn(T) returnerer false når elementet finnes fra før.
 *
 * Indeksgrenser: Ikke relevant for denne kontrakten, se Liste<T> for indeksbaserte metoder.
 */

public interface Beholder<T> {

    boolean leggInn(T t);//returnerer boolean for å vise om den funka
    boolean fjern(T t);
    int antall();
    boolean tom();
    boolean inneholder(T t);
    void nullstill();
}
