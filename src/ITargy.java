package src;

/**
 * Az ITargy interfész biztosítja a különböző megvásárolható tárgyak, mint a só, az üzemanyag vagy a kotrófejek heterogén kollekcióban való tárolását és kezelését.
 */
public interface ITargy {
    /**
     * Visszaadja a tárgy vételárát.
     *
     * @return A tárgy ára.
     */
    int getAr();

    /**
     * Apply to.
     *
     * @param gep the gep
     */
    void applyTo(Hokotro gep);
}