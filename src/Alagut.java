package src;

/**
 * Speciális útszakasz, amely a föld alatt halad, így ide nem esik be a hó.
 */
public class Alagut extends Utszakasz {

    public Alagut(Csomopont start, Csomopont end) {
        super(start, end);
    }

    /**
     * Felülírja az ősosztály metódusát, hogy az alagútba ne essen hó.
     */
    @Override
    public void hoEsik() {}
        
}