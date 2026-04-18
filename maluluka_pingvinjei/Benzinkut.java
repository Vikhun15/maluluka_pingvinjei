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
        
        if (termek instanceof Salt || termek instanceof Fuel || termek instanceof Rock) {
            
            super.elad(termek, gep);
            
        } else {
            //hiba
        }
    }
}