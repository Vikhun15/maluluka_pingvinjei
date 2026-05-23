package Views;

import Controllers.GameController;
import Models.Palya;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.Enumeration;

public class GameWindow extends JFrame {
    private GameCanvas canvas;
    private VehicleControlPanel controlPanel;
    private StatusBar statusBar;
    private GameController controller;

    public GameWindow(Palya palya, GameController controller) {
        super("Hókotró Szimulátor 2026");

        setupModernUI();

        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setContentPane(mainPanel);

        this.canvas = new GameCanvas(palya);
        this.controlPanel = new VehicleControlPanel(controller);
        this.statusBar = new StatusBar();

        mainPanel.add(canvas, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.EAST);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        initMenu();

        this.setSize(1100, 768);
        this.setLocationRelativeTo(null);

        this.addKeyListener(controller);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Rákényszeríti a Swinget egy modern, natív kinézetre és lecseréli a globális betűtípust.
     */
    private void setupModernUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof javax.swing.plaf.FontUIResource) {
                    UIManager.put(key, modernFont);
                }
            }

            UIManager.put("Button.arc", 5);
            UIManager.put("Panel.background", Color.decode("#F5F6FA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new EmptyBorder(5, 5, 5, 5));

        JMenu fileMenu = new JMenu("Fájl");
        JMenuItem loadItem = new JMenuItem("Pálya betöltése...");
        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Szimuláció mentés (*.dat)", "dat"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                controller.handleLoad(fileChooser.getSelectedFile());
            }
        });
        JMenuItem saveItem = getSaveItem();
        JMenuItem exitItem = new JMenuItem("Kilépés");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu simMenu = new JMenu("Szimuláció");
        JMenuItem step1Item = new JMenuItem("Léptetés 1x (Space)");
        step1Item.addActionListener(e -> controller.handleStep());

        JMenuItem step5Item = new JMenuItem("Léptetés 5x (Ctrl+Space)");
        step5Item.addActionListener(e -> {
            for (int i = 0; i < 5; i++) controller.handleStep();
        });

        JCheckBoxMenuItem randomItem = new JCheckBoxMenuItem("Véletlenszerűség BE/KI (R)");

        simMenu.add(step1Item);
        simMenu.add(step5Item);
        simMenu.addSeparator();
        simMenu.add(randomItem);

        JMenu palyaMenu = new JMenu("Pálya");
        palyaMenu.add(new JMenuItem("Csomópont hozzáadása"));

        JMenu jarmuMenu = new JMenu("Jármű");
        jarmuMenu.add(new JMenuItem("Jármű mozgatása"));

        menuBar.add(fileMenu);
        menuBar.add(simMenu);
        menuBar.add(palyaMenu);
        menuBar.add(jarmuMenu);

        this.setJMenuBar(menuBar);
    }

    private JMenuItem getSaveItem() {
        JMenuItem saveItem = new JMenuItem("Állapot mentése...");
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Szimuláció mentés (*.dat)", "dat"));
            fileChooser.setSelectedFile(new File("savegame.dat"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".dat")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".dat");
                }
                controller.handleSave(selectedFile);
            }
        });
        return saveItem;
    }

    public StatusBar getStatusBar() { return statusBar; }
    public VehicleControlPanel getControlPanel() { return controlPanel; }
    public GameCanvas getCanvas() { return canvas; }
}