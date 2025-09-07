import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;

public class FenetreConnexion extends BorderPane {
    private TextField pseudo;
    private TextField addresse;
    private TextField port;
    private Button connexion;
    private Label erreur;
    private Label titre;
    private AppliJeu appliJeu;

    public FenetreConnexion(AppliJeu appliJeu) {
        this.appliJeu = appliJeu;
        this.setPadding(new Insets(10));
        this.pseudo = new TextField();
        this.pseudo.setPromptText("Pseudo");
        this.pseudo.setMaxWidth(200);
        this.addresse = new TextField();
        this.addresse.setPromptText("Adresse IP du serveur");
        this.addresse.setMaxWidth(200);
        this.port = new TextField();
        this.port.setPromptText("Port");
        this.port.setMaxWidth(200);
        this.connexion = new Button("Connexion");
        this.erreur = new Label();
        this.erreur.setTextFill(Color.RED);
        this.titre = new Label("Connexion");
        this.titre.setFont(new Font(20));
        this.connexion.setOnAction(new ControleurConnexion(this, appliJeu));

        VBox vb = new VBox();
        vb.getChildren().addAll(this.titre, this.pseudo, this.addresse, this.port, this.connexion, this.erreur);
        vb.setAlignment(Pos.CENTER);
        this.setCenter(vb);
    }

    public String getPseudo() {
        return this.pseudo.getText();
    }

    public String getPort() {
        return this.port.getText();
    }

    public String getAddresse() {
        return this.addresse.getText();
    }

    public void afficherErreur(String message) {
        this.erreur.setText(message);
    }
}
