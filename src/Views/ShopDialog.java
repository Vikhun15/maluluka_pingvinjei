package Views;

import Controllers.GameController;
import Models.Bolt;
import Models.Hokotro;
import Models.ITargy; // Az interfész beimportálása
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShopDialog extends JDialog {
    private final GameController controller;
    private Hokotro vasarloHokotro;
    private JPanel itemsPanel;

    public ShopDialog(JFrame owner, GameController controller) {
        super(owner, "Bolt Interakció", true);
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setSize(400, 350);
        this.setLocationRelativeTo(getOwner());

        JLabel headerLabel = new JLabel("Üdvözöljük! Válasszon az alábbi kínálatból:", SwingConstants.CENTER);
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

    /**
     * Megjeleníti a boltot a modellből kapott objektumok alapján.
     */
    public void showShop(Bolt bolt, Hokotro hokotro) {
        this.vasarloHokotro = hokotro;
        itemsPanel.removeAll();

        // 1. A Bolt adja vissza a megvásárolható ITargy objektumok listáját!
        // (A Benzinkut itt automatikusan egy szűkebb listát fog visszaadni a polimorfizmus miatt)
        List<ITargy> kinalat = bolt.getKinalat();

        if (kinalat != null && !kinalat.isEmpty()) {
            for (ITargy termek : kinalat) {
                addItemRow(termek);
            }
        } else {
            itemsPanel.add(new JLabel("A kínálat jelenleg üres."));
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
        this.setVisible(true);
    }

    private void addItemRow(ITargy termek) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setMaximumSize(new Dimension(360, 35));
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // 2. Kiírjuk a termék nevét ÉS az árát a modellből lekérve
        // Ehhez az ITargy interfészben lennie kell getNev() és getAr() metódusoknak.
        String labelText = termek.getNev() + " - " + termek.getAr() + " PingCoin";
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(nameLabel, BorderLayout.CENTER);

        JButton buyButton = new JButton("Vásárlás");
        buyButton.addActionListener(e -> {
            controller.handleProductPurchase(termek, vasarloHokotro);
            this.dispose();
        });
        row.add(buyButton, BorderLayout.EAST);

        itemsPanel.add(row);
    }
}