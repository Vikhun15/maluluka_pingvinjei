package Models;

/**
 * The type Koszoro fej.
 */
/*
 * A kőszórófej egy olyan hókotróra szerelhető kotrófej, ami zúzott követ szór a sávra, így takarítva az utat.
 * Működéséhez elengedhetetlen a kő, amelyet a sávra tud szórni.
 */
public class KoszoroFej extends Kotrofej {

    /**
     * Instantiates a new Koszoro fej.
     *
     * @param ar the ar
     */
    public KoszoroFej(int ar) {
        super(ar);
        setNev("Kőszóró fej");
    }

    /**
     * Implementálja a Kotrofej metódusát, meghívásakor a paraméterek között
     * szereplő sávot leszórja zúzott kővel, ha van a játékosnál.
     *
     * @param sav A sáv, amin a hatást ki kell fejteni.
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {

        ZuzottKo ko = gep.getZuzottKo();
        int mennyiseg = ko.getMennyiseg();

        if (mennyiseg > 0) {
            ko.csokkent(1);
            sav.setKo(true);
        }
    }


    @Override
    public ITargy masol(){
        return new KoszoroFej(this.ar);
    }
}
