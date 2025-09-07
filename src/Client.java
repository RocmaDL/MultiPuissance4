import java.io.*;
import java.net.*;
import javafx.application.Platform;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String pseudo;
    private String host;
    private String port;
    private AppliJeu appliJeu;
    private Equipe equipe;
    private String pseudoAdversaire = null;

    public void connect(String host, String port) {
        try {
            this.socket = new Socket(host, Integer.parseInt(port));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connecté à " + host + ":" + port);
            send("CONNECT " + this.pseudo + " " + this.host);

            // Écouter les messages du serveur
            new Thread(() -> {
                try {
                    String message;
                    while ((message = this.in.readLine()) != null) {
                        System.out.println("Message du serveur: " + message);
                        String[] parts = message.split(" ");
                        String command = parts[0];
                        String equipe;
                        switch (command) {
                            case "CONNECTED":
                                equipe = parts[1];
                                this.equipe = Equipe.get(equipe);
                                String start = null;
                                String s = "START";
                                try {
                                    start = parts[2];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    start = null;
                                }
                                if (s.equals(start)) {
                                    Platform.runLater(() -> {

                                        this.appliJeu.fenetreJeu();

                                    });
                                }
                                break;
                            case "FULL":
                                System.out.println("La partie est pleine");
                                break;
                            case "UPDATE":
                                equipe = parts[2];
                                Equipe joueur = Equipe.get(equipe);
                                int c = Integer.parseInt(parts[1]);
                                if (!joueur.equals(this.equipe)) {
                                    Platform.runLater(() -> {
                                        this.appliJeu.getModele().drop(c, joueur);
                                        this.appliJeu.maj(Change.DROP);
                                    });
                                }
                                break;
                            case "SELECT":
                                int index = Integer.parseInt(parts[1]);
                                equipe = parts[2];
                                Equipe j = Equipe.get(equipe);
                                Platform.runLater(() -> {
                                    this.appliJeu.maj(index, j, false);
                                });
                                break;
                            case "RESET":
                                Platform.runLater(() -> {
                                    this.appliJeu.reset();
                                });
                                break;
                            case "GAGNE":
                                equipe = parts[1];
                                Equipe gagnant = Equipe.get(equipe);
                                Platform.runLater(() -> {
                                    this.appliJeu.getModele().setJoueur(gagnant);
                                });
                                break;
                            case "EGALITE":
                                Platform.runLater(() -> {
                                    Equipe ej = Equipe.JAUNE;
                                    this.appliJeu.getModele().setJoueur(ej);
                                });
                                break;
                            case "CONNECT":
                                String pseudo = parts[1];
                                System.out.println("Nouvelle connexion : " + pseudo);
                                String cible = null;
                                try {
                                    cible = parts[2];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    cible = null;
                                }
                                break;
                            case "VIDE":
                                Platform.runLater(() -> {
                                    this.appliJeu.plusDeJoueur();
                                });
                                break;
                            case "ADVERSAIRE":
                                String pseudoAdversaire = parts[1];
                                this.setPseudoAdversaire(pseudoAdversaire);
                                break;
                            default:
                                System.out.println("Commande inconnue : " + command);
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu : " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Impossible de se connecter à l'hôte : " + host);
            System.out.println(e);
            System.exit(1);
        }
    }

    public Client(String pseudo, String port, AppliJeu appliJeu) {
        this.pseudo = pseudo;
        this.port = port;
        this.host = "localhost";
        this.appliJeu = appliJeu;
        this.connect(this.host, this.port);
    }

    public Client(String pseudo, String host, String port, AppliJeu appliJeu) {
        this.pseudo = pseudo;
        this.host = host;
        this.port = port;
        this.appliJeu = appliJeu;
        this.connect(this.host, this.port);
    }

    public void close() {
        try {
            this.disconnect();
            this.out.close();
            this.in.close();
            this.socket.close();
            System.out.println("Connexion fermée");
            this.appliJeu.close();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture de la connexion");
            e.printStackTrace();
        }
    }

    public void send(String message) {
        this.out.println(message);
        this.out.flush();
    }

    public String receive() {
        try {
            return this.in.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message");
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        this.send("DISCONNECT " + this.pseudo);
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public String getPseudoAdversaire() {
        return this.pseudoAdversaire;
    }

    public void setPseudoAdversaire(String pseudoAdversaire) {
        this.pseudoAdversaire = pseudoAdversaire;
    }

    public void rejouer() {
        this.send("REPLAY");
    }

}
