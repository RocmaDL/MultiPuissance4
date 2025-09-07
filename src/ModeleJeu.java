public class ModeleJeu {
    public static final int LIGNES = 6;
    public static final int COLONNES = 7;
    public static final int PUISSANCE = 4;
    private Equipe[][] grille;
    private int selectIndex;
    private Equipe joueur;
    private Equipe gagnant;
    private boolean perdu;

    /**
     * Constructeur de la classe ModeleJeu
     * 
     * @param joueur
     */
    public ModeleJeu(Equipe joueur) {
        init();
        this.joueur = joueur;
        this.gagnant = null;
        this.selectIndex = COLONNES / 2;
    }

    /**
     * Initialisation de la grille
     */
    public void init() {
        this.grille = new Equipe[LIGNES][COLONNES];
        for (int l = 0; l < LIGNES; l++) {
            this.grille[l] = new Equipe[COLONNES];
            for (int c = 0; c < COLONNES; c++) {
                this.grille[l][c] = Equipe.AUCUNE;
            }
        }
    }

    /**
     * Selection de la colonne
     */
    public void selectionGauche() {
        if (this.selectIndex > 0) {
            this.selectIndex--;
        }
    }

    /**
     * Selection de la colonne
     */
    public void selectionDroite() {
        if (this.selectIndex < COLONNES - 1) {
            this.selectIndex++;
        }
    }

    /**
     * Retourne la case la plus basse de la colonne
     * 
     * @param c
     * @return
     */
    public int casePlusBasse(int c) {
        if (this.grille[0][c] != Equipe.AUCUNE)
            return -1;
        for (int l = 0; l < LIGNES; l++) {
            if (this.grille[l][c] != Equipe.AUCUNE) {
                return l - 1;
            }
        }
        return LIGNES - 1;
    }

    /**
     * Permet de poser un pion
     * 
     * @return
     */
    public boolean drop() {
        int l = this.casePlusBasse(this.selectIndex);
        if (l != -1) {
            this.grille[l][this.selectIndex] = this.joueur;
            this.verifFin();
            this.selectIndex = COLONNES / 2;
            this.joueur = this.joueur == Equipe.JAUNE ? Equipe.ROUGE : Equipe.JAUNE;
        }
        return l != -1;
    }

    public boolean drop(int c, Equipe joueur) {
        int l = this.casePlusBasse(c);
        if (l != -1) {
            this.grille[l][c] = joueur;
            this.verifFin();
            this.joueur = this.joueur == Equipe.JAUNE ? Equipe.ROUGE : Equipe.JAUNE;
        }
        return l != -1;
    }

    /**
     * Verifie si la grille est remplie
     */
    public void verifFin() {
        boolean remplie = true;
        for (int l = 0; l < LIGNES; l++) {
            for (int c = 0; c < COLONNES; c++) {
                if (this.getCase(l, c) == Equipe.AUCUNE) {
                    remplie = false;
                    continue;
                }
                Direction[] dirs = Direction.values();
                for (Direction dir : dirs) {
                    Coords co = dir.getVoisin(l, c);
                    if (co.getC() < 0 || co.getC() >= COLONNES || co.getL() < 0 || co.getL() >= LIGNES)
                        continue;
                    verif(l, c, dir, 1);
                }
            }
        }
        this.perdu = remplie;
    }

    /**
     * Verifie si la partie est gagnee
     * 
     * @param l
     * @param c
     * @param dir
     * @param i
     */
    private void verif(int l, int c, Direction dir, int i) {
        Coords co = dir.getVoisin(l, c);
        if (co.getC() < 0 || co.getC() >= COLONNES || co.getL() < 0 || co.getL() >= LIGNES)
            return;
        if (this.grille[co.getL()][co.getC()] == this.grille[l][c]) {
            if (i == PUISSANCE - 1) {
                this.gagnant = this.grille[l][c];
                return;
            }
            verif(co.getL(), co.getC(), dir, i + 1);
        }
    }

    /**
     * Retourne la case de la grille
     * 
     * @param l
     * @param c
     * @return
     */
    public Equipe getCase(int l, int c) {
        return this.grille[l][c];
    }

    /**
     * Retourne l'index de la colonne selectionnee
     * 
     * @return
     */
    public int getSelectIndex() {
        return selectIndex;
    }

    /**
     * Modifie l'index de la colonne selectionnee
     * 
     * @param selectIndex
     */
    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    /**
     * Retourne le joueur
     * 
     * @return
     */
    public Equipe getJoueur() {
        return joueur;
    }

    /**
     * Modifie le joueur
     * 
     * @param joueur
     */
    public void setJoueur(Equipe joueur) {
        System.out.println(this.joueur);
        this.joueur = joueur;
        System.out.println(this.joueur);
    }

    /**
     * Retourne si la partie est gagnee
     * 
     * @return
     */
    public boolean estGagnee() {
        return this.gagnant != null;
    }

    /**
     * Retourne si la partie est perdue
     * 
     * @return
     */
    public boolean estPerdu() {
        return this.perdu;
    }

    /**
     * Retourne le gagnant
     * 
     * @return
     */
    public Equipe getGagnant() {
        return gagnant;
    }

    public boolean estVide() {
        boolean grilleVide = true;
        for (int l = 0; l < LIGNES; l++) {
            for (int c = 0; c < COLONNES; c++) {
                if (this.grille[l][c] != Equipe.AUCUNE) {
                    grilleVide = false;
                }
            }
        }
        return grilleVide;
    }

    public boolean estEnJeu() {
        return !this.estVide() && !this.estGagnee() && !this.estPerdu();
    }

    /**
     * Remet a zero la partie
     */
    public void reset() {
        Equipe ancienGagnant = this.gagnant;
        boolean egalite = this.perdu;
        init();
        this.gagnant = null;
        this.perdu = false;
        this.selectIndex = COLONNES / 2;
        if (egalite) {
            // On garde le même joueur qu'avant
            // ...no change pour this.joueur...
        } else if (ancienGagnant != null) {
            // Le vainqueur rejoue en premier
            this.joueur = ancienGagnant;
        }
    }

    /**
     * Retourne la grille
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int l = 0; l < LIGNES; l++) {
            for (int c = 0; c < COLONNES; c++) {
                sb.append(this.grille[l][c]);
            }
            sb.append("\n");
        }
        sb.append("index: " + this.selectIndex);
        return sb.toString();
    }
}
