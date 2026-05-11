package src.Models;

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
     * X koordináta
     */
    private int x;

    /**
     * Y koordináta
     */
    private int y;

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
        java.util.Optional.ofNullable(this.aktualisJarmu).ifPresentOrElse(j -> {
            j.karambolozik();
            jarmu.karambolozik();
            System.out.println("KARAMBOL! Járművek ütköztek a csomópontban. Mindkét jármű 3 körig kimarad.");
        }, () -> this.aktualisJarmu = jarmu);
    }

    /**
     * Kijelentkezteti a paraméterként kapott járművet.
     *
     * @param jarmu Az elhaladó jármű.
     */
    public void jarmuKilep(Jarmu jarmu) {
        java.util.Optional.ofNullable(this.aktualisJarmu).filter(j -> j == jarmu).ifPresent(j -> this.aktualisJarmu = null);
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


    /**
     * gets Id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }






    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        this.y = y;
    }
}