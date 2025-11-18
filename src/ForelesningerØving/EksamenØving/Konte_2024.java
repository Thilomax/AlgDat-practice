package ForelesningerØving.EksamenØving;

import org.w3c.dom.Node;

import java.util.Comparator;
import java.util.Objects;

public class Konte_2024 {
    public static void main(String[] args) {

    }
}
class Konte_2024BinærSøkeTre<T> {
    private Konte_2024BinærSøkeTre<T> venstre, høyre = null;
    private Comparator<T> comp;
    private T verdi;
    public Konte_2024BinærSøkeTre(Comparator<T> comp, T verdi) {
        this.verdi = verdi;
        this.comp = comp;
    }
    public boolean inneholder(T verdi) {
        int cV = comp.compare(verdi, this.verdi);
        if (cV == 0) {
            return true;
        } else if (cV < 0) {
            return this.venstre != null && this.venstre.inneholder(verdi);
        } else {
            return this.høyre != null && this.høyre.inneholder(verdi);
        }
    }
    public void leggInn(T verdi) {
        Objects.requireNonNull(verdi);
        int cV = comp.compare(verdi, this.verdi);
        //hvis vi må til venstre og venstre er null, så oppretter vi instansen av bst-et der
         if (cV < 0) {
             if (this.venstre == null) venstre = new Konte_2024BinærSøkeTre<>(comp,verdi);
             else this.venstre.leggInn(verdi);
        } else {
             if (this.høyre == null) høyre = new Konte_2024BinærSøkeTre<>(comp, verdi);
             else this.høyre.leggInn(verdi);
        }
    }
}
