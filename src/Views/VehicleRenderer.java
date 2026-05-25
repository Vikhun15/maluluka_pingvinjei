package Views;

import Models.Jarmu;
import Models.Sav;
import Models.Utszakasz;
import Models.Csomopont;
import Models.Hokotro;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VehicleRenderer {

    private Image autoImg;
    private Image buszImg;
    private Image hokotroImg;

    public VehicleRenderer() {
        try {
            autoImg = ImageIO.read(new File("src/Images/auto.png"));
            buszImg = ImageIO.read(new File("src/Images/busz.png"));
            hokotroImg = ImageIO.read(new File("src/Images/hokotro.png"));
        } catch (IOException e) {
            System.out.println("Figyelmeztetés: Egy vagy több jármű kép nem található. Színes dobozok lesznek kirajzolva.");
        }
    }

    public void draw(Graphics2D g2d, Jarmu jarmu, boolean isSelected) {
        Sav aktualisSav = jarmu.getAktualisSav();
        if (aktualisSav == null) return;


        int x = 0;
        int y = 0;

        if (jarmu.getAktualisCsomopont() != null) {
            x = jarmu.getAktualisCsomopont().getX();
            y = jarmu.getAktualisCsomopont().getY();
        } else if (jarmu.getAktualisSav() != null) {
            Utszakasz ut = jarmu.getAktualisSav().getUtszakasz();
            x = (ut.getKezdoPont().getX() + ut.getVegPont().getX()) / 2;
            y = (ut.getKezdoPont().getY() + ut.getVegPont().getY()) / 2;
        } else {
            return;
        }


        Utszakasz ut = aktualisSav.getUtszakasz();
        Csomopont kezdo = ut.getKezdoPont();
        Csomopont veg = ut.getVegPont();

        int size = 30;

        if (isSelected) {
            g2d.setColor(new Color(255, 255, 255, 120));
            g2d.fillOval(x - (size/2) - 10, y - (size/2) - 10, size + 20, size + 20);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - (size/2) - 10, y - (size/2) - 10, size + 20, size + 20);
        }

        String tipus = jarmu.getJarmuTipus();
        Image imgToDraw = switch (tipus) {
            case "Auto" -> autoImg;
            case "Busz" -> buszImg;
            case "Hokotro" -> hokotroImg;
            default -> null;
        };

        if (imgToDraw != null) {
            g2d.drawImage(imgToDraw, x - (size/2), y - (size/2), size, size, null);
        } else {
            Color boxColor = tipus.equals("Hokotro") ? Color.decode("#FFF02B") :
                    tipus.equals("Busz") ? Color.decode("#033C8F") : Color.decode("#F08C05");
            g2d.setColor(boxColor);
            g2d.fillRect(x - (size/2), y - (size/2), size, size);
        }

        String label = String.valueOf(jarmu.getId());
        if (tipus.equals("Hokotro")) {
            Hokotro hk = (Hokotro) jarmu;
            if (hk.getAktualisFej() != null) {
                label += " (" + hk.getAktualisFej().getNev() + ")";
            }
        }
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(x - (size/2), y + (size/2) + 2, g2d.getFontMetrics().stringWidth(label) + 4, 14);
        g2d.setColor(Color.BLACK);
        g2d.drawString(label, x - (size/2) + 2, y + (size/2) + 13);

        if (jarmu.getKimaradoKorok() > 0) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x - (size/2), y - (size/2), size, size);

            g2d.drawLine(x - (size/2), y - (size/2), x + (size/2), y + (size/2));
            g2d.drawLine(x + (size/2), y - (size/2), x - (size/2), y + (size/2));
        }
    }
}