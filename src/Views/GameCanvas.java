package Views;

import Models.*;
import javax.swing.*;
import java.awt.*;
import Observers.IObserver;

public class GameCanvas extends JPanel implements IObserver {
    private Palya palya;
    private RoadRenderer roadRenderer;
    private NodeRenderer nodeRenderer;
    private VehicleRenderer vehicleRenderer;

    public GameCanvas(Palya palya) {
        this.palya = palya;
        this.roadRenderer = new RoadRenderer();
        this.nodeRenderer = new NodeRenderer();
        this.vehicleRenderer = new VehicleRenderer();
        this.setBackground(new Color(34, 139, 34));
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

        for (Utszakasz ut : palya.getUtszakaszok()) {
            roadRenderer.drawEdge(g2d, ut);
        }

        for (Csomopont cs : palya.getCsomopontok()) {
            nodeRenderer.drawNode(g2d, cs);
        }

        for (Jarmu jarmu : palya.getJarmuvek()) {
            vehicleRenderer.draw(g2d, jarmu);
        }
    }
}