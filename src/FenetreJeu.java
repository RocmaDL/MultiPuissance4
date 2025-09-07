import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.stage.Stage;

public class FenetreJeu extends BorderPane {
    private ModeleJeu modele;
    private Grille grille;
    private Label tourL;
    private Label scoreJ;
    private Label scoreR;
    private Client client;
    private Alert popup;

    public FenetreJeu(ModeleJeu modele, Client client) {
        this.client = client;
        this.modele = modele;
        this.setPadding(new Insets(10));
        Text pseudo = new Text("Pseudo : " + this.client.getPseudo());
        pseudo.setFont(new Font(20));
        Text couleurJoueur = new Text("Tu es les " + this.client.getEquipe().getNom());
        couleurJoueur.setFont(new Font(20));
        this.tourL = new Label("Au tour des " + this.modele.getJoueur().getNom());
        this.tourL.setFont(new Font(20));
        VBox vbTop = new VBox();
        vbTop.getChildren().addAll(pseudo, couleurJoueur, this.tourL);
        this.setTop(vbTop);

        Label s = new Label("Score");
        s.setFont(new Font(20));

        this.scoreJ = new Label("0");
        this.scoreR = new Label("0");
        this.scoreJ.setFont(new Font(20));
        this.scoreR.setFont(new Font(20));
        this.scoreJ.setTextFill(Equipe.JAUNE.getColor());
        this.scoreR.setTextFill(Equipe.ROUGE.getColor());

        VBox vb = new VBox();
        vb.getChildren().addAll(s, this.scoreJ, this.scoreR);
        this.setRight(vb);

        this.grille = new Grille();
        this.setCenter(this.grille);
    }

    public void maj(Change change) {
        this.grille.maj(this.modele, change);
        this.tourL.setText("Au tour des " + this.modele.getJoueur().getNom());
        if (this.modele.estPerdu()) {
            Platform.runLater(() -> {
                this.popup = popUpMatchNul();
                popup.setOnCloseRequest((e) -> {
                    // Retirer l'appel à this.reset() et la mise à jour du plateau ici
                });
                this.popup.show();
            });
        } else if (this.modele.estGagnee()) {

            Alert popup = popUpGagnee(this.modele.getGagnant());
            popup.setOnCloseRequest((e) -> {
                // Retirer l'appel à this.reset() et la mise à jour du plateau ici
            });
            if (this.modele.getGagnant() == Equipe.JAUNE) {
                this.scoreJ.setText(Integer.toString(Integer.parseInt(this.scoreJ.getText()) + 1));
            } else if (this.modele.getGagnant() == Equipe.ROUGE) {
                this.scoreR.setText(Integer.toString(Integer.parseInt(this.scoreR.getText()) + 1));
            }
            this.popup.show();

        }
    }

    public void maj(int c, Equipe equipe, boolean drop) {
        this.grille.maj(this.modele, c, equipe, drop);
    }

    public Alert popUpGagnee(Equipe gagnant) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Les " + gagnant.getNom() + " ont gagnés !");
        alert.setHeaderText("L'équipe des " + gagnant.getNom() + " a gagné !");
        alert.setContentText("Voulez-vous rejouer ?");
        alert.getButtonTypes().clear();
        Button oui = new Button("Oui");
        Button non = new Button("Non");
        HBox hb = new HBox();
        hb.getChildren().addAll(oui, non);
        alert.getDialogPane().setContent(hb);
        oui.setOnAction((e) -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
            this.client.rejouer();
            this.reset(); // Appel à reset() ici
            this.grille.maj(this.modele, Change.RIEN);
            this.tourL.setText("Au tour des " + this.modele.getJoueur().getNom());
        });
        non.setOnAction((e) -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
        });
        return alert;
    }

    public Alert popUpMatchNul() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Match nul");
        alert.setHeaderText("Match nul, les deux équipes ont remplies le plateau");
        alert.setContentText("Voulez-vous rejouer ?");
        alert.getButtonTypes().clear();
        Button oui = new Button("Oui");
        Button non = new Button("Non");
        HBox hb = new HBox();
        hb.getChildren().addAll(oui, non);
        alert.getDialogPane().setContent(hb);
        oui.setOnAction((e) -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
            this.client.rejouer();
            this.grille.maj(this.modele, Change.RIEN);
            this.tourL.setText("Au tour des " + this.modele.getJoueur().getNom());
        });
        non.setOnAction((e) -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
        });
        return alert;
    }

    public Alert popUpPlusDeJoueur() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Plus de joueur");
        alert.setHeaderText("Il n'y a plus de joueur dans la partie");
        alert.setContentText("Voulez-vous quitter ?");
        alert.getButtonTypes().clear();
        Button oui = new Button("Oui");
        Button non = new Button("Non");
        HBox hb = new HBox();
        hb.getChildren().addAll(oui, non);
        alert.getDialogPane().setContent(hb);
        oui.setOnAction((e) -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
            this.client.close();
        });
        non.setOnAction((e) -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
        });
        return alert;
    }

    public void plusDeJoueur() {
        Platform.runLater(() -> {
            this.popup = popUpPlusDeJoueur();
            popup.show();
        });
    }

    public void showPopPup() {
        Platform.runLater(() -> {
            this.popup.show();
        });
    }

    public void reset() {
        this.modele.reset();
        this.grille.reset();
    }
}