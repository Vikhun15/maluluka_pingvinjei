package Models;

import java.util.ArrayList;
import java.util.List;
import Observers.IObserver;
import Observers.IObservable;

/**
 * Két csomópontot összekötő alapegység. Fő feladata a sávok összefogása.
 */
public class Utszakasz implements IObservable {
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
     * Az útszakasz egyedi azonosítója.
     */
    protected int id;

    /**
     * Automatikus sorszámozáshoz használt számláló.
     */
    protected static int nextId = 0;

    /**
     * Az útszakaszhoz tartozó sávok listája.
     */
    protected List<Sav> savok = new ArrayList<>();

    /**
     * Az útszakasz kezdő csomópontja.
     */
    protected Csomopont kezdoCsomopont;

    /**
     * Az útszakasz végső csomópontja.
     */
    protected Csomopont vegCsomopont;

    private boolean isAlagut = false;
    private boolean isHid = false;

    /**
     * Instantiates a new Utszakasz.
     *
     * @param kezdo the kezdo
     * @param veg   the veg
     */
    public Utszakasz(Csomopont kezdo, Csomopont veg) {
        this.id = nextId++;
        this.kezdoCsomopont = kezdo;
        this.vegCsomopont = veg;
    }

    /**
     * Visszaadja az útszakaszhoz tartozó sávok listáját.
     *
     * @return Sávok listája.
     */
    public List<Sav> getSavok() {
        return savok;
    }

    /**
     * Havazás esetén hívódik meg, növeli a sávokon a hórétegek számát.
     */
    public void hoEsik() {
        for (Sav s : savok) {
            s.hoEsik();
        }
        notifyObservers();
    }

    /**
     * Visszaadja az útszakasz egyedi azonosítóját.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets kezdo pont.
     *
     * @param kezdoCsomopont the kezdo csomopont
     */
    public void setKezdoCsomopont(Csomopont kezdoCsomopont) {
        this.kezdoCsomopont = kezdoCsomopont;
    }

    /**
     * Sets veg pont.
     *
     * @param vegCsomopont the veg csomopont
     */
    public void setVegCsomopont(Csomopont vegCsomopont) {
        this.vegCsomopont = vegCsomopont;
    }

    /**
     * Gets kezdo pont.
     *
     * @return the kezdo pont
     */
    public Csomopont getKezdoPont() {
        return this.kezdoCsomopont;
    }

    /**
     * Gets veg pont.
     *
     * @return the veg pont
     */
    public Csomopont getVegPont() {
        return this.vegCsomopont;
    }

    public boolean getIsHid(){
        return isHid;
    }

    public void setIsHid(boolean isHid){
        this.isHid = isHid;
    }

    public boolean getIsAlagut(){
        return isAlagut;
    }

    public void setIsAlagut(boolean isAlagut){
        this.isAlagut = isAlagut;
    }
}