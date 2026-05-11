package src.Models;

/**
 * The type Bolt.
 */
public class Bolt extends Epulet {

    /**
     * Instantiates a new Bolt.
     */
    public Bolt() {
        super();
    }

    //A Hókotró ára
    private static final int hokotroAr = 100;

    /**
     * Elad.
     *
     * @param termek the termek
     * @param vevo   the vevo
     */
    public void elad(ITargy termek, Hokotro vevo) {
        if (termek.getAr() <= vevo.getPenz()) {
            vevo.penzLevon(vevo.getPenz() - termek.getAr());
            System.out.println("Sikeres vásárlás! Maradék pénz: " + vevo.getPenz());
            termek.applyTo(vevo);
        } else {
            System.out.println("Nincs elég pénzed a vásárláshoz!");
        }
    }

    /**
     * Elad hokotro.
     *
     * @param vevo the vevo
     */
    public void eladHokotro(Hokotro vevo) {
        if (hokotroAr <= vevo.getPenz()) {
            vevo.penzLevon(hokotroAr);
            System.out.println("Sikeres vásárlás! Maradék pénz: " + vevo.getPenz());
        } else {
            System.out.println("Nincs elég pénzed a vásárláshoz!");
        }
    }
}
