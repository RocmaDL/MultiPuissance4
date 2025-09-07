
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Classe qui gère les événements clavier
 */
public class ControleurClavier implements EventHandler<KeyEvent> {
    private AppliJeu vueJeu;
    private ModeleJeu modeleJeu;

    public ControleurClavier(AppliJeu vueJeu, ModeleJeu modeleJeu) {
        this.vueJeu = vueJeu;
        this.modeleJeu = modeleJeu;
    }

    /**
     * Méthode qui gère les événements clavier
     * 
     * @param e l'événement clavier
     */
    @Override
    public void handle(KeyEvent e) {
        if (this.modeleJeu.estGagnee() || AppliJeu.WAITING || this.modeleJeu.estPerdu()) {
            this.vueJeu.showPopPup();
            return;
        }
        if (!this.modeleJeu.estVide() && this.modeleJeu.estGagnee() && this.modeleJeu.estPerdu()) {
        }
        KeyCode k = e.getCode();
        System.out.println(this.vueJeu.getClient().getEquipe().equals(this.modeleJeu.getJoueur())
                && this.vueJeu.getClient().getPseudoAdversaire() != null);
        System.out.println(this.vueJeu.getClient().getPseudoAdversaire());
        if (this.vueJeu.getClient().getPseudoAdversaire() != null) {
            if (k == KeyCode.LEFT) {
                if (this.vueJeu.getClient().getEquipe().equals(this.modeleJeu.getJoueur())) {
                    this.modeleJeu.selectionGauche();
                    this.vueJeu.maj(Change.LEFT);
                    this.vueJeu.getClient()
                            .send("SELECT " + this.modeleJeu.getSelectIndex() + " "
                                    + this.vueJeu.getClient().getEquipe());
                }
            } else if (k == KeyCode.RIGHT) {
                if (this.vueJeu.getClient().getEquipe().equals(this.modeleJeu.getJoueur())) {
                    this.modeleJeu.selectionDroite();
                    this.vueJeu.maj(Change.RIGHT);
                    this.vueJeu.getClient()
                            .send("SELECT " + this.modeleJeu.getSelectIndex() + " "
                                    + this.vueJeu.getClient().getEquipe());
                }
            } else if (k == KeyCode.ENTER || k == KeyCode.SPACE) {
                if (this.vueJeu.getClient().getEquipe().equals(this.modeleJeu.getJoueur())) {
                    this.vueJeu.getClient()
                            .send("DROP " + this.modeleJeu.getSelectIndex() + " "
                                    + this.vueJeu.getClient().getEquipe());
                    this.modeleJeu.drop();
                    this.vueJeu.maj(Change.DROP);
                    System.out
                            .println("DROP " + this.modeleJeu.getSelectIndex() + " "
                                    + this.vueJeu.getClient().getEquipe());

                }
            }

        }
    }
}
