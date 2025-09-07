# Multi Puissance 4

## Présentation du projet

Ce projet implémente une version réseau du célèbre jeu Puissance 4, utilisant une architecture client/serveur. L'application est développée en Java avec une interface graphique JavaFX, permettant une expérience utilisateur fluide et agréable.

## Fonctionnalités

- **Architecture client/serveur** : permet à deux joueurs de s'affronter en réseau
- **Interface graphique intuitive** : réalisée avec JavaFX
- **Animation des pions** : chute des pions avec transition fluide
- **Système de score** : suivi des victoires côté client
- **Possibilité de rejouer** : après chaque partie terminée

## Règles du jeu

Deux joueurs s'affrontent en plaçant tour à tour un pion de leur couleur (jaune ou rouge) dans une grille de 7 colonnes et 6 lignes. Les pions tombent jusqu'à atteindre le bas de la grille ou un autre pion déjà placé.

Le but est d'aligner 4 pions de sa couleur :

- horizontalement
- verticalement
- diagonalement

Si la grille est entièrement remplie sans qu'aucun joueur n'ait réalisé un alignement, la partie est déclarée nulle.

## Architecture technique

Le projet est structuré selon le pattern MVC (Modèle-Vue-Contrôleur) :

- **Modèle** : gestion de la logique du jeu ([`ModeleJeu.java`](src/ModeleJeu.java))
- **Vue** : interface utilisateur ([`FenetreJeu.java`](src/FenetreJeu.java), [`Grille.java`](src/Grille.java))
- **Contrôleur** : gestion des interactions ([`ControleurClavier.java`](src/ControleurClavier.java))

La communication réseau est gérée par :

- Côté serveur : [`Serveur.java`](src/Serveur.java), [`ClientHandler.java`](src/ClientHandler.java)
- Côté client : [`Client.java`](src/Client.java)

## Installation et prérequis

### Prérequis

- Java JDK 8 ou supérieur
- JavaFX (inclus dans le JDK 8-10, à installer séparément pour les versions ultérieures)

### Compilation

Un script de compilation est fourni pour faciliter la mise en route :

```bash
chmod +x compil.sh
./compil.sh
```

Pour compiler manuellement :

```bash
javac --module-path /chemin/vers/javafx/lib --add-modules javafx.controls -d bin src/*.java
```

## Guide d'utilisation

### Démarrage du serveur

1. Lancez le serveur :

   ```bash
   java --module-path /chemin/vers/javafx/lib --add-modules javafx.controls -cp bin AppliServeur
   ```

   ou utilisez votre IDE pour exécuter la classe `AppliServeur`.

2. Dans l'interface du serveur :
   - Saisissez un numéro de port disponible (ex: 1234)
   - Cliquez sur "Créer le serveur"
3. L'interface affiche alors :
   - L'adresse IP du serveur (à communiquer aux joueurs)
   - Le port utilisé
   - La liste des clients connectés (mise à jour en temps réel)

### Connexion des clients

1. Lancez l'application client :

   ```bash
   java --module-path /chemin/vers/javafx/lib --add-modules javafx.controls -cp bin AppliJeu
   ```

   ou utilisez votre IDE pour exécuter la classe `AppliJeu`.

2. Dans l'interface de connexion :

   - Saisissez un pseudonyme
   - Saisissez l'adresse IP du serveur (laissez vide pour "localhost")
   - Saisissez le port du serveur
   - Cliquez sur "Connexion"

3. La partie démarre automatiquement lorsque deux joueurs sont connectés.

### Contrôles en jeu

- **Flèche gauche** : déplacer la sélection d'une colonne vers la gauche
- **Flèche droite** : déplacer la sélection d'une colonne vers la droite
- **Entrée/Espace** : placer un pion dans la colonne sélectionnée

Le tour passe automatiquement à l'adversaire après chaque coup joué.

### Fin de partie

Lorsqu'un joueur aligne 4 pions ou que la grille est remplie :

1. Une fenêtre popup s'affiche annonçant le résultat
2. Les joueurs peuvent choisir de rejouer ou non
3. Si les deux joueurs acceptent, une nouvelle partie commence
4. Les scores sont mis à jour localement pour chaque client

## Protocole de communication

La communication entre client et serveur utilise un protocole texte simple avec des commandes comme :

- `CONNECT pseudo host` : connexion d'un client
- `DROP colonne equipe` : placement d'un pion
- `SELECT colonne equipe` : sélection d'une colonne
- `REPLAY` : demande de nouvelle partie

## Perspectives d'amélioration

- Implémentation d'une intelligence artificielle pour jouer contre l'ordinateur
- Ajout d'un système de comptes utilisateurs
- Support pour plus de deux joueurs (variante du jeu)
- Ajout d'effets sonores et d'options de personnalisation

## Licence

Ce projet est distribué sous licence libre.

## Auteurs

- [RocmaDL](https://www.github.com/RocmaDL)

