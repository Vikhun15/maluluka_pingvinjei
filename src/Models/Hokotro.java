package Models;

import java.util.ArrayList;

/**
 * A takarító által vezetett jármű, amely kotrófejekkel takarítja az utat.
 * Magában foglalja a pénzkészletet, a sót és az üzemanyagot.
 */
public class Hokotro extends Jarmu {
    /**
     * A hókotró (vagy vezetője) által birtokolt pénzösszeg.
     */
    private int penz;

    /**
     * A hókotróra szerelt aktuális kotrófej.
     */
    private Kotrofej aktualisFej;

    /**
     * A hókotró sókészlete.
     */
    private final So so;

    private final ArrayList<Kotrofej> birtokoltFejek;

    /**
     * A hókotró üzemanyagkészlete.
     */
    private final Uzemanyag uzemanyag;

    /**
     * A hókotró zúzott kő készlete.
     */
    private final ZuzottKo zuzottKo;

    /**
     * Inicializálja a hókotrót az alapértelmezett erőforrásokkal.
     *
     * @param indul avez sáv, ahol kezdődik
     */
    public Hokotro(Sav indul) {
        super(indul);
        this.penz = 0;
        this.so = new So(10);
        this.uzemanyag = new Uzemanyag(20);
        this.birtokoltFejek = new ArrayList<>();
        this.zuzottKo = new ZuzottKo(10);
        this.setJarmuTipus("Hokotro");
    }

    /**
     * Üres Hokotro konstruktora.
     */
    public Hokotro() {
        super();
        this.penz = 0;
        this.so = new So(10);
        this.uzemanyag = new Uzemanyag(20);
        this.birtokoltFejek = new ArrayList<>();
        this.zuzottKo = new ZuzottKo(10);
        this.setJarmuTipus("Hokotro");
    }

    /**
     * Az aktív kotrófejen meghívja a hatasKifejtese metódust a megadott sávon.
     *
     * @param sav a tisztítandó sáv
     */
    public void takarit(Sav sav) {
        if (aktualisFej == null) {
            System.out.println("Nincs kotrófej felszerelve, nem lehet takarítani!");
            return;
        }

        int hoElotte = sav.getHoRetegek();
        boolean jegElotte = sav.jeges();
        boolean torottJegElotte = sav.getTorottJeg();

        aktualisFej.hatasKifejtese(sav, this);

        int hoUtana = sav.getHoRetegek();
        boolean jegUtana = sav.jeges();
        boolean torottJegUtana = sav.getTorottJeg();

        int eltavolitottHo = Math.max(0, hoElotte - hoUtana);
        boolean eltavolitottJeg = jegElotte && !jegUtana;
        boolean eltavolitottTorottJeg = torottJegElotte && !torottJegUtana;

        int fizetseg = 0;
        fizetseg += eltavolitottHo * 5;
        fizetseg += eltavolitottJeg ? 15 : 0;
        fizetseg += eltavolitottTorottJeg ? 10 : 0;

        if (fizetseg > 0) {
            this.setPenz(this.getPenz() + fizetseg);
            System.out.println("Sikeres takarítás! Kapott jutalom: " + fizetseg + " PingCoin. (Összesen: " + this.getPenz() + ")");
        } else {
            System.out.println("A takarítás nem hozott változást. Nem jár fizetség!");
        }

        notifyObservers();
    }

    /**
     * Ezt hívja meg az új fej, amikor megvásárolják a boltban.
     * Beteszi a listába, és rögtön fel is szereli.
     *
     * @param ujFej az új kotrófej
     */
    public void ujFejetBegyujt(Kotrofej ujFej) {
        this.birtokoltFejek.add(ujFej);
        if(this.aktualisFej == null){
            this.aktualisFej = ujFej;
        }
        System.out.println("-> Uj fej a raktarban. Jelenlegi fejek szama: " + birtokoltFejek.size());
    }

    /**
     * Visszaadja a birtokolt fejek listáját.
     *
     * @return a birtokolt fejek
     */
    public ArrayList<Kotrofej> getBirtokoltFejek() {
        return this.birtokoltFejek;
    }

    /**
     * Kicseréli a hókotró aktív kotrófejét.
     *
     * @param ujFej az új felszerelendő kotrófej
     */
    public void fejetCserel(Kotrofej ujFej) {
        this.aktualisFej = ujFej;
    }


    /**
     * Végrehajtja a jármű mozgását.
     */

    @Override
    public void elindul(Sav hova, Csomopont cel) {
        super.elindul(hova, cel);
    }

    /**
     * Visszaadja a hókotró sójait.
     *
     * @return a só
     */
    public So getSo() {
        return so;
    }

    /**
     * Visszaadja a hókotró üzemanyagát.
     *
     * @return az üzemanyag
     */
    public Uzemanyag getUzemanyag() {
        return uzemanyag;
    }

    /**
     * Visszaadja a hókotró zúzott kő készletét.
     *
     * @return a zúzott kő
     */
    public ZuzottKo getZuzottKo() {
        return zuzottKo;
    }

    /**
     * Visszaadja a hókotró pénzét.
     *
     * @return a pénz mennyisége
     */
    public int getPenz() {
        return penz;
    }

    /**
     * Beállítja a hókotró pénzét.
     *
     * @param penz az új pénz mennyisége
     */
    public void setPenz(int penz) {
        this.penz = penz;
    }

    /**
     * Pénzt szerez a hókotró.
     *
     * @param osszeg a keresett összeg
     */
    public void penzKeres(int osszeg) {
        this.penz += osszeg;
    }

    /**
     * Levonja a pénzt a hókotrótól.
     *
     * @param osszeg a levont összeg
     */
    public void penzLevon(int osszeg) {
        this.penz -= osszeg;
    }

    /**
     * Visszaadja a jelenlegi aktuális fejet.
     *
     * @return az aktuális kotrófej
     */
    public Kotrofej getAktualisFej() {
        return aktualisFej;
    }

    public void setAktualisFej(Kotrofej kf){
        aktualisFej = kf;
    }

    public boolean hasFej(ITargy termek){
        for(Kotrofej fej : birtokoltFejek){
            if(fej.getNev().equals(termek.getNev())){
                return true;
            }
        }
        return false;
    }
}