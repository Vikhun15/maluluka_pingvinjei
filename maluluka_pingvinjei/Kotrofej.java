/**
 * A Kotrofej egy absztrakt ősosztály, amiből leszármazik az összes kotrófej.
 * Mivel megvalósítja az ITargy interfészt, megvásárolható a boltban.
 */

public abstract class Kotrofej implements ITargy {
    
    /**
     *  Egyértelműen azonosítja a kotrófejet.
     */
    protected int id;
    
    /** 
     *  Meghatározza a Kotrófej árát. 
     */
    protected int ar;

    /*
     * A következő id azonosító.
     */
    static int nextId = 0;

    /**
     * Létrehozza a Kotrofej objektumot a megadott id és ár alapján.
     * @param id
     * @param ar
     */
    Kotrofej(int ar) {
        id = nextId++;
        this.ar = ar;
    }  

    /**
     * Egy implementálandó metódust biztosít a belőle leszármaztatott osztályoknak,
     * amelyek így a saját, egyedi, képességeik szerint képesek kifejteni a hatásukat 
     * az adott sávon.
     * @param sav A sáv, amin a hatást ki kell fejteni (pl. havat tolni, jeget törni).
     * @param gep A hókotró, amire a fej fel van szerelve. Ezen keresztül fér hozzá a sóhoz vagy az üzemanyaghoz.
     */
    public abstract void hatasKifejtese(Sav sav, Hokotro gep);

    /**
     * Implementálja az ITargy metódusát, visszaadja az ar attribútum értékét.
     * @return A kotrófej ára.
     */
    @Override
    public int getAr() {
        return this.ar;
    }
}