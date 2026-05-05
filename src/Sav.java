package src;

/**
 * A Sav osztályon keresztül tudnak eljutni a járművek az egyik csomópontból a másikba.
 * Nyilvántartja a hórétegeket, a jeget, és kezeli a forgalom okozta jegesedést.
 */
public class Sav {
    protected int id;
    protected static int nextId = 0;

    private int hoRetegek = 0;
    private boolean jeg = false;
    private int athaladtAutok = 0;
    private boolean sozva = false;
    private boolean torottJeg = false;
    private boolean jarhato = true;
    private Utszakasz szuloUtszakasz;
    private int hoRetegKovon = 0;
    private boolean kovezve = false;

    public Sav() {
        this.id = nextId++;
    }

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
        hoRetegek++;
        if (kovezve) hoRetegKovon++;
        if (sozva) {
            hoRetegek -= 2;
            if (hoRetegek < 0) hoRetegek = 0;
            if (kovezve) {
                hoRetegKovon -= 2;
                if (hoRetegKovon < 0) hoRetegKovon = 0;
            }
        }

        jarhato = (hoRetegek < 8);
        if (hoRetegKovon >= 3) {
            hoRetegKovon = 0;
            kovezve = false;
        }
    }

    /**
     * Sets utszakasz.
     *
     * @param ut the ut
     */
    public void setUtszakasz(Utszakasz ut) {
        this.szuloUtszakasz = ut;
    }

    /**
     * Gets utszakasz.
     *
     * @return the utszakasz
     */
    public Utszakasz getUtszakasz() {
        return this.szuloUtszakasz;
    }

    /**
     * Jeges boolean.
     *
     * @return the boolean
     */
    public boolean jeges() {
        return jeg;
    }

    /**
     * A jégtörő fej hatására a jégpáncél feltörik.
     */
    public void jegetTor() {
        jeg = false;
        torottJeg = true;
    }

    /**
     * Söpréskor vagy hóhányáskor a hó és a zúzott kő eltűnik a sávról.
     *
     * @param hova A szomszédos sáv, ahova a hó átkerül.
     */
    public void havatTol(Sav hova) {
        this.hoRetegek = 0;

        java.util.Optional.ofNullable(hova).ifPresent(h -> {
            h.hoEsik();
            if (kovezve) {
                h.setKo(true);
            }
        });

        kovezve = false;
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

    /**
     * Sets sozva.
     *
     * @param s the s
     */
//* Állapot beállítása */
    public void setSozva(boolean s) {
        this.sozva = s;
    }

    /**
     * Gets sozva.
     *
     * @return the sozva
     */
    public boolean getSozva() {
        return sozva;
    }

    /**
     * Sets jarhato.
     *
     * @param jarhato the jarhato
     */
    public void setJarhato(boolean jarhato) {
        this.jarhato = jarhato;
    }

    /**
     * Gets jarhato.
     *
     * @return the jarhato
     */
    public boolean getJarhato() {
        return jarhato;
    }

    /**
     * Gets torott jeg.
     *
     * @return the torott jeg
     */
    public boolean getTorottJeg() {
        return torottJeg;
    }

    /**
     * Sets torott jeg.
     *
     * @param torottJeg the torott jeg
     */
    public void setTorottJeg(boolean torottJeg) {
        this.torottJeg = torottJeg;
    }

    /**
     * Gets retegek.
     *
     * @return the retegek
     */
    public int gethoRetegek() {
        return hoRetegek;
    }

    /**
     * Koves boolean.
     *
     * @return the boolean
     */
    public boolean koves() {
        return kovezve;
    }

    /**
     * Sets ko.
     *
     * @param kovezve the kovezve
     */
    public void setKo(boolean kovezve) {
        this.kovezve = kovezve;
    }
}