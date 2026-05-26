package Models;

import java.util.Random;
import Observers.IObserver;
import Observers.IObservable;
import java.util.ArrayList;
import java.util.List;

/**
 * Absztrakt ősosztály a pályán mozgó járművekhez.
 * Felelős a mozgásért, a jégen csúszásért és a karambolok kezeléséért.
 */
public abstract class Jarmu implements IObservable {

    private transient List<IObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(IObserver o) {
        if(observers == null){
            observers = new ArrayList<>();
        }
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        if(observers != null){
            observers.remove(o);
        }
    }

    @Override
    public void notifyObservers() {
        if(observers != null){
            for (IObserver o : observers) {
                o.update();
            }
        }
    }

    /**
     * A Jarmu egyedi azonosítója.
     */
    protected int id;

    /**
     * A példányosított Jarmu objektumok automatikus sorszámozásához használt számláló.
     */
    protected static int nextId = 0;

    private String jarmuTipus;

    /**
     * A jármű jelenlegi tartózkodási helye
     */
    protected Sav aktualisSav = null;

    protected Csomopont aktualisCsomopont = null;

    /**
     * Eltárolja, hogy a jármű hány körből marad ki.
     */
    protected int kimaradoKorok;

    /**
     * The constant rng.
     */
    protected static final Random rng = new Random();

    private int x;
    private int y;

    protected Csomopont celCsomopont = null;

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
     * Metódus a jármű helyváltoztatására.
     *
     * @param hova A cél sáv, ahová a jármű mozog.
     */
    public void mozog(Sav hova){
        notifyObservers();
    }

    public void elindul(Sav hova, Csomopont cel) {
        if (this.kimaradoKorok > 0) return;

        if (this.aktualisCsomopont != null) {
            this.aktualisCsomopont.jarmuKilep(this);
            this.aktualisCsomopont = null;
        }

        this.aktualisSav = hova;
        this.celCsomopont = cel;

        hova.jarmuAthalad();
        notifyObservers();
    }

    /**
     * 2. FÁZIS: A jármű a következő körben beér a csomópontba.
     */
    public void megerkezik() {
        if (this.kimaradoKorok > 0 || this.celCsomopont == null || this.aktualisSav == null) return;

        if (aktualisSav.jeges() || (aktualisSav.getTorottJeg() && !aktualisSav.getKovezve())) {
            this.csuszkal(aktualisSav, celCsomopont);
            if (this.kimaradoKorok > 0) return;
        }

        this.aktualisCsomopont = celCsomopont;
        this.celCsomopont.jarmuBefogad(this);

        this.celCsomopont = null;
        this.aktualisSav = null;

        notifyObservers();
    }

    public void csuszkal(Sav hova, Csomopont vegPont) {
        double esely = rng.nextDouble();
        if (esely <= 0.10) {
            Jarmu masikJarmu = vegPont.getAktualisJarmu();
            if (masikJarmu != null) {
                System.out.println("BUMM! " + this.getJarmuTipus() + " karambolozott!");
                masikJarmu.karambolozik();
                this.karambolozik();
            } else {
                System.out.println("HUH! " + this.getJarmuTipus() + " megcsúszott a jégen, de az út üres volt!");
                this.aktualisCsomopont = vegPont;
                vegPont.jarmuBefogad(this);
                this.celCsomopont = null;
                this.aktualisSav = null;
            }
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

    public Csomopont getAktualisCsomopont() {
        return aktualisCsomopont;
    }
    public void setAktualisCsomopont(Csomopont aktualisCsomopont) {
        this.aktualisCsomopont = aktualisCsomopont;
    }

    public int getKimaradoKorok() {
        return kimaradoKorok;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getJarmuTipus(){
        return jarmuTipus;
    }

    public void setJarmuTipus(String jarmuTipus){
        this.jarmuTipus = jarmuTipus;
    }

    public Csomopont getCelCsomopont() {
        return celCsomopont;
    }

    public void setCelCsomopont(Csomopont celCsomopont) {
        this.celCsomopont = celCsomopont;
    }

}