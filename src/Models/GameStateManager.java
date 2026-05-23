package Models;

import java.io.*;
import java.util.*;

public class GameStateManager {

    public void saveGame(Palya palya, File file) {

        if(file == null){
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
                writer.println(ut.id + ";" + ut.kezdoCsomopont.id + ";"
                        + ut.vegCsomopont.id + ";" + savokSzama + ";" + tipus);
            }

            writer.println("[JARMUVEK]");
            for (Jarmu j : palya.getJarmuvek()) {
                Optional<Sav> savOpt = Optional.ofNullable(j.getAktualisSav());
                int pozID = savOpt.map(s -> s.getUtszakasz().getVegPont().id).orElse(-1);
                String className = j.getClass().getSimpleName();

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
                }else if (j.getJarmuTipus().equals("Busz")) {
                    Busz a = (Busz) j;
                    int kezdoId = Optional.ofNullable(a.getKezdoAllomas()).map(c -> c.id).orElse(-1);
                    int vegId = Optional.ofNullable(a.getVegAllomas()).map(c -> c.id).orElse(-1);
                    writer.println(className + ";" + j.id + ";" + pozID + ";" + kezdoId + ";" + vegId);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Palya loadGame(File file) {
        ArrayList<Csomopont> csomopontok = new ArrayList<>();
        ArrayList<Utszakasz> utszakaszok = new ArrayList<>();
        ArrayList<Jarmu> jarmuvek = new ArrayList<>();
        Map<Integer, Csomopont> csomopontMap = new HashMap<>();
        Map<Integer, Utszakasz> utszakaszMap = new HashMap<>();

        String currentSection = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }

                String[] parts = line.split(";");
                switch (currentSection) {
                    case "CSOMOPONTOK":
                        int id = Integer.parseInt(parts[0]);
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        String type = parts[3];
                        Csomopont cs = new Csomopont();
                        cs.id = id;
                        cs.setX(x);
                        cs.setY(y);
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
                        csomopontok.add(cs);
                        csomopontMap.put(id, cs);
                        Csomopont.nextId = Math.max(Csomopont.nextId, id + 1);
                        break;

                    case "SZAKASZOK":
                        int utId = Integer.parseInt(parts[0]);
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
                        }
                        ut.id = utId;
                        for (int i = 0; i < savokSzama; i++) {
                            ut.getSavok().add(new Sav());
                        }
                        utszakaszok.add(ut);
                        utszakaszMap.put(utId, ut);
                        Utszakasz.nextId = Math.max(Utszakasz.nextId, utId + 1);
                        break;

                    case "JARMUVEK":
                        String jarmuType = parts[0];
                        int jId = Integer.parseInt(parts[1]);
                        int pozID = Integer.parseInt(parts[2]);

                        if ("Hokotro".equals(jarmuType)) {
                            int kotrofejID = Integer.parseInt(parts[3]);
                            int fuel = Integer.parseInt(parts[4]);
                            int salt = Integer.parseInt(parts[5]);
                            int zuzottKo = Integer.parseInt(parts[6]);
                            int penz = Integer.parseInt(parts[7]);

                            Hokotro h = new Hokotro();
                            h.id = jId;
                            h.getUzemanyag().setLiterek(fuel);
                            h.getSo().setMennyiseg(salt);
                            h.getZuzottKo().setMennyiseg(zuzottKo);
                            h.setPenz(penz);

                            setVehiclePosition(h, pozID, csomopontMap, utszakaszok);
                            jarmuvek.add(h);
                        } else if ("Auto".equals(jarmuType)) {
                            int otthonId = Integer.parseInt(parts[3]);
                            int munkahelyId = Integer.parseInt(parts[4]);

                            Epulet otthon = (otthonId != -1)
                                    ? csomopontMap.get(otthonId).getEpulet()
                                    : null;
                            Epulet munkahely = (munkahelyId != -1)
                                    ? csomopontMap.get(munkahelyId).getEpulet()
                                    : null;
                            Auto a = new Auto(otthon, munkahely);
                            a.id = jId;
                            setVehiclePosition(a, pozID, csomopontMap, utszakaszok);
                            jarmuvek.add(a);
                        }
                        Jarmu.nextId = Math.max(Jarmu.nextId, jId + 1);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Palya();
        }

        return new Palya(csomopontok, utszakaszok, jarmuvek);
    }

    private void setVehiclePosition(Jarmu jarmu, int pozID,
                                    Map<Integer, Csomopont> csomopontMap,
                                    ArrayList<Utszakasz> utszakaszok) {
        if (pozID != -1) {
            Csomopont target = csomopontMap.get(pozID);
            for (Utszakasz ut : utszakaszok) {
                if (ut.getVegPont() == target && !ut.getSavok().isEmpty()) {
                    jarmu.setAktualisSav(ut.getSavok().get(0));
                    break;
                }
            }
        }
    }
}