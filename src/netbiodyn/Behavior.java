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
package netbiodyn; // Creation of a package, this line indicating that the actual file will be in this package

import netbiodyn.ihm.Env_Parameters;
import netbiodyn.ihm.WndEditElementDeReaction;
import netbiodyn.util.UtilPoint3D;
import java.awt.Point; // Possible creation of points with coordinates (x,y) 
import java.util.ArrayList; // Possible creation of tables
import java.util.HashMap; // Possible creation of hashMaps
import javax.swing.JTextArea; // Possible creation of text areas
import javax.swing.JTextPane; // Possible creation of windows/areas ?
import netbiodyn.util.RandomGen;

/**
 *
 * @author ballet
 */
public class Behavior extends Moteur { 

    private Env_Parameters parameters; //Creation of an Env_Parameters object
    private final InstanceReaxel entiteVide; //Creation of an non-modifiable InstanceReaxel object
    public JTextPane _description = new JTextPane(); // Creation of a new window/area
    private double _k = 1; //

    public ArrayList<String> _reactifs = new ArrayList<>(); // Creation of a table which will contain reagents
    public ArrayList<String> _produits = new ArrayList<>(); // Creation of a table which will contain products 
    public ArrayList<String> _positions = new ArrayList<>(); // Creation of a table which will contain direction of reactions (reversible,...) ?

    public ArrayList<InstanceReaction> _reactionsPossibles = new ArrayList<InstanceReaction>(); //Creation of a table which will contain the possible reactions 
    public JTextArea _code = new JTextArea(); //Creation of a new text area
    public boolean _code_parse = false; 
    public ArrayList<WndEditElementDeReaction> _ListElementsReactions = new ArrayList<WndEditElementDeReaction>(); //Creation of a table which will contain reaction elements ?

    /**
     * Creates new form MoteurReaction
     */
    
    //Creation of a new Behavior object
    public Behavior() {
        initComponents(); //Initialization of some graphical components
        button_move.setVisible(false); //set this JLabel invisible
        button_display_relations.setVisible(false); //set this JButton invisible
        button_timer.setVisible(false); //set this JButton invisible
        boxManipulated.setVisible(false); //set this JTextField invisible
        boxNames.setVisible(false); //set this JTextField invisible
        boxRelais.setVisible(false); //set this JTextField invisible
        // Position order : 2 East 2 North Top West 2 South Bottom.
        // Position initialisation : 0=no, 1=yes, 2=impossible
        _positions.add("122222222"); // First line of reactive-product
        for (int i = 1; i < 9; i++) {
            _positions.add("212101210");
        }

        // Instantiation of an empty InstanceReaxel, with no name
        entiteVide = new InstanceReaxel();
        entiteVide.setNom("0");
    }

    //Clone of the Behavior object (with "this" in parameter)
    public Behavior clone() {
        Behavior m = new Behavior(); //Creation of a new Behavior object
        //Get all the parameters of the old Behavior object to put them into the new one
        m.setEtiquettes(getEtiquettes());
        m._description.setText(_description.getText().replace('\n', '§'));
        m._reactifs = (ArrayList<String>) _reactifs.clone();
        m._produits = (ArrayList<String>) _produits.clone();
        m._positions = (ArrayList<String>) _positions.clone();
        m._ListElementsReactions = (ArrayList<WndEditElementDeReaction>) _ListElementsReactions.clone();
        m.set_k(get_k());
        return m; //Return the new Behavior object
    }

