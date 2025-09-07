
import javafx.scene.shape.Circle;

public class Pion extends Circle{
    public static final double SIZE = 20;
    private Equipe equipe;
    public Pion(){
        this.equipe = Equipe.AUCUNE;
        init();
    }

    /**
     * Crée un pion avec une équipe
     * @param equipe
     */
    public Pion(Equipe equipe){
        this.equipe = equipe;
        init();
    }

    /**
     * Crée un pion avec une équipe et une position
     * @param equipe
     * @param x
     * @param y
     */
    private void init(){
        this.setRadius(SIZE);
        update();
    }

    /**
     * Met à jour la couleur du pion
     */
    public void update(){
        this.setFill(equipe.getColor());
    }

    /**
     * Change l'équipe du pion
     * @param equipe
     */
    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
        update();
    }

    /**
     * Retourne l'équipe du pion
     * @return
     */
    public Equipe getEquipe() {
        return equipe;
    }

    /**
     * Retourne si le pion est vide
     * @return
     */
    public boolean estVide(){
        return this.equipe == Equipe.AUCUNE;
    }

    /**
     * Réinitialise le pion
     */
    public void reset(){
        this.equipe = Equipe.AUCUNE;
        update();
    }
}
