/**
 * Olyan speciális bolt, amely korlátozott kínálattal rendelkezik: 
 * itt kizárólag sót és biokerozint tudnak vásárolni a játékosok.
 */
public class Benzinkut extends Bolt {

    public Benzinkut() {
        super();
    }

    /**
     * Felülírja a Bolt elad metódusát. Csak akkor engedi a vásárlást, 
     * ha a termék Só (Salt) vagy Üzemanyag (Fuel).
     */
    @Override
    public void elad(ITargy termek, Hokotro gep) {
        
        // Az "instanceof" operátorral megnézzük, milyen típusú a termék
        if (termek instanceof Salt || termek instanceof Fuel) {
            
            // Ha só vagy kerozin, akkor rábízzuk az ősosztályra (Bolt) az eladást
            super.elad(termek, gep);
            
        } else {
            // Ha bármi más (pl. SoproFej, SarkanyFej), akkor elzavarjuk a vevőt
            System.out.println("Benzinkut: Hiba! Itt csak sot es biokerozint arulunk, kotrofejeket nem!");
        }
    }
}