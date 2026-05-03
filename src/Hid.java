package src;

/**
 * Speciális útszakasz, amely a föld felett halad.
 */
public class Hid extends Utszakasz {

    public Hid(Csomopont start, Csomopont end) {
        super(start, end);
    }

    /**
     * Felülírja a hoEsik metódust, mert a hídra több hó esik.
     */
    @Override
    public void hoEsik() {
        //?
    }
}