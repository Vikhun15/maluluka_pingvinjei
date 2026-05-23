package Views;
import Models.*;

/**
 * A View osztály a játék grafikus felhasználói felületét kezeli CLI-ben.
 * Az MVC architektúra nézet komponense.
 */
public class View {
    /**
     * Megjeleníti a játék aktuális állapotát.
     *
     * @param palya a pálya, amelynek az állapotát meg kell jeleníteni
     */
    public void display(Palya palya) {
        System.out.println("\n=== Játékállapot ===");
        System.out.println("Csomópontok:");
        for (Csomopont cs : palya.getCsomopontok()) {
            String type = cs.getEpulet() != null ? cs.getEpulet().getClass().getSimpleName() : "Nincs";
            String vehicle = cs.getAktualisJarmu() != null ? cs.getAktualisJarmu().getClass().getSimpleName() + "(" + cs.getAktualisJarmu().getId() + ")" : "Nincs";
            System.out.println("  ID: " + cs.getId() + ", Típus: " + type + ", Jármű: " + vehicle + ", Pozíció: (" + cs.getX() + "," + cs.getY() + ")");
        }

        System.out.println("\nÚtszakaszok:");
        for (Utszakasz ut : palya.getUtszakaszok()) {
            System.out.println("  ID: " + ut.getId() + ", Kezdő: " + ut.getKezdoPont().getId() + ", Vég: " + ut.getVegPont().getId() + ", Típus: " + ut.getClass().getSimpleName() + ", Sávok: " + ut.getSavok().size());
            for (Sav s : ut.getSavok()) {
                System.out.println("    Sáv ID: " + s.getId() + ", Hó: " + s.getHoRetegek() + ", Jeges: " + s.jeges() + ", Sózva: " + s.getSozva());
            }
        }

        System.out.println("\nJárművek:");
        for (Jarmu j : palya.getJarmuvek()) {
            String pos = j.getAktualisSav() != null ? "Sáv a " + j.getAktualisSav().getUtszakasz().getId() + "-n" : "Nincs";
            System.out.println("  ID: " + j.getId() + ", Típus: " + j.getClass().getSimpleName() + ", Pozíció: " + pos);
            if ("Hokotro".equals(j.getClass().getSimpleName())) {
                Hokotro h = (Hokotro) j;
                System.out.println("    Üzemanyag: " + h.getUzemanyag().getLiterek() + ", Só: " + h.getSo().getMennyiseg() + ", Zúzott kő: " + h.getZuzottKo().getMennyiseg() + ", Pénz: " + h.getPenz());
            } else if ("Auto".equals(j.getClass().getSimpleName())) {
                Auto a = (Auto) j;
                String home = a.getOtthon() != null ? a.getOtthon().getId() + "" : "Nincs";
                String work = a.getMunkahely() != null ? a.getMunkahely().getId() + "" : "Nincs";
                System.out.println("    Otthon: " + home + ", Munka: " + work);
            }
        }
        System.out.println();
    }

    /**
     * Megjeleníti az elérhető parancsok menüjét.
     */
    public void showMenu() {
        System.out.println("Elérhető parancsok:");
        System.out.println("  move <vehicle_id> <lane_id>   - Jármű mozgatása a sávra");
        System.out.println("  step                          - Egy lépés előrehaladása (havazás, járműfrissítés)");
        System.out.println("  stat                          - Jelenlegi statisztika");
        System.out.println("  save                          - Játék mentése");
        System.out.println("  load                          - Játék betöltése");
        System.out.println("  test                          - Tesztek futtatása");
        System.out.println("  quit/exit                     - Kilépés a játékból");
    }

    /**
     * Hibaüzenet megjelenítése.
     *
     * @param message a hibaüzenet
     */
    public void showError(String message) {
        System.out.println("HIBA: " + message);
    }

    /**
     * Információs üzenet megjelenítése.
     *
     * @param message az információs üzenet
     */
    public void showMessage(String message) {
        System.out.println("INFO: " + message);
    }
}
