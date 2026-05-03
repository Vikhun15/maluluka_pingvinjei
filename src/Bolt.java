package src;

public class Bolt extends Epulet {

    //A Hókotró ára
    private static final int hokotroAr = 100;

    public void elad(ITargy termek, Hokotro vevo){
            if(termek.getAr() <= vevo.getPenz()){
                vevo.levonPenz(vevo.getPenz() - termek.getAr());
                System.out.println("Sikeres vásárlás! Maradék pénz: " + vevo.getPenz());
            } else {
                System.out.println("Nincs elég pénzed a vásárláshoz!");
            }
    }

    public void eladHokotro(Hokotro vevo){
        if(hokotroAr <= vevo.getPenz()){
            vevo.levonPenz(hokotroAr);
            System.out.println("Sikeres vásárlás! Maradék pénz: " + vevo.getPenz());
        } else {
            System.out.println("Nincs elég pénzed a vásárláshoz!");
        }
    }
}
