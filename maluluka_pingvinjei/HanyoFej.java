/**
 * A HanyoFej egy Kotrofej, amely a sávon lévő havat és a feltört jeget az út mellé szórja,
 * ezáltal megtisztítva az adott sávot anélkül, hogy a szomszédos sávba tolná azt.
 */
public class HanyoFej extends Kotrofej {

    public HanyoFej(int ar) {
        super(ar);
    }

    /**
     * Kifejti a hányófej specifikus hatását a paraméterben kapott sávon.
     * @param sav A sáv, amin a hatást ki kell fejteni.
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {
        // ha null akkor nincsen szomszédos sáv
        sav.havatTol(null); 
    }
}