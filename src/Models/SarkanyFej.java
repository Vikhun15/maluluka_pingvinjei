package src.Models;

/**
 * A Sarkanyfej egy speciális Kotrofej, amely egyedi módon, olvasztással,
 * és biokerozin felhasználásával tünteti el a jeget és a havat az útról.
 */
public class SarkanyFej extends Kotrofej {

    /**
     * Konstruktor, amely beállítja a fej árát az ősosztályon keresztül.
     *
     * @param ar a sárkányfej ára PingCoinban
     */
    public SarkanyFej(int ar) {
        super(ar);
    }

    /**
     * Kifejti a hatását: ha van elég üzemanyag a hókotróban, akkor
     * felolvasztja a sávot, és levon 5 liter biokerozint.
     *
     * @param sav a sáv, amit le kell takarítani
     * @param gep a hókotró, amire a fej fel van szerelve
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {

        Uzemanyag uzemanyag = gep.getUzemanyag();

        java.util.Optional.ofNullable(uzemanyag).filter(f -> f.getLiterek() >= 5).ifPresent(f -> {
            sav.olvaszt();
            f.fogyaszt(5);
        });

        if (uzemanyag.getLiterek() < 5) {
            System.out.println("Sikertelen langszoras! Nincs eleg biokerozin a hokotrodban!");
            System.out.println(uzemanyag.getLiterek() + " liter biokerozinod van.");
        }
    }
}