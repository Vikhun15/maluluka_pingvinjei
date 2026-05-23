package Views;

import Controllers.GameController;
import Models.Palya;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameWindow extends JFrame {
    private GameCanvas canvas;
    private VehicleControlPanel controlPanel;
    private StatusBar statusBar;
    private GameController controller;

    public GameWindow(Palya palya, GameController controller) {
        super("Hókotró Szimulátor 2026");
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Komponensek inicializálása
        this.canvas = new GameCanvas(palya);
        this.controlPanel = new VehicleControlPanel(controller);
        this.statusBar = new StatusBar();

        // Elrendezés a dokumentum alapján
        this.add(canvas, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.EAST);
        this.add(statusBar, BorderLayout.SOUTH);

        initMenu();

        this.setSize(1024, 768);
        this.setLocationRelativeTo(null);
        this.addKeyListener(controller);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Fájl menü
        JMenu fileMenu = new JMenu("Fájl");
        JMenuItem loadItem = new JMenuItem("Pálya betöltése...");
        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Szimuláció mentés (*.dat)", "dat"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.handleLoad(fileChooser.getSelectedFile());

            }
        });
        JMenuItem saveItem = getSaveItem();
        JMenuItem exitItem = new JMenuItem("Kilépés");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(loadItem); fileMenu.add(saveItem); fileMenu.add(exitItem);

        // Szimuláció menü
        JMenu simMenu = new JMenu("Szimuláció");
        JMenuItem step1Item = new JMenuItem("Léptetés 1x (Space)");
        step1Item.addActionListener(e -> controller.handleStep());
        JMenuItem step5Item = new JMenuItem("Léptetés 5x (Ctrl+Space)");
        step5Item.addActionListener(e -> {;
            for (int i = 0; i < 5; i++) {
                controller.handleStep();
            }
        });
        JCheckBoxMenuItem randomItem = new JCheckBoxMenuItem("Véletlenszerűség BE/KI (R)");
        randomItem.addActionListener(e -> controller.toggleRandomness(randomItem.isSelected()));
        simMenu.add(step1Item); simMenu.add(step5Item); simMenu.add(randomItem);

        // Pálya és Jármű menük
        JMenu palyaMenu = new JMenu("Pálya");
        palyaMenu.add(new JMenuItem("Csomópont hozzáadása"));

        JMenu jarmuMenu = new JMenu("Jármű");
        jarmuMenu.add(new JMenuItem("Jármű mozgatása"));

        menuBar.add(fileMenu);
        menuBar.add(simMenu);
        menuBar.add(palyaMenu);
        menuBar.add(jarmuMenu);

        this.setJMenuBar(menuBar);

        // Eseménykezelők bekötése (példa)
        step1Item.addActionListener(e -> controller.handleStep());
        exitItem.addActionListener(e -> System.exit(0));
    }

    private JMenuItem getSaveItem() {
        JMenuItem saveItem = new JMenuItem("Állapot mentése...");
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter(
                            "Szimuláció mentés (*.dat)", "dat"
                    )
            );
            fileChooser.setSelectedFile(new File("savegame.dat"));

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
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