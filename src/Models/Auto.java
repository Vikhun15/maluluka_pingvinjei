package Models;

/**
 * Az autó feladata az otthona és munkahelye közötti közlekedés.
 * Áthaladása elősegíti az utak jegesedését.
 */
public class Auto extends Jarmu {

    /**
     * A jármű jelenlegi tartózkodási helye
     */
    protected Epulet otthon;

    /**
     * The Munkahely.
     */
    protected Epulet munkahely;

    /**
     * Instantiates a new Auto.
     *
     * @param sav the sav
     */
    public Auto(Sav sav) {
        super(sav);
        this.otthon = null;
        this.munkahely = null;
        this.setJarmuTipus("Auto");
    }

    /**
     * Instantiates a new Auto.
     *
     * @param otthon    the otthon
     * @param munkahely the munkahely
     */
    public Auto(Epulet otthon, Epulet munkahely) {
        super();
        this.otthon = otthon;
        this.munkahely = munkahely;
        this.setJarmuTipus("Auto");
    }

    public void setAktualisSav(Sav aktualisSav) {
        super.setAktualisSav(aktualisSav);
    }

    /**
     * Sets otthon.
     *
     * @param otthon the otthon
     */
    public void setOtthon(Epulet otthon) {
        this.otthon = otthon;
    }

    /**
     * Sets munkahely.
     *
     * @param munkahely the munkahely
     */
    public void setMunkahely(Epulet munkahely) {
        this.munkahely = munkahely;
    }

    /**
     * Gets otthon.
     *
     * @return the otthon
     */
    public Epulet getOtthon() {
        return otthon;
    }

    /**
     * Gets munkahely.
     *
     * @return the munkahely
     */
    public Epulet getMunkahely() {
        return munkahely;
    }

    /**
     * Felülírja a mozog metódust, hogy a tervezett útvonalon haladjon.
     * Ennél a típusnál a játékos nem választ irányt.
     *
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
        notifyObservers();
    }
}