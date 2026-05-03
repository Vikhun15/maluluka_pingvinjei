/**
 * Speciális csomópont, amely konkrét funkcióval bíró építményt jelöl a térképen.
 */
public abstract class Epulet extends Csomopont {
    /** Az adott épület egyedi azonosítója. */
    protected int id;

    /**
     * Konstruktor az épület azonosítójának beállításához.
     */
    public Epulet() {
        super();
        this.id = super.id; // Az épület id = csomópont ID-jával
    }
}