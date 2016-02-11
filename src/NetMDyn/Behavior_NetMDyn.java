/* This file is part of NetMDyn.
 *
 *   NetMDyn is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   any later version.
 *
 *   NetMDyn is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with NetBioDyn; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * Behavior_NetMDyn.java
 *
 * Created on February 11 2016, 19:04
 */



package NetMDyn;

import netbiodyn.AllInstances;
import netbiodyn.Behavior;
import netbiodyn.InstanceReaction;
import netbiodyn.InstanceReaxel;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.ihm.WndEditElementDeReaction;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

import java.awt.Point; // A point representing a location in (x,y) coordinate space
import java.util.ArrayList; // Possible creation of tables
import java.util.HashMap; // Possible creation of hashmaps

/**
 * Class of Behavior management
 * 
 * @author Master 2 Bioinformatique
 */

public class Behavior_NetMDyn extends Behavior {
	private int type_behavior;
	private double K = 0.0;
	private double proba;
	
	
    public int getType_behavior() {
		return type_behavior;
	}

	public void setType_behavior(int type_behavior) {
		this.type_behavior = type_behavior;
	}

	public double getK() {
		return K;
	}

	public void setK(double k) {
		K = k;
	}

	public double getProba() {
		return proba;
	}

	public void setProba(double proba) {
		this.proba = proba;
	}

	/**
	 * 
	 * @param s : the simulator
	 * @param param : the environment parameters
	 * @param instances : instances
	 * @param time
	 */
    public void computeReactions(Simulator_NetMDyn s, Env_Parameters param, AllInstances_NetMDyn instances, int time) {
        this.setParameters(param); // Put new values to parameters of Env_Parameters object linked to the Behavior one
        _reactionsPossibles = new ArrayList<>(); // Creation of a new table which will contain all possible reactions

        this.simuler_semi_situee(instances, time);
        s.decrementer_nb_processus_a_traiter();
    }
    
    /**
     * Clone of this object into a new one
     * @return the new behavior
     */
    public Behavior_NetMDyn clone() {
        Behavior_NetMDyn  m = new Behavior_NetMDyn (); // Creation of a new Behavior object
        // Get all the parameters of the old Behavior object to put them into the new one
        m.setEtiquettes(getEtiquettes());
        m._description.setText(_description.getText().replace('\n', '§'));
        m._reactifs = (ArrayList<String>) _reactifs.clone();
        m._produits = (ArrayList<String>) _produits.clone();
        m._positions = (ArrayList<String>) _positions.clone();
        m._ListElementsReactions = (ArrayList<WndEditElementDeReaction>) _ListElementsReactions.clone();
        m._visibleDansPanel=_visibleDansPanel;
        m.setProba(getProba());
        m.setK(getK());
        m.setType_behavior(getType_behavior());
        return m; //Return the new Behavior object
    }
    
    /**
     * 
     * @param instances
     * @param time
     */
    public void simuler_semi_situee(AllInstances_NetMDyn instances, int time) {
        HashMap<String, Point> dico_rea = new HashMap<>();

        if (_reactifs.size() <= 0) {
            return; // End of the function if there is no reagent
        }
        // For all the manipulated elements (compartment,...), 
        // Search of the "clinamon" 'molecule C' to obtain its pseudoforme thereafter
        // If control atom
        InstanceReaxel c_a; 
        int xb, yb, zb, x, y, z;

        for (int j = instances.getSize() - 1; j >= 0; j--) { // Path of AllInstances object
            c_a = instances.getInList(j); // The InstanceReaxel is the one at the position j in AllInstances object
            // Search of a molecule A into the list
            if (c_a.getNom().equals(_reactifs.get(0)) && c_a.isSelectionne() == false) {
                x = c_a.getX();
                y = c_a.getY();
                z = c_a.getZ();
                // Test if a reaction occurs or not
                double hasard = RandomGen.getInstance().nextDouble();
                if (hasard < this.proba) {
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
                        if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
                        	// This case is synonym of a big problem
                        	System.err.println("GROS PROBLEME en " + x + "*" + y + "*" + z);
                        	System.err.println("EGAL A " + instances.getInList(j).getX() + "*" + instances.getInList(j).getY() + "*" + instances.getInList(j).getZ() + " ? ");
                        }
                        else{
                        	System.err.println("BIG PROBLEM IN " + x + "*" + y + "*" + z);
                        	System.err.println("EQUAL TO " + instances.getInList(j).getX() + "*" + instances.getInList(j).getY() + "*" + instances.getInList(j).getZ() + " ? ");
                        }
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
                                            // Manage the "edges" of the "torus"

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
                                    if (ddd <= 1 || ddd >= 4) { // Remove diagonals (d=2 or d=3)
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

                        //J decrement of j according to the list position
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
    
    public ArrayList<String> toSave() {
        ArrayList<String> toSave = super.toSave();
        // Description
        toSave.add("\tType_Behaviour:" + type_behavior + "\n");
        toSave.add("\tK:" + K + "\n");
        toSave.add("\tProba:" + proba + "\n");
        return toSave;
    }


}
