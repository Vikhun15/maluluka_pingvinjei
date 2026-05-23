package Views;

import Models.*;
import javax.swing.*;
import java.awt.*;
import Observers.IObserver;
import java.awt.event.MouseEvent;

public class GameCanvas extends JPanel implements IObserver {
    private Palya palya;
    private RoadRenderer roadRenderer;
    private NodeRenderer nodeRenderer;
    private VehicleRenderer vehicleRenderer;

    private Hokotro selectedHokotro;

    public GameCanvas(Palya palya) {
        this.palya = palya;
        this.roadRenderer = new RoadRenderer();
        this.nodeRenderer = new NodeRenderer();
        this.vehicleRenderer = new VehicleRenderer();

        this.setBackground(Color.decode("#4CAF50"));
        this.setBorder(BorderFactory.createLineBorder(Color.decode("#388E3C"), 2));
        ToolTipManager.sharedInstance().registerComponent(this);
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