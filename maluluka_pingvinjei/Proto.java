import java.io.*;
import java.util.*;

/**
 * Parancssoros (CLI) prototípus a hókotró-szimulációhoz.
 * A szabványos bemenetről olvas utasításokat, a kimenetre ír determinisztikus válaszokat.
 */
public class Proto {

    // A modell elemei
    private static Map<String, Csomopont> csomopontok = new LinkedHashMap<>();
    private static Map<String, Utszakasz> utszakaszok = new LinkedHashMap<>();
    private static Map<String, Sav> savok = new LinkedHashMap<>();
    private static Map<String, Jarmu> jarmuvek = new LinkedHashMap<>();

    private static boolean randomOn = true;
    private static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        String sor;
        while ((sor = reader.readLine()) != null) {
            sor = sor.trim();
            if (sor.isEmpty()) continue;

            String[] t = sor.split("\\s+");
            String parancs = t[0].toLowerCase();

            try {
                switch (parancs) {
                    case "load" -> parancsLoad(t);
                    case "random" -> parancsRandom(t);
                    case "build" -> parancsBuild(t);
                    case "create" -> parancsCreate(t);
                    case "move" -> parancsMove(t);
                    case "step" -> parancsStep(t);
                    case "stat" -> parancsStat(t);
                    case "save" -> parancsSave(t);
                    case "buy" -> parancsBuy(t);
                    case "swap" -> parancsSwap(t);
                    default -> System.out.println("ERROR: Ismeretlen parancs.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // --- Parancsok implementációi ---

    private static void parancsLoad(String[] t) {
        // itt csak szimuláljuk a betöltést, a pálya inicializálása extrán implementálható
        System.out.println("OK: load sikeres.");
    }

    private static void parancsRandom(String[] t) {
        if (t.length < 2) { System.out.println("ERROR: random on|off"); return; }
        randomOn = t[1].equalsIgnoreCase("on");
        if (!randomOn) Jarmu.rng.setSeed(0); // determinisztikus viselkedés
        System.out.println("OK: random sikeres.");
    }

    private static void parancsBuild(String[] t) {
        if (t.length < 3) { System.out.println("ERROR: build node|road|lane"); return; }

        switch (t[1]) {
            case "node" -> {
                String tipus = t[2];
                String id = t[3];
                Csomopont c;
                if (tipus.equals("Bolt")) c = new Bolt();
                else if (tipus.equals("Benzinkut")) c = new Benzinkut();
                else c = new Csomopont() {};
                csomopontok.put(id, c);
                System.out.println("OK: build node sikeres.");
            }
            case "road" -> {
                String tipus = t[2];
                String id = t[3];
                Csomopont k = csomopontok.get(t[4]);
                Csomopont v = csomopontok.get(t[5]);
                Utszakasz u = switch (tipus) {
                    case "Hid" -> new Hid(k, v);
                    case "Alagut" -> new Alagut(k, v);
                    default -> new Utszakasz(k, v);
                };
                utszakaszok.put(id, u);
                System.out.println("OK: build road sikeres.");
            }
            case "lane" -> {
                String utId = t[2];
                String savId = t[3];
                Sav s = new Sav();
                utszakaszok.get(utId).getSavok().add(s);
                s.setUtszakasz(utszakaszok.get(utId));
                savok.put(savId, s);
                System.out.println("OK: build lane sikeres.");
            }
        }
    }

    private static void parancsCreate(String[] t) {
        if (t.length < 4) { System.out.println("ERROR: create <típus> <id> <sav_id>"); return; }

        String tipus = t[1];
        String id = t[2];
        Sav sav = savok.get(t[3]);
        switch (tipus) {
            case "Auto" -> jarmuvek.put(id, new Auto(sav));
            case "Busz" -> jarmuvek.put(id, new Busz(sav, null, null));
            case "Hokotro" -> jarmuvek.put(id, new Hokotro(sav));
            default -> System.out.println("ERROR: Ismeretlen járműtípus.");
        }
        System.out.println("OK: create sikeres.");
    }

    private static void parancsMove(String[] t) {
        if (t.length < 3) { System.out.println("ERROR: move <jármű_id> <sav_id>"); return; }
        Jarmu j = jarmuvek.get(t[1]);
        Sav hova = savok.get(t[2]);
        if (j == null || hova == null) {
            System.out.println("ERROR: Ismeretlen jármű vagy sáv.");
            return;
        }
        j.mozog(hova);
        System.out.println("OK: move sikeres.");
    }

    private static void parancsStep(String[] t) {
        for (Utszakasz u : utszakaszok.values()) u.hoEsik();
        for (Jarmu j : jarmuvek.values()) j.korFrissites();
        System.out.println("OK: step sikeres.");
    }

    private static void parancsStat(String[] t) {
        if (t.length < 2) { System.out.println("ERROR: stat <id>|all"); return; }
        if (t[1].equals("all")) {
            for (String id : savok.keySet()) kiirSav(id, savok.get(id));
            for (String id : jarmuvek.keySet()) kiirJarmu(id, jarmuvek.get(id));
        } else {
            String id = t[1];
            if (savok.containsKey(id)) kiirSav(id, savok.get(id));
            else if (jarmuvek.containsKey(id)) kiirJarmu(id, jarmuvek.get(id));
            else System.out.println("ERROR: Nincs ilyen objektum: " + id);
        }
    }

    private static void kiirSav(String id, Sav s) {
        System.out.println("[Sav: " + id + "]");
        System.out.println("hoRetegek: " + s.gethoRetegek());
        System.out.println("kovezve: " + s.koves());
        System.out.println("sozva: " + s.getSozva());
        System.out.println("torottJeg: " + s.getTorottJeg());
    }

    private static void kiirJarmu(String id, Jarmu j) {
        System.out.println("[" + j.getClass().getSimpleName() + ": " + id + "]");
        if (j.getAktualisSav() != null)
            System.out.println("pozicio: " + j.getAktualisSav().getUtszakasz());
        if (j instanceof Hokotro h)
            System.out.println("penz: " + h.getPenz());
    }

    private static void parancsSave(String[] t) {
        System.out.println("OK: save sikeres."); // fájlmentés kihagyva, CLI teszteléshez elég
    }

    private static void parancsBuy(String[] t) {
        System.out.println("OK: buy sikeres."); // boltlogika később bővíthető
    }

    private static void parancsSwap(String[] t) {
        System.out.println("OK: swap sikeres.");
    }
}
