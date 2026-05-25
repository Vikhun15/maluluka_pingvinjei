package Views;

import Models.Utszakasz;
import Models.Csomopont;
import Models.Sav;
import java.util.List;
import java.awt.*;

public class RoadRenderer {

    public void drawEdge(Graphics2D g2d, Utszakasz ut) {
        Csomopont kezdo = ut.getKezdoPont();
        Csomopont veg = ut.getVegPont();

        int x1 = kezdo.getX();
        int y1 = kezdo.getY();
        int x2 = veg.getX();
        int y2 = veg.getY();

        List<Sav> savok = ut.getSavok();
        int savokSzama = savok.size();

        if (savokSzama == 0) return;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length == 0) return;

        double nx = -dy / length;
        double ny = dx / length;

        int laneWidth = 8;
        int spacing = 2;
        int totalLaneWidth = laneWidth + spacing;
        for (int i = 0; i < savokSzama; i++) {
            Sav sav = savok.get(i);

            double offset = (i - (savokSzama - 1) / 2.0) * totalLaneWidth;

            int startX = (int) (Math.round(x1 + nx * offset));
            int startY = (int) (Math.round(y1 + ny * offset));
            int endX = (int) (Math.round(x2 + nx * offset));
            int endY = (int) (Math.round(y2 + ny * offset));

            Color laneColor = Color.decode("#808080");

            if (sav.getHoRetegek() >= 8) {
                laneColor = Color.decode("#2C3E50");
            } else if (sav.getHoRetegek() > 0) {
                laneColor = Color.decode("#ECF0F1");
            } else if (sav.jeges()) {
                laneColor = Color.decode("#81D4FA");
            }

            g2d.setColor(laneColor);
            g2d.setStroke(new BasicStroke(laneWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(startX, startY, endX, endY);
        }

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
    }
}