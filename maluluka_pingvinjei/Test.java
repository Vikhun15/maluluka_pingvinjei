import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Test {
    private static BufferedReader olvaso = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        String sor;
        int valasztas = 0;

        while (true) {
            System.out.println("--- Skeleton Tesztmenü ---");
            System.out.println("1. - Hóesés sima úton");
            System.out.println("2. - Hóesés alagútban");
            System.out.println("3. - Hóesés hídon");
            System.out.println("4. - Söprő fej használata");
            System.out.println("5. - Mozgás a pályán");
            System.out.println("6. - Boltban vásárlás");
            System.out.println("7. - Karambol");
            System.out.println("8. - Sárkány fej használata");
            System.out.println("9. - Jégtörő fej használata");
            System.out.println("10. - Hókotró karambol esetében új hókotró vétele");
            System.out.println("11. - Fejcsere a Takarító játékos esetében");
            System.out.println("12. - Só működése");
            System.out.println("13. - Só szórása");
            System.out.println("14. - Sávváltás");
            System.out.println("15. - Kereszteződés");
            System.out.println("X - Kilépés");

            try {
                sor = olvaso.readLine();
                if (sor.equalsIgnoreCase("X")) return;
                valasztas = Integer.parseInt(sor);
            } catch (Exception e) {
                System.out.println("Érvénytelen bemenet.");
                continue;
            }

            switch (valasztas) {
                case 1: UseCase_HoesesSima(); break;
                case 2: UseCase_HoesesAlagut(); break;
                case 3: UseCase_HoesesHid(); break;
                case 4: UseCase_SoproFej(); break;
                case 5: UseCase_MozgasPalya(); break;
                case 6: UseCase_BoltVasar(); break;
                case 7: UseCase_Karambol(); break;
                case 8: UseCase_SarkanyFej(); break;
                case 9: UseCase_JegtoroFej(); break;
                case 10: UseCase_UjHokotro(); break;
                case 11: UseCase_Fejcsere(); break;
                case 12: UseCase_SoMukodes(); break;
                case 13: UseCase_SoSzoras(); break;
                case 14: UseCase_Savvaltas(); break;
                case 15: UseCase_Keresztezodes(); break;
            }
        }
    }

    // --- Segédmetódusok a szimulációhoz ---

    private static boolean kerdes(String kerdes) {
        System.out.print(kerdes + " (i/n): ");
        try {
            String valasz = olvaso.readLine();
            return valasz.equalsIgnoreCase("i");
        } catch (IOException e) { return false; }
    }

    private static void naplo(String ki, String kit, String mit) {
        System.out.println(ki + " -> " + kit + "." + mit);
    }

    // --- Use Case Megvalósítások ---

    private static void UseCase_HoesesSima() {
        Utszakasz u = new Utszakasz(new Csomopont(){}, new Csomopont(){}); // Anonim csomópontokkal
        naplo("Tesztelő", "u", "hoEsik()");
        u.hoEsik();
        System.out.println("Sima út: Minden sáv hórétege megnövelve 1-gyel.");
    }

    private static void UseCase_HoesesAlagut() {
        Alagut al = new Alagut(null, null);
        naplo("Tesztelő", "al", "hoEsik()");
        al.hoEsik();
        System.out.println("Alagút: A hóréteg nem változott (felülírva).");
    }

    private static void UseCase_HoesesHid() {
        Hid h = new Hid(null, null);
        naplo("Tesztelő", "h", "hoEsik()");
        h.hoEsik();
        System.out.println("Híd: Minden sáv hórétege megnövelve 2-vel.");
    }

    private static void UseCase_SoproFej() {
        Hokotro h = new Hokotro();
        Sav s = new Sav();
        SoproFej sf = new SoproFej(100);
        h.ujFejetBegyujt(sf);

        naplo("Tesztelő", "h", "takarit(s)");
        h.takarit(s);
        naplo("h", "sf", "hatasKifejtese(s, h)");
        naplo("sf", "s", "havatTol(szomszed)");
        System.out.println("Söprő fej: Hó eltolva a szomszédos sávra.");
    }

    private static void UseCase_MozgasPalya() {
        Auto a = new Auto(null);
        Sav s = new Sav();
        naplo("Tesztelő", "a", "mozog(s)");
        naplo("a", "s", "autoAthalad(a)");
       
        if (kerdes("Áthaladhat az autó a sávon?")) {
            naplo("s", "a", "lepesSikeres()");
            naplo("a", "induloCsomopont", "jarmuKilep(a)");
            naplo("a", "erkezoCsomopont", "jarmuBefogad(a)");
        } else {
            System.out.println("Sikertelen mozgás.");
        }
    }

    private static void UseCase_BoltVasar() {
        Hokotro h = new Hokotro();
        Bolt b = new Bolt();
        ITargy t = new Fuel(50); // Példa tárgy

        naplo("Játékos", "h", "vasarol(t)");
        naplo("h", "b", "elad(t, h)");
        naplo("b", "t", "getAr()");

        if (kerdes("Van elég pénze a hókotrónak?")) {
            naplo("b", "h", "penztLevon(ar)");
            System.out.println("Mit vásároltál? (1-5: Fej, 6-7: Készlet)");
            // Itt a választástól függően fejetCserel() vagy keszletetFrissit()
            System.out.println("Vásárlás sikeres.");
        } else {
            System.out.println("Nincs elég pénz.");
        }
    }

    private static void UseCase_Karambol() {
        Jarmu j1 = new Auto(null);
        Sav s = new Sav();
        naplo("Tesztelő", "j1", "mozog(s)");
        naplo("j1", "s", "jarmuBelep(j1)");

        if (kerdes("Jeges a sáv?")) {
            naplo("s", "j1", "csuszkal()");
            if (kerdes("Történik karambol egy másik járművel?")) {
                naplo("s", "j1", "karambolozik()");
                naplo("s", "j2", "karambolozik()");
                System.out.println("A sáv járhatatlanná vált.");
            }
        }
    }

    private static void UseCase_SarkanyFej() {
        Hokotro h = new Hokotro();
        SarkanyFej sf = new SarkanyFej(200);
        Sav s = new Sav();
        h.ujFejetBegyujt(sf);

        naplo("Takarító", "h", "takarit(s)");
        naplo("h", "sf", "hatasKifejtese(s, h)");
        naplo("sf", "h", "getFuel()");
       
        if (kerdes("Van üzemanyag? (liter > 0)")) {
            naplo("sf", "s", "olvaszt()");
            naplo("sf", "fuel", "fogyaszt(5)");
            System.out.println("Sárkányfej: Hó és jég eltüntetve.");
        }
    }

    private static void UseCase_JegtoroFej() {
        Hokotro h = new Hokotro();
        JegtoroFej jf = new JegtoroFej(150);
        Sav s = new Sav();
       
        naplo("Tesztelő", "h", "takarit(s)");
        naplo("h", "jf", "hatasKifejtese(s, h)");
        naplo("jf", "s", "jegetTor()");
        System.out.println("Jégtörő fej: Jég feltörve (torottJeg = true).");
    }

    private static void UseCase_UjHokotro() {
        Busz tak = new Busz();
        Bolt b = new Bolt();
        Hokotro minta = new Hokotro();

        naplo("Játékos", "tak", "vasarolUjHokotro()");
        naplo("tak", "b", "eladHokotro(minta, tak)");
        naplo("b", "minta", "clone(penz)");
       
        if (kerdes("Van elég pénz az új hókotróra?")) {
            naplo("b", "tak", "penztKeres(-ar)");
            System.out.println("Új hókotró létrehozva és átadva.");
        }
    }

    private static void UseCase_Fejcsere() {
        Hokotro h = new Hokotro();
        Kotrofej ujFej = new HanyoFej(100);
        naplo("Játékos", "h", "fejetCserel(ujFej)");
        h.ujFejetBegyujt(ujFej);
        System.out.println("A hókotró feje lecserélve.");
    }

    private static void UseCase_SoMukodes() {
        Sav s = new Sav();
        s.setSozva(true);
        naplo("Tesztelő", "s", "korFrissites()");
        System.out.println("A sáv sózva van: Jég elolvadt, hóréteg csökkentve 2-vel.");
    }

    private static void UseCase_SoSzoras() {
        Hokotro h = new Hokotro();
        Soszorofej sf = new Soszorofej(80);
        Sav s = new Sav();
        h.ujFejetBegyujt(sf);

        naplo("Takarító", "h", "takarit(s)");
        naplo("h", "sf", "hatasKifejtese(s, h)");
        naplo("sf", "h", "getSalt()");
        naplo("sf", "salt", "fogyaszt(1)");
        naplo("sf", "s", "setSozva(true)");
        System.out.println("A sávot megsózták.");
    }

    private static void UseCase_Savvaltas() {
        Auto a = new Auto(null);
        Sav cel = new Sav();
        naplo("Tesztelő", "a", "mozog(cel)");
        naplo("a", "cel", "autoAthalad(a)");
        System.out.println("Sávváltás elindítva.");
    }

    private static void UseCase_Keresztezodes() {
        Auto a = new Auto(null);
        Csomopont cs = new Bolt(); // Épület is csomópont
        Sav kovetkezoSav = new Sav();

        naplo("Folyamat", "cs", "jarmuBefogad(a)");
        if (kerdes("Van szabad kapacitás a csomópontban?")) {
            System.out.println("Autó a csomópontban várakozik.");
            naplo("Tesztelő", "a", "mozog(kovetkezoSav)");
            naplo("a", "cs", "jarmuKilep(a)");
            naplo("a", "kovetkezoSav", "autoAthalad(a)");
        }
    }
}