package src.Models;
import src.Views.*;
import src.Controllers.*;

/**
 * A Main osztály a program belépési pontja.
 */
public class Main {
    /**
     * A program fő metódusa.
     *
     * @param args parancssori argumentumok
     */
    public static void main(String[] args) {
        Palya palya = new Palya();
        View view = new View();
        Controller controller = new Controller();
        controller.run(palya, view);
    }
}
