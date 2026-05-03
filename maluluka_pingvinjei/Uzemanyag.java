/**
 * A Hókotró Sárkányfejének működéséhez szükséges üzemanyagot reprezentálja.
 * Nyilvántartja az aktuális üzemanyagszintet és az árat.
 */
public class Uzemanyag implements ITargy {
    /** Az üzemanyag egységára. */
    private int ar;
    /** A jelenleg a játékosnál lévő üzemanyag mennyisége literben. */
    private int literek;

    Uzemanyag(int ar) {
        this.ar = ar;
        this.literek = 20; 
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

    @Override
    public void applyTo(Hokotro gep) {
        gep.getUzemanyag().tankol(20);
        System.out.println("-> Üzemanyag feltankolva! Jelenlegi mennyiség: " + gep.getUzemanyag().getLiterek() + " liter.");
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