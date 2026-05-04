package src;

/**
 * Egy olyan osztály, amely a gráf alapú úthálózat csúcsait reprezentálja.
 * Képes járművek befogadására, és útszakaszok indulnak ki belőle, vagy érkeznek be ide.
 */
public class Csomopont {
    /**
     * Az adott csomópont egyedi azonosítója.
     */
    protected int id;

    /**
     * Automatikus sorszámozáshoz használt statikus számláló.
     */
    protected static int nextId = 0;

    private Epulet epulet = null;

    /**
     * The Aktualis jarmu.
     */
    protected Jarmu aktualisJarmu = null;

    /**
     * Konstruktor, amely beállítja az egyedi azonosítót.
     */
    public Csomopont() {
        this.id = nextId++;
    }


    /**
     * Regisztrálja és befogadja a paraméterként kapott járművet.
     *
     * @param jarmu A befogadandó jármű.
     */
    public void jarmuBefogad(Jarmu jarmu) {
        if (this.aktualisJarmu != null) {
            this.aktualisJarmu.karambolozik();
            jarmu.karambolozik();
            System.out.println("KARAMBOL! Járművek ütköztek a csomópontban. Mindkét jármű 3 körig kimarad.");
        } else {
            this.aktualisJarmu = jarmu;
        }
    }

    /**
     * Kijelentkezteti a paraméterként kapott járművet.
     *
     * @param jarmu Az elhaladó jármű.
     */
    public void jarmuKilep(Jarmu jarmu) {
        if (this.aktualisJarmu == jarmu) {
            this.aktualisJarmu = null;
        }
    }

    /**
     * Gets epulet.
     *
     * @return the epulet
     */
    public Epulet getEpulet() {
        return epulet;
    }

    /**
     * Sets epulet.
     *
     * @param epulet the epulet
     */
    public void setEpulet(Epulet epulet) {
        this.epulet = epulet;
    }

    /**
     * Gets aktualis jarmu.
     *
     * @return the aktualis jarmu
     */
    public Jarmu getAktualisJarmu() {
        return aktualisJarmu;
    }
}