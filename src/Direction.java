/**
 * Enumération des directions possibles pour se déplacer sur une grille.
 * Chaque direction est associée à un déplacement en ligne et en colonne.
 */
public enum Direction{
    HAUT_GAUCHE(-1, -1),
    HAUT(-1, 0),
    HAUT_DROITE(-1, 1),
    GAUCHE(0, -1),
    DROITE(0, 1),
    BAS_GAUCHE(1, -1),
    BAS(1, 0),
    BAS_DROITE(1, 1);
    private int offsetL; // déplacement en ligne
    private int offsetC; // déplacement en colonne

    /**
     * Constructeur de la classe Direction.
     * @param offsetL déplacement en ligne
     * @param offsetC déplacement en colonne
     */
    Direction(int offsetL, int offsetC){
        this.offsetL = offsetL;
        this.offsetC = offsetC;
    }


    /**
     * Retourne le déplacement en ligne.
     * @return le déplacement en ligne
     */
    public int getOffsetC() {
        return offsetC;
    }

    /**
     * Retourne le déplacement en colonne.
     * @return le déplacement en colonne
     */
    public int getOffsetL() {
        return offsetL;
    }

    /**
     * Retourne les coordonnées du voisin de la case courante dans la direction donnée.
     * @param l ligne de la case courante
     * @param c colonne de la case courante
     * @return les coordonnées du voisin de la case courante dans la direction donnée
     */
    public Coords getVoisin(int l, int c){
        return new Coords(getOffsetL()+l, getOffsetC()+c);
    }

    /**
     * Retourne les coordonnées des voisins de la case courante.
     * @param l ligne de la case courante
     * @param c colonne de la case courante
     * @return les coordonnées des voisins de la case courante
     */
    public static Coords[] voisins(int l, int c){
        Coords[] voisins = new Coords[values().length];
        for (int i = 0; i < values().length; i++) {
            voisins[i] = new Coords(values()[i].getOffsetL()+l, values()[i].getOffsetC()+c);
        }
        return voisins;
    }

    /**
     * Retourne les coordonnées des voisins de la case courante.
     * @return les coordonnées des voisins de la case courante
     */
    public static Coords[] voisins(){
        return Direction.voisins(0, 0);
    }

    /**
     * Retourne la direction opposée à la direction courante.
     * @return la direction opposée à la direction courante
     */
    @Override
    public String toString() {
        switch(this){
            case HAUT_GAUCHE:
                return "HG";
            case HAUT:
                return "H";
            case HAUT_DROITE:
                return "HD";
            case GAUCHE:
                return "G";
            case DROITE:
                return "D";
            case BAS_GAUCHE:
                return "BG";
            case BAS:
                return "B";
            case BAS_DROITE:
                return "BD";
            default:
                return "";
        }
    }
}