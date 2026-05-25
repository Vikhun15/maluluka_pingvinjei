package Views;

import Models.*;
import javax.swing.*;
import java.awt.*;
import Observers.IObserver;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GameCanvas extends JPanel implements IObserver {
    private final Palya palya;
    private final RoadRenderer roadRenderer;
    private final NodeRenderer nodeRenderer;
    private final VehicleRenderer vehicleRenderer;

    private Hokotro selectedHokotro;

    private Csomopont hoveredCsomopont = null;

    public GameCanvas(Palya palya) {
        this.palya = palya;
        this.roadRenderer = new RoadRenderer();
        this.nodeRenderer = new NodeRenderer();
        this.vehicleRenderer = new VehicleRenderer();

        this.setBackground(Color.decode("#42413f"));
        this.setBorder(BorderFactory.createLineBorder(Color.decode("#388E3C"), 2));
        ToolTipManager.sharedInstance().registerComponent(this);
        this.setPreferredSize(new Dimension(2000, 2000));

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                checkHover(e.getX(), e.getY());
            }
        });
    }

    /**
     * Megnézi, hogy az egér egy csomópont felett van-e, és ha változás van, újrarajzolja a vásznat.
     */
    private void checkHover(int mouseX, int mouseY) {
        Csomopont newHover = null;
        int hitbox = 20;

        for (Csomopont cs : palya.getCsomopontok()) {
            if (Math.abs(mouseX - cs.getX()) <= hitbox && Math.abs(mouseY - cs.getY()) <= hitbox) {
                newHover = cs;
                break;
            }
        }

        if (this.hoveredCsomopont != newHover) {
            this.hoveredCsomopont = newHover;
            this.repaint();
        }
    }

    /**
     * Ellenőrzi, hogy a cél csomópont elérhető-e a jelenlegiből (van-e közvetlen út).
     */
    private boolean isReachable(Csomopont start, Csomopont target) {
        if (start == null || target == null || start == target) return false;

        for (Utszakasz u : palya.getUtszakaszok()) {
            if ((u.getKezdoPont() == start && u.getVegPont() == target) ||
                    (u.getVegPont() == start && u.getKezdoPont() == target)) {
                return true;
            }
        }
        return false;
    }

    public void setSelectedHokotro(Hokotro hk) {
        this.selectedHokotro = hk;
        this.repaint();
    }

    @Override
    public void update() {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawGrid(g2d);

        for (Utszakasz ut : palya.getUtszakaszok()) {
            roadRenderer.drawEdge(g2d, ut);
        }

        for (Csomopont cs : palya.getCsomopontok()) {
            if(selectedHokotro != null){

                Csomopont jelenlegi = selectedHokotro.getAktualisCsomopont();
                if (cs == hoveredCsomopont) {
                    if (jelenlegi != cs) {
                        boolean elerheto = isReachable(jelenlegi, cs);

                        if (elerheto) {
                            g2d.setColor(new Color(76, 175, 80, 180));
                        } else {
                            g2d.setColor(new Color(244, 67, 54, 180));
                        }

                        int glowSize = 40;
                        g2d.fillOval(cs.getX() - glowSize/2, cs.getY() - glowSize/2, glowSize, glowSize);
                    }
                }
                else if(cs == jelenlegi){
                    g2d.setColor(new Color(255, 255, 255, 120));
                    int glowSize = 40;
                    g2d.fillOval(cs.getX() - glowSize/2, cs.getY() - glowSize/2, glowSize, glowSize);

                }
            }

            nodeRenderer.drawNode(g2d, cs);
        }

        for (Jarmu jarmu : palya.getJarmuvek()) {
            vehicleRenderer.draw(g2d, jarmu, jarmu == selectedHokotro);
        }

        drawDirectionHints(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 30));
        int gridSize = 50;
        for (int i = 0; i < getWidth(); i += gridSize) g2d.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += gridSize) g2d.drawLine(0, i, getWidth(), i);
    }

    /**
     * Vizuálisan megjelöli a lehetséges haladási irányokat (W, A, S, D)
     * az éppen kiválasztott hókotró pozíciója körül.
     */
    private void drawDirectionHints(Graphics2D g2d) {
        if (selectedHokotro == null) return;

        Csomopont akt = selectedHokotro.getAktualisCsomopont();
        if (akt == null) return;

        int x1 = akt.getX();
        int y1 = akt.getY();

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));

        for (Utszakasz u : palya.getUtszakaszok()) {
            Csomopont cel = null;
            if (u.getKezdoPont() == akt) cel = u.getVegPont();
            else if (u.getVegPont() == akt) cel = u.getKezdoPont();

            if (cel != null) {
                int dx = cel.getX() - x1;
                int dy = cel.getY() - y1;

                String key = "";
                if (Math.abs(dx) > Math.abs(dy)) {
                    key = dx > 0 ? "D" : "A";
                } else {
                    key = dy > 0 ? "S" : "W";
                }

                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist == 0) continue;
                int hintX = (int) (x1 + (dx / dist) * 35);
                int hintY = (int) (y1 + (dy / dist) * 35);

                g2d.setColor(Color.decode("#2C3E50"));
                g2d.fillOval(hintX - 10, hintY - 10, 20, 20);

                g2d.setColor(Color.WHITE);

                int textOffset = key.equals("W") ? -6 : -4;
                g2d.drawString(key, hintX + textOffset, hintY + 4);
            }
        }
    }

    @Override
    public String getToolTipText(MouseEvent event){
        if(palya == null) return null;

        int mouseX = event.getX();
        int mouseY = event.getY();
        int hitboxTolerance = 20;

        for(Jarmu j: palya.getJarmuvek()){
            Sav aktualisSav = j.getAktualisSav();
            if(aktualisSav != null){
                Utszakasz szakasz = aktualisSav.getUtszakasz();
                int jarmuX = (szakasz.getKezdoPont().getX() + szakasz.getVegPont().getX()) / 2;
                int jarmuY = (szakasz.getKezdoPont().getY() + szakasz.getVegPont().getY()) / 2;

                if (Math.abs(mouseX - jarmuX) <= hitboxTolerance && Math.abs(mouseY - jarmuY) <= hitboxTolerance) {
                    return "<html><div style='padding: 5px; font-family: Segoe UI;'>" +
                            "<b>Jármű:</b> " + j.getJarmuTipus() + " (ID: " + j.getId() + ")<br>" +
                            "<b>Kimaradó körök:</b> " + j.getKimaradoKorok() +
                            "</div></html>";
                }
            }

            for (Csomopont cs : palya.getCsomopontok()) {
                if (Math.abs(mouseX - cs.getX()) <= hitboxTolerance && Math.abs(mouseY - cs.getY()) <= hitboxTolerance) {
                    String epuletNeve = cs.getEpulet() != null ? cs.getEpulet().getClass().getSimpleName() : "Nincs épület (Üres csomópont)";

                    return "<html><div style='padding: 5px; font-family: Segoe UI;'>" +
                            "<b>Csomópont ID:</b> " + cs.getId() + "<br>" +
                            "<b>Koordináta:</b> " + cs.getX() + ", " + cs.getY() + "<br>" +
                            "<b>Típus:</b> <font color='blue'>" + epuletNeve + "</font><br>" +
                            "<b>Itt álló jármű:</b> " + (cs.getAktualisJarmu() != null ? cs.getAktualisJarmu().getJarmuTipus() : "Nincs") +
                            "</div></html>";
                }
            }
        }

        return null;
    }
}