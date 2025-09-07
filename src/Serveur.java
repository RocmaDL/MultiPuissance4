import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.net.InetAddress;

public class Serveur extends Thread {
    private static final Logger logger = Logger.getLogger(Serveur.class.getName());
    private ServerSocket serveur;
    private List<ClientHandler> clientsHandlers;
    private int port;
    private ModeleJeu jeu;
    private int confirmationRejouer = 0;
    private boolean envoyer = true;
    private boolean adversairesAssignes = false;

    public Serveur(int port) {
        this.port = port;
        this.clientsHandlers = new ArrayList<>();
        this.jeu = new ModeleJeu(Equipe.JAUNE);
    }

    public String getAddresse() {
        String addresse = "";
        try {
            addresse = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération de l'adresse IP : " + e.getMessage());
        }
        return addresse;
    }

    public void run() {
        try {
            this.serveur = new ServerSocket(port);
            System.out.println(this.getAddresse());
            System.out.println("Serveur créé sur le port " + port);
            while (true) {
                if (this.envoyer) {
                    if (this.jeu.estPerdu()) {
                        this.broadcast("EGALITE");
                        this.envoyer = false;
                    }
                    if (this.jeu.estGagnee()) {
                        Equipe equipeGagnante = this.jeu.getGagnant();
                        Equipe equipePerdante = equipeGagnante.equals(Equipe.JAUNE) ? Equipe.ROUGE : Equipe.JAUNE;
                        this.broadcast("GAGNE " + equipeGagnante.getSymbole());
                        for (ClientHandler c : this.clientsHandlers) {
                            if (c.getEquipe().equals(equipeGagnante)) {
                                c.send("CONNECTED " + equipeGagnante);
                            } else {
                                c.send("CONNECTED " + equipePerdante);
                            }
                        }
                        this.envoyer = false;
                    }
                }
                if (this.confirmationRejouer == 2) {
                    System.out.println("RESET");
                    this.jeu.reset();
                    this.broadcast("RESET");
                    this.confirmationRejouer = 0;
                    this.envoyer = true;
                }
                if (this.jeu.estEnJeu()) {
                    if (this.clientsHandlers.size() < 2) {
                        Equipe joueurRestant = null;
                        for (ClientHandler clientHandler : this.clientsHandlers) {
                            joueurRestant = clientHandler.getEquipe();
                        }
                        if (joueurRestant != null) {
                            this.broadcast("GAGNE " + joueurRestant.getSymbole());
                            this.broadcast("VIDE");
                        } else {
                            this.close();
                        }
                    }
                }
                for (ClientHandler ch : new ArrayList<>(clientsHandlers)) {
                    if (ch.getSocket().isClosed()) {
                        this.removeClient(ch.getPseudo());
                    }
                }
                if (this.clientsHandlers.size() < 2) {
                    Socket client = this.serveur.accept();
                    ClientHandler clientHandler = new ClientHandler(client, this);
                    if (this.clientsHandlers.size() == 0) {
                        clientHandler.send("CONNECTED " + Equipe.JAUNE + " START");
                        clientHandler.setEquipe(Equipe.JAUNE);
                    } else if (this.clientsHandlers.size() == 1) {
                        clientHandler.send("CONNECTED " + Equipe.ROUGE + " START");
                        clientHandler.setEquipe(Equipe.ROUGE);

                    }
                    this.clientsHandlers.add(clientHandler);
                    clientHandler.start();
                }
                if (this.clientsHandlers.size() == 2 && !adversairesAssignes) {
                    ClientHandler joueur1 = this.clientsHandlers.get(0);
                    ClientHandler joueur2 = this.clientsHandlers.get(1);
                    joueur1.setAdversairePseudo(joueur2.getPseudo());
                    joueur2.setAdversairePseudo(joueur1.getPseudo());
                    adversairesAssignes = true;
                }
                Thread.sleep(50); // Délai pour éviter de saturer le thread
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la création du serveur : " + e.getMessage());
        }
    }

    public List<ClientHandler> getClientsHandlers() {
        return clientsHandlers;
    }

    public ModeleJeu getJeu() {
        return jeu;
    }

    public void setJeu(ModeleJeu jeu) {
        this.jeu = jeu;
    }

    public void broadcast(String message) {
        for (ClientHandler clientHandler : clientsHandlers) {
            clientHandler.send(message);
        }
        System.out.println("BROADCAST " + message);
    }

    public void removeClient(String pseudo) {
        for (ClientHandler clientHandler : clientsHandlers) {
            if (clientHandler.getPseudo().equals(pseudo)) {
                clientsHandlers.remove(clientHandler);
                break;
            }
        }
    }

    public void addConfirmationRejouer() {
        this.confirmationRejouer++;
        System.out.println("Confirmation : " + this.confirmationRejouer);
    }

    public void close() {
        try {
            for (ClientHandler clientHandler : clientsHandlers) {
                clientHandler.close();
            }
            this.serveur.close();
            System.out.println("Serveur fermé");
        } catch (Exception e) {
            logger.severe("Erreur lors de la fermeture du serveur : " + e.getMessage());
        }
    }
}