    //
    public void protoReaxelNameChanged(String oldName, String newName) {
        ArrayList<String> reactifs = (ArrayList<String>) _reactifs.clone(); //Clone of the table containing reagents
        for (int i = 0; i < reactifs.size(); i++) { //Path of all entries of the table
            String _reactif = reactifs.get(i); //Get the name of the reagent at this position
            if (_reactif.equals(oldName)) { //Comparison between the name of the reagent and the wanted name
                _reactif = newName; //If the two names are identical, replace the name of the reagent by the new one 
                _reactifs.remove(i); //Remove this reagent into the table
                _reactifs.add(i, _reactif); //Put the new reagent into the table at the same position as the removed one 
            }
        }
        
        //Same action for the products than for the reagents
        ArrayList<String> prod = (ArrayList<String>) _produits.clone();
        for (int i = 0; i < prod.size(); i++) {
            String p = prod.get(i);
            if (p.equals(oldName)) {
                p = newName;
                _produits.remove(i);
                _produits.add(i, p);
            }
        }
        
        //Changes into all reactions ?
        for (WndEditElementDeReaction elt : _ListElementsReactions) {
            elt.modifierModelTypesCombo(elt, oldName, newName);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    //Creation of some graphical components
    private void initComponents() {
        setBackground(new java.awt.Color(204, 204, 255)); //Creation of a new Periwinkle fond 
        setTitre("Titre"); //Put a title on this background ?
    }

    //
    public void computeReactions(Simulator s, Env_Parameters param, AllInstances instances, int time) {
        this.setParameters(param); //Put new values to parameters of Env_Parameters object linked to the Behavior one
        _reactionsPossibles = new ArrayList<>(); //Creation of a new table which will contain all possible reactions
        this.simuler_semi_situee(instances, time);
        s.decrementer_nb_processus_a_traiter();
    }

    public ArrayList<InstanceReaction> getReactionsPossibles() {
        return _reactionsPossibles;
    }

    //Know if an object already exists into the Behavior class
    public boolean equals(Object o) {
        if (o instanceof Behavior) {
            Behavior r = (Behavior) o; //If the object belongs to Behavior class, this object becomes a Behavior object
            return ((r.getEtiquettes().equals(this.getEtiquettes()))
                    && (r._reactifs.size() == this._reactifs.size())
                    && (r._produits.size() == this._produits.size())); //return the tags, reagents and products of this new object
        }
        return false; //return nothing if the object doesn't belong to Behavior class
    }

    private boolean zoneAutorisee(int numReactif, int x0, int y0, int z0, int dx, int dy, int dz) {
        // Borders
        if (x0 + dx < 0 || x0 + dx > getParameters().getX() - 1 || y0 + dy < 0 || y0 + dy > getParameters().getY() - 1) {
            return false;
        }
        if (z0 + dz < 0 || z0 + dz > getParameters().getZ() - 1) {
            return false;
        }
        // No-borders
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
        HashMap<String, Point> dico_rea = new HashMap<>();

        if (_reactifs.size() <= 0) {
            return; //End of the function if there is no reagent
        }
        //For all the manipulated elements (compartment,...), 
        //Search of the "clinamon" 'molecule C' to obtain its pseudoforme thereafter
        //If control atom
        InstanceReaxel c_a; 
        int xb, yb, zb, x, y, z;

        for (int j = instances.getSize() - 1; j >= 0; j--) { //Path of AllInstances object
            c_a = instances.getInList(j); //The InstanceReaxel is the one at the position j in AllInstances object
            //Search of a molecule A into the list
            if (c_a.getNom().equals(_reactifs.get(0)) && c_a.isSelectionne() == false) {
                x = c_a.getX();
                y = c_a.getY();
                z = c_a.getZ();
                // Test if a reaction occurs or not
                double hasard = RandomGen.getInstance().nextDouble();
                if (hasard < this._k) {
                    int x_min, x_max, y_min, y_max, z_min, z_max;

                    x_min = Math.max(0, x - 1);
                    x_max = Math.min(getParameters().getX() - 1, x + 1);
                    y_min = Math.max(0, y - 1);
                    y_max = Math.min(getParameters().getY() - 1, y + 1);
                    z_min = Math.max(0, z - 1);
                    z_max = Math.min(getParameters().getZ() - 1, z + 1);

                    // Search if there are all necessary reagents around
                    boolean tousLesReactifs = true;
                    ArrayList<InstanceReaxel> listReactifs = new ArrayList<>();
                    InstanceReaxel central = instances.getFast(x, y, z);
                    if (central == null) {
                        // This case is synonym of a big problem
                        System.err.println("GROS PROBLEME en " + x + "*" + y + "*" + z);
                        System.err.println("EGAL A " + instances.getInList(j).getX() + "*" + instances.getInList(j).getY() + "*" + instances.getInList(j).getZ() + " ? ");
                    }
                    listReactifs.add(central); // This InstanceReaxel (central) will necessarily react
                    for (int r = 1; r < _reactifs.size(); r++) {
                        boolean trouve = false;
                        boolean hors_cube = false;
                        if (!_reactifs.get(r).equals("*")) {
                            ArrayList<InstanceReaxel> lst_reactifs_tmp = new ArrayList<>();
                            for (int xx = x_min; xx <= x_max; xx++) {
                                for (int yy = y_min; yy <= y_max; yy++) {
                                    for (int zz = z_min; zz <= z_max; zz++) {
                                        int d = (x - xx) * (x - xx) + (y - yy) * (y - yy) + (z - zz) * (z - zz);
                                        if (d <= 1 && zoneAutorisee(r, x, y, z, xx - x, yy - y, zz - z) == true) { // Remove diagonals and forbidden areas 
                                            hors_cube = false;
                                            xb = xx;
                                            yb = yy;
                                            zb = zz;
                                            //Manage the "edges" of the "torus"

                                            if (hors_cube == false) {
                                                InstanceReaxel rea = instances.getFast(xb, yb, zb);
                                                if (rea != null) {
                                                    if (rea.isSelectionne() == false) {
                                                        if (xb != x || yb != y || zb != z) // The "central" is already taken
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
                            if (trouve == true) // Only one reagent is taken among all possibilities
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

                    // Search if there is sufficient space around to put all products
                    boolean espace_pour_produits = false;
                    ArrayList<UtilPoint3D> lst_pos = new ArrayList<>();
                    int nb_produits_effectif = 0;

                    if (tousLesReactifs == true) {
                        // Calculation of the number of effective products
                    	for (int pr = 0; pr < _produits.size(); pr++) {
                            if (!_produits.get(pr).equals("-")) {
                                nb_produits_effectif++;
                            }
                        }
                        boolean hors_cube = false;
                        for (int xx = x_min; xx <= x_max; xx++) {
                            for (int yy = y_min; yy <= y_max; yy++) {
                                for (int zz = z_min; zz <= z_max; zz++) {
                                    int ddd = (x - xx) * (x - xx) + (y - yy) * (y - yy) + (z - zz) * (z - zz);
                                    if (ddd <= 1 || ddd >= 4) { // Remove diagonals (d=2 ou d=3)
                                        xb = xx;
                                        yb = yy;
                                        zb = zz;
                                        //Manage the "edges" of the "torus"
                                        hors_cube = false;
                                        if (hors_cube == false) {
                                            if (xx == x && yy == y && zz == z) { // The "central" is already taken
                                                lst_pos.add(new UtilPoint3D(xb, yb, zb));
                                            } else if (instances.getFast(xb, yb, zb) == null) { // Creation
                                                lst_pos.add(new UtilPoint3D(xb, yb, zb));
                                            } else if (listReactifs.contains(instances.getFast(xb, yb, zb))) // Replacement ; if it's a reagent, its position is taken into account because it will disappear (react)
                                            {
                                                lst_pos.add(new UtilPoint3D(xb, yb, zb));
                                            }
                                            // Case where one impervious link prevent diagonal reactions
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

                    // Placement of products according to their position into the lists 
                    if (espace_pour_produits == true) {
                        InstanceReaction rp = new InstanceReaction();
                        _reactionsPossibles.add(rp);

                        // Replacements
                        for (int r = 0; r < listReactifs.size(); r++) {
                            int posx = 0, posy = 0, posz = 0;
                            // Removals
                            if (listReactifs.get(r) != null) { // It's null if it's "0"  - To modify to take into account the vacuum + memorize this vacuum position and see if this "0" count for a location of products
                                posx = listReactifs.get(r).getX();
                                posy = listReactifs.get(r).getY();
                                posz = listReactifs.get(r).getZ();
                                if (listReactifs.get(r).getNom() != null) { // non "0"
                                    rp._reactifs_noms.add(instances.getFast(posx, posy, posz).getNom());
                                    rp._reactifs_pos.add(new UtilPoint3D(posx, posy, posz));
                                }

                            }
                            // Addition
                            if (r < _produits.size()) {
                                if (!_produits.get(r).equals("-")) {
                                    rp._produits_noms.add(_produits.get(r));
                                    rp._produits_pos.add(new UtilPoint3D(posx, posy, posz));
                                    lst_pos.remove(new UtilPoint3D(posx, posy, posz)); // The place is not available anymore
                                }
                            }
                        }
                        // Simple additions
                        for (int p = listReactifs.size(); p < _produits.size(); p++) {
                            if (!_produits.get(p).equals("-")) {
                                int pos;
                                UtilPoint3D pt;
                                pos = RandomGen.getInstance().nextInt(lst_pos.size()); // Choice of the addition position
                                pt = lst_pos.get(pos);
                                rp._produits_noms.add(_produits.get(p));
                                rp._produits_pos.add(new UtilPoint3D(pt.x, pt.y, pt.z));

                                lst_pos.remove(pt);
                            }
                        }

                        //J decrement de j according to the list position
                        int deltaReactifs = listReactifs.size() - 1;
                        // Count of those which are * before * c_a
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
                        j -= deltaReactifs;

                        // Memorization of the reaction
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
        // Description
        toSave.add("\tDescription:" + _description.getText().replace('\n', '§') + "\n");

        // "Semi-located" and located
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

        // Complex
        for (int i = 0; i < _ListElementsReactions.size(); i++) {
            toSave.add(_ListElementsReactions.get(i).toSave());
        }
        return toSave;
    }

    //Get all parameters of Env_Parameters object linked to the Behavior one
    public Env_Parameters getParameters() {
        return parameters;
    }

    //Put new values to parameters of Env_Parameters object linked to the Behavior one
    public void setParameters(Env_Parameters parameters) {
        this.parameters = parameters;
    }
}
