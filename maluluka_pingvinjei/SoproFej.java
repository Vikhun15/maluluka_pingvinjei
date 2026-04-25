import java.util.List;

/**
 * A Soprofej egy speciális Kotrofej, amely egyedi módon, söpréssel takarítja le, aminek
 * következtében a feltört jég eltűnik, a hó pedig áttolódik másik sávra.
 */
public class SoproFej extends Kotrofej {

    /**
     * Konstruktor, amely beállítja a söprőfej árát.
     * @param ar A söprőfej ára.
     */
    public SoproFej(int ar) {
        super(ar);
    }

    /**
     * Implementálja a Kotrofej metódusát, meghívásakor a paraméterek között
     * szereplő sav sávon található jég darabokat és/vagy havat elsöpri.
     * @param sav A sáv, amin a hatást ki kell fejteni.
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {
        Utszakasz ut = sav.getUtszakasz();
        if (ut == null) {
            sav.havatTol(null); // ha nincs szomszéd, csak söpri
            return;
        }
        List<Sav> savok = ut.getSavok();
        for (Sav s : savok) {
            if (!s.equals(sav)) {
                sav.havatTol(s);
                return; // csak az első szomszédba tolja
            }
        }
        sav.havatTol(null); // ha nincs szomszéd
    }
}