/**
 * Olyan épület (csomópont), ahol a takarítók a megkeresett PingCoin-ért 
 * cserébe különböző kotrófejeket, sót és biokerozint vásárolhatnak.
 */
public class Bolt extends Epulet {

    public Bolt() {
        super();
    }

    /**
     * Elad egy tárgyat a hókotrónak, ha van rá elég pénze.
     * @param termek A megvásárolni kívánt tárgy (fej, só vagy üzemanyag).
     * @param gep A vásárlást végző hókotró.
     */
    public void elad(ITargy termek, Hokotro gep) {
        
        int ar = termek.getAr(); 
    
        if (gep.getPenz() >= ar) {
            
            gep.penzKeres(-ar);
            System.out.println("Bolt: Sikeres vasarlas! Leveve " + ar + " PingCoin.");
            System.out.println("Bolt: Uj egyenleg: " + gep.getPenz());
            
            // Itt a átkéne adni a terméket
            
        } else {
            System.out.println("Bolt: Sikertelen vasarlas! Nincs eleg penzed.");
            System.out.println("Bolt: A termek ara: " + ar + ", de neked csak " + gep.getPenz() + " van.");
        }
    }
}