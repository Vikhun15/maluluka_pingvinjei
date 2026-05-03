package src;

/**
 * Az autó feladata az otthona és munkahelye közötti közlekedés.
 * Áthaladása elősegíti az utak jegesedését.
 */
public class Auto extends Jarmu {

    /** A jármű jelenlegi tartózkodási helye */
    protected int start;
    protected int end;

    public Auto(int start, int end){
        super();
        this.start = start;
        this.end = end;
    }

    public void setAktualisSav(Sav aktualisSav) {
        super.setAktualisSav(aktualisSav);
    }

    /**
     * Felülírja a mozog metódust, hogy a tervezett útvonalon haladjon.
     * Ennél a típusnál a játékos nem választ irányt.
     * @param hova A következő sáv az útvonalon.
     */
    @Override
    public void mozog(Sav hova) {
        //? UA mint a Jarmu-ben esetket szétválasztani.
    }
}