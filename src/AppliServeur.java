import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class AppliServeur extends Application {
    private static Serveur serveur;
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private static VBox listeClients;
    private int port;

    @Override
    public void start(Stage stage) throws Exception {
        this.root = new BorderPane();
        this.scene = new Scene(this.root, 400, 400);
        this.stage = stage;
        this.stage.setScene(this.scene);
        this.stage.setTitle("Serveur Puissance 4");
        this.stage.show();
        fenetreCreationServeur();
        this.stage.setOnCloseRequest((e) -> {
            this.serveur.close();
            System.exit(0);
        });
    }

    public void fenetreCreationServeur() {
        this.root.setPadding(new Insets(10));
        BorderPane bp = new BorderPane();
        TextField port = new TextField();
        VBox vb = new VBox();
        Text instruction = new Text("Entrez le port du serveur");
        Button creer = new Button("Créer le serveur");
        port.setPromptText("Port");
        vb.getChildren().addAll(instruction, port);
        bp.setCenter(vb);
        Text titre = new Text("Création du serveur");
        titre.setStyle("-fx-font-size: 32");
        bp.setTop(titre);
        bp.setBottom(creer);
        this.root.setCenter(bp);
        creer.setOnAction((e) -> {
            try {
                creerServeur(Integer.parseInt(port.getText()));
            } catch (Exception ex) {
                instruction.setText("Entrez un nombre valide");
            }
        });
    }

    public void fenetreMenuServeur() {
        this.root.setPadding(new Insets(10));
        BorderPane bp = new BorderPane();
        listeClients = new VBox();
        List<ClientHandler> clients = this.serveur.getClientsHandlers();
        for (ClientHandler clientHandler : clients) {
            Text client = new Text(clientHandler.getPseudo() + " (" + clientHandler.getHost() + ")");
            listeClients.getChildren().add(client);
        }
        Text titre = new Text("Serveur Puissance 4 au port " + this.port);
        titre.setStyle("-fx-font-size: 24");
        Text host = new Text("Adresse IP : " + this.serveur.getAddresse());
        host.setStyle("-fx-font-size: 24");
        VBox vb = new VBox();
        vb.getChildren().addAll(titre, host);
        bp.setTop(vb);
        bp.setCenter(listeClients);
        this.root.setCenter(bp);
    }

    public static void updateClientList() {
        Platform.runLater(() -> {
            listeClients.getChildren().clear();
            List<ClientHandler> clients = serveur.getClientsHandlers();
            for (ClientHandler clientHandler : clients) {
                Text client = new Text(clientHandler.getPseudo() + " (" + clientHandler.getHost() + ")");
                listeClients.getChildren().add(client);
            }
        });
    }

    @Override
    public void init() throws Exception {

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void creerServeur(int port) {
        this.serveur = new Serveur(port);
        this.serveur.start();
        this.port = port;
        fenetreMenuServeur();
    }

}
