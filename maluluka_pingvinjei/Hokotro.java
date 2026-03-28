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
    
    /** A hókotró üzemanyagkészlete. */
    private Fuel fuel;

    /**
     * Inicializálja a hókotrót az alapértelmezett erőforrásokkal.
     */
    public Hokotro(Sav indul) {
        super(indul);
        this.penz = 0;
        this.salt = new Salt(10);
        this.fuel = new Fuel(20);
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

        if (hova.jeges()) {
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
    public int getPenz() { return penz; }
    public void penzKeres(int osszeg) { this.penz += osszeg; }
}