package Models;

import java.util.ArrayList;
import java.util.List;

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
    public boolean elad(ITargy termek, Hokotro vevo) {
        if (termek.getAr() <= vevo.getPenz()) {
            vevo.penzLevon(vevo.getPenz() - termek.getAr());
            System.out.println("Sikeres vásárlás! Maradék pénz: " + vevo.getPenz());
            termek.applyTo(vevo);
            return true;
        } else {
            System.out.println("Nincs elég pénzed a vásárláshoz!");
            return false;
        }
    }

    /**
     * Visszaadja a normál bolt teljes kínálatát.
     * Minden híváskor friss példányokat adunk vissza, hogy a vásárlás során
     * független objektumok kerüljenek a hókotróhoz.
     */
    public List<ITargy> getKinalat() {
        List<ITargy> kinalat = new ArrayList<>();

        // Alapanyagok
        kinalat.add(new So(30));
        kinalat.add(new Uzemanyag(50));
        kinalat.add(new ZuzottKo(25));

        // Kotrófejek
        kinalat.add(new SarkanyFej(100));
        kinalat.add(new HanyoFej(110));
        kinalat.add(new JegtoroFej(120));
        kinalat.add(new KoszoroFej(130));
        kinalat.add(new SoproFej(140));
        kinalat.add(new Soszorofej(150));

        return kinalat;
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

    @Override
    public Bolt asBolt() {
        return this;
    }
}
