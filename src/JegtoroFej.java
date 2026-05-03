package src;

/**
 * A Jegtorofej egy speciális Kotrofej, amely egyedi módon interaktál a sávokkal, a rajtuk
 * található jeget feltöri, így feltörtjeget képezve[cite: 331].
 */
public class JegtoroFej extends Kotrofej {

    public JegtoroFej(int ar) {
        super(ar);
    }

    /**
     * Implementálja a Kotrofej azonos metódusát, meghívásakor a paraméterek között szereplő 
     * sav sávon található jeget feltöri[cite: 342].
     * A "7. Jégtörő fej használata" szekvenciadiagram alapján a fej
     * meghívja a sáv jegetTor() metódusát[cite: 650].
     * @param sav A sáv, amin a hatást ki kell fejteni.
     * @param gep A hókotró, amire a fej fel van szerelve.
     */
    @Override
    public void hatasKifejtese(Sav sav, Hokotro gep) {
        sav.jegetTor();
    }
}