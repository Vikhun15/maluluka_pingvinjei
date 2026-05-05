package src;

/**
 * Speciális útszakasz, amely a föld alatt halad, így ide nem esik be a hó.
 */
public class Alagut extends Utszakasz {


    /**
     * Instantiates a new Alagut.
     *
     * @param kezdo the kezdo
     * @param veg   the veg
     */
    public Alagut(Csomopont kezdo, Csomopont veg) {
        super(kezdo, veg);
    }

    /**
     * Felülírja az ősosztály metódusát, hogy az alagútba ne essen hó.
     */
    @Override
    public void hoEsik() {
        //Nem történik semmi, mert az alagútba nem esik hó.
    }

}