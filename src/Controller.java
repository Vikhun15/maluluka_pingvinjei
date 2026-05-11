package src;

/**
 * A Controller osztály a játék vezérlésért felelős az MVC architektúrában.
 * Kezeli a felhasználói bemeneteket és módosítja a modellt.
 */
public class Controller {
    private final java.util.Scanner scanner = new java.util.Scanner(System.in);
    private final GameStateManager gameStateManager = new GameStateManager();
    private final TestManager testManager = new TestManager();

    /**
     * Elindítja a játék fő ciklusát.
     *
     * @param palya a játék pályája
     * @param view a nézet, amely megjeleníti az információkat
     */
    public void run(Palya palya, View view) {
        while (true) {
            view.showMenu();
            String command = getInput("Enter command: ");
            if (!processCommand(command, palya, view)) {
                break;
            }
        }
    }

    /**
     * Elindítja a játék fő ciklusát a menü nélkül.
     *
     * @param palya a játék pályája
     * @param view a nézet, amely megjeleníti az információkat
     */
    public void runWithoutBloat(Palya palya, View view) {
        while (true) {
            String command = getInput("Enter command: ");
            if (!processCommand(command, palya, view)) {
                break;
            }
        }
    }

    /**
     * Feldolgozza a felhasználó parancsát.
     *
     * @param command a parancs szövege
     * @param palya a játék pályája
     * @param view a nézet
     * @return igaz, ha a játéknak folytatódnia kell; hamis, ha kilépni kell
     */
    private boolean processCommand(String command, Palya palya, View view) {
        String[] parts = command.trim().split("\\s+");
        if (parts.length == 0) return true;
        String cmd = parts[0].toLowerCase();
        switch (cmd) {
            case "move":
                if (parts.length < 3) {
                    view.showError("Usage: move <vehicle_id> <lane_id>");
                    return true;
                }
                try {
                    int vehicleId = Integer.parseInt(parts[1]);
                    int laneId = Integer.parseInt(parts[2]);
                    if (vehicleId >= 0 && vehicleId < palya.getJarmuvek().size()) {
                        Jarmu j = palya.getJarmuvek().get(vehicleId);
                        Sav targetSav = null;
                        for (Utszakasz ut : palya.getUtszakaszok()) {
                            for (Sav s : ut.getSavok()) {
                                if (s.id == laneId) {
                                    targetSav = s;
                                    break;
                                }
                            }
                            if (targetSav != null) break;
                        }
                        if (targetSav != null) {
                            j.mozog(targetSav);
                            view.showMessage("Move successful.");
                        } else {
                            view.showError("Lane not found.");
                        }
                    } else {
                        view.showError("Invalid vehicle ID.");
                    }
                } catch (NumberFormatException e) {
                    view.showError("Invalid number format.");
                }
                break;
            case "step":
                for (Utszakasz u : palya.getUtszakaszok()) {
                    u.hoEsik();
                }
                for (Jarmu j : palya.getJarmuvek()) {
                    j.korFrissites();
                }
                view.showMessage("Step executed.");
                break;
            case "stat":
                break;
            case "save":
                gameStateManager.saveGame(palya);
                view.showMessage("Game saved successfully.");
                break;
            case "load":
                palya = gameStateManager.loadGame();
                view.showMessage("Game loaded successfully.");
                break;
            case "test":
                testManager.runTests();
                break;
            case "quit":
            case "exit":
                return false;
            default:
                view.showError("Unknown command: " + cmd);
        }
        return true;
    }

    /**
     * Beolvas egy sort a felhasználótól.
     *
     * @param prompt az prompt üzenet
     * @return a felhasználó által begépelt szöveg
     */
    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
