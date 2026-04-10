/*
 * A kőszórófej egy olyan hókotróra szerelhető kotrófej, ami zúzott követ szór a sávra, így takarítva az utat.
 * Működéséhez elengedhetetlen a kő, amelyet a sávra tud szórni.
*/
public class KoszoroFej extends Kotrofej {

    public KoszoroFej(int ar) {
        super(ar);
    }

    /**
     * Implementálja a Kotrofej metódusát, meghívásakor a paraméterek között
     * szereplő sávot leszórja zúzott kővel, ha van a játékosnál.
     * @param sav A sáv, amin a hatást ki kell fejteni.
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {
        
        Rock rock = gep.getRock();
        int mennyiseg = rock.getMennyiseg();
        
        if (mennyiseg > 0) { 
            rock.csokkent(1); 
            sav.setKo(true); 
        }
    }
}
