package src;

/**
 * A Hókotró által a jégmentesítéshez használt szóróanyagot reprezentálja.
 * Tárolja a rendelkezésre álló mennyiséget és a beszerzési árat.
 * Az ITargy interfész megvalósításával lehetővé teszi a kereskedelmi egységekben történő vásárlást.
 */
public class Salt implements ITargy {
    /**
     * A só egységnyi ára.
     */
    private final int ar;

    /**
     * A játékosnál lévő só mennyisége.
     */
    private int mennyiseg;

    /**
     * Instantiates a new Salt.
     *
     * @param ar the ar
     */
    public Salt(int ar) {
        this.ar = ar;
        this.mennyiseg = 0;
    }

    @Override
    public int getAr() {
        return this.ar;
    }

    /**
     * Visszaadja a játékosnál lévő só mennyiségét.
     *
     * @return A só mennyisége.
     */
    public int getMennyiseg() {
        return this.mennyiseg;
    }

    /**
     * Sets mennyiseg.
     *
     * @param ujMennyiseg the uj mennyiseg
     */
    public void setMennyiseg(int ujMennyiseg) {
        mennyiseg = ujMennyiseg;
    }

    @Override
    public void applyTo(Hokotro gep) {
        gep.getSo().novel(10);
        System.out.println("-> Só készlet feltöltve! Jelenlegi mennyiség: " + gep.getSo().getMennyiseg() + " egység.");
    }


    /**
     * Levonja a kiszórt sómennyiséget a készletből.
     *
     * @param menny A csökkentendő mennyiség.
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
     * @param menny A mennyiség, amivel növelni kell a készletet.
     */
    public void novel(int menny) {
        mennyiseg += menny;
    }
}