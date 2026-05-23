package Views;

import Models.Csomopont;
import java.awt.*;

public class NodeRenderer {

    public void drawNode(Graphics2D g2d, Csomopont cs) {
        int x = cs.getX();
        int y = cs.getY();
        int size = 20; // A csomópont mérete

        // Alapértelmezett szín: fekete, ha nincs épület
        Color nodeColor = Color.decode("#000000");
        String label = String.valueOf(cs.getId());

        // Épülettípus lekérése virtuális metódussal (instanceof nélkül)
        if (cs.getEpulet() != null) {
            String epuletTipus = cs.getEpulet().getClass().getSimpleName();
            if (epuletTipus.equals("Otthon")) {
                nodeColor = Color.decode("#E30707"); // Piros
            } else if (epuletTipus.equals("Munkahely")) {
                nodeColor = Color.decode("#0787E3"); // Világoskék
            } else if (epuletTipus.equals("Bolt")) {
                nodeColor = Color.decode("#946213"); // Barna
            } else if (epuletTipus.equals("Benzinkut")) {
                nodeColor = Color.decode("#1F5C06"); // Sötétzöld
            }
        }

        // Kirajzolás úgy, hogy az X és Y koordináta pontosan a kör/négyzet KÖZEPE legyen
        g2d.setColor(nodeColor);
        g2d.fillOval(x - size/2, y - size/2, size, size);

        // Keret
        g2d.setColor(Color.WHITE);
        g2d.drawOval(x - size/2, y - size/2, size, size);

        // Felirat a csomópont mellé/fölé
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, x + size/2 + 2, y + 5);
    }
}