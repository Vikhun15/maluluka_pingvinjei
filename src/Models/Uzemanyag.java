package Models;

/**
 * A Hókotró Sárkányfejének működéséhez szükséges üzemanyagot reprezentálja.
 * Nyilvántartja az aktuális üzemanyagszintet és az árat.
 */
public class Uzemanyag implements ITargy {
    /**
     * Az üzemanyag egységára.
     */
    private final int ar;
    /**
     * A jelenleg a játékosnál lévő üzemanyag mennyisége literben.
     */
    private int literek;

    /**
     * Üzemanyag konstruktora az árat beállítva.
     *
     * @param ar az üzemanyag egységára
     */
    Uzemanyag(int ar) {
        this.ar = ar;
        this.literek = 0;
    }

    /**
     * Az üzemanyag egységárát adja vissza.
     *
     * @return az ár
     */
    @Override
    public int getAr() {
        return this.ar;
    }

    /**
     * Csökkenti az üzemanyag mennyiségét.
     *
     * @param menny a felhasznált mennyiség
     */
    public void fogyaszt(int menny) {
        this.literek -= menny;
    }

    /**
     * Az üzemanyagot alkalmazza a hókotrón.
     *
     * @param gep a hókotró
     */
    @Override
    public void applyTo(Hokotro gep) {
        gep.getUzemanyag().tankol(20);
        System.out.println("-> Üzemanyag feltankolva! Jelenlegi mennyiség: " + gep.getUzemanyag().getLiterek() + " liter.");
    }

    /**
     * Visszaadja a jelenlegi üzemanyag mennyiségét.
     *
     * @return az üzemanyag mennyisége literben
     */
    public int getLiterek() {
        return this.literek;
    }

    /**
     * Növeli az üzemanyag mennyiségét.
     *
     * @param menny a tankolt mennyiség
     */
    public void tankol(int menny) {
        this.literek += menny;
    }

    public String getNev() {
        return "Üzemanyag";
    }

    public void setLiterek(int fuel) {
        this.literek = fuel;
    }



    @Override
    public ITargy masol() {
        Uzemanyag ret = new Uzemanyag(this.ar);
        ret.literek = this.literek;
        return ret;
    }
}

