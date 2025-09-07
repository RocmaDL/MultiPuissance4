import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String pseudo;
    private String host;
    private Equipe equipe;
    private Serveur serveur;
    private String adversairePseudo;

    public ClientHandler(Socket socket, Serveur serveur) {
        this.serveur = serveur;
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du client handler");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = this.in.readLine()) != null) {
                String[] parts = message.split(" ");
                String command = parts[0];
                int colonne;
                String equipe;
                switch (command) {
                    case "CONNECT":
                        String pseudo = parts[1];
                        String host = parts[2];
                        if (pseudo != null && host != null) {
                            this.pseudo = pseudo;
                            this.host = host;
                            this.serveur.broadcast("CONNECT " + pseudo + " ");
                            // Mettre à jour l'interface utilisateur pour afficher la liste des clients
                            AppliServeur.updateClientList();
                            System.out.println("Nouvelle connexion : " + pseudo + " sur " + host);
                        }
                        break;
                    case "DROP":
                        if (this.serveur.getClientsHandlers().size() == 2) {
                            colonne = Integer.parseInt(parts[1]);
                            equipe = parts[2];
                            if (this.equipe.equals(this.serveur.getJeu().getJoueur())) {
                                this.serveur.getJeu().setSelectIndex(colonne);
                                this.serveur.getJeu().drop();
                                this.serveur.broadcast("UPDATE " + colonne + " " + equipe);
                                System.out.println("DROP " + colonne + " " + equipe);
                            }
                        }
                        break;
                    case "UPDATE":
                        colonne = Integer.parseInt(parts[1]);
                        equipe = parts[2];
                        if (!(this.equipe.getSymbole().equals(equipe))) {
                            this.out.println("UPDATE " + colonne + " " + equipe);
                        }
                        break;
                    case "SELECT":
                        if (this.serveur.getClientsHandlers().size() == 2) {
                            colonne = Integer.parseInt(parts[1]);
                            equipe = parts[2];
                            if (this.equipe.equals(this.serveur.getJeu().getJoueur())) {
                                this.serveur.getJeu().setSelectIndex(colonne);
                                this.serveur.broadcast("SELECT " + colonne + " " + equipe);
                            }
                        }
                        break;
                    case "DISCONNECT":
                        String p = parts[1];
                        if (p != null) {
                            this.serveur.removeClient(p);
                            AppliServeur.updateClientList();
                            System.out.println("Déconnexion de " + p);
                        }
                        break;
                    case "REPLAY":
                        this.serveur.addConfirmationRejouer();
                        break;
                    default:
                        System.out.println("Commande inconnue: " + command);
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du message");
            e.printStackTrace();
        }
    }

    public void send(String message) {
        this.out.println(message);
        this.out.flush();
    }

    public void close() {
        try {
            this.out.close();
            this.in.close();
            this.socket.close();
            System.out.println("Connexion fermée");
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture de la connexion");
            e.printStackTrace();
        }
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getHost() {
        return this.host;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public BufferedReader getIn() {
        return this.in;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public String getAdversairePseudo() {
        return adversairePseudo;
    }

    public void setAdversairePseudo(String adversairePseudo) {
        this.adversairePseudo = adversairePseudo;
        send("ADVERSAIRE " + adversairePseudo);
    }
}
