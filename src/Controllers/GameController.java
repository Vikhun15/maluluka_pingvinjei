package Controllers;

import Models.*;
import Views.FejekDialog;
import Views.GameCanvas;
import Views.GameWindow;
import Views.ShopDialog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController implements KeyListener, MouseListener {

    private final Palya palya;
    private GameWindow window;

    private final GameStateManager gameStateManager = new GameStateManager();
    private final TestManager testManager = new TestManager();

    private Hokotro kivalasztottHokotro;
    private Busz kivalasztottBusz;
    private int aktivJatekosIndex = 0;
    private int jelenlegiKor = 1;

    private final List<Jarmu> jatekosJarmuvek = new ArrayList<>();

    public GameController(Palya palya) {
        this.palya = palya;
        for (Jarmu j : palya.getJarmuvek()) {
            if (j.getJarmuTipus().equals("Hokotro") || j.getJarmuTipus().equals("Busz")) {
                jatekosJarmuvek.add(j);
            }
        }
    }

    public void setWindow(GameWindow window) {
        this.window = window;
        if (this.window != null && this.window.getCanvas() != null) {
            this.window.getCanvas().addMouseListener(this);
        }
    }

    public void setKivalasztottHoktoro(Hokotro hokotro) {
        this.kivalasztottHokotro = hokotro;

        if (window != null && window.getCanvas() != null) {
            window.getCanvas().setSelectedHokotro(hokotro);
        }
    }

    public void setKivalasztottBusz(Busz busz) {
        this.kivalasztottBusz = busz;
        if (window != null && window.getCanvas() != null) {
            window.getCanvas().setSelectedBusz(busz);
        }
    }

    public void passzoldABotot() {
        if (jatekosJarmuvek.isEmpty()) return;

        aktivJatekosIndex++;

        if (aktivJatekosIndex >= jatekosJarmuvek.size()) {
            aktivJatekosIndex = 0;

            executeEnvironmentStep();
            mozgasdAzAutokat();
            jelenlegiKor++;

            if (window != null) {
                window.getStatusBar().setStatus(jelenlegiKor, "A kör lezajlott, mindenki lépett.");
            }
        }

        frissitAktivJatekost();
    }

    private void executeEnvironmentStep() {
        for (Utszakasz u : palya.getUtszakaszok()) {
            u.hoEsik();
        }
        for (Jarmu j : palya.getJarmuvek()) {
            j.korFrissites();
            j.megerkezik();
        }

        if (window != null) {
            window.getStatusBar().setStatus(jelenlegiKor, "Környezet frissítve (havazás, autók léptek).");
        }
    }

    public void handleStep() {
        if (jatekosJarmuvek.isEmpty()) return;
        Jarmu aktiv = jatekosJarmuvek.get(aktivJatekosIndex);

        if (aktiv.getKimaradoKorok() > 0) {
            aktiv.korFrissites();
        } else if (aktiv.getAktualisCsomopont() == null) {
            if (aktiv.getCelCsomopont() == null) {
                if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Előbb válassz irányt (WASD / Kattintás)!");
                return;
            }
        }

        passzoldABotot();
    }

    public void mozgasdAzAutokat() {
        for (Jarmu j : palya.getJarmuvek()) {
            if (j.getJarmuTipus().equals("Auto")) {
                Auto auto = (Auto) j;
                if (auto.getKimaradoKorok() > 0) {
                    auto.korFrissites();
                    continue;
                }

                if (auto.getAktualisCsomopont() == null) {
                    auto.megerkezik();
                } else {
                    Csomopont jelenlegi = auto.getAktualisCsomopont();

                    Csomopont celEpuletCsomopont = null;
                    for (Csomopont cs : palya.getCsomopontok()) {
                        if (cs.getEpulet() == auto.getAktualisCel()) {
                            celEpuletCsomopont = cs;
                            break;
                        }
                    }

                    if (celEpuletCsomopont != null) {
                        if (jelenlegi == celEpuletCsomopont) {
                            auto.megfordul();
                        } else {
                            Sav nextSav = getNextSavOnShortestPath(jelenlegi, celEpuletCsomopont);
                            if (nextSav != null) {
                                Utszakasz ut = nextSav.getUtszakasz();
                                Csomopont cel = (ut.getKezdoPont() == jelenlegi) ? ut.getVegPont() : ut.getKezdoPont();
                                auto.elindul(nextSav, cel);
                            }
                        }
                    }
                }
            }
        }
    }

    public void handleMove(Jarmu j, Sav targetSav) {
        Csomopont regiPont = j.getAktualisCsomopont();

        j.mozog(targetSav);

        Utszakasz ut = targetSav.getUtszakasz();
        Csomopont ujPont = (ut.getKezdoPont() == regiPont) ? ut.getVegPont() : ut.getKezdoPont();

        if (regiPont != null) {
            regiPont.jarmuKilep(j);
        }
        j.setAktualisCsomopont(ujPont);


        if (window != null) {
            window.getStatusBar().setStatus(jelenlegiKor, "Sikeres mozgás ide: " + ujPont.getId());
        }

        passzoldABotot();
    }

    public void handleTakarit() {
        if (kivalasztottHokotro != null) {
            kivalasztottHokotro.takarit(kivalasztottHokotro.getAktualisSav());
            if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Takarítás elvégezve.");
            passzoldABotot();
        }
    }

    /**
     * Megkeresi a két csomópontot összekötő útszakasz első sávját.
     * Visszatérési értéke null, ha a két csomópont nem közvetlen szomszédja egymásnak.
     */
    private Sav findSavBetween(Csomopont a, Csomopont b) {
        if (a == null || b == null) return null;

        for (Utszakasz u : palya.getUtszakaszok()) {
            if ((u.getKezdoPont() == a && u.getVegPont() == b) ||
                    (u.getVegPont() == a && u.getKezdoPont() == b)) {

                if (!u.getSavok().isEmpty()) {
                    return getBestEmptySav(u);
                }
            }
        }
        return null;
    }

    public void handleMapClick(int x, int y) {
        Csomopont clickedNode = palya.getCsomopontByCoordinates(x, y);

        if (clickedNode != null) {
            if (window != null) {
                window.getStatusBar().setStatus(jelenlegiKor, "Kattintva: " + clickedNode.getId());
            }

            if (kivalasztottHokotro != null || kivalasztottBusz != null) {
                Jarmu aktivJarmu = kivalasztottHokotro != null ? kivalasztottHokotro : kivalasztottBusz;
                Csomopont jelenlegi = aktivJarmu.getAktualisCsomopont();
                Sav aktualisSav = aktivJarmu.getAktualisSav();

                if (jelenlegi != null && jelenlegi != clickedNode) {
                    Sav celSav = findSavBetween(jelenlegi, clickedNode);
                    if (celSav != null) {
                        aktivJarmu.elindul(celSav, clickedNode);
                        if (window != null) {
                            window.getCanvas().update();
                            window.getStatusBar().setStatus(jelenlegiKor, "Irány kiválasztva! Nyomj Space-t a megérkezéshez és kör leadásához.");
                        }
                    } else {
                        if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Nem lehet oda lépni, nincs közvetlen út!");
                    }
                } else if (jelenlegi == null && aktualisSav != null) {
                    if (aktivJarmu.getCelCsomopont() == null) {
                        Utszakasz ut = aktualisSav.getUtszakasz();
                        if (clickedNode == ut.getKezdoPont() || clickedNode == ut.getVegPont()) {
                            aktivJarmu.setCelCsomopont(clickedNode);
                            if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Irány rögzítve! Nyomj Space-t a befejezéshez.");
                        } else {
                            if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Kérlek, az utca egyik végpontjára kattints!");
                        }
                    } else {
                        if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Az irány rögzítve van. Nyomj Space-t a megérkezéshez!");
                    }
                }
            }
        }
    }

    public void registerObservers() {
        if (window == null) return;
        GameCanvas canvas = window.getCanvas();

        for (Utszakasz ut : palya.getUtszakaszok()) {
            for (Sav sav : ut.getSavok()) {
                sav.addObserver(canvas);
            }
        }

        for (Jarmu j : palya.getJarmuvek()) {
            j.addObserver(canvas);
        }
        for (Csomopont cs : palya.getCsomopontok()) {
            if (cs.getEpulet() != null) {
                if (cs.getEpulet().asBolt() != null) {
                    cs.getEpulet().asBolt().addObserver(canvas);
                }
            }
        }
    }

    public void handleBuyAttempt(Hokotro hk) {
        if (window == null) return;

        Csomopont aktualisPont = hk.getAktualisCsomopont();
        if (aktualisPont == null || aktualisPont.getEpulet() == null) {
            window.getStatusBar().setStatus(jelenlegiKor, "HIBA: Itt nem lehet vásárolni!");
            return;
        }

        Bolt elerhetoBolt = aktualisPont.getEpulet().asBolt();
        if (elerhetoBolt != null) {
            ShopDialog shopDialog = new ShopDialog(window, this);
            shopDialog.showShop(elerhetoBolt, hk);
        } else {
            window.getStatusBar().setStatus(jelenlegiKor, "HIBA: Az épület nem bolt!");
        }
    }

    public void handleProductPurchase(ITargy termek, Hokotro hk) {
        Csomopont cp = hk.getAktualisCsomopont();

        if (cp != null && cp.getEpulet() != null && cp.getEpulet().asBolt() != null) {
            Bolt bolt = cp.getEpulet().asBolt();

            boolean sikeres = bolt.elad(termek, hk);

            if (window != null) {
                if (sikeres) {
                    window.getStatusBar().setStatus(jelenlegiKor, "Sikeres vásárlás: " + termek.getNev());
                    hk.notifyObservers();
                } else {
                    window.getStatusBar().setStatus(jelenlegiKor, "Sikertelen vásárlás (nincs elég pénz)! " + termek.getNev());
                }
            }
        }
    }

    public void handleFejChange(Hokotro hk) {
        if (window == null) return;
        FejekDialog fejekDialog = new FejekDialog(window, this);
        fejekDialog.showFejek(hk);
    }

    public void handleFejCsere(Kotrofej fej, Hokotro hk) {
        hk.setAktualisFej(fej);
        hk.notifyObservers();
    }

    public void handleSave(File file) {
        gameStateManager.saveGame(palya, file);
        if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Játék elmentve.");
    }

    public void handleLoad(File file) {
        Palya betoltottPalya = gameStateManager.loadGame(file);
        if (betoltottPalya != null) {
            palya.getCsomopontok().clear();
            palya.getCsomopontok().addAll(betoltottPalya.getCsomopontok());
            palya.getUtszakaszok().clear();
            palya.getUtszakaszok().addAll(betoltottPalya.getUtszakaszok());
            palya.getJarmuvek().clear();
            palya.getJarmuvek().addAll(betoltottPalya.getJarmuvek());

            registerObservers();

            if (window != null) {
                window.getCanvas().update();
                window.getStatusBar().setStatus(jelenlegiKor, "Játék betöltve.");
            }
        }

        jatekosJarmuvek.clear();
        for (Jarmu j : palya.getJarmuvek()) {
            if (j.getJarmuTipus().equals("Hokotro") || j.getJarmuTipus().equals("Busz")) {
                jatekosJarmuvek.add(j);
            }
        }

        for (Jarmu j : jatekosJarmuvek) {
            if (j.getAktualisCsomopont() == null) {
                j.setCelCsomopont(null);
            }
        }

        aktivJatekosIndex = 0;
        frissitAktivJatekost();

    }

    private void frissitAktivJatekost() {
        if (jatekosJarmuvek.isEmpty()) return;

        Jarmu aktiv = jatekosJarmuvek.get(aktivJatekosIndex);

        if (aktiv.getJarmuTipus().equals("Hokotro")) {
            setKivalasztottBusz(null);
            setKivalasztottHoktoro((Hokotro) aktiv);
            if (window != null) window.getControlPanel().setHokotro((Hokotro) aktiv);

        } else if (aktiv.getJarmuTipus().equals("Busz")) {
            setKivalasztottHoktoro(null);
            setKivalasztottBusz((Busz) aktiv);
            if (window != null) window.getControlPanel().setBusz((Busz) aktiv);
        }

        if (window != null) {
            window.getStatusBar().setStatus(jelenlegiKor, "Játékos váltás: " + aktiv.getJarmuTipus() + " következik.");
            window.getCanvas().update();
        }
    }

    /**
     * Megkeresi azt a sávot, amely a jelenlegi csomópontból a megadott irányba vezet.
     */
    private Sav findTargetSavByDirection(Csomopont aktualisPont, String irany) {
        if (aktualisPont == null) return null;

        for (Utszakasz u : palya.getUtszakaszok()) {
            Csomopont celPont = null;

            if (u.getKezdoPont() == aktualisPont) {
                celPont = u.getVegPont();
            } else if (u.getVegPont() == aktualisPont) {
                celPont = u.getKezdoPont();
            }

            if (celPont != null && !u.getSavok().isEmpty()) {
                boolean joIrany = isJoIrany(aktualisPont, irany, celPont);

                if (joIrany) {
                    return getBestEmptySav(u);
                }
            }
        }
        return null;
    }

    private static boolean isJoIrany(Csomopont aktualisPont, String irany, Csomopont celPont) {
        int dx = celPont.getX() - aktualisPont.getX();
        int dy = celPont.getY() - aktualisPont.getY();

        boolean joIrany = false;

        boolean a = Math.abs(dy) > Math.abs(dx);
        boolean b = Math.abs(dx) > Math.abs(dy);
        joIrany = switch (irany) {
            case "fel" -> (dy < 0) && a;
            case "le" -> (dy > 0) && a;
            case "bal" -> (dx < 0) && b;
            case "jobb" -> (dx > 0) && b;
            default -> false;
        };
        return joIrany;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            handleStep();
            return;
        }

        Jarmu aktivJarmu = null;
        if (kivalasztottHokotro != null) {
            aktivJarmu = kivalasztottHokotro;
        } else if (kivalasztottBusz != null) {
            aktivJarmu = kivalasztottBusz;
        }

        if (aktivJarmu != null) {
            Csomopont jelenlegi = aktivJarmu.getAktualisCsomopont();
            Sav aktualisSav = aktivJarmu.getAktualisSav();

            if (jelenlegi != null) {
                String irany = "";
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) irany = "fel";
                else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) irany = "le";
                else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) irany = "bal";
                else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) irany = "jobb";

                if (!irany.isEmpty()) {
                    Sav celSav = findTargetSavByDirection(jelenlegi, irany);
                    if (celSav != null) {
                        Utszakasz ut = celSav.getUtszakasz();
                        Csomopont celPont = (ut.getKezdoPont() == jelenlegi) ? ut.getVegPont() : ut.getKezdoPont();

                        aktivJarmu.elindul(celSav, celPont);

                        if (window != null) {
                            window.getCanvas().update();
                            window.getStatusBar().setStatus(jelenlegiKor, "Irány kiválasztva! Nyomj Space-t a megérkezéshez és kör leadásához.");
                        }
                    } else {
                        if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Erre nincs út!");
                    }
                }
            } else if (aktualisSav != null && aktivJarmu.getCelCsomopont() == null) {
                String irany = "";
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) irany = "fel";
                else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) irany = "le";
                else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) irany = "bal";
                else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) irany = "jobb";

                if (!irany.isEmpty()) {
                    Utszakasz ut = aktualisSav.getUtszakasz();

                    Csomopont fakeKozep = new Csomopont();
                    fakeKozep.setX((ut.getKezdoPont().getX() + ut.getVegPont().getX()) / 2);
                    fakeKozep.setY((ut.getKezdoPont().getY() + ut.getVegPont().getY()) / 2);

                    Csomopont valasztottCel = null;
                    if (isJoIrany(fakeKozep, irany, ut.getKezdoPont())) valasztottCel = ut.getKezdoPont();
                    else if (isJoIrany(fakeKozep, irany, ut.getVegPont())) valasztottCel = ut.getVegPont();

                    if (valasztottCel != null) {
                        aktivJarmu.setCelCsomopont(valasztottCel);
                        if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Irány rögzítve! Nyomj Space-t a megérkezéshez.");
                    } else {
                        if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Abba az irányba nincs végpontja az útnak!");
                    }
                }
            } else {
                if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Úton vagy! Nyomj Space-t a megérkezéshez.");
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        handleMapClick(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Nem használjuk, de kötelező implementálni a MouseListener miatt
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Nem használjuk, de kötelező implementálni a MouseListener miatt
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nem használjuk, de kötelező implementálni a MouseListener miatt
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nem használjuk, de kötelező implementálni a MouseListener miatt
    }

    /**
     * BFS (Szélességi Keresés) algoritmus a legrövidebb útvonal megtalálására.
     * Visszaadja azt az 1 db sávot, amelyre az autónak lépnie kell, hogy a cél felé haladjon.
     */
    private Sav getNextSavOnShortestPath(Csomopont start, Csomopont target) {
        if (start == null || target == null || start == target) return null;

        java.util.Queue<Csomopont> queue = new java.util.LinkedList<>();
        java.util.Map<Csomopont, Csomopont> parentMap = new java.util.HashMap<>();
        java.util.Map<Csomopont, Sav> edgeToParentMap = new java.util.HashMap<>();

        queue.add(start);
        parentMap.put(start, null);

        boolean found = false;

        while (!queue.isEmpty()) {
            Csomopont current = queue.poll();

            if (current == target) {
                found = true;
                break;
            }

            for (Utszakasz ut : palya.getUtszakaszok()) {
                Csomopont szomszed = null;

                if (ut.getKezdoPont() == current) szomszed = ut.getVegPont();
                else if (ut.getVegPont() == current) szomszed = ut.getKezdoPont();

                if (szomszed != null && !parentMap.containsKey(szomszed) && !ut.getSavok().isEmpty()) {
                    parentMap.put(szomszed, current);
                    edgeToParentMap.put(szomszed, getBestEmptySav(ut));
                    queue.add(szomszed);
                }
            }
        }

        if (!found) return null;

        Csomopont step = target;
        Sav firstSav = null;
        while (parentMap.get(step) != null) {
            firstSav = edgeToParentMap.get(step);
            step = parentMap.get(step);
            if (parentMap.get(step) == null) {
                break;
            }
        }

        return firstSav;
    }

    /**
     * Kiválasztja a legjobb sávot egy útszakaszon.
     * Visszaadja az első olyan sávot, amin épp nem tartózkodik jármű.
     * Ha minden sáv foglalt, alapértelmezetten visszaadja a legelsőt.
     */
    private Sav getBestEmptySav(Utszakasz ut) {
        if (ut.getSavok().isEmpty()) return null;

        for (Sav sav : ut.getSavok()) {
            boolean foglalt = false;
            for (Jarmu j : palya.getJarmuvek()) {
                if (j.getAktualisSav() == sav) {
                    foglalt = true;
                    break;
                }
            }
            if (!foglalt) {
                return sav;
            }
        }
        return ut.getSavok().get(0);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void handleRandomToggle(boolean selected) {
        testManager.setRandomnessEnabled(selected);
        if (window != null) {
            String status = selected ? "Véletlenszerű események engedélyezve." : "Véletlenszerű események letiltva.";
            window.getStatusBar().setStatus(jelenlegiKor, status);
        }
    }
}