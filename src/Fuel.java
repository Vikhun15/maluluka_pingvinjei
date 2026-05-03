package src;

/**
 * A Hókotró Sárkányfejének működéséhez szükséges üzemanyagot reprezentálja.
 * Nyilvántartja az aktuális üzemanyagszintet és az árat.
 */
public class Fuel implements ITargy {
    /** Az üzemanyag egységára. */
    private int ar;
    /** A jelenleg a játékosnál lévő üzemanyag mennyisége literben. */
    private int literek;

    Fuel(int ar) {
        this.ar = ar;
        this.literek = 0; 
    }

    @Override
    public int getAr() {
        return this.ar;
    }

    /**
     * Csökkenti az üzemanyag mennyiségét.
     * @param menny A felhasznált mennyiség.
     */
    public void fogyaszt(int menny) {
        this.literek -= menny;
    }

    /**
     * Visszaadja a jelenlegi üzemanyag mennyiségét.
     * @return Az üzemanyag mennyisége literben.
     */
    public int getLiterek() {
        return this.literek;
    }   

    /**
     * Növeli az üzemanyag mennyiségét.
     * @param menny A tankolt mennyiség.
     */
    public void tankol(int menny) {
        this.literek += menny;
    }
}