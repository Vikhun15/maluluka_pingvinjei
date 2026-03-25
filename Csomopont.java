/**
 * Egy olyan absztrakt osztály, amely a gráf alapú úthálózat csúcsait reprezentálja.
 * Képes járművek befogadására, és útszakaszok indulnak ki belőle, vagy érkeznek be ide.
 */
public abstract class Csomopont {
    /** Az adott csomópont egyedi azonosítója. */
    protected int id;
    
    /** Automatikus sorszámozáshoz használt statikus számláló. */
    protected static int nextId = 0;

    /**
     * Konstruktor, amely beállítja az egyedi azonosítót.
     */
    public Csomopont() {
        this.id = nextId++;
    }

    /**
     * Regisztrálja és befogadja a paraméterként kapott járművet.
     * @param jarmu A befogadandó jármű.
     */
    public void jarmuBefogad(Jarmu jarmu) {
        //?
    }

    /**
     * Kijelentkezteti a paraméterként kapott járművet.
     * @param jarmu Az elhaladó jármű.
     */
    public void jarmuKilep(Jarmu jarmu) {
        //?
    }
}