package Models;

/**
 * Az autó feladata az otthona és munkahelye közötti közlekedés.
 * Áthaladása elősegíti az utak jegesedését.
 */
public class Auto extends Jarmu {

    protected Epulet otthon;
    protected Epulet munkahely;

    // Állapotjelző: igaz, ha a munkahelyre tart. Hamis, ha hazafelé.
    private boolean munkabaMegy = true;

    public Auto(Sav sav) {
        super(sav);
        this.otthon = null;
        this.munkahely = null;
        this.setJarmuTipus("Auto");
    }

    public Auto(Epulet otthon, Epulet munkahely) {
        super();
        this.otthon = otthon;
        this.munkahely = munkahely;
        this.setJarmuTipus("Auto");
    }


    /**
     * Visszaadja azt az épületet, ahová az autó jelenleg tart.
     */
    public Epulet getAktualisCel() {
        return munkabaMegy ? munkahely : otthon;
    }

    /**
     * Megfordítja az autó haladási irányát (ha megérkezett a céljába).
     */
    public void megfordul() {
        munkabaMegy = !munkabaMegy;
        System.out.println("Egy autó célba ért, és megfordult a következő körre.");
    }

    public void setAktualisSav(Sav aktualisSav) {
        super.setAktualisSav(aktualisSav);
    }

    public void setOtthon(Epulet otthon) { this.otthon = otthon; }
    public void setMunkahely(Epulet munkahely) { this.munkahely = munkahely; }
    public Epulet getOtthon() { return otthon; }
    public Epulet getMunkahely() { return munkahely; }

    @Override
    public void elindul(Sav hova, Csomopont cel) {
        super.elindul(hova, cel);
    }

    @Override
    public void mozog(Sav hova) {
        if (this.kimaradoKorok > 0) return;

        this.setAktualisSav(hova);

        Csomopont cel = hova.getUtszakasz().getVegPont();
        if (hova.jeges() || (hova.getTorottJeg() && !hova.koves())) {
            this.csuszkal(hova, cel);
            if (this.kimaradoKorok > 0) return;
        }

        cel.jarmuBefogad(this);

        if (this.kimaradoKorok > 0) return;

        hova.jarmuAthalad();
        notifyObservers();
    }
}