package Models;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

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
        buildTestMap();
        //GenerateMap();
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

    public Csomopont getCsomopontByCoordinates(int x, int y) {
        for (Csomopont cs : csomopontok) {
            if(abs(cs.getX() - x) <= 10 && abs(cs.getY() - y) <= 10){
                 return cs;
            }
        }
        return null;
    }

    /**
     * Egy fix, logikus felépítésű tesztpályát generál a grafikus megjelenítéshez.
     */
    private void buildTestMap() {

        Csomopont center = new Csomopont();
        center.setX(350); center.setY(300);
        center.setEpulet(new Bolt());
        csomopontok.add(center);

        Csomopont north = new Csomopont();
        north.setX(350); north.setY(50);
        north.setEpulet(new Otthon());
        csomopontok.add(north);

        Csomopont south = new Csomopont();
        south.setX(350); south.setY(550);
        south.setEpulet(new Munkahely());
        csomopontok.add(south);

        Csomopont west = new Csomopont();
        west.setX(50); west.setY(300);
        west.setEpulet(new Benzinkut());
        csomopontok.add(west);

        Csomopont east = new Csomopont();
        east.setX(650); east.setY(300);
        csomopontok.add(east);

        Utszakasz utNorth = new Utszakasz(center, north);
        Sav savN1 = new Sav(); savN1.setUtszakasz(utNorth);
        Sav savN2 = new Sav(); savN2.setUtszakasz(utNorth);
        utNorth.getSavok().add(savN1);
        utNorth.getSavok().add(savN2);
        utszakaszok.add(utNorth);

        Utszakasz utSouth = new Utszakasz(center, south);
        Sav savS1 = new Sav(); savS1.setUtszakasz(utSouth);
        utSouth.getSavok().add(savS1);
        utszakaszok.add(utSouth);

        Alagut utWest = new Alagut(center, west);
        Sav savW1 = new Sav(); savW1.setUtszakasz(utWest);
        utWest.getSavok().add(savW1);
        utszakaszok.add(utWest);

        Hid utEast = new Hid(center, east);
        Sav savE1 = new Sav(); savE1.setUtszakasz(utEast);
        Sav savE2 = new Sav(); savE2.setUtszakasz(utEast);
        utEast.getSavok().add(savE1);
        utEast.getSavok().add(savE2);
        utszakaszok.add(utEast);

        Hokotro hk = new Hokotro(savE1);
        hk.setAktualisCsomopont(center);
        hk.setPenz(500);
        jarmuvek.add(hk);
        center.jarmuBefogad(hk);

        Auto auto = new Auto(north.getEpulet(), south.getEpulet());
        auto.setAktualisSav(savN1);
        auto.setAktualisCsomopont(north);
        jarmuvek.add(auto);
        north.jarmuBefogad(auto);
    }
}
