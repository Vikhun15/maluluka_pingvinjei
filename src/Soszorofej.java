package src;

/**
 * A sószóró fej egy olyan hókotróra szerelhető kotrófej, ami sót szór a sávra, így takarítva az utat].
 * Működéséhez elengedhetetlen a só, amelyet a sávra tud szórni.
 */
public class Soszorofej extends Kotrofej {

    public Soszorofej(int ar) {
        super(ar);
    }

    /**
     * Implementálja a Kotrofej metódusát, meghívásakor a paraméterek között
     * szereplő sávot leszórja sóval, ha van a játékosnál.
     * @param sav A sáv, amin a hatást ki kell fejteni.
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {
        
        Salt salt = gep.getSalt();
        int mennyiseg = salt.getMennyiseg();
        
        if (mennyiseg > 0) { 
            salt.csokkent(1); 
            sav.setSozva(true); 
        }
    }
}