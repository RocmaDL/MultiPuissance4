import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ControleurConnexion implements EventHandler<ActionEvent> {
    private FenetreConnexion vueConnexion;
    private AppliJeu vueJeu;

    public ControleurConnexion(FenetreConnexion vueConnexion, AppliJeu vueJeu) {
        this.vueConnexion = vueConnexion;
        this.vueJeu = vueJeu;
    }

    @Override
    public void handle(ActionEvent e) {
        String pseudo = this.vueConnexion.getPseudo();
        String port = this.vueConnexion.getPort();
        if (pseudo.isEmpty() || port.isEmpty()) {
            this.vueConnexion.afficherErreur("Veuillez remplir tous les champs");
        } else {
            try {
                System.out.println("Connexion de " + pseudo + " sur le port " + port);
                if (this.vueConnexion.getAddresse().isEmpty()) {
                    this.vueJeu.connexion(pseudo, port);
                } else {
                    this.vueJeu.connexion(pseudo, this.vueConnexion.getAddresse(), port);
                }
            } catch (NumberFormatException ex) {
                this.vueConnexion.afficherErreur("Le port doit être un nombre");
            }
        }
    }

}
