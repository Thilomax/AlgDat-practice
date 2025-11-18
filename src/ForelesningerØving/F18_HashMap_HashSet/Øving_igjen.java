package ForelesningerØving.F18_HashMap_HashSet;

public class Øving_igjen {
    public static void main(String[] args) {

    }
}

class StringSet {
/*
Bruker hashkoder for  ̊a lagre en mengde strenger effektivt.
Bruker lenkede lister dersom en hashkode gjenbrukes.
*/
private int size;
private Node[] hashtabell;
private final class Node {
Node neste; String verdi;
private Node(String verdi, Node neste) {
this.verdi = verdi; this.neste = neste;
}
}
public StringSet(int size) {
this.size = size;
hashtabell = new Node[size];
}
private int beregnPosisjon(String verdi) {
int i = verdi.hashCode();
i = i % size;
if (i < 0) i = size + i;
return i;
}
public boolean leggInn(String verdi) {
    if (inneholder(verdi)) return false;
    int i = beregnPosisjon(verdi);
    hashtabell[i] = new Node(verdi, hashtabell[i]);
    return true;
}
public boolean inneholder(String verdi) {
    int posisjon = beregnPosisjon(verdi);
    Node p = hashtabell[posisjon];
    //oppretter en peker node akkurat som Node p = rot, bare at rota er den første noden i den lenkede listen i arrayet.
    while(p!=null) {
        if (p.verdi.equals(verdi))
            return true;
        p = p.neste;
    }
    return false;
}
}
