package Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatusBar extends JPanel {
    private final JLabel statusLabel;

    public StatusBar() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(Color.decode("#FFFFFF"));

        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#E0E0E0")),
                new EmptyBorder(6, 12, 6, 12)
        ));

        statusLabel = new JLabel("Kör: 1 | Utolsó esemény: Rendszer készen áll.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.decode("#7F8C8D"));

        this.add(statusLabel);
    }

    public void setStatus(int kor, String esemeny) {
        statusLabel.setText("Kör: " + kor + " | Utolsó esemény: " + esemeny);
    }
}