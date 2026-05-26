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
            nodeColor = switch (epuletTipus) {
                case "Otthon" -> Color.decode("#E30707");
                case "Munkahely" -> Color.decode("#0787E3");
                case "Bolt" -> Color.decode("#946213");
                case "Benzinkut" -> Color.decode("#1F5C06");
                default -> nodeColor;
            };
        }

        g2d.setColor(nodeColor);
        g2d.fillOval(x - size/2, y - size/2, size, size);

        g2d.setColor(Color.WHITE);
        g2d.drawOval(x - size/2, y - size/2, size, size);


        g2d.setColor(Color.BLACK);
        g2d.drawString(label, x + size/2 + 2, y + 5);
    }
}