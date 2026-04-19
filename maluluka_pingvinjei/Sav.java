/**
 * A Sav osztályon keresztül tudnak eljutni a járművek az egyik csomópontból a másikba.
 * Nyilvántartja a hórétegeket, a jeget, és kezeli a forgalom okozta jegesedést.
 */
public class Sav {
    private int hoRetegek = 0;
    private boolean jeg = false; 
    private int athaladtAutok = 0;
    private boolean sozva = false; 
    private boolean torottJeg = false;
    private boolean jarhato = true;
    private Utszakasz szuloUtszakasz;
    private int hoRetegKovon = 0;
    private boolean kovezve = false;

    /**
     * Növeli az áthaladt autók számát. Ha eléri a 4-et, a sáv jegessé válik.
     */
    public void jarmuAthalad() {
        athaladtAutok++;
        if (athaladtAutok >= 4) { 
            jeg = true; 
            athaladtAutok = 0; 
        }
    }

    /**
     * Havazáskor növeli a hórétegek számát,
     * illetve a zúzott kőre esett hórétegek számát is.
     * Ha az utóbbi eléri a hármat, akkor a kő hatása megszűnik.
     */
    public void hoEsik() {
        if(hoRetegek >=8){
            jarhato = false;
        }
        hoRetegek++;
        if(kovezve){
            hoRetegKovon++;
        }
        if(sozva){
            hoRetegek -= 2;
            if(hoRetegek < 0){
                hoRetegek = 0;
            }
        }
        if(sozva && kovezve){
            hoRetegKovon -= 2;
            if(hoRetegKovon < 0){
                hoRetegKovon = 0;
            }
        }
        if(hoRetegKovon >= 3){
            hoRetegKovon = 0;
            kovezve = false;
        }
    }

    public void setUtszakasz(Utszakasz ut) {
        this.szuloUtszakasz = ut;
    }

    public Utszakasz getUtszakasz() {
        return this.szuloUtszakasz;
    }

    public boolean jeges(){return jeg;}

    /**
     * A jégtörő fej hatására a jégpáncél feltörik.
     */
    public void jegetTor() {
        jeg = false; 
        torottJeg = true; 
    }

    /**
     * Söpréskor vagy hóhányáskor a hó és a zúzott kő eltűnik a sávról.
     * @param hova A szomszédos sáv, ahova a hó átkerül.
     */
    public void havatTol(Sav hova) {
        this.hoRetegek = 0;
        if(kovezve && hova != null){
            hova.setKo(true);
        }
        kovezve = false;
        if (hova != null) hova.hoEsik(); 
    }

    /**
     * Felolvasztja a havat és a jeget a sávról.
     */
    public void olvaszt() {
        hoRetegek = 0; 
        jeg = false; 
        torottJeg = false;
    }

     /**
     * Felolvasztja a havat és a jeget a sávról.
     */
    public void sopor() {
        hoRetegek = 0; 
        kovezve = false;
        torottJeg = false;
    }

    //* Állapot beállítása */
    public void setSozva(boolean s) { this.sozva = s; }
    public boolean getSozva() { return sozva; }
    public void setJarhato(boolean jarhato){
        this.jarhato = jarhato;
    }
    public boolean getJarhato(){
        return jarhato;
    }
    public boolean getTorottJeg(){
        return torottJeg;
    }
    public void setTorottJeg(boolean torottJeg){
        this.torottJeg = torottJeg;
    }
    public int gethoRetegek(){
        return hoRetegek;
    }
    public boolean koves(){
        return kovezve;
    }
    public void setKo(boolean kovezve){
        this.kovezve = kovezve;
    }
}