import java.util.ArrayList;
import java.util.List;

/**
 * Két csomópontot összekötő alapegység. Fő feladata a sávok összefogása.
 */
public class Utszakasz {
    /** Az útszakasz egyedi azonosítója. */
    protected int id;
    
    /** Automatikus sorszámozáshoz használt számláló. */
    protected static int nextId = 0;
    
    /** Az útszakaszhoz tartozó sávok listája. */
    protected List<Sav> savok = new ArrayList<>();

    public Utszakasz() {
        this.id = nextId++;
    }

    /**
     * Visszaadja az útszakaszhoz tartozó sávok listáját.
     * @return Sávok listája.
     */
    public List<Sav> getSavok() {
        //? ehhez késő van
        return savok;
    }

    /**
     * Havazás esetén hívódik meg, növeli a sávokon a hórétegek számát.
     */
    public void hoEsik() {
        for(Sav s : savok) {
            s.hoEsik();
        }
    }
}