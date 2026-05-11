package src.Models;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The type Game state manager.
 */
//Ez az osztály felel a persistence-ért
public class GameStateManager {

        /**
        * Save game.
        *
        * @param palya the palya
        */
        public void saveGame(Palya palya) {
            // Save csomopontok.txt
            try (java.io.PrintWriter writer = new java.io.PrintWriter("csomopontok.txt")) {
                for (Csomopont cs : palya.getCsomopontok()) {
                    String type = Optional.ofNullable(cs.getEpulet()).map(e -> e.getClass().getSimpleName()).orElse("Csomopont");
                    writer.println(cs.id + ";" + cs.getX() + ";" + cs.getY() + ";" + type);
                }
            } catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
            }

            // Save szakaszok.txt
            try (java.io.PrintWriter writer = new java.io.PrintWriter("szakaszok.txt")) {
                for (Utszakasz ut : palya.getUtszakaszok()) {
                    String tipus = ut.getClass().getSimpleName();
                    int savokSzama = ut.getSavok().size();
                    writer.println(ut.id + ";" + ut.kezdoCsomopont.id + ";" + ut.vegCsomopont.id + ";" + savokSzama + ";" + tipus);
                }
            } catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
            }

            // Save auto.txt and hokotro.txt
            try (java.io.PrintWriter autoWriter = new java.io.PrintWriter("auto.txt");
                 java.io.PrintWriter hokotroWriter = new java.io.PrintWriter("hokotro.txt")) {
                for (Jarmu j : palya.getJarmuvek()) {
                    Optional<Sav> savOpt = Optional.ofNullable(j.getAktualisSav());
                    int pozID = savOpt.map(s -> s.getUtszakasz().getVegPont().id).orElse(-1);
                    String className = j.getClass().getSimpleName();
                    if ("Hokotro".equals(className)) {
                        Hokotro h = (Hokotro) j;
                        int kotrofejekID = Optional.ofNullable(h.getAktualisFej()).map(f -> f.id).orElse(-1);
                        int fuel = h.getUzemanyag().getLiterek();
                        int salt = h.getSo().getMennyiseg();
                        int zuzottKo = h.getZuzottKo().getMennyiseg();
                        int penz = h.getPenz();
                        hokotroWriter.println(j.id + ";" + pozID + ";" + kotrofejekID + ";" + fuel + ";" + salt + ";" + zuzottKo + ";" + penz);
                    } else if ("Auto".equals(className)) {
                        Auto a = (Auto) j;
                        int otthonId = Optional.ofNullable(a.getOtthon()).map(e -> e.id).orElse(-1);
                        int munkahelyId = Optional.ofNullable(a.getMunkahely()).map(e -> e.id).orElse(-1);
                        autoWriter.println(j.id + ";" + pozID + ";" + otthonId + ";" + munkahelyId);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
        * Load game palya.
        *
        * @return the palya
        */
        public Palya loadGame() {
            ArrayList<Csomopont> csomopontok = new ArrayList<>();
            ArrayList<Utszakasz> utszakaszok = new ArrayList<>();
            ArrayList<Jarmu> jarmuvek = new ArrayList<>();
            java.util.HashMap<Integer, Csomopont> csomopontMap = new java.util.HashMap<>();
            java.util.HashMap<Integer, Utszakasz> utszakaszMap = new java.util.HashMap<>();
            java.util.HashMap<Integer, Jarmu> jarmuMap = new java.util.HashMap<>();

            // Load csomopontok
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("csomopontok.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    int id = Integer.parseInt(parts[0]);
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String type = parts[3];
                    Csomopont cs = new Csomopont();
                    cs.id = id;
                    cs.setX(x);
                    cs.setY(y);
                    if (!type.equals("Csomopont")) {
                        switch (type) {
                            case "Munkahely":
                                cs.setEpulet(new Munkahely());
                                break;
                            case "Bolt":
                                cs.setEpulet(new Bolt());
                                break;
                            case "Otthon":
                                cs.setEpulet(new Otthon());
                                break;
                        }
                    }
                    csomopontok.add(cs);
                    csomopontMap.put(id, cs);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return new Palya(); // fallback
            }

            // Update nextId for Csomopont
            Csomopont.nextId = csomopontok.stream().mapToInt(cs -> cs.id).max().orElse(0) + 1;

            // Load szakaszok
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("szakaszok.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    int id = Integer.parseInt(parts[0]);
                    int kezdoId = Integer.parseInt(parts[1]);
                    int vegId = Integer.parseInt(parts[2]);
                    int savokSzama = Integer.parseInt(parts[3]);
                    String tipus = parts[4];
                    Csomopont kezdo = csomopontMap.get(kezdoId);
                    Csomopont veg = csomopontMap.get(vegId);
                    Utszakasz ut;
                    switch (tipus) {
                        case "Alagut":
                            ut = new Alagut(kezdo, veg);
                            break;
                        case "Hid":
                            ut = new Hid(kezdo, veg);
                            break;
                        default:
                            ut = new Utszakasz(kezdo, veg);
                            break;
                    }
                    ut.id = id;
                    for (int i = 0; i < savokSzama; i++) {
                        ut.getSavok().add(new Sav());
                    }
                    utszakaszok.add(ut);
                    utszakaszMap.put(id, ut);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return new Palya();
            }

            // Update nextId for Utszakasz
            Utszakasz.nextId = utszakaszok.stream().mapToInt(ut -> ut.id).max().orElse(0) + 1;

            // Load autos
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("auto.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    int id = Integer.parseInt(parts[0]);
                    int pozID = Integer.parseInt(parts[1]);
                    int otthonId = Integer.parseInt(parts[2]);
                    int munkahelyId = Integer.parseInt(parts[3]);
                    Optional<Epulet> otthonOpt = Optional.of(otthonId)
                            .filter(i -> i != -1)
                            .map(csomopontMap::get)
                            .map(Csomopont::getEpulet);
                    Optional<Epulet> munkahelyOpt = Optional.of(munkahelyId)
                            .filter(i -> i != -1)
                            .map(csomopontMap::get)
                            .map(Csomopont::getEpulet);
                    Auto a = new Auto(otthonOpt.orElse(null), munkahelyOpt.orElse(null));
                    a.id = id;
                    // Set position
                    Optional.of(pozID).filter(i -> i != -1).ifPresent(i -> {
                        Csomopont cs = csomopontMap.get(i);
                        for (Utszakasz ut : utszakaszok) {
                            if (ut.getVegPont() == cs && !ut.getSavok().isEmpty()) {
                                a.setAktualisSav(ut.getSavok().get(0));
                                break;
                            }
                        }
                    });
                    jarmuvek.add(a);
                }
            } catch (java.io.IOException e) {
                // Ignore if file not found
            }

            // Load hokotrok
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("hokotro.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    int id = Integer.parseInt(parts[0]);
                    int pozID = Integer.parseInt(parts[1]);
                    int kotrofejekID = Integer.parseInt(parts[2]);
                    int fuel = Integer.parseInt(parts[3]);
                    int salt = Integer.parseInt(parts[4]);
                    int zuzottKo = Integer.parseInt(parts[5]);
                    int penz = Integer.parseInt(parts[6]);
                    Hokotro h = new Hokotro();
                    h.id = id;
                    // Set fuel
                    int currentFuel = h.getUzemanyag().getLiterek();
                    Optional.of(fuel).filter(f -> f > currentFuel).ifPresent(f -> h.getUzemanyag().tankol(f - currentFuel));
                    Optional.of(fuel).filter(f -> f < currentFuel).ifPresent(f -> h.getUzemanyag().fogyaszt(currentFuel - f));
                    h.getSo().setMennyiseg(salt);
                    h.getZuzottKo().setMennyiseg(zuzottKo);
                    h.setPenz(penz);
                    // TODO: set aktualisFej if kotrofejekID != -1
                    // Set position
                    Optional.of(pozID).filter(i -> i != -1).ifPresent(i -> {
                        Csomopont cs = csomopontMap.get(i);
                        for (Utszakasz ut : utszakaszok) {
                            if (ut.getVegPont() == cs && !ut.getSavok().isEmpty()) {
                                h.setAktualisSav(ut.getSavok().get(0));
                                break;
                            }
                        }
                    });
                    jarmuvek.add(h);
                }
            } catch (java.io.IOException e) {
                // Ignore if file not found
            }

            // Update nextId for Jarmu
            Jarmu.nextId = jarmuvek.stream().mapToInt(j -> j.id).max().orElse(0) + 1;

            return new Palya(csomopontok, utszakaszok, jarmuvek);
        }
}
