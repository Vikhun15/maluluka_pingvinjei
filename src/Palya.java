package src;

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

    private void GenerateMap() {

        for (int i = 0; i < MAX_NODES; i++) {
            Csomopont csomopont = new Csomopont();
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

    private Auto generateAuto() {
        Epulet start = null;
        Epulet end = null;

        for (int j = 0; j < csomopontok.size(); j++) {
            if (csomopontok.get(j).getEpulet() instanceof Otthon) {
                start = csomopontok.get(j).getEpulet();
                break;
            }
        }

        for (int j = 0; j < csomopontok.size(); j++) {
            if (csomopontok.get(j).getEpulet() instanceof Munkahely) {
                end = csomopontok.get(j).getEpulet();
                break;
            }
        }

        return new Auto(start, end);
    }
}
