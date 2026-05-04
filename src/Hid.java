package src;

/**
 * Speciális útszakasz, amely a föld felett halad.
 */
public class Hid extends Utszakasz {

    /**
     * Instantiates a new Hid.
     *
     * @param start the start
     * @param end   the end
     */
    public Hid(Csomopont start, Csomopont end) {
        super(start, end);
    }

    /**
     * Felülírja a hoEsik metódust, mert a hídra több hó esik.
     */
    @Override
    public void hoEsik() {
        for (Sav s : savok) {
            s.hoEsik();
            s.hoEsik();
        }
    }
}