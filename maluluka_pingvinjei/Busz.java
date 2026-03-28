/**
 * A buszvezető által irányított jármű, célja a végállomások közötti ingázás.
 */
public class Busz extends Jarmu {

    /** Számon tartja, hányszor tette meg a távot a végállomások között. */
    private int megfordulasokSzama;

    /** A busz kezdo allomása */
    private Csomopont kezdoAllomas;
    
    /** A busz érkezési végállomása. */
    private Csomopont vegAllomas;

    public Busz(Sav induloSav, Csomopont kezdo, Csomopont veg) {
        super(induloSav);
        this.megfordulasokSzama = 0;
        this.kezdoAllomas = kezdo;
        this.vegAllomas = veg;
    }

    @Override
    public void mozog(Sav hova) {
        if (this.kimaradoKorok > 0) return;

        this.setAktualisSav(hova);

        if (hova.jeges()) {
            this.csuszkal();
            if (this.kimaradoKorok > 0) return;
        }

        Csomopont cel = hova.getUtszakasz().getVegPont();
        cel.jarmuBefogad(this); 

        if (this.kimaradoKorok > 0) return;

        hova.jarmuAthalad();
        // 5. MUNKA: Megfordulás, ha végállomásra ért
        if (cel == this.vegAllomas) {
            this.megfordulasokSzama++;
            Csomopont temp = this.kezdoAllomas;
            this.kezdoAllomas = this.vegAllomas;
            this.vegAllomas = temp;
        }
    }
}