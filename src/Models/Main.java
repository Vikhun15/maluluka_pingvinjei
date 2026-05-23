package Models;

import Controllers.GameController;
import Models.Palya;
import Views.GameWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Palya palya = new Palya();

            GameController controller = new GameController(palya);
            GameWindow window = new GameWindow(palya, controller);

            controller.setWindow(window);
            controller.registerObservers();
            for (Jarmu j : palya.getJarmuvek()) {
                if (j.getJarmuTipus().equals("Hokotro")) {
                    Hokotro hk = (Hokotro) j;
                    controller.setKivalasztottHoktoro(hk);
                    window.getControlPanel().setHokotro(hk);
                    break;
                }
            }
            window.setVisible(true);
        });
    }
}