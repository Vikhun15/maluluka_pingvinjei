package Models;

/**
 * A Hókotró által a jégmentesítéshez használt szóróanyagot reprezentálja.
 * Tárolja a rendelkezésre álló mennyiséget és a beszerzési árat.
 * Az ITargy interfész megvalósításával lehetővé teszi a kereskedelmi egységekben történő vásárlást.
 */
public class So implements ITargy {
    /**
     * A só egységnyi ára.
     */
    private final int ar;

    /**
     * A játékosnál lévő só mennyisége.
     */
    private int mennyiseg;

    /**
     * Só konstruktora az árat beállítva.
     *
     * @param ar az egységár
     */
    public So(int ar) {
        this.ar = ar;
        this.mennyiseg = 0;
    }

    /**
     * A só egységárát adja vissza.
     *
     * @return az ár
     */
    @Override
    public int getAr() {
        return this.ar;
    }

    /**
     * Visszaadja a játékosnál lévő só mennyiségét.
     *
     * @return a só mennyisége
     */
    public int getMennyiseg() {
        return this.mennyiseg;
    }

    /**
     * A só mennyiségét beállítja.
     *
     * @param ujMennyiseg az új mennyiség
     */
    public void setMennyiseg(int ujMennyiseg) {
        mennyiseg = ujMennyiseg;
    }

    /**
     * A sót alkalmazza a hókotrón.
     *
     * @param gep a hókotró
     */
    @Override
    public void applyTo(Hokotro gep) {
        gep.getSo().novel(10);
        System.out.println("-> Só készlet feltöltve! Jelenlegi mennyiség: " + gep.getSo().getMennyiseg() + " egység.");
    }

    /**
     * Levonja a kiszórt sómennyiséget a készletből.
     *
     * @param menny a csökkentendő mennyiség
     */
    public void csokkent(int menny) {
        int ujMennyiseg = this.getMennyiseg() - menny;
        if (ujMennyiseg < 0) {
            ujMennyiseg = 0;
        }
        this.setMennyiseg(ujMennyiseg);
    }

    /**
     * Hozzáadja a vásárolt mennyiséget a készlethez.
     *
     * @param menny a mennyiség, amivel növelni kell a készletet
     */
    public void novel(int menny) {
        mennyiseg += menny;
    }

    public String getNev() {
        return "Só";
    }
}

