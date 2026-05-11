package src.Models;

import java.util.Random;

/**
 * Absztrakt ősosztály a pályán mozgó járművekhez.
 * Felelős a mozgásért, a jégen csúszásért és a karambolok kezeléséért.
 */
public abstract class Jarmu {
    /**
     * A Jarmu egyedi azonosítója.
     */
    protected int id;

    /**
     * A példányosított Jarmu objektumok automatikus sorszámozásához használt számláló.
     */
    protected static int nextId = 0;

    /**
     * A jármű jelenlegi tartózkodási helye
     */
    protected Sav aktualisSav = null;

    /**
     * Eltárolja, hogy a jármű hány körből marad ki.
     */
    protected int kimaradoKorok;

    /**
     * The constant rng.
     */
    protected static final Random rng = new Random();

    /**
     * Konstruktor, amely beállítja az egyedi azonosítót.
     */
    public Jarmu() {
        this.id = nextId++;
        this.kimaradoKorok = 0;
    }

    /**
     * Instantiates a new Jarmu.
     *
     * @param induloSav the indulo sav
     */
    public Jarmu(Sav induloSav) {
        this.id = nextId++;
        this.aktualisSav = induloSav;
        this.kimaradoKorok = 0;
    }

    /**
     * Absztrakt metódus a jármű helyváltoztatására.
     *
     * @param hova A cél sáv, ahová a jármű mozog.
     */
    public abstract void mozog(Sav hova);

    /**
     * A jármű jégpáncélon történő megcsúszását szimulálja.
     * A csúszás következtében a jármű elveszítheti az irányítást.
     *
     * @param hova the hova
     */
    public void csuszkal(Sav hova) {
        double esely = rng.nextDouble();

        //ez potenciálisan lehetne jó ütközés logikának, vagy hasonló
        if (esely <= 0.30) {
            Csomopont vegPont = hova.getUtszakasz().getVegPont();
            Jarmu masikJarmu = vegPont.getAktualisJarmu();
            java.util.Optional.ofNullable(masikJarmu).ifPresent(m -> {
                m.karambolozik();
                this.karambolozik();
            });
        }
    }

    /**
     * Ütközés esetén beállítja a kimaradó köröket és blokkolja a járművet.
     * Alapértelmezésben 3 kör marad ki.
     */
    public void karambolozik() {
        this.kimaradoKorok = 3;
    }

    /**
     * Frissíti a jármű állapotát az adott körben.
     */
    public void korFrissites() {
        if (kimaradoKorok > 0) kimaradoKorok--;
    }

    /**
     * Visszaadja a jármű egyedi azonosítóját.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Visszaadja a sávot, amelyen a jármű jelenleg tartózkodik.
     *
     * @return Az aktuális sáv.
     */
    public Sav getAktualisSav() {
        return this.aktualisSav;
    }

    /**
     * Beállítja a jármű új tartózkodási helyét.
     *
     * @param ujSav Az új sáv, ahová a jármű került.
     */
    public void setAktualisSav(Sav ujSav) {
        this.aktualisSav = ujSav;
    }
}