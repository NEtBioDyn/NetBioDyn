/* This file is part of NetBioDyn.
 *
 *   NetBioDyn is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   any later version.
 *
 *   NetBioDyn is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with NetBioDyn; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * Behavior.java
 *
 * Created on 15 octobre 2007, 19:59
 */
package netbiodyn; //On crée un package, cette ligne indiquant que le fichier actuel sera dans ce package 


//import bsh.Interpreter;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.ihm.WndEditElementDeReaction;
import netbiodyn.util.UtilPoint3D;
import java.awt.Point; // Création d'un point aux coordonnées (x,y) 
import java.util.ArrayList; // Création d'un tableau
import java.util.HashMap; // Création d'une HashMap
import javax.swing.JTextArea; // Création d'un zone de texte 
import javax.swing.JTextPane; // Création d'une fenêtre / zone ?
import netbiodyn.util.RandomGen;

/**
 *
 * @author ballet
 */
public class Behavior extends Moteur { //

    private Env_Parameters parameters;
    private final InstanceReaxel entiteVide;
    public JTextPane _description = new JTextPane(); // création d'une nouvelle fenêtre / zone
    private double _k = 1;

    public ArrayList<String> _reactifs = new ArrayList<>(); // Création d'un tableau qui va contenir les réactifs
    public ArrayList<String> _produits = new ArrayList<>(); // Création d'un tableau qui va contenir les produits
    public ArrayList<String> _positions = new ArrayList<>(); // Création d'un tableau qui va contenir les positions

    public ArrayList<InstanceReaction> _reactionsPossibles = new ArrayList<InstanceReaction>(); //Création d'une liste des réactions possibles
    public JTextArea _code = new JTextArea(); 
    public boolean _code_parse = false;
    public ArrayList<WndEditElementDeReaction> _ListElementsReactions = new ArrayList<WndEditElementDeReaction>(); //Création d'une liste contenant les éléments de la réaction

    /**
     * Creates new form MoteurReaction
     */
    public Behavior() {
        initComponents(); // initialisation de la fenêtre du comportement
        // Mise en placedes boutons, mais de manière non cliquable
        button_move.setVisible(false); 
        button_display_relations.setVisible(false);
        button_timer.setVisible(false);
        boxManipulated.setVisible(false);
        boxNames.setVisible(false);
        boxRelais.setVisible(false);
        // Init des positions: 0=no, 1=yes, 2=impossible
        _positions.add("122222222"); // First line of reactive-product
        for (int i = 1; i < 9; i++) {
            _positions.add("212101210");
        }

        // Instanciation d'une entité vide
        entiteVide = new InstanceReaxel();
        entiteVide.setNom("0");
    }

    @Override
    public Behavior clone() {
        Behavior m = new Behavior(); //Création d'un nouveau comportement
        m.setEtiquettes(getEtiquettes());
        m._description.setText(_description.getText().replace('\n', '§')); //Mise en place de la description
        m._reactifs = (ArrayList<String>) _reactifs.clone(); //Clone des réactifs pour ce comportement
        m._produits = (ArrayList<String>) _produits.clone(); //Clone des produits pour ce comportement
        m._positions = (ArrayList<String>) _positions.clone(); //Clone des positions pour ce comportement
        m._ListElementsReactions = (ArrayList<WndEditElementDeReaction>)  _ListElementsReactions.clone(); //Clone des éléments de la réaction pour ce comportement
        m.set_k(get_k());
        return m;
    }

    //Changement de nom de réactif ?
    public void protoReaxelNameChanged(String oldName, String newName) {
        ArrayList<String> reactifs = (ArrayList<String>) _reactifs.clone(); //Clone de la liste des réactifs du comportement
        for (int i = 0; i < reactifs.size(); i++) {
            String _reactif = reactifs.get(i); // On récupère l'élément de la liste clonée des réactifs

            if (_reactif.equals(oldName)) {
                _reactif = newName; //Si le réactif a le nom indiqué, on change son nom
                _reactifs.remove(i); // On supprime le réactif modifié de la liste des réactifs
                _reactifs.add(i, _reactif); //On rajoute le réactif avec le nouveau nom à la position i ?
            }
        }
        
        ArrayList<String> prod = (ArrayList<String>) _produits.clone(); //Clone de la liste des produits du comportement
        for (int i = 0; i < prod.size(); i++) {
            String p = prod.get(i); // On récupère l'élément de la liste clonée des produits
            if (p.equals(oldName)) {
                p = newName; //Si le produit a le nom indiqué, on change son nom
                _produits.remove(i); // On supprime le produit modifié de la liste des produits
                _produits.add(i, p); //On rajoute le produit avec le nouveau nom à la position i ?
            }
        }
        
        for (WndEditElementDeReaction elt : _ListElementsReactions) {
            elt.modifierModelTypesCombo(elt, oldName, newName);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 204, 255)); //Mise en place d'un fond de couleur lavande
        setTitre("Titre"); 
    }// </editor-fold>//GEN-END:initComponents

    public void computeReactions(Simulator s, Env_Parameters param, AllInstances instances, int time) {
        this.setParameters(param); //mise en place des paramètres de la réaction
        _reactionsPossibles = new ArrayList<>(); //création d'une nouvelle liste pour les réactions possibles
        this.simuler_semi_situee(instances, time);
        s.decrementer_nb_processus_a_traiter();
    }

    public ArrayList<InstanceReaction> getReactionsPossibles() {
        return _reactionsPossibles; //obtenir la liste des réactions possibles
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Behavior) { //Si l'objet recherché correspond au comportemet
            Behavior r = (Behavior) o; // L'objet recherché devient un comportement
            return ((r.getEtiquettes().equals(this.getEtiquettes()))
                    && (r._reactifs.size() == this._reactifs.size())
                    && (r._produits.size() == this._produits.size())); //On en retourne les étiquettes, la liste des réactifs et la liste des produits
        }
        return false; // si ce n'est aps le cas, on retiurne False
    }

    // ?
    private boolean zoneAutorisee(int numReactif, int x0, int y0, int z0, int dx, int dy, int dz) {
        // Bordures
        if (x0 + dx < 0 || x0 + dx > getParameters().getX() - 1 || y0 + dy < 0 || y0 + dy > getParameters().getY() - 1) {
            return false;
        }
        if (z0 + dz < 0 || z0 + dz > getParameters().getZ() - 1) {
            return false;
        }
        // Non bordure
        if (dx == 0 && dy == 0) {
            if (_positions.get(numReactif).charAt(0) == '1') {
                return true;
            }
        }
        if (dx == 1 && dy == 0) {
            if (_positions.get(numReactif).charAt(1) == '1') {
                return true;
            }
        }
        if (dx == 0 && dy == -1) {
            if (_positions.get(numReactif).charAt(3) == '1') {
                return true;
            }
        }
        if (dx == -1 && dy == 0) {
            if (_positions.get(numReactif).charAt(5) == '1') {
                return true;
            }
        }
        if (dx == 0 && dy == 1) {
            if (_positions.get(numReactif).charAt(7) == '1') {
                return true;
            }
        }
        if (dz == 1) {
            if (_positions.get(numReactif).charAt(4) == '1') {
                return true;
            }
        }
        if (dz == -1) {
            if (_positions.get(numReactif).charAt(8) == '1') {
                return true;
            }
        }
        return false;
    }

    public void simuler_semi_situee(AllInstances instances, int time) {
        HashMap<String, Point> dico_rea = new HashMap<>(); //Création de la liste contenant la liste des réactions (et les coordonnées ?)

        if (_reactifs.size() <= 0) {
            return;
        }
        //Pour tous les éléments manipulés (compartiment, controle_atome,...), on recherche le clinamon 'molecule C' pour obtenir sa pseudoforme pour la suite si controle atome (?)
        InstanceReaxel c_a;

        int xb, yb, zb, x, y, z;

        for (int j = instances.getSize() - 1; j >= 0; j--) { //parcourt des instances en sens décroissant
            c_a = instances.getInList(j); 
            //recherche d'une molécule A  dans la liste

            if (c_a.getNom().equals(_reactifs.get(0)) && c_a.isSelectionne() == false) {
                x = c_a.getX(); //obtient la position x de l'instance ?
                y = c_a.getY(); //obtient la position y de l'instance ?
                z = c_a.getZ(); //obtient la position z de l'instance ?

                // Teste si la reaction a lieu ou pas
                double hasard = RandomGen.getInstance().nextDouble(); 
                if (hasard < this._k) {
                    int x_min, x_max, y_min, y_max, z_min, z_max;

                    x_min = Math.max(0, x - 1);
                    x_max = Math.min(getParameters().getX() - 1, x + 1);
                    y_min = Math.max(0, y - 1);
                    y_max = Math.min(getParameters().getY() - 1, y + 1);
                    z_min = Math.max(0, z - 1);
                    z_max = Math.min(getParameters().getZ() - 1, z + 1);

                    // Recherche si autour il y a tous les reactifs necessaires
                    boolean tousLesReactifs = true;
                    ArrayList<InstanceReaxel> listReactifs = new ArrayList<>();
                    InstanceReaxel central = instances.getFast(x, y, z);
                    if (central == null) {
                        // Gros probleme
                        System.err.println("GROS PROBLEME en " + x + "*" + y + "*" + z);
                        System.err.println("EGAL A " + instances.getInList(j).getX() + "*" + instances.getInList(j).getY() + "*" + instances.getInList(j).getZ() + " ? ");
                    }
                    listReactifs.add(central); // Le central va forcement reagir
                    for (int r = 1; r < _reactifs.size(); r++) {
                        boolean trouve = false;
                        boolean hors_cube = false;
                        if (!_reactifs.get(r).equals("*")) {
                            ArrayList<InstanceReaxel> lst_reactifs_tmp = new ArrayList<>();
                            for (int xx = x_min; xx <= x_max; xx++) {
                                for (int yy = y_min; yy <= y_max; yy++) {
                                    for (int zz = z_min; zz <= z_max; zz++) {
                                        int d = (x - xx) * (x - xx) + (y - yy) * (y - yy) + (z - zz) * (z - zz);
                                        if (d <= 1 && zoneAutorisee(r, x, y, z, xx - x, yy - y, zz - z) == true) { // On enleve les diagonales et les zones interdites
                                            hors_cube = false;
                                            xb = xx;
                                            yb = yy;
                                            zb = zz;
                                            //gere les 'bords' du tore

                                            if (hors_cube == false) {
                                                InstanceReaxel rea = instances.getFast(xb, yb, zb);
                                                if (rea != null) {
                                                    if (rea.isSelectionne() == false) {
                                                        if (xb != x || yb != y || zb != z) // On a deja pris le central
                                                        {
                                                            if (rea.getNom().equals(_reactifs.get(r))) {
                                                                if (listReactifs.contains(rea) == false) {
                                                                    lst_reactifs_tmp.add(rea);
                                                                    trouve = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (_reactifs.get(r).equals("0")) {
                                                    boolean deja_present = false;
                                                    for (int re = 0; re < listReactifs.size(); re++) {
                                                        InstanceReaxel instance = listReactifs.get(re);
//                                                        System.out.println(re + " - " + instance);
                                                        if (instance.getX() == xb && instance.getY() == yb && instance.getZ() == zb) {
                                                            deja_present = true;
                                                            re = listReactifs.size();
                                                        }
                                                    }
                                                    if (deja_present == false) {
                                                        InstanceReaxel tmp_cube = new InstanceReaxel();
                                                        tmp_cube.setX(xb);
                                                        tmp_cube.setY(yb);
                                                        tmp_cube.setZ(zb);
                                                        lst_reactifs_tmp.add(tmp_cube);
                                                        trouve = true;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (trouve == true) // On prend 1 seul reactif parmi tous les possibles
                            {
                                int n = RandomGen.getInstance().nextInt(lst_reactifs_tmp.size());
                                listReactifs.add(lst_reactifs_tmp.get(n));
                            }

                        } else {
                            trouve = true;
                        }
                        if (trouve == false) {
                            tousLesReactifs = false;
                            r = _produits.size();
                        }

                    }

                    // Recherche si autours il y a l'espace suffisant pour placer tous les produits
                    boolean espace_pour_produits = false;
                    ArrayList<UtilPoint3D> lst_pos = new ArrayList<>();
                    int nb_produits_effectif = 0;

                    if (tousLesReactifs == true) {
                        // Calcul du nb de produits effectifs
                        for (int pr = 0; pr < _produits.size(); pr++) {
                            if (!_produits.get(pr).equals("-")) {
                                nb_produits_effectif++;
                            }
                        }

                        // int xx_min,xx_max, yy_min, yy_max, zz_min, zz_max;
                        //xx_min = x-1; xx_max = x+1;
                        boolean hors_cube = false;
                        for (int xx = x_min; xx <= x_max; xx++) {
                            for (int yy = y_min; yy <= y_max; yy++) {
                                for (int zz = z_min; zz <= z_max; zz++) {
                                    int ddd = (x - xx) * (x - xx) + (y - yy) * (y - yy) + (z - zz) * (z - zz);
                                    if (ddd <= 1 || ddd >= 4) { // On enleve les diagonales (d=2 ou d=3)
                                        xb = xx;
                                        yb = yy;
                                        zb = zz;
                                        //gere les 'bords' du tore
                                        hors_cube = false;
                                        if (hors_cube == false) {
                                            if (xx == x && yy == y && zz == z) { // Le central reagit forcement
                                                lst_pos.add(new UtilPoint3D(xb, yb, zb));
                                            } else if (instances.getFast(xb, yb, zb) == null) { // creation
                                                lst_pos.add(new UtilPoint3D(xb, yb, zb));
                                            } else if (listReactifs.contains(instances.getFast(xb, yb, zb))) // remplacement // Si c'est un reactif, sa position compte car il va disparaitre (reagir)
                                            {
                                                lst_pos.add(new UtilPoint3D(xb, yb, zb));
                                            }
                                            // Cas ou un lien impermeable empeche la reaction en diagonale
                                            //if(   (2+xx-x + yy-y) % 2 == 0){
                                            //}
                                        }
                                    }
                                }
                            }
                        }
                        if (lst_pos.size() >= nb_produits_effectif) {
                            espace_pour_produits = true;
                        }
                    }

                    // Placement des produits en fonction de leur position dans les listes
                    if (espace_pour_produits == true) {
                        InstanceReaction rp = new InstanceReaction();
                        _reactionsPossibles.add(rp);

                        // Remplacements
                        for (int r = 0; r < listReactifs.size(); r++) {
                            int posx = 0, posy = 0, posz = 0;
                            // Suppression
                            if (listReactifs.get(r) != null) { // C'est null si c'est "0"  - A modifier pour prendre en compte le vide. Il faudra aussi memoriser la position du vide et voir si le "0" compte comme emplacement pour les produits
                                posx = listReactifs.get(r).getX();
                                posy = listReactifs.get(r).getY();
                                posz = listReactifs.get(r).getZ();
                                //cliEnv.EnleverReaxel(posx, posy);
                                if (listReactifs.get(r).getNom() != null) { // non "0"
                                    rp._reactifs_noms.add(instances.getFast(posx, posy, posz).getNom());
                                    rp._reactifs_pos.add(new UtilPoint3D(posx, posy, posz));
                                }

                            }
                            // Ajout
                            if (r < _produits.size()) {
                                if (!_produits.get(r).equals("-")) {
                                    //cliEnv.AjouterReaxel(posx, posy, produits.get(r), cliEnv._matrice_reaxels, cliEnv._liste_reaxels);
                                    rp._produits_noms.add(_produits.get(r));
                                    rp._produits_pos.add(new UtilPoint3D(posx, posy, posz));
                                    lst_pos.remove(new UtilPoint3D(posx, posy, posz)); // La place n'est plus disponible
                                }
                            }
                        }
                        // Ajouts simples
                        for (int p = listReactifs.size(); p < _produits.size(); p++) {
                            if (!_produits.get(p).equals("-")) {
                                int pos;
                                UtilPoint3D pt;
                                pos = RandomGen.getInstance().nextInt(lst_pos.size()); // Choix de la position de l'ajout
                                pt = lst_pos.get(pos);
                                //cliEnv.AjouterReaxel(pt.x, pt.y, produits.get(p), cliEnv._matrice_reaxels, cliEnv._liste_reaxels);
                                rp._produits_noms.add(_produits.get(p));
                                rp._produits_pos.add(new UtilPoint3D(pt.x, pt.y, pt.z));

                                lst_pos.remove(pt);
                            }
                        }

                        // Decrement de j en fct de la diff entre prod et reactifs
                        //j -= listReactifs.Count - nb_produits_effectif + 1;
                        // Decrement de j en fct de la position dans la liste
                        int deltaReactifs = listReactifs.size() - 1;
                        // Compte de ceux qui sont * avant * c_a
                        for (InstanceReaxel listReactif : listReactifs) {
                            int pos_in_lst = instances.indexOf(listReactif);
                            if (pos_in_lst >= 0 && pos_in_lst < j) {
                                if (listReactif.getNom() != null) {
                                    deltaReactifs--;
                                }
                            }
                            if (listReactif.getNom() == null) {
                                deltaReactifs--;
                            }
                        }
                        j -= deltaReactifs;// listReactifs.Count;// deltaReactifs + 1;

                        // Memorisation de la reaction
                        //cliEnv._tab_reactions_produites.Add(cliEnv._time + " \t" + this.Etiquettes);
                        if (dico_rea.containsKey(this.getEtiquettes()) == false) {
                            dico_rea.put(this.getEtiquettes(), new Point(time, 1));
                        } else {
                            Point pt = dico_rea.get(this.getEtiquettes());
                            pt.y += 1;
                            dico_rea.put(this.getEtiquettes(), pt);
                        }
                    }
                }
            }
        }
    }
    //}

    public void set_k(double k) {
        _k = k;
        setTitre(getEtiquettes() + ", p=" + ((Double) _k).toString());
    }

    public double get_k() {
        return _k;
    }

    @Override
    public ArrayList<String> toSave() {
        ArrayList<String> toSave = super.toSave();
        // description
        toSave.add("\tDescription:" + _description.getText().replace('\n', '§') + "\n");

        // semi-situee et situee
        for (String _reactif : _reactifs) {
            toSave.add("\treactif:" + _reactif + "\n");
        }
        for (String _produit : _produits) {
            toSave.add("\tproduit:" + _produit + "\n");
        }
        toSave.add("\tk:" + ((Double) (this._k)).toString() + "\n");
        // Positions
        for (String _position : _positions) {
            toSave.add("\tpos:" + _position + "\n");
        }

        // complexe
        for (int i = 0; i < _ListElementsReactions.size(); i++) {
            toSave.add(_ListElementsReactions.get(i).toSave());
        }
        return toSave;
    }

    public Env_Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Env_Parameters parameters) {
        this.parameters = parameters;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
