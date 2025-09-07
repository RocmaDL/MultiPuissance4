
import javafx.scene.paint.Color;

/**
 * Enumération des équipes possibles
 */
public enum Equipe {
    AUCUNE(0, Color.WHITE, "V", " "),
    JAUNE(1, Color.GOLD, "J", "jaunes"),
    ROUGE(2, Color.RED, "R", "rouges");

    private int id; // Identifiant de l'équipe
    private Color color; // Couleur de l'équipe
    private String symbole; // Symbole de l'équipe
    private String nom; // Nom de l'équipe

    /**
     * Constructeur de l'équipe
     * 
     * @param id      Identifiant de l'équipe
     * @param color   Couleur de l'équipe
     * @param symbole Symbole de l'équipe
     * @param nom     Nom de l'équipe
     */
    Equipe(int id, Color color, String symbole, String nom) {
        this.id = id;
        this.color = color;
        this.symbole = symbole;
        this.nom = nom;
    }

    /**
     * Retourne l'identifiant de l'équipe
     * 
     * @return Identifiant de l'équipe
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne la couleur de l'équipe
     * 
     * @return Couleur de l'équipe
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retourne le symbole de l'équipe
     * 
     * @return Symbole de l'équipe
     */
    public String getSymbole() {
        return symbole;
    }

    /**
     * Retourne le nom de l'équipe
     * 
     * @return Nom de l'équipe
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne l'équipe correspondant à l'identifiant
     * 
     * @param id Identifiant de l'équipe
     * @return Equipe correspondant à l'identifiant
     */
    public Equipe valueOf(int id) {
        for (Equipe e : Equipe.values()) {
            if (e.getId() == id) {
                return e;
            }
        }
        return AUCUNE;
    }

    /**
     * Retourne l'équipe correspondant à la couleur
     * 
     * @param color Couleur de l'équipe
     * @return Equipe correspondant à la couleur
     */
    public Equipe valueOf(Color color) {
        for (Equipe e : Equipe.values()) {
            if (e.getColor() == color) {
                return e;
            }
        }
        return AUCUNE;
    }

    public static Equipe get(String symbole) {
        for (Equipe e : Equipe.values()) {
            if (e.getSymbole().equals(symbole)) {
                return e;
            }
        }
        return AUCUNE;
    }

    /**
     * Retourne l'équipe correspondant au symbole
     * 
     * @param symbole Symbole de l'équipe
     * @return Equipe correspondant au symbole
     */
    @Override
    public String toString() {
        return this.getSymbole();
    }
}
