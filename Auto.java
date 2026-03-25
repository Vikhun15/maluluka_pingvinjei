/**
 * Az autó feladata az otthona és munkahelye közötti közlekedés.
 * Áthaladása elősegíti az utak jegesedését.
 */
public class Auto extends Jarmu {

    /** A jármű jelenlegi tartózkodási helye */
    protected Sav aktualisSav;

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
        //? UA mint a Jarmu-ben esetket szétválasztani.
    }
}