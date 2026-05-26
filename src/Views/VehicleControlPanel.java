package Views;

import Controllers.GameController;
import Models.Busz;
import Models.Hokotro;
import Observers.IObserver;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VehicleControlPanel extends JPanel implements IObserver {
    private Hokotro selectedHokotro;
    private Busz selectedBusz;

    private final JLabel idLabel = new JLabel("ID: -");
    private final JLabel penzLabel = new JLabel("Pénz: 0 PingCoin");
    private final JLabel soLabel = new JLabel("Sókészlet: 0 kg");
    private final JLabel biokerozinLabel = new JLabel("Biokerozin: 0 liter");
    private final JLabel zuzottKoLabel = new JLabel("Zúzott kő: 0 kg");
    private final JLabel kotrofejLabel = new JLabel("Aktuális kotrófej: Nincs");

    private JButton csereButton;
    private JButton buyButton;
    private JButton takaritButton;
    private JButton passButton;

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
        buttonPanel.setMaximumSize(new Dimension(250, 80));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        csereButton = new JButton("Csere");
        csereButton.setFocusable(false);
        csereButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        csereButton.addActionListener(e -> {
            if (selectedHokotro != null) {
                controller.handleFejChange(selectedHokotro);
            }
        });

        buyButton = new JButton("Vásárlás");
        buyButton.setFocusable(false);
        buyButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buyButton.setBackground(Color.decode("#2196F3"));
        buyButton.addActionListener(e -> {
            if (selectedHokotro != null) {
                controller.handleBuyAttempt(selectedHokotro);
            }
        });

        takaritButton = new JButton("Takarítás");
        takaritButton.setFocusable(false);
        takaritButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        takaritButton.setBackground(Color.decode("#2196F3"));
        takaritButton.addActionListener(e -> {
            if (selectedHokotro != null) {
                controller.handleTakarit();
            }
        });

        passButton = new JButton("Passz");
        passButton.setFocusable(false);
        passButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passButton.setBackground(Color.decode("#2196F3"));
        passButton.addActionListener(e -> {
            controller.handleStep();
        });

        buttonPanel.add(csereButton);
        buttonPanel.add(buyButton);
        buttonPanel.add(takaritButton);
        buttonPanel.add(passButton);
        this.add(buttonPanel);
    }

    private void addStyledLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(Color.decode("#57606F"));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
        this.add(Box.createVerticalStrut(12));
    }

    public void setHokotro(Hokotro hk) {
        if (this.selectedHokotro != null) this.selectedHokotro.removeObserver(this);
        if (this.selectedBusz != null) this.selectedBusz.removeObserver(this);

        this.selectedBusz = null;
        this.selectedHokotro = hk;

        if (hk != null) {
            hk.addObserver(this);
            update();
        }
    }

    public void setBusz(Busz busz) {
        if (this.selectedHokotro != null) this.selectedHokotro.removeObserver(this);
        if (this.selectedBusz != null) this.selectedBusz.removeObserver(this);

        this.selectedHokotro = null;
        this.selectedBusz = busz;

        if (busz != null) {
            busz.addObserver(this);
            update();
        }
    }

    @Override
    public void update() {
        if (selectedHokotro != null) {
            idLabel.setText("ID: " + selectedHokotro.getId() + " (Hókotró)");
            penzLabel.setText("Pénz: " + selectedHokotro.getPenz() + " PingCoin");
            soLabel.setText("Sókészlet: " + selectedHokotro.getSo().getMennyiseg() + " kg");
            biokerozinLabel.setText("Biokerozin: " + selectedHokotro.getUzemanyag().getLiterek() + " liter");
            zuzottKoLabel.setText("Zúzott kő: " + selectedHokotro.getZuzottKo().getMennyiseg() + " kg");
            kotrofejLabel.setText("Aktuális kotrófej: " + (selectedHokotro.getAktualisFej() != null ? selectedHokotro.getAktualisFej().getNev() : "Nincs"));

            csereButton.setEnabled(true);
            buyButton.setEnabled(true);
            takaritButton.setEnabled(true);

        } else if (selectedBusz != null) {
            idLabel.setText("ID: " + selectedBusz.getId() + " (Busz)");
            penzLabel.setText("Kezdő állomás: " + (selectedBusz.getKezdoAllomas() != null ? selectedBusz.getKezdoAllomas().getId() : "Nincs"));
            soLabel.setText("Végállomás: " + (selectedBusz.getVegAllomas() != null ? selectedBusz.getVegAllomas().getId() : "Nincs"));
            biokerozinLabel.setText("Állapot: " + (selectedBusz.getKimaradoKorok() > 0 ? "Karambolozott!" : "Úton"));
            zuzottKoLabel.setText("");
            kotrofejLabel.setText("");

            csereButton.setEnabled(false);
            buyButton.setEnabled(false);
            takaritButton.setEnabled(false);

        } else {
            idLabel.setText("ID: -");
            penzLabel.setText("Nincs jármű kiválasztva");
            soLabel.setText("");
            biokerozinLabel.setText("");
            zuzottKoLabel.setText("");
            kotrofejLabel.setText("");

            csereButton.setEnabled(false);
            buyButton.setEnabled(false);
            takaritButton.setEnabled(false);
        }
    }
}