package src;

/**
 * A buszvezető által irányított jármű, célja a végállomások közötti ingázás.
 */
public class Busz extends Jarmu {

    /** Számon tartja, hányszor tette meg a távot a végállomások között. */
    private int megfordulasokSzama;

    public Busz(Sav induloSav) {
        super();
        this.megfordulasokSzama = 0;
    }

    @Override
    public void mozog(Sav hova) {
        //?
    }
}