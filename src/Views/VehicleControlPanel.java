package Views;

import Controllers.GameController;
import Models.Hokotro;
import Observers.IObserver;

import javax.swing.*;
import java.awt.*;

public class VehicleControlPanel extends JPanel implements IObserver {
    private Hokotro selectedHokotro;
    private JLabel idLabel = new JLabel("ID: -");
    private JLabel penzLabel = new JLabel("Pénz: 0 PingCoin");
    private JLabel soLabel = new JLabel("Sókészlet: 0 egység");
    private JLabel biokerozinLabel = new JLabel("Biokerozin: 0 egység");
    private JLabel zuzottKoLabel = new JLabel("Zúzott kő: 0 egység");
    private JLabel kotrofejLabel = new JLabel("Aktuális kotrófej: Nincs");

    public VehicleControlPanel(GameController controller) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(250, 0));
        this.setBorder(BorderFactory.createTitledBorder("Jármű Vezérlés"));

        this.add(idLabel);
        this.add(penzLabel);
        this.add(soLabel);
        this.add(biokerozinLabel);
        this.add(zuzottKoLabel);
        this.add(kotrofejLabel);

        JButton csereButton = new JButton("Csere");
        csereButton.setFocusable(false);
        JButton buyButton = new JButton("Vásárlás");
        buyButton.setFocusable(false);
        this.add(csereButton);
        this.add(buyButton);

        buyButton.addActionListener(e -> {
            if (selectedHokotro != null) {
                controller.handleBuyAttempt(selectedHokotro);
            }
        });

    }

    public void setHokotro(Hokotro hk) {
        if (this.selectedHokotro != null) {
            this.selectedHokotro.removeObserver(this);
        }
        this.selectedHokotro = hk;
        if (hk != null) {
            hk.addObserver(this);
            update();
        }
    }

    @Override
    public void update() {
        if (selectedHokotro != null) {
            idLabel.setText("ID: " + selectedHokotro.getId());
            penzLabel.setText("Pénz: " + selectedHokotro.getPenz() + " PingCoin");
            soLabel.setText("Sókészlet: " + selectedHokotro.getSo().getMennyiseg() + " Kg");
            biokerozinLabel.setText("Biokerozin: " + selectedHokotro.getUzemanyag().getLiterek() + " liter");
            zuzottKoLabel.setText("Zúzott kő: " + selectedHokotro.getZuzottKo().getMennyiseg() + " kg");
            kotrofejLabel.setText("Aktuális kotrófej: " + (selectedHokotro.getAktualisFej() != null ? selectedHokotro.getAktualisFej().getNev() : "Nincs"));
        }
    }
}