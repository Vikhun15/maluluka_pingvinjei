package src.Models;

import java.util.ArrayList;
import java.util.Random;

/**
 * The type Palya.
 */
public class Palya {

    private final int MAX_NODES = 20;
    private final int MAX_CARS = 5;

    private final ArrayList<Csomopont> csomopontok;
    private final ArrayList<Utszakasz> utszakaszok;
    private final ArrayList<Jarmu> jarmuvek;
    private final Random random = new Random();

    /**
     * Instantiates a new Palya.
     */
    public Palya() {
        csomopontok = new ArrayList<>();
        utszakaszok = new ArrayList<>();
        jarmuvek = new ArrayList<>();
        GenerateMap();
    }

    /**
     * Instantiates a new Palya for loading.
     *
     * @param csomopontok the csomopontok
     * @param utszakaszok the utszakaszok
     * @param jarmuvek    the jarmuvek
     */
    public Palya(ArrayList<Csomopont> csomopontok, ArrayList<Utszakasz> utszakaszok, ArrayList<Jarmu> jarmuvek) {
        this.csomopontok = csomopontok;
        this.utszakaszok = utszakaszok;
        this.jarmuvek = jarmuvek;
    }

    private void GenerateMap() {

        for (int i = 0; i < MAX_NODES; i++) {
            Csomopont csomopont = new Csomopont();
            csomopont.setX(random.nextInt(1000)); // Random x between 0-999
            csomopont.setY(random.nextInt(1000)); // Random y between 0-999
            if (random.nextBoolean()) {
                int type = random.nextInt(3);
                switch (type) {
                    case 0:
                        csomopont.setEpulet(new Munkahely());
                        break;
                    case 1:
                        csomopont.setEpulet(new Bolt());
                        break;
                    case 2:
                        csomopont.setEpulet(new Otthon());
                        break;
                    default:
                        break;
                }
            }
            csomopontok.add(csomopont);
        }

        for (int i = 0; i < MAX_NODES; i++) {
            for (int j = i + 1; j < MAX_NODES; j++) {

                int type = random.nextInt(10);
                switch (type) {
                    case 0, 1:
                        Utszakasz savosUtszakasz = new Utszakasz(csomopontok.get(i), csomopontok.get(j));
                        savosUtszakasz.getSavok().add(new Sav());
                        savosUtszakasz.getSavok().add(new Sav());
                        utszakaszok.add(savosUtszakasz);
                        break;
                    case 2:
                        Utszakasz savosUtszakasz2 = new Utszakasz(csomopontok.get(i), csomopontok.get(j));
                        savosUtszakasz2.getSavok().add(new Sav());
                        savosUtszakasz2.getSavok().add(new Sav());
                        savosUtszakasz2.getSavok().add(new Sav());
                        utszakaszok.add(savosUtszakasz2);
                        break;
                    case 3:
                        Alagut alagut = new Alagut(csomopontok.get(i), csomopontok.get(j));
                        alagut.getSavok().add(new Sav());
                        utszakaszok.add(alagut);
                        break;
                    case 4:
                        Hid hid = new Hid(csomopontok.get(i), csomopontok.get(j));
                        hid.getSavok().add(new Sav());
                        utszakaszok.add(hid);
                        break;
                    default:
                        Utszakasz egySavosUtszakasz = new Utszakasz(csomopontok.get(i), csomopontok.get(j));
                        egySavosUtszakasz.getSavok().add(new Sav());
                        utszakaszok.add(egySavosUtszakasz);
                        break;
                }
                Utszakasz utszakasz = new Utszakasz(csomopontok.get(i), csomopontok.get(j));
                utszakaszok.add(utszakasz);
            }
        }

        for (int i = 0; i < MAX_CARS; i++) {
            Auto auto = generateAuto();
            jarmuvek.add(auto);
        }


    }
    /**
     * Egy Auto járművet generál otthon és munkahelypontokkal.
     *
     * @return az új Auto jármű
     */
    private Auto generateAuto() {
        Epulet start = csomopontok.stream()
                .map(Csomopont::getEpulet)
                .filter(java.util.Objects::nonNull)
                .filter(e -> "Otthon".equals(e.getClass().getSimpleName()))
                .findFirst()
                .orElse(null);

        Epulet end = csomopontok.stream()
                .map(Csomopont::getEpulet)
                .filter(java.util.Objects::nonNull)
                .filter(e -> "Munkahely".equals(e.getClass().getSimpleName()))
                .findFirst()
                .orElse(null);

        return new Auto(start, end);
    }

    /**
     * Visszaadja a csomópontok listáját.
     *
     * @return a csomópontok
     */
    public ArrayList<Csomopont> getCsomopontok() {
        return csomopontok;
    }

    /**
     * Visszaadja az útszakaszok listáját.
     *
     * @return az útszakaszok
     */
    public ArrayList<Utszakasz> getUtszakaszok() {
        return utszakaszok;
    }

    /**
     * Visszaadja a járművek listáját.
     *
     * @return a járművek
     */
    public ArrayList<Jarmu> getJarmuvek() {
        return jarmuvek;
    }
}
