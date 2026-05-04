package src;

/**
 * A Sarkanyfej egy speciális Kotrofej, amely egyedi módon, olvasztással,
 * és biokerozin felhasználásával tünteti el a jeget és a havat az útról.
 */
public class SarkanyFej extends Kotrofej {

    /**
     * Konstruktor, amely beállítja a fej árát az ősosztályon keresztül.
     *
     * @param ar A sárkányfej ára PingCoinban.
     */
    public SarkanyFej(int ar) {
        super(ar);
    }

    /**
     * Kifejti a hatását: ha van elég üzemanyag a hókotróban, akkor
     * felolvasztja a sávot, és levon 5 liter biokerozint.
     * * @param sav A sáv, amit le kell takarítani.
     *
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {

        // 1. Elkérjük a hókotrótól az üzemanyagtartályt
        Fuel fuel = gep.getUzemanyag();

        // 2. Megnézzük, van-e benne legalább 5 liter
        if (fuel != null && fuel.getLiterek() >= 5) {

            // 3. Ha van elég, felolvasztjuk a sávot (eltűnik a hó és a jég)
            sav.olvaszt();

            // 4. Levonjuk a felhasznált 5 liter kerozint
            fuel.fogyaszt(5);

        } else if (fuel.getLiterek() < 5) {
            System.out.println("Sikertelen langszoras! Nincs eleg biokerozin a hokotrodban!");
            System.out.println(fuel.getLiterek() + " liter biokerozinod van.");
        }
    }
}