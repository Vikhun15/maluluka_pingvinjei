package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Olyan speciális bolt, amely korlátozott kínálattal rendelkezik:
 * itt kizárólag sót és biokerozint tudnak vásárolni a játékosok.
 */
public class Benzinkut extends Bolt {

    /**
     * Benzinkut konstruktora.
     */
    public Benzinkut() {
        super();
    }

    /**
     * Felülírja a Bolt elad metódusát. Csak akkor engedi a vásárlást,
     * ha a termék Só (So) vagy Üzemanyag (Uzemanyag).
     * 
     * @param termek az értékesítendő termék
     * @param gep a hókotró, amely a terméket megvásárol
     */
    @Override
    public boolean elad(ITargy termek, Hokotro gep) {

        String className = termek.getClass().getSimpleName();
        if ("So".equals(className) || "Uzemanyag".equals(className) || "ZuzottKo".equals(className)) {
            return super.elad(termek, gep);
        }
        return false;
    }

    /**
     * Visszaadja a benzinkút szűkített kínálatát.
     * Itt nincsenek kotrófejek, csak a működéshez szükséges alapanyagok.
     */
    @Override
    public List<ITargy> getKinalat() {
        List<ITargy> kinalat = new ArrayList<>();

        kinalat.add(new So(30));
        kinalat.add(new Uzemanyag(20));
        kinalat.add(new ZuzottKo(30));

        return kinalat;
    }
}