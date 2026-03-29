import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {
    private static BufferedReader olvaso = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        String sor;
        int valasztas = 0;

        while (true) {
            System.out.println("\n--- Skeleton Tesztmenü (Valódi Logikával) ---");
            System.out.println("1. - Hóesés sima úton");
            System.out.println("2. - Hóesés alagútban");
            System.out.println("3. - Hóesés hídon");
            System.out.println("4. - Söprő fej használata");
            System.out.println("5. - Mozgás a pályán");
            System.out.println("6. - Boltban vásárlás (Kotrófej)");
            System.out.println("7. - Karambol");
            System.out.println("8. - Sárkány fej használata");
            System.out.println("9. - Jégtörő fej használata");
            System.out.println("10. - Új Hókotró példányosítása");
            System.out.println("11. - Fejcsere a Takarító játékos esetében");
            System.out.println("12. - Só működése (sáv sózása)");
            System.out.println("13. - Só szórása sószórófejjel");
            System.out.println("14. - Autó sávváltása");
            System.out.println("15. - Kereszteződésen áthaladás");
            System.out.println("X - Kilépés");
            System.out.print("Válassz egy menüpontot: ");

            try {
                sor = olvaso.readLine();
                if (sor.equalsIgnoreCase("X")) return;
                valasztas = Integer.parseInt(sor);
            } catch (Exception e) {
                System.out.println("Érvénytelen bemenet.");
                continue;
            }

            System.out.println("\n------------------------------------------------");
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
                default: System.out.println("Nincs ilyen opció."); break;
            }
            System.out.println("------------------------------------------------");
        }
    }

    // --- Use Case Megvalósítások ---

    private static void UseCase_HoesesSima() {
        Utszakasz u = new Utszakasz(new Csomopont(){}, new Csomopont(){}); 
        Sav s = new Sav();
        u.getSavok().add(s);

        System.out.println("Előtte: Sáv járható? " + s.getJarhato());
        System.out.println(""+ s.gethoRetegek());
        System.out.println("Havazás 8 alkalommal a sima úton...");
        for(int i = 0; i < 8; i++){
            u.hoEsik();
            System.out.println(""+ s.gethoRetegek());
        }
        
        System.out.println("Utána: Sáv járható? " + s.getJarhato() + " (8 hóréteg után járhatatlanná vált)");
    }
 
    private static void UseCase_HoesesAlagut() {
        Alagut al = new Alagut(new Csomopont(){}, new Csomopont(){});
        Sav s = new Sav();
        al.getSavok().add(s);

        System.out.println("Előtte: Sáv járható? " + s.getJarhato());
        System.out.println(""+ s.gethoRetegek());
        System.out.println("Havazás 7 alkalommal az alagútban...");
        for(int i = 0; i < 8; i++){
            al.hoEsik();
            System.out.println(""+ s.gethoRetegek());
        }

        System.out.println("Utána: Sáv járható? " + s.getJarhato() + " (Az alagútba nem esik be a hó)");
    }

    private static void UseCase_HoesesHid() {
        Hid h = new Hid(new Csomopont(){}, new Csomopont(){});
        Sav s = new Sav();
        h.getSavok().add(s);

        System.out.println("Előtte: Sáv járható? " + s.getJarhato());
        System.out.println(""+ s.gethoRetegek());
        System.out.println("Havazás 4 alkalommal a hídon (duplán esik!)...");
        for(int i = 0; i < 4; i++){
            h.hoEsik();
            System.out.println(""+ s.gethoRetegek());
        }

        System.out.println("Utána: Sáv járható? " + s.getJarhato() + " (4 havazás * 2 = 8 hóréteg -> járhatatlan)");
    }

    private static void UseCase_SoproFej() {
        Hokotro h = new Hokotro();
        SoproFej sf = new SoproFej(100);
        Sav s = new Sav();
        h.ujFejetBegyujt(sf);

        System.out.println("Hó esik a sávra...");
        s.hoEsik(); s.hoEsik();
        System.out.println(""+ s.gethoRetegek());
        System.out.println("Hókotró söprőfejjel elkezdi a takarítást...");
        h.takarit(s);
        System.out.println(""+ s.gethoRetegek());
        System.out.println("Utána: A sáv takarítva (a hó át lett tolva, a jég megolvadt).");
    }

    private static void UseCase_MozgasPalya() {
        Csomopont cel = new Csomopont(){};
        Utszakasz u = new Utszakasz(new Csomopont(){}, cel);
        Sav s = new Sav();
        s.setUtszakasz(u);
        
        Auto a = new Auto(new Sav());
        System.out.println("Előtte: Autó aktuális sávja be van állítva a cél sávra? " + (a.getAktualisSav() == s));
        
        System.out.println("Autó megpróbál mozogni a sávra...");
        a.mozog(s);
        
        System.out.println("Utána: Autó sikeresen a sávra lépett és megérkezett a cél csomópontba.");
        System.out.println("Autó aktuális sávja a cél sáv lett? " + (a.getAktualisSav() == s));
    }

    private static void UseCase_BoltVasar() {
        Hokotro h = new Hokotro();
        h.penzKeres(200); // Adunk neki pénzt, hogy tudjon vásárolni
        Bolt b = new Bolt();
        HanyoFej ujFej = new HanyoFej(100);

        System.out.println("Előtte: Hókotró pénze: " + h.getPenz() + ", Birtokolt fejek száma: " + h.getBirtokoltFejek().size());
        System.out.println("A Hókotró megpróbál venni egy Hányófejet (Ára: 100)...");
        
        b.elad(ujFej, h);
        
        System.out.println("Utána: Hókotró pénze: " + h.getPenz() + ", Birtokolt fejek száma: " + h.getBirtokoltFejek().size());
    }

    private static void UseCase_Karambol() {
        Csomopont cs = new Csomopont(){};
        Auto a1 = new Auto(null);
        Auto a2 = new Auto(null);

        System.out.println("Első autó belép a csomópontba...");
        cs.jarmuBefogad(a1);
        System.out.println("Első autó kimaradó körei: " + a1.kimaradoKorok);
        
        System.out.println("Második autó is megpróbál belépni ugyanabba a csomópontba (Karambol!)...");
        cs.jarmuBefogad(a2);
        
        System.out.println("Első autó kimaradó körei karambol után: " + a1.kimaradoKorok);
        System.out.println("Második autó kimaradó körei karambol után: " + a2.kimaradoKorok);
    }

    private static void UseCase_SarkanyFej() {
        Hokotro h = new Hokotro(); // Alapból 20 liter kerozinja van
        SarkanyFej sf = new SarkanyFej(200);
        Sav s = new Sav();
        h.ujFejetBegyujt(sf);

        System.out.println("Előtte: Hókotró kerozin szintje: " + h.getFuel().getLiterek() + " liter.");
        System.out.println("Hókotró elkezdi felolvasztani a sávot...");
        
        h.takarit(s);
        
        System.out.println("Utána: Hókotró kerozin szintje: " + h.getFuel().getLiterek() + " liter (5 litert fogyasztott).");
    }

    private static void UseCase_JegtoroFej() {
        Hokotro h = new Hokotro();
        JegtoroFej jf = new JegtoroFej(150);
        h.ujFejetBegyujt(jf);
        Sav s = new Sav();

        System.out.println("4 autó áthalad a sávon, hogy jeges legyen...");
        for(int i=0; i<4; i++) s.jarmuAthalad();
        
        System.out.println("Előtte: A sáv jeges? " + s.jeges());
        System.out.println("Hókotró elkezdi feltörni a jeget...");
        
        h.takarit(s);
        
        System.out.println("Utána: A sáv jeges? " + s.jeges() + " (Jég feltörve)");
    }

    private static void UseCase_UjHokotro() {
        System.out.println("A játékos elvesztette a hókotróját, ezért egy újat indítunk útnak.");
        Hokotro ujHokotro = new Hokotro(new Sav());
        System.out.println("Új hókotró sikeresen létrehozva!");
        System.out.println("Alapértelmezett pénze: " + ujHokotro.getPenz());
        System.out.println("Alapértelmezett só: " + ujHokotro.getSalt().getMennyiseg());
        System.out.println("Alapértelmezett üzemanyag: " + ujHokotro.getFuel().getLiterek());
    }

    private static void UseCase_Fejcsere() {
        Hokotro h = new Hokotro();
        Kotrofej sf = new SoproFej(100);
        Kotrofej jf = new JegtoroFej(150);
        
        h.ujFejetBegyujt(sf); // Alapból ez lesz az aktuális
        System.out.println("Jelenlegi felszerelt fej lecserélése Jégtörő fejre...");
        
        h.fejetCserel(jf);
        if(h.getAktualisFej()==jf) System.out.println("A fej sikeresen lecserélve (referencia frissítve az aktuálisFej attribútumban)."); 
        else System.out.println("A fej nem lett lecserélve.");
    }

    private static void UseCase_SoMukodes() {
        Sav s = new Sav();
        System.out.println("Sáv sózása manuálisan...");
        s.setSozva(true);
        System.out.println("A sáv 'sozva' attribútuma true-ra állt. (A jég elolvadása a kör frissítésénél történne meg)."+ s.getSozva());
    }

    private static void UseCase_SoSzoras() {
        Hokotro h = new Hokotro(); // Alapból 10 egység sója van
        Soszorofej sf = new Soszorofej(80);
        Sav s = new Sav();
        h.ujFejetBegyujt(sf);
        System.out.println("A sáv 'sozva' állapota:"+ s.getSozva());
        System.out.println("Előtte: Hókotró só készlete: " + h.getSalt().getMennyiseg());
        System.out.println("Sószóró fejjel a hókotró leszórja az utat...");
        
        h.takarit(s);
        
        System.out.println("Utána: Hókotró só készlete: " + h.getSalt().getMennyiseg() + " (1 egység elhasználva).");
        System.out.println("A sáv 'sozva' attribútuma true-ra állt. (A jég elolvadása a kör frissítésénél történne meg)."+ s.getSozva());
    }

    private static void UseCase_Savvaltas() {
        Csomopont c1 = new Csomopont(){};
        Csomopont c2 = new Csomopont(){};
        Utszakasz u = new Utszakasz(c1, c2);
        
        Sav induloSav = new Sav();
        Sav celSav = new Sav();
        celSav.setUtszakasz(u);
        
        Auto a = new Auto(induloSav);
        System.out.println("Az autó sávot vált...");
        a.mozog(celSav);
        System.out.println("Az autó sikeresen áthaladt az új sávra."); 
        System.out.println("Az autó aktuális sávja a cél sáv lett? " + (a.getAktualisSav() == celSav));
    }

    private static void UseCase_Keresztezodes() {
        Bolt b = new Bolt(); // A Bolt is egy Csomopont
        Auto a = new Auto(null);
        
        System.out.println("Az autó megérkezik a kereszteződésbe (csomópontba)...");
        b.jarmuBefogad(a);
        
        System.out.println("Kereszteződés állapota: az autó befogadva. Karambol nem történt, mert üres volt.");
        System.out.println("Kimaradó körök száma: " + a.kimaradoKorok);
    }
}