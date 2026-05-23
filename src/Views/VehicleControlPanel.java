package Views;

import Controllers.GameController;
import Models.Hokotro;
import Observers.IObserver;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VehicleControlPanel extends JPanel implements IObserver {
    private Hokotro selectedHokotro;
    private JLabel idLabel = new JLabel("ID: -");
    private JLabel penzLabel = new JLabel("Pénz: 0 PingCoin");
    private JLabel soLabel = new JLabel("Sókészlet: 0 kg");
    private JLabel biokerozinLabel = new JLabel("Biokerozin: 0 liter");
    private JLabel zuzottKoLabel = new JLabel("Zúzott kő: 0 kg");
    private JLabel kotrofejLabel = new JLabel("Aktuális kotrófej: Nincs");

    public VehicleControlPanel(GameController controller) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(280, 0));
        this.setBackground(Color.decode("#FFFFFF"));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 14);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#E0E0E0"), 1, true),
                "Jármű Vezérlés",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                titleFont,
                Color.decode("#2C3E50")
        );

        this.setBorder(new CompoundBorder(titledBorder, new EmptyBorder(15, 15, 15, 15)));

        addStyledLabel(idLabel);
        addStyledLabel(penzLabel);
        addStyledLabel(soLabel);
        addStyledLabel(biokerozinLabel);
        addStyledLabel(zuzottKoLabel);
        addStyledLabel(kotrofejLabel);

        this.add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(Color.decode("#FFFFFF"));
        buttonPanel.setMaximumSize(new Dimension(250, 38));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton csereButton = new JButton("Csere");
        csereButton.setFocusable(false);
        csereButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        csereButton.addActionListener(e -> {
            if (selectedHokotro != null) {
                controller.handleFejChange(selectedHokotro);
            }
        });

        JButton buyButton = new JButton("Vásárlás");
        buyButton.setFocusable(false);
        buyButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buyButton.setBackground(Color.decode("#2196F3"));

        buyButton.addActionListener(e -> {
            if (selectedHokotro != null) {
                controller.handleBuyAttempt(selectedHokotro);
            }
        });

        JButton takaritButton = new JButton("Takarítás");
        takaritButton.setFocusable(false);
        takaritButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        takaritButton.setBackground(Color.decode("#2196F3"));
        takaritButton.addActionListener(e ->{
            if(selectedHokotro != null){
                controller.handeTakarit();
            }
        });

        JButton passButton = new JButton("Passz");
        passButton.setFocusable(false);
        passButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passButton.setBackground(Color.decode("#2196F3"));
        passButton.addActionListener(e ->{
            controller.handleStep();
        });

        buttonPanel.add(csereButton);
        buttonPanel.add(buyButton);
        buttonPanel.add(takaritButton);
        buttonPanel.add(passButton);
        this.add(buttonPanel);
    }

    /**
     * Segédmetódus a feliratok egységes stílusára és térközére.
     */
    private void addStyledLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(Color.decode("#57606F"));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(12));
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
            soLabel.setText("Sókészlet: " + selectedHokotro.getSo().getMennyiseg() + " kg");
            biokerozinLabel.setText("Biokerozin: " + selectedHokotro.getUzemanyag().getLiterek() + " liter");
            zuzottKoLabel.setText("Zúzott kő: " + selectedHokotro.getZuzottKo().getMennyiseg() + " kg");
            kotrofejLabel.setText("Aktuális kotrófej: " + (selectedHokotro.getAktualisFej() != null ? selectedHokotro.getAktualisFej().getNev() : "Nincs"));
        }
    }
}