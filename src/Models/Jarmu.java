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

}