package ForelesningerØving.F7_Rekursjon;

public class Oppgave {
    /*Implementer en rekursiv metode public static int a(int n) som beregner leddet an i tallfølgen
    definert av differensligningen: an = 2 an−1 + 3 an−2 for n ≥ 2, med a0 = 1 og a1 = 2.
    Hvorfor er den ineffektiv?
    */

    //oppgave 1
    public static int a(int n){
        if (n==0) return 1;
        else if (n==1) return 2;
        else {
            return 2*a(n-1)+ 3*a(n-2);
        }
    }
    /*Den er ineffektiv fordi for å regne ut a(4) trenger den a(3) og a(2) og for å regne ut a(3) trenger den a(2) og a(1), så a(2) regnes ut flere ganger. Når n blir stor, vokser antall kall raskt.*/


    //oppgave 2
    public static int tverrsum(int n){
        if (n<10) return n; //basistilfellet, fordi vi  trenger ikke finne et mindre siffer når tallet er mindre enn 10, siden det er bare ett siffer.
        else return tverrsum(n/10) + (n%10); /*n % 10 % betyr rest etter divisjon.  n % 10 gir det siste sifferet i tallet.
                                                    Eksempel: 472 % 10 = 2.
                                                    n / 10
                                                    - / betyr heltallsdivisjon.
                                                    - n / 10 fjerner det siste sifferet.
                                                    - Eksempel: 472 / 10 = 47.
                                                    Kort sagt:
                                                        n % 10 → henter siste siffer
                                                        n / 10 → fjerner siste siffer slik at du kan fortsette rekursivt med resten

                                                        Så for 472:
                                                                tverrsum(472) = tverrsum(47) + 2
                                                                tverrsum(47) = tverrsum(4) + 7
                                                                tverrsum(4) = 4 (basistilfelle)
                                                                Regnestykket blir: 4 + 7 + 2 = 13.*/
    }

    //oppgave 3
    /* Euklids algoritme sier at største felles divisor for a og b er det samme som største felles divisor for b og resten du får når a deles på b (a % b).*/
    public static int euklid(int a, int b){
        if (b==0) return a; //fordi, etter euklid, når b er 0 så er a resten av når du deler a på b. Hvis b er 0, er a den største felles divisoren.
        int r = a%b;
        return euklid(b,r);
    }

    // oppgave 4
    public static int sum(int n){
        if (n==1) return n;
        return sum(n-1)+n;
    }

    //oppgave 5
    public static int sum(int[] a, int v, int h) {
        // Basistilfellet: hvis intervallet bare består av ett element,
        // så er summen rett og slett verdien på dette elementet.
        if (v == h) return a[v];
        else {
            // Finn midtpunktet mellom venstre (v) og høyre (h) indeks.
            // Dette deler intervallet i to omtrent like store deler.
            int midt = (v + h) / 2;

            // Rekursiv del: regn ut summen av venstre del (a[v..midt])
            // og høyre del (a[midt+1..h]), og legg dem sammen.
            return sum(a, v, midt) + sum(a, midt + 1, h);
        }
    }

    //oppgave 6
    /*Første rekursive kall: HanoisTårn(n-1, fra, hjelp, til)
(flytt de små diskene bort til hjelpepinnen).

Print-linja: flytt disk n (den største i dette delproblemet).

Andre rekursive kall: HanoisTårn(n-1, hjelp, til, fra)
(flytt de små diskene fra hjelp → til, oppå den største).

2. Hvordan rekursjonsstacken fungerer

Når n > 1, så blir metoden bare lagt på stacken og venter på å få svar fra kallet med n-1.

Så den første printen du får, kommer faktisk helt innerst, når n har blitt 1.

Når n == 1, da treffer vi basistilfellet, og metoden kan gjøre en faktisk flytting (skrive ut).

Deretter går stacken tilbake og kjører print-linja for n=2, som betyr å flytte den nest største.

Så fortsetter det samme mønsteret oppover.
*/
    public static void HanoisTårn(int antall, char fra, char til, char hjelp) {
        // Basistilfellet:
        // Hvis det ikke er noen brikker igjen å flytte, gjør vi ingenting.
        if (antall <= 0) return;

        // Første rekursive kall:
        // Flytt (antall - 1) brikker fra "fra"-pinnen til "hjelp"-pinnen.
        // Dette frigjør den største brikken (nummer = antall) på "fra"-pinnen.
        HanoisTårn(antall - 1, fra, hjelp, til);

        // Selve flyttingen av den største brikken i dette delproblemet:
        // Vi simulerer flyttingen ved å printe ut hva som skal gjøres.
        // Dette er stedet der "den største" faktisk flyttes,
        // fordi alle mindre brikker er tatt vekk og lagt midlertidig på "hjelp".
        System.out.println("Flytter disk " + antall + " fra " + fra + " til " + til);

        // Andre rekursive kall:
        // Flytt (antall - 1) brikker fra "hjelp"-pinnen til "til"-pinnen.
        // Nå legger vi de små brikkene oppå den store på riktig pinne.
        HanoisTårn(antall - 1, hjelp, til, fra);
    }

    public static void main(String[] args) {
        System.out.println(a(4));
        System.out.println(tverrsum(472));
        System.out.println(euklid(30,6));
        System.out.println(sum(3));
        HanoisTårn(2,'a','b','c');
    }
}
