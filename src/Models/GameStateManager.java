package Models;

import java.io.*;
import java.util.*;

public class GameStateManager {

    public void saveGame(Palya palya, File file) {
        if (file == null) {
            file = new File("savegame.dat");
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("[CSOMOPONTOK]");
            for (Csomopont cs : palya.getCsomopontok()) {
                String type = (cs.getEpulet() == null)
                        ? "Csomopont"
                        : cs.getEpulet().getClass().getSimpleName();
                writer.println(cs.id + ";" + cs.getX() + ";" + cs.getY() + ";" + type);
            }

            writer.println("[SZAKASZOK]");
            for (Utszakasz ut : palya.getUtszakaszok()) {
                String tipus = ut.getClass().getSimpleName();
                int savokSzama = ut.getSavok().size();
                writer.println(ut.id + ";" + ut.getKezdoPont().id + ";"
                        + ut.getVegPont().id + ";" + savokSzama + ";" + tipus);
            }

            writer.println("[JARMUVEK]");
            for (Jarmu j : palya.getJarmuvek()) {
                String className = j.getClass().getSimpleName();

                int pozID = -1;
                if (j.getAktualisCsomopont() != null) {
                    pozID = j.getAktualisCsomopont().id;
                } else if (j.getAktualisSav() != null) {
                    pozID = j.getAktualisSav().getUtszakasz().id;
                }

                if (j.getJarmuTipus().equals("Hokotro")) {
                    Hokotro h = (Hokotro) j;
                    int kotrofejID = Optional.ofNullable(h.getAktualisFej()).map(f -> f.id).orElse(-1);
                    int fuel = h.getUzemanyag().getLiterek();
                    int salt = h.getSo().getMennyiseg();
                    int zuzottKo = h.getZuzottKo().getMennyiseg();
                    int penz = h.getPenz();
                    writer.println(className + ";" + j.id + ";" + pozID + ";" + kotrofejID + ";"
                            + fuel + ";" + salt + ";" + zuzottKo + ";" + penz);

                } else if (j.getJarmuTipus().equals("Auto")) {
                    Auto a = (Auto) j;
                    int otthonId = Optional.ofNullable(a.getOtthon()).map(e -> e.id).orElse(-1);
                    int munkahelyId = Optional.ofNullable(a.getMunkahely()).map(e -> e.id).orElse(-1);
                    writer.println(className + ";" + j.id + ";" + pozID + ";" + otthonId + ";" + munkahelyId);

                } else if (j.getJarmuTipus().equals("Busz")) {
                    Busz b = (Busz) j;
                    int kezdoId = Optional.ofNullable(b.getKezdoAllomas()).map(c -> c.id).orElse(-1);
                    int vegId = Optional.ofNullable(b.getVegAllomas()).map(c -> c.id).orElse(-1);
                    writer.println(className + ";" + j.id + ";" + pozID + ";" + kezdoId + ";" + vegId);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Palya loadGame(java.io.File file) {
        ArrayList<Csomopont> csomopontok = new ArrayList<>();
        ArrayList<Utszakasz> utszakaszok = new ArrayList<>();
        ArrayList<Jarmu> jarmuvek = new ArrayList<>();

        java.util.HashMap<Integer, Csomopont> csomopontMap = new java.util.HashMap<>();
        java.util.HashMap<Integer, Utszakasz> utszakaszMap = new java.util.HashMap<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            String currentSection = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("[")) {
                    currentSection = line;
                    continue;
                }

                String[] parts = line.split(";");

                switch (currentSection) {
                    case "[CSOMOPONTOK]":
                        Csomopont cs = new Csomopont();
                        cs.id = Integer.parseInt(parts[0]);
                        cs.setX(Integer.parseInt(parts[1]));
                        cs.setY(Integer.parseInt(parts[2]));
                        String type = parts[3];

                        if (!type.equals("Csomopont")) {
                            switch (type) {
                                case "Munkahely" -> cs.setEpulet(new Munkahely());
                                case "Bolt" -> cs.setEpulet(new Bolt());
                                case "Otthon" -> cs.setEpulet(new Otthon());
                                case "Benzinkut" -> cs.setEpulet(new Benzinkut());
                            }
                        }
                        csomopontok.add(cs);
                        csomopontMap.put(cs.id, cs);
                        break;

                    case "[SZAKASZOK]":
                        int utId = Integer.parseInt(parts[0]);
                        Csomopont kezdo = csomopontMap.get(Integer.parseInt(parts[1]));
                        Csomopont veg = csomopontMap.get(Integer.parseInt(parts[2]));
                        int savokSzama = Integer.parseInt(parts[3]);
                        String utTipus = parts[4];

                        Utszakasz ut = switch (utTipus) {
                            case "Alagut" -> new Alagut(kezdo, veg);
                            case "Hid" -> new Hid(kezdo, veg);
                            default -> new Utszakasz(kezdo, veg);
                        };
                        ut.id = utId;
                        for (int i = 0; i < savokSzama; i++) {
                            ut.getSavok().add(new Sav());
                        }

                        for (Sav s : ut.getSavok()) s.setUtszakasz(ut);

                        utszakaszok.add(ut);
                        utszakaszMap.put(ut.id, ut);
                        break;

                    case "[JARMUVEK]":
                        String jarmuTipus = parts[0];
                        int jarmuId = Integer.parseInt(parts[1]);
                        int pozId = Integer.parseInt(parts[2]);

                        Jarmu ujJarmu = null;

                        if (jarmuTipus.equals("Hokotro")) {
                            Hokotro h = new Hokotro(null);
                            h.getUzemanyag().tankol(Integer.parseInt(parts[4]));
                            h.getSo().setMennyiseg(Integer.parseInt(parts[5]));
                            h.getZuzottKo().setMennyiseg(Integer.parseInt(parts[6]));
                            h.setPenz(Integer.parseInt(parts[7]));
                            ujJarmu = h;

                        } else if (jarmuTipus.equals("Auto")) {
                            int otthonId = Integer.parseInt(parts[3]);
                            int munkahelyId = Integer.parseInt(parts[4]);

                            Epulet otthon = csomopontMap.containsKey(otthonId) ? csomopontMap.get(otthonId).getEpulet() : null;
                            Epulet munkahely = csomopontMap.containsKey(munkahelyId) ? csomopontMap.get(munkahelyId).getEpulet() : null;

                            ujJarmu = new Auto(otthon, munkahely);

                        } else if (jarmuTipus.equals("Busz")) {
                            int kezdoId = Integer.parseInt(parts[3]);
                            int vegId = Integer.parseInt(parts[4]);
                            Csomopont kAllomas = csomopontMap.get(kezdoId);
                            Csomopont vAllomas = csomopontMap.get(vegId);

                            ujJarmu = new Busz(kAllomas, vAllomas);
                        }

                        if (ujJarmu != null) {
                            ujJarmu.id = jarmuId;

                            if (csomopontMap.containsKey(pozId)) {
                                Csomopont csp = csomopontMap.get(pozId);
                                ujJarmu.setAktualisCsomopont(csp);
                                csp.jarmuBefogad(ujJarmu);
                            } else if (utszakaszMap.containsKey(pozId)) {
                                Utszakasz u = utszakaszMap.get(pozId);
                                if (!u.getSavok().isEmpty()) {
                                    ujJarmu.setAktualisSav(u.getSavok().get(0));
                                    ujJarmu.elindul(u.getSavok().get(0), u.getVegPont());
                                }
                            }
                            jarmuvek.add(ujJarmu);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("HIBA: Nem sikerült beolvasni a fájlt!");
            return null;
        }

        Csomopont.nextId = csomopontok.stream().mapToInt(c -> c.id).max().orElse(0) + 1;
        Utszakasz.nextId = utszakaszok.stream().mapToInt(u -> u.id).max().orElse(0) + 1;
        Jarmu.nextId = jarmuvek.stream().mapToInt(j -> j.id).max().orElse(0) + 1;

        return new Palya(csomopontok, utszakaszok, jarmuvek);
    }
}