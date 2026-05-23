package Views;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private JLabel statusLabel;

    public StatusBar() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Kör: 0 | Utolsó esemény: Kész.");
        this.add(statusLabel);
        this.setBorder(BorderFactory.createEtchedBorder());
    }

    public void setStatus(int kor, String esemeny) {
        statusLabel.setText("Kör: " + kor + " | Utolsó esemény: " + esemeny);
    }
}