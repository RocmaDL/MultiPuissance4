
public class Coords{
    private int l;//ligne 
    private int c;//colonne
    
    /**
     * Constructeur de la classe Coords
     * @param l la ligne
     * @param c la colonne
     */
    public Coords(int l, int c){
        this.l = l;
        this.c = c;
    }
    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    public int getL() {return l;}

    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    public int getC() {return c;}

    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    public Coords plus(Coords c1, Coords c2){return new Coords(c1.getL()+c2.getL(), c1.getC()+c2.getC());}

    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    public Coords moins(Coords c1, Coords c2){return new Coords(c1.getL()-c2.getL(), c1.getC()-c2.getC());}

    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    public Coords plus(Coords c1, int l, int c){return new Coords(c1.getL()+l, c1.getC()+c);}

    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    public Coords moins(Coords c1, int l, int c){return new Coords(c1.getL()-l, c1.getC()-c);}

    /**
     * Constructeur de la classe Coords
     * @param c la chaine de caractère contenant les coordonnées
     */
    @Override
    public String toString() {
        return l+";"+c;
    }
}