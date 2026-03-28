/**
 * Speciális útszakasz, amely a föld felett halad.
 */
public class Hid extends Utszakasz {
    /**
     * Felülírja a hoEsik metódust, mert a hídra több hó esik.
     */

    public Hid(Csomopont kezdo, Csomopont veg) {
        super(kezdo, veg); 
    }

    @Override
    public void hoEsik() {
        for (Sav s : savok) {
            s.hoEsik();
            s.hoEsik();
        }
    }
}