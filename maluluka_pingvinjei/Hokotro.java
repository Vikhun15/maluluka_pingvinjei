import java.util.List;
import java.util.ArrayList;

/**
 * A takarító által vezetett jármű, amely kotrófejekkel takarítja az utat.
 * Magában foglalja a pénzkészletet, a sót és az üzemanyagot.
 */
public class Hokotro extends Jarmu {
    /** A hókotró (vagy vezetője) által birtokolt pénzösszeg. */
    private int penz;
    
    /** A hókotróra szerelt aktuális kotrófej. */
    private Kotrofej aktualisFej;
    
    /** A hókotró sókészlete. */
    private Salt salt;

    private List<Kotrofej> birtokoltFejek;
    
    /** A hókotró üzemanyagkészlete. */
    private Fuel fuel;

    /** A hókotró zúzott kő készlete. */
    private Rock rock;
    /**
     * Inicializálja a hókotrót az alapértelmezett erőforrásokkal.
     */
    public Hokotro(Sav indul) {
        super(indul);
        this.penz = 0;
        this.salt = new Salt(10);
        this.rock = new Rock(10);
        this.fuel = new Fuel(20);
        this.birtokoltFejek = new ArrayList<>();
    }
    public Hokotro() {
        super();
        this.penz = 0;
        this.salt = new Salt(10);
        this.fuel = new Fuel(20);
        this.birtokoltFejek = new ArrayList<>();
    }
  
    /**
     * Az aktív kotrófejen meghívja a hatasKifejtese metódust a megadott sávon.
     * @param sav A tisztítandó sáv.
     */
    public void takarit(Sav sav) {
        if (aktualisFej != null) {
            aktualisFej.hatasKifejtese(sav, this);
        }
    }

    /**
     * Ezt hívja meg az új fej, amikor megvásárolják a boltban.
     * Beteszi a listába, és (opcionálisan) rögtön fel is szereli.
     */
    public void ujFejetBegyujt(Kotrofej ujFej) {
        this.birtokoltFejek.add(ujFej);
        this.aktualisFej = ujFej; 
        System.out.println("-> Uj fej a raktarban. Jelenlegi fejek szama: " + birtokoltFejek.size());
    }

    /**
     * Visszaadja a birtokolt fejek listáját (pl. a UI vagy a tesztelő számára).
     */
    public List<Kotrofej> getBirtokoltFejek() {
        return this.birtokoltFejek;
    }

    /**
     * Kicseréli a hókotró aktív kotrófejét.
     * @param ujFej Az új felszerelendő kotrófej.
     */
    public void fejetCserel(Kotrofej ujFej) {
        this.aktualisFej = ujFej;
    }

    @Override
    public void mozog(Sav hova) {
        if (this.kimaradoKorok > 0) return; // 1. Ha törött, meg sem mozdul

        this.setAktualisSav(hova); // 2. Rálép a sávra

        if (hova.jeges() || hova.getTorottJeg() && !hova.koves()) {
            this.csuszkal(); // 3. Jég-check 
            if (this.kimaradoKorok > 0) return; 
        }

        // 4. BEÉRKEZÉS ÉS ÜTKÖZÉS-VIZSGÁLAT
        Csomopont cel = hova.getUtszakasz().getVegPont();
        cel.jarmuBefogad(this); 

        if (this.kimaradoKorok > 0) return; // Ha a csomópontban ütközött, nem takarít

        // 5. MUNKA
        hova.jarmuAthalad();
        this.aktualisFej.hatasKifejtese(hova, this);
        
    }

    // Getterek a fejek számára a működéshez
    public Salt getSalt() { return salt; }
    public Fuel getFuel() { return fuel; }
    public Rock getRock() { return rock; }
    public int getPenz() { return penz; }
    public void penzKeres(int osszeg) { this.penz += osszeg; }
    public Kotrofej getAktualisFej() { return aktualisFej; }
}