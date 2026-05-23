package Views;

import Models.Csomopont;
import java.awt.*;

public class NodeRenderer {

    public void drawNode(Graphics2D g2d, Csomopont cs) {
        int x = cs.getX();
        int y = cs.getY();
        int size = 20;

        Color nodeColor = Color.decode("#000000");
        String label = String.valueOf(cs.getId());

        if (cs.getEpulet() != null) {
            String epuletTipus = cs.getEpulet().getClass().getSimpleName();
            if (epuletTipus.equals("Otthon")) {
                nodeColor = Color.decode("#E30707");
            } else if (epuletTipus.equals("Munkahely")) {
                nodeColor = Color.decode("#0787E3");
            } else if (epuletTipus.equals("Bolt")) {
                nodeColor = Color.decode("#946213");
            } else if (epuletTipus.equals("Benzinkut")) {
                nodeColor = Color.decode("#1F5C06");
            }
        }

        g2d.setColor(nodeColor);
        g2d.fillOval(x - size/2, y - size/2, size, size);

        g2d.setColor(Color.WHITE);
        g2d.drawOval(x - size/2, y - size/2, size, size);

        g2d.setColor(Color.WHITE);
        g2d.drawString(label, x + size/2 + 2, y + 5);
    }
}