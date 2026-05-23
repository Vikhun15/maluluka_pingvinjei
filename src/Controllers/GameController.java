package Controllers;

import Models.*;
import Views.GameCanvas;
import Views.GameWindow;
import Views.ShopDialog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class GameController implements KeyListener {

    // --- Rendszer Referenciák ---
    private final Palya palya;
    private GameWindow window;

    // --- Állapotkezelők ---
    private final GameStateManager gameStateManager = new GameStateManager();
    private final TestManager testManager = new TestManager();

    // --- Játék Állapot ---
    private Hokotro kivalasztottHokotro;
    private int aktivJatekosIndex = 0;
    private int jelenlegiKor = 1; // Körök számontartása, ha a Pálya nem tárolja

    // A Proto kikerült a konstruktorból!
    public GameController(Palya palya) {
        this.palya = palya;
    }

    public void setWindow(GameWindow window) {
        this.window = window;
    }

    public void setKivalasztottHoktoro(Hokotro hokotro) {
        this.kivalasztottHokotro = hokotro;
    }

    // ==========================================
    // JÁTÉKMENET ÉS KÖRÖK
    // ==========================================

    public void passzoldABotot() {
        aktivJatekosIndex++;
        int jatekosokSzama = 1;

        if (aktivJatekosIndex >= jatekosokSzama) {
            aktivJatekosIndex = 0;
            jelenlegiKor++; // Növeljük a körszámlálót
            executeEnvironmentStep();
        }
    }

    private void executeEnvironmentStep() {
        for (Utszakasz u : palya.getUtszakaszok()) {
            u.hoEsik();
        }
        for (Jarmu j : palya.getJarmuvek()) {
            j.korFrissites();
        }

        if (window != null) {
            window.getStatusBar().setStatus(jelenlegiKor, "Környezet frissítve (havazás, autók léptek).");
        }
    }

    public void handleStep() {
        executeEnvironmentStep();
    }

    // ==========================================
    // INTERAKCIÓK
    // ==========================================

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

    public void handeTakarit() {
        if (kivalasztottHokotro != null) {
            kivalasztottHokotro.takarit(kivalasztottHokotro.getAktualisSav());
            if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Takarítás elvégezve.");
            passzoldABotot();
        }
    }

    public void handleMapClick(int x, int y) {
        // Logikai csomópont megkeresése a koordináták alapján
        Csomopont clickedNode = palya.getCsomopontByCoordinates(x, y);
        if (clickedNode != null) {
            if (window != null) window.getStatusBar().setStatus(jelenlegiKor, "Kattintva: " + clickedNode.getId());
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
    }

    // ==========================================
    // BOLT ÉS VÁSÁRLÁS (KÖZVETLEN MODELL HÍVÁSOK)
    // ==========================================

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
                } else {
                    window.getStatusBar().setStatus(jelenlegiKor, "Sikertelen vásárlás (nincs elég pénz)! " + termek.getNev());
                }
            }
        }
    }

    // ==========================================
    // RENDSZER
    // ==========================================

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
                int dx = celPont.getX() - aktualisPont.getX();
                int dy = celPont.getY() - aktualisPont.getY();

                boolean joIrany = false;

                switch (irany) {
                    case "fel":   joIrany = (dy < 0) && (Math.abs(dy) > Math.abs(dx)); break;
                    case "le":    joIrany = (dy > 0) && (Math.abs(dy) > Math.abs(dx)); break;
                    case "bal":   joIrany = (dx < 0) && (Math.abs(dx) > Math.abs(dy)); break;
                    case "jobb":  joIrany = (dx > 0) && (Math.abs(dx) > Math.abs(dy)); break;
                }

                if (joIrany) {
                    return u.getSavok().get(0);
                }
            }
        }
        return null;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            handleStep();
            return;
        }

        if (kivalasztottHokotro != null) {
            String irany = "";
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) irany = "fel";
            else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) irany = "le";
            else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) irany = "bal";
            else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) irany = "jobb";

            if (!irany.isEmpty()) {
                Csomopont jelenlegi = kivalasztottHokotro.getAktualisCsomopont();
                Sav celSav = findTargetSavByDirection(jelenlegi, irany);

                if (celSav != null) {
                    handleMove(kivalasztottHokotro, celSav);
                } else {
                    if (window != null) {
                        window.getStatusBar().setStatus(jelenlegiKor, "HIBA: Arra nincs út!");
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void toggleRandomness(boolean selected) {
        testManager.setRandomnessEnabled(selected);
        if (window != null) {
            String status = selected ? "Véletlenszerű események engedélyezve." : "Véletlenszerű események letiltva.";
            window.getStatusBar().setStatus(jelenlegiKor, status);
        }
    }
}