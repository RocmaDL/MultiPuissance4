import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Grille extends StackPane {
    private GridPane grid; // grille de jeu
    private List<Pion> selection; // selection de pions
    private List<List<Pion>> grille; // grille de pions
    private int selectIndex; // index de la selection
    private Equipe joueur; // joueur courant

    /**
     * Constructeur de la classe Grille
     */
    public Grille() {
        this.selectIndex = ModeleJeu.COLONNES / 2;
        this.joueur = Equipe.JAUNE;
        initPions();
        grid.setBackground(new Background(
                new BackgroundFill(Color.DARKBLUE, new CornerRadii(20), new Insets(Pion.SIZE * 3, 0, 0, 0))));
    }

    /**
     * Initialisation des pions
     */
    private void initPions() {
        if (this.grid == null) {
            this.grid = new GridPane();
            this.getChildren().add(grid);
        } else {
            this.grid.getChildren().clear();
        }
        grid.setVgap(Pion.SIZE * 0.8);
        grid.setHgap(Pion.SIZE * 0.8);
        grid.setPadding(new Insets(10));
        grid.getChildren().clear();
        this.selection = new ArrayList<>();
        for (int c = 0; c < ModeleJeu.COLONNES; c++) {
            Pion p = new Pion();
            if (c == selectIndex)
                p.setEquipe(joueur);
            grid.add(p, c, 0);
            this.selection.add(p);
        }
        this.grille = new ArrayList<>();
        for (int l = 0; l < ModeleJeu.LIGNES; l++) {
            this.grille.add(new ArrayList<>());
            for (int c = 0; c < ModeleJeu.COLONNES; c++) {
                Pion p = new Pion();
                grid.add(p, c, l + 1);
                this.grille.get(l).add(p);
            }
        }
    }

    /**
     * Mise à jour de la grille
     * 
     * @param modele
     * @param change
     */
    public void maj(ModeleJeu modele, Change change) {
        Platform.runLater(() -> {
            Pion changed = null;
            for (int l = 0; l < ModeleJeu.LIGNES; l++) {
                for (int c = 0; c < ModeleJeu.COLONNES; c++) {
                    Pion pion = this.grille.get(l).get(c);
                    if (pion.getEquipe() != modele.getCase(l, c))
                        changed = this.grille.get(l).get(c);
                    pion.setEquipe(modele.getCase(l, c));
                    pion.update();
                }
            }
            final Pion selected = this.selection.get(this.selectIndex);
            selected.setEquipe(Equipe.AUCUNE);
            if (change == Change.DROP && changed != null) {
                final Pion toChange = changed;
                final Equipe dernierJoueur = this.joueur;
                selected.setEquipe(Equipe.AUCUNE);
                changed.setEquipe(Equipe.AUCUNE);

                double ty = -changed.sceneToLocal(selected.localToScene(changed.getTranslateX(), changed.getTranslateY()))
                        .getY();

                final Pion p = new Pion(this.joueur);
                TranslateTransition tt = new TranslateTransition();
                tt.setNode(p);
                tt.setToY(ty);
                tt.setDuration(Duration.millis(500));
                tt.setOnFinished((event) -> {
                    grid.getChildren().remove(p);
                    this.selection.get(this.selectIndex).setEquipe(this.joueur);
                    toChange.setEquipe(dernierJoueur);
                    AppliJeu.WAITING = false;
                });
                tt.play();
                grid.add(p, this.selectIndex, 0);
                p.setCenterY(ty);
                AppliJeu.WAITING = true;
            }
            this.selectIndex = modele.getSelectIndex();
            this.joueur = modele.getJoueur();
            if (changed == null)
                this.selection.get(this.selectIndex).setEquipe(this.joueur);
        });
    }

    /**
     * Mise à jour de la grille
     * 
     * @param modele
     * @param colonne
     * @param equipe
     * @param drop
     */
    public void maj(ModeleJeu modele, int colonne, Equipe equipe, boolean drop) {
        Platform.runLater(() -> {
            Pion changed = null;
            for (int l = 0; l < ModeleJeu.LIGNES; l++) {
                for (int c = 0; c < ModeleJeu.COLONNES; c++) {
                    Pion pion = this.grille.get(l).get(c);
                    if (pion.getEquipe() != modele.getCase(l, c)) {
                        changed = pion;
                    }
                    pion.setEquipe(modele.getCase(l, c));
                    pion.update();
                }
            }
            this.selection.get(this.selectIndex).setEquipe(Equipe.AUCUNE);
            if (changed != null && drop) {
                final Pion toChange = changed;
                final Equipe dernierJoueur = equipe;
                final Pion selected = this.selection.get(this.selectIndex);
                selected.setEquipe(Equipe.AUCUNE);
                toChange.setEquipe(Equipe.AUCUNE);

                double ty = -toChange
                        .sceneToLocal(selected.localToScene(toChange.getTranslateX(), toChange.getTranslateY())).getY();
                final Pion p = new Pion(equipe);
                TranslateTransition tt = new TranslateTransition();
                tt.setNode(p);
                tt.setToY(ty);
                tt.setDuration(Duration.millis(500));
                tt.setOnFinished(event -> {
                    grid.getChildren().remove(p);
                    selected.setEquipe(equipe);
                    toChange.setEquipe(dernierJoueur);
                });
                tt.play();
                grid.add(p, colonne, 0);
                p.setCenterY(ty);
            } else if (changed != null) {
                changed.setEquipe(equipe);
            }
            this.selectIndex = colonne;
            this.joueur = equipe;
            this.selection.get(this.selectIndex).setEquipe(equipe);
        });
    }

    /**
     * Selection du pion
     * 
     * @param index
     */
    public void setJoueur(Equipe joueur) {
        this.joueur = joueur;
        this.selection.get(this.selectIndex).setEquipe(this.joueur);
        this.selection.get(this.selectIndex).update();
    }

    /**
     * Reset de la grille
     */
    public void reset() {
        this.initPions();
    }
}
