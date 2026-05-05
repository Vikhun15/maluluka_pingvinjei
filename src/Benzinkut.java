package src;

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
    public void elad(ITargy termek, Hokotro gep) {

        String className = termek.getClass().getSimpleName();
        if ("So".equals(className) || "Uzemanyag".equals(className) || "ZuzottKo".equals(className)) {
            super.elad(termek, gep);
        }
    }
}