import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe principale de l'application du jeu Puissance 4.
 * Cette classe étend la classe Application de JavaFX et représente l'interface
 * graphique du jeu.
 */
public class AppliJeu extends Application {
    private ModeleJeu modele;
    public static boolean WAITING;
    private Scene scene;
    private BorderPane root;
    private Client client;

    /**
     * Constructeur de la classe AppliJeu
     */
    @Override
    public void init() throws Exception {
        this.modele = new ModeleJeu(Equipe.JAUNE);
        WAITING = false;
    }

    /**
     * Méthode start de la classe Application
     * 
     * @param stage La fenêtre de l'application
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        BorderPane top = new BorderPane();
        top.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label titre = new Label("Puissance 4 ");

        titre.setFont(new Font(40));
        top.setLeft(titre);
        root.setTop(top);
        fenetreConnexion();
        this.scene = new Scene(root, 600, 500);
        stage.setTitle("Puissance 4");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((e) -> {
            this.client.close();
        });
    }

    /**
     * Créer la fenetre du jeu
     * 
     * @return Pane de la fenetre de jeu
     */
    public BorderPane fenetreJeu() {
        BorderPane root = new FenetreJeu(modele, this.client);
        this.root.setCenter(root);
        this.scene.setOnKeyPressed(new ControleurClavier(this, modele));
        return root;
    }

    public BorderPane fenetreConnexion() {
        BorderPane root = new FenetreConnexion(this);
        this.root.setCenter(root);
        return root;
    }

    /**
     * Créer une fenetre de paramètres
     * 
     * @return La fenetre de paramètres
     */
    public Dialog<List<String>> dialogParametre() {
        return null;
    }

    public void maj(Change change) {
        Platform.runLater(() -> {
            ((FenetreJeu) this.root.getCenter()).maj(change);
        });
    }

    public void maj(int c, Equipe equipe, boolean drop) {
        Platform.runLater(() -> {
            ((FenetreJeu) this.root.getCenter()).maj(c, equipe, drop);
        });
    }

    public void reset() {
        Platform.runLater(() -> {
            ((FenetreJeu) this.root.getCenter()).reset();
        });
    }

    public void connexion(String pseudo, String port) {
        this.client = new Client(pseudo, port, this);
    }

    public void connexion(String pseudo, String host, String port) {
        this.client = new Client(pseudo, host, port, this);
    }

    public Client getClient() {
        return this.client;
    }

    /**
     * Retourne le modèle du jeu
     * 
     * @return Le modèle du jeu
     */
    public ModeleJeu getModele() {
        return this.modele;
    }

    public void close() {
        System.exit(0);
    }

    public void plusDeJoueur() {
        ((FenetreJeu) this.root.getCenter()).plusDeJoueur();
    }

    public void showPopPup() {
        ((FenetreJeu) this.root.getCenter()).showPopPup();
    }

    public static void main(String[] args) {
        launch(AppliJeu.class);
    }
}