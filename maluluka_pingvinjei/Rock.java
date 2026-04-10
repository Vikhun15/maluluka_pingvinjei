/**
 * A Hókotró által a csúszásmentesítéshez használt szóróanyagot reprezentálja. 
 * Tárolja a rendelkezésre álló mennyiséget és a beszerzési árat. 
 * Az ITargy interfész megvalósításával lehetővé teszi a kereskedelmi egységekben történő vásárlást.
 */

public class Rock implements ITargy{
    /** A zúzott kő egységnyi ára. */
    private int ar;

    /** A játékosnál lévő zúzott kő mennyisége. */
    private int mennyiseg;

    Rock(int ar){
        this.ar=ar;
        mennyiseg=10;
    }

    @Override
    public int getAr() {
        return this.ar;
    }

    /**
     * Visszaadja a játékosnál lévő zúzott kő mennyiségét.
     * @return A kő mennyisége.
     */
    public int getMennyiseg() {
        return this.mennyiseg;
    }

    @Override
    public void applyTo(Hokotro gep) {
        gep.getRock().novel(10);
        System.out.println("-> Zúzott kő készlet feltöltve! Jelenlegi mennyiség: " + gep.getRock().getMennyiseg() + " egység.");
    }

    public void setMennyiseg(int ujMennyiseg){
        mennyiseg=ujMennyiseg;
    }

    /**
     * Levonja a kiszórt kőmennyiséget a készletből.
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
     * @param menny A mennyiség, amivel növelni kell a készletet.
     */
    public void novel(int menny) {
        mennyiseg+=menny;
        if(mennyiseg >= 20){
            mennyiseg = 20;
        }
    }
}
