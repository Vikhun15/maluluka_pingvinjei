/**
 * Egy olyan absztrakt osztály, amely a gráf alapú úthálózat csúcsait reprezentálja.
 */
public abstract class Csomopont {
    protected int id;
    protected static int nextId = 0;
    
    /** Az az egyetlen jármű, ami éppen a csomópontban tartózkodik. */
    protected Jarmu aktualisJarmu;

    public Csomopont() {
        this.id = nextId++;
        this.aktualisJarmu = null;
    }

    /**
     * Regisztrálja és befogadja a paraméterként kapott járművet.
     * Ha már van bent valaki, akkor karambol történik!
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
     * @param jarmu Az elhaladó jármű.
     */
    public void jarmuKilep(Jarmu jarmu) {
        if (this.aktualisJarmu == jarmu) {
            this.aktualisJarmu = null; 
        }
    }
}