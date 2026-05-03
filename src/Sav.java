package src;

/**
 * A Sav osztályon keresztül tudnak eljutni a járművek az egyik csomópontból a másikba.
 * Nyilvántartja a hórétegeket, a jeget, és kezeli a forgalom okozta jegesedést.
 */
public class Sav {
    private int hoRetegek = 0;
    private boolean jeg = false; 
    private int athaladtAutok = 0;
    private boolean sozva = false; 
    private boolean torottJeg = false;
    private boolean jarhato = true;

    /**
     * Növeli az áthaladt autók számát. Ha eléri a 4-et, a sáv jegessé válik.
     * @param j A sávon áthaladó jármű.
     */
    public void autoAthalad(Jarmu j) {
        athaladtAutok++;
        if (athaladtAutok >= 4) { 
            jeg = true; 
            athaladtAutok = 0; 
        }
    }

    /**
     * Havazáskor növeli a hórétegek számát.
     */
    public void hoEsik() {
        hoRetegek++;
    }

    /**
     * A jégtörő fej hatására a jégpáncél feltörik.
     */
    public void jegetTor() {
        jeg = false; 
        torottJeg = true; 
    }

    /**
     * Söpréskor vagy hóhányáskor a hó eltűnik a sávról.
     * @param hova A szomszédos sáv, ahova a hó átkerül.
     */
    public void havatTol(Sav hova) {
        this.hoRetegek = 0; 
        if (hova != null) hova.hoEsik(); 
    }

    /**
     * Felolvasztja a havat és a jeget a sávról.
     */
    public void olvaszt() {
        hoRetegek = 0; 
        jeg = false; 
        torottJeg = false;
    }

    //* Állapot beállítása */
    public void setSozva(boolean s) { this.sozva = s; }
}