/**
 * A Hókotró által a jégmentesítéshez használt szóróanyagot reprezentálja. 
 * Tárolja a rendelkezésre álló mennyiséget és a beszerzési árat. 
 * Az ITargy interfész megvalósításával lehetővé teszi a kereskedelmi egységekben történő vásárlást.
 */
public class Salt implements ITargy {
    /** A só egységnyi ára. */
    private int ar;

    /** A játékosnál lévő só mennyisége. */
    private int mennyiseg;

    @Override
    public int getAr() {
        return this.ar;
    }

    /**
     * Visszaadja a játékosnál lévő só mennyiségét.
     * @return A só mennyisége.
     */
    public int getMennyiseg() {
        return this.mennyiseg;
    }

    /**
     * Levonja a kiszórt sómennyiséget a készletből.
     * @param menny A csökkentendő mennyiség.
     */
    public void csokkent(int menny) {
        //?
    }

    /**
     * Hozzáadja a vásárolt mennyiséget a készlethez.
     * @param menny A mennyiség, amivel növelni kell a készletet.
     */
    public void novel(int menny) {
       //?
    }
}