/**
 * Az autó feladata az otthona és munkahelye közötti közlekedés.
 * Áthaladása elősegíti az utak jegesedését.
 */
public class Auto extends Jarmu {

    /** A jármű jelenlegi tartózkodási helye */
    protected Sav aktualisSav;

    protected Epulet otthon;

    protected Epulet munkahely;

    public Auto(Sav induloSav) {
        super(induloSav);
    }

    /**
     * Felülírja a mozog metódust, hogy a tervezett útvonalon haladjon.
     * Ennél a típusnál a játékos nem választ irányt.
     * @param hova A következő sáv az útvonalon.
     */
    @Override
    public void mozog(Sav hova) {
        if (this.kimaradoKorok > 0) return;

        this.setAktualisSav(hova);

        if (hova.jeges() || (hova.getTorottJeg() && !hova.koves())) {
            this.csuszkal(hova);
            if (this.kimaradoKorok > 0) return;
        }

        Csomopont cel = hova.getUtszakasz().getVegPont();
        cel.jarmuBefogad(this); 

        if (this.kimaradoKorok > 0) return;

        // HALAD
        hova.jarmuAthalad(); 
    }
}