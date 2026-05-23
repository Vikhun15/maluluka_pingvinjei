package Views;

import Controllers.GameController;
import Models.Hokotro;
import Models.Kotrofej;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FejekDialog extends JDialog {
    private final GameController controller;
    private Hokotro vasarloHokotro;
    private JPanel itemsPanel;

    public FejekDialog(JFrame owner, GameController controller) {
        super(owner, "Fej váltás", true);
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setSize(400, 350);
        this.setLocationRelativeTo(getOwner());

        JLabel headerLabel = new JLabel("Az alábbi fejek állnak rendelkezésre:", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(headerLabel, BorderLayout.NORTH);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Bezárás");
        closeButton.addActionListener(e -> this.dispose());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }
    public void showFejek(Hokotro hokotro) {
        this.vasarloHokotro = hokotro;
        itemsPanel.removeAll();

        List<Kotrofej> fejek = hokotro.getBirtokoltFejek();

        if (fejek != null && !fejek.isEmpty()) {
            for (Kotrofej fej : fejek) {
                if(fej == hokotro.getAktualisFej()){
                    addSelectedFejRow(fej);
                }
                else{
                    addItemRow(fej);
                }
            }
        } else {
            itemsPanel.add(new JLabel("Jelenleg nem rendelkezik a hókotró fejekkel."));
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
        this.setVisible(true);
    }

    private void addSelectedFejRow(Kotrofej fej){
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setMaximumSize(new Dimension(360, 35));
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        String labelText = fej.getNev();
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(nameLabel, BorderLayout.CENTER);
        row.setBackground(new Color(200,200,200));

        itemsPanel.add(row);
    }

    private void addItemRow(Kotrofej fej) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setMaximumSize(new Dimension(360, 35));
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        String labelText = fej.getNev();
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(nameLabel, BorderLayout.CENTER);

        JButton csereButton = new JButton("Csere");
        csereButton.addActionListener(e -> {
            controller.handleFejCsere(fej, vasarloHokotro);
            this.dispose();
        });
        row.add(csereButton, BorderLayout.EAST);

        itemsPanel.add(row);
    }
}