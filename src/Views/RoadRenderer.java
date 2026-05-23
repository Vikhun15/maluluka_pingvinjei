package Views;

import Models.Utszakasz;
import Models.Csomopont;
import Models.Sav;
import java.awt.*;

public class RoadRenderer {

    public void drawEdge(Graphics2D g2d, Utszakasz ut) {
        // Csomópontok és koordinátáik lekérése a modellből
        Csomopont kezdo = ut.getKezdoPont();
        Csomopont veg = ut.getVegPont();

        int x1 = kezdo.getX();
        int y1 = kezdo.getY();
        int x2 = veg.getX();
        int y2 = veg.getY();

        // Alapszín beállítása (például az első sáv állapota alapján)
        Color roadColor = Color.decode("#808080"); // Alap szürke

        if (!ut.getSavok().isEmpty()) {
            Sav elsoSav = ut.getSavok().get(0);
            // Itt alkalmazhatók a dokumentációban lévő színkódok
            if (elsoSav.getHoRetegek() >= 8) {
                roadColor = Color.decode("#00008B"); // Túl sok hó
            } else if (elsoSav.getHoRetegek() > 0) {
                roadColor = Color.decode("#FFFFFF"); // Havas
            } else if (elsoSav.jeges()) {
                roadColor = Color.decode("#ADD8E6"); // Jeges
            }
        }

        // Út (él) kirajzolása vastag vonalként
        g2d.setColor(roadColor);
        g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);

        // Fekete szegély az út szélére, hogy kontrasztos legyen
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        // Opcionális: két vékony fekete vonalat is húzhatsz az út két szélére párhuzamosan
    }
}