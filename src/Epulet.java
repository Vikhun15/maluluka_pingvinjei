package src;

/**
 * Speciális csomópont, amely konkrét funkcióval bíró építményt jelöl a térképen.
 */
public abstract class Epulet{
    /** Az adott épület egyedi azonosítója. */
    protected int id;

    /** Automatikus sorszámozáshoz használt statikus számláló. */
    protected static int nextId = 0;
    /**
     * Konstruktor az épület azonosítójának beállításához.
     */
    public Epulet() {
        this.id = nextId++;
    }

    /**
     * Visszaadja az épület egyedi azonosítóját.
     * @return Az épület ID-ja.
     */
    public int getId() {
        return this.id;
    }
}