package Views;

import Models.Jarmu;
import Models.Sav;
import Models.Utszakasz;
import Models.Csomopont;
import Models.Hokotro; // Csak a típus ellenőrzéshez a stringből
import java.awt.*;

public class VehicleRenderer {

    public void draw(Graphics2D g2d, Jarmu jarmu) {
        Sav aktualisSav = jarmu.getAktualisSav();
        if (aktualisSav == null) return; // Ha nincs a pályán, nem rajzoljuk

        Utszakasz ut = aktualisSav.getUtszakasz();
        Csomopont kezdo = ut.getKezdoPont();
        Csomopont veg = ut.getVegPont();

        int x = (kezdo.getX() + veg.getX()) / 2;
        int y = (kezdo.getY() + veg.getY()) / 2;


        int size = 30;

        String tipus = jarmu.getJarmuTipus();
        Color boxColor = Color.GRAY;
        Color textColor = Color.BLACK;
        String label = String.valueOf(jarmu.getId());

        if (tipus.equals("Auto")) {
            boxColor = Color.decode("#F08C05"); // Narancs
        } else if (tipus.equals("Busz")) {
            boxColor = Color.decode("#033C8F"); // Kék
            textColor = Color.WHITE;
        } else if (tipus.equals("Hokotro")) {
            boxColor = Color.decode("#FFF02B"); // Sárga
            // Itt nyugodtan lekasztolhatjuk, mert a logikai típusellenőrzés megtörtént
            Hokotro hk = (Hokotro) jarmu;
            if (hk.getAktualisFej() != null) {
                label += " (" + hk.getAktualisFej().getNev() + ")";
            }
        }

        g2d.setColor(boxColor);
        g2d.fillRect(x - (size/2), y - (size/2), size, size);

        g2d.setColor(textColor);
        g2d.drawString(label, x - (size/2) + 2, y + 5);

        if (jarmu.getKimaradoKorok() > 0) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x - (size/2), y - (size/2), size, size);
        }
    }
}