package Views;

import Controllers.GameController;
import Models.Bolt;
import Models.Hokotro;
import Models.ITargy;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShopDialog extends JDialog {
    private final GameController controller;
    private Hokotro vasarloHokotro;
    private JPanel itemsPanel;
    private Bolt bolt;

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
        this.bolt = bolt;
        itemsPanel.removeAll();

        List<ITargy> kinalat = bolt.getKinalat();

        if (kinalat != null && !kinalat.isEmpty()) {
            for (ITargy termek : kinalat) {
                if(!hokotro.hasFej(termek)){
                    addItemRow(termek, hokotro.getPenz() >= termek.getAr());
                }
            }
        } else {
            itemsPanel.add(new JLabel("A kínálat jelenleg üres."));
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
        this.setVisible(true);
    }

    private void addItemRow(ITargy termek, boolean canBuy) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setMaximumSize(new Dimension(360, 35));
        row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String labelText = termek.getNev() + " - " + termek.getAr() + " PingCoin";
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(nameLabel, BorderLayout.CENTER);

        JButton buyButton = new JButton("Vásárlás");

        if(!canBuy){
            row.setBackground(new Color(255,100,100));
            row.setForeground(new Color(255,255,255));
            buyButton.setEnabled(false);
        }

        buyButton.addActionListener(e -> {
            controller.handleProductPurchase(termek, vasarloHokotro);
            reload();
        });
        row.add(buyButton, BorderLayout.EAST);

        itemsPanel.add(row);
    }

    private void reload(){
        itemsPanel.removeAll();

        List<ITargy> kinalat = bolt.getKinalat();

        if (kinalat != null && !kinalat.isEmpty()) {
            for (ITargy termek : kinalat) {
                if(!vasarloHokotro.hasFej(termek)){
                    addItemRow(termek, vasarloHokotro.getPenz() >= termek.getAr());
                }
            }
        } else {
            itemsPanel.add(new JLabel("A kínálat jelenleg üres."));
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
}