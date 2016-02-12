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
 * Simulator_NetMDyn.java
 *
 * Created on February 12 2016, 12:12
 */


package NetMDyn;

import java.awt.event.ActionEvent; // A semantic event which indicates that a component-defined action occurred
import java.awt.event.ActionListener; // The listener interface for receiving action events
import java.util.ArrayList; // Possible creation of tables
import java.util.HashMap; // Possible creation of hashmaps
import java.util.List; // Possible creation of lists
import javax.swing.event.EventListenerList; // Possible creation of lists of EventListeners

import NetMDyn.ihm.IhmListener_NetMDyn;
import netbiodyn.ihm.IhmListener;
import netbiodyn.AllInstances;
import netbiodyn.InstanceReaction;
import netbiodyn.InstanceReaxel;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

/**
 * Class of Simulator management
 * 
 * @author Master 2 Bioinformatique
 */

public class Simulator_NetMDyn {

	    private final EventListenerList listeners;

	    private int speed = 1;
	    private int time = 0;
	    private boolean pause = false;
	    private boolean stopped = true;
	    private int maxStep = -1;
	    private final Model_NetMDyn model;
	    private javax.swing.Timer timer_play;

	    private AllInstances_NetMDyn instances;
	    private AllInstances_NetMDyn instancesFutur;

	    private int nb_processus_a_traiter = 0;

	    /**
	     * Initialization of the Simulator object
	     * @param model : Model object
	     */
	    public Simulator_NetMDyn(Model_NetMDyn model) {
	        listeners = new EventListenerList();

	        this.model = model;
	        init();
	        initTimer();
	    }

	    /**
	     * Initialization of the Instances
	     */
	    private void init() {
	        instances = new AllInstances_NetMDyn(model.getInstances());
	    }
	    
	    /**
	     * Add a listener
	     * @param listen : the listener to add
	     */
	    public void addListener(IhmListener_NetMDyn listen) {
	        listeners.add(IhmListener_NetMDyn.class, listen);
	    }

	    /**
	     * Initialization of a Timer
	     */
	    private void initTimer() {
	        timer_play = new javax.swing.Timer(this.getSpeed(), new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                play();
	            }
	        });
	    }

	    /**
	     * Start a simulation
	     */
	    public void start() {
	        if (getTime() == 0) {
	            init();
	        }
	        setStopped(false);
	        setPause(false);
	    }

	    /**
	     * Stop a simulation
	     */
	    public void stop() {
	        setStopped(true);
	        timer_play.stop();
	        init();
	        pause = false;
	        setTime(0);

	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), this.updateList(), getTime());
	        }
	    }

	    /**
	     * Play the simulation
	     */
	    private void play() {        
	        // Half-life
	        // --------
	        for (int pos_in_list = instances.getSize() - 1; pos_in_list >= 0; pos_in_list--) {
	            InstanceReaxel_NetMDyn c = instances.getInList(pos_in_list);
	            // Half-life simulation
	            if (c.getDemie_vie() > 0 && !c.isSelectionne()) {
	                double proba_mort = 1 - Math.pow(0.5, 1.0 / c.getDemie_vie());
	                if (RandomGen.getInstance().nextDouble() < proba_mort) {
	                    instances.removeReaxel(c);
	                }
	            }
	        }

	        // ***************************************
	        // Take into account all reactions
	        // ***************************************
	        // List of all reaction motors
	        List<Behavior_NetMDyn> lst_react = model.getListManipulesReactions();
	        // Set to zero the future matrix of reaxels
	        Env_Parameters param = model.getParameters();
	        instancesFutur = new AllInstances_NetMDyn(param.getX(), param.getY(), param.getZ());

	        // Parallel launch of all reactions
	        setNb_processus_a_traiter(lst_react.size());
	        for (Behavior_NetMDyn lst_react1 : lst_react) {
	            lst_react1.computeReactions(this, param, instances.clone(), getTime());
	        }

	        // Concatenation of all possible reactions
	        ArrayList<InstanceReaction> lst_rp = new ArrayList<>();
	        for (Behavior_NetMDyn lst_react1 : lst_react) {
	            lst_rp.addAll(lst_react1._reactionsPossibles);
	        }

	        // Index of mixed path 
	        ArrayList<Integer> lst_int = RandomGen.getInstance().liste_entiers_melanges(lst_rp.size());
	        // Execution of the effective choice of reactions
	        for (int r = 0; r < lst_rp.size(); r++) {
	            InstanceReaction rp = lst_rp.get(lst_int.get(r));
	            // Try to apply the transformation
	            boolean possible = true;

	            if (rp._type != 2) { // "Semi-situées" and situated reactions
	                // Check if reagents are always here
	                for (int i = 0; i < rp._reactifs_noms.size(); i++) {
	                    int x = rp._reactifs_pos.get(i).x;
	                    int y = rp._reactifs_pos.get(i).y;
	                    int z = rp._reactifs_pos.get(i).z;
	                    // Case of empty
	                    if (instances.getFast(x, y, z) == null) {
	                        if (!rp._reactifs_noms.get(i).equals("0")) { // The reaction want a empty place
	                            possible = false;
	                            i = rp._reactifs_noms.size();
	                        }
	                    } else // Case of non-empty
	                    {
	                        if (!instances.getFast(x, y, z).getNom().equals(rp._reactifs_noms.get(i))) { // The reaction can find the good name of reaxel in the actual matrix
	                            possible = false;
	                            i = rp._reactifs_noms.size();
	                        }
	                    }
	                }

	                // Check if there is a place for products
	                if (possible == true) {
	                    for (int i = 0; i < rp._produits_noms.size(); i++) {
	                        int x = rp._produits_pos.get(i).x;
	                        int y = rp._produits_pos.get(i).y;
	                        int z = rp._produits_pos.get(i).z;
	                        if (instancesFutur.getFast(x, y, z) != null) { // If occupied space
	                            if (!rp._produits_noms.get(i).equals("")) { // If a product has to be placed in here = impossible !
	                                possible = false;
	                                i = rp._produits_noms.size();
	                            }
	                        }
	                    }
	                }

	                // Transformation if possible
	                if (possible == true) {
	                    // Addition of products into the future matrix
	                    for (int i = 0; i < rp._produits_noms.size(); i++) {
	                        int x = rp._produits_pos.get(i).x;
	                        int y = rp._produits_pos.get(i).y;
	                        int z = rp._produits_pos.get(i).z;
	                        this.AjouterFuturReaxel(x, y, z, rp._produits_noms.get(i));
	                    }

	                    // Deletion of reagents into the actual matrix to avoid a new reaction
	                    for (int i = 0; i < rp._reactifs_noms.size(); i++) {
	                        int x = rp._reactifs_pos.get(i).x;
	                        int y = rp._reactifs_pos.get(i).y;
	                        int z = rp._reactifs_pos.get(i).z;
	                        if (instances.getFast(x, y, z) != null) {
	                            instances.removeReaxel(x, y, z);
	                        }
	                    }
	                }
	            }
	            // Next possible reaction
	        }

	        // End of effective application of reactions
	        // Put non-reactive reaxels in the future list and into the future matrix
	        instancesFutur.setMatrixAndList(instances.getList());

	        instances = new AllInstances_NetMDyn(instancesFutur.getList(), instancesFutur.getMatrix(), instancesFutur.getX(), instancesFutur.getY(), instancesFutur.getZ());
	        this.setTime(getTime() + 1);
	        int t = getTime();

	        if (getMaxStep() != -1) {
	            if (getTime() == getMaxStep()) {                
	                setStopped(true);
	                timer_play.stop();
	                pause = false;
	                setMaxStep(-1);
	            }
	        }

	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), this.updateList(), t);
	        }

	    }

	    public void play_one() {
	        if (this.getTime() == 0) {
	            init();
	        }
	        this.play();
	    }

	    /**
	     * Delete all reaxels of an Entity
	     *
	     * @param reaxels : list of reaxels to remove
	     */
	    public void ProtoReaxelDeleted(ArrayList<String> reaxels) {
	        for (String p : reaxels) {
	            this.removeEntityInstances(p);
	        }
	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), updateList(), getTime());
	        }
	    }

	    /**
	     * 
	     * @param entities
	     * @return a hashmap
	     */
	    public HashMap<String, Integer> updateList() {
	        HashMap<String, Integer> entities = instances.getBook();
	        ArrayList<Entity_NetMDyn> reaxels = model.getListManipulesNoeuds();
	        for (Entity_NetMDyn entity : reaxels) {
	            if (entities.containsKey(entity._etiquettes) == false) {
	                entities.put(entity._etiquettes, 0);
	            }
	        }

	        return entities;
	    }

	    /**
	     * Remove all Reaxels of an Entity
	     * @param nom : name of the Entity
	     */
	    private void removeEntityInstances(String nom) {
	        for (int c = instances.getSize() - 1; c >= 0; c--) {
	            InstanceReaxel_NetMDyn cube = instances.getInList(c);
	            if (cube.getNom().equals(nom)) {
	                instances.removeReaxel(cube);
	            }
	        }
	    }

	    /**
	     * Add Reaxels of the Entity
	     * @param points : list of reaxels
	     * @param etiquette : name of the Entity
	     */
	    public void addEntityInstances(ArrayList<UtilPoint3D> points, String etiquette) {
	        boolean toUpdate = false;
	        for (UtilPoint3D point : points) {
	            int x = point.x;
	            int y = point.y;
	            int z = point.z;
	            if (this.AjouterReaxel(x, y, z, etiquette)) {
	                toUpdate = true;
	            }
	        }
	        if (toUpdate) {
	            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	                listen.matrixUpdate(getInstances(), updateList(), getTime());
	            }
	        }
	    }
	    /**
	     * Delete the Reaxel at these coordinates
	     * @param x : coordinate
	     * @param y : coordinate
	     * @param z : coordinate
	     */
	    public void removeEntityInstance(int x, int y, int z) {
	        boolean changed = instances.removeReaxel(x, y, z);
	        if (changed) {
	            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	                listen.matrixUpdate(getInstances(), updateList(), getTime());
	            }
	        }
	    }

	    /**
	     * Add a Reaxel at theses coordinates
	     * @param i : X coordinate
	     * @param j : Y coordinate
	     * @param k : Z coordinate
	     * @param etiquette : name of the Reaxel
	     * @return if the Reaxel has been added or not
	     */
	    private boolean AjouterReaxel(int i, int j, int k, String etiquette) {
	        boolean changed = false;
	        ArrayList<Entity_NetMDyn> reaxels = model.getListManipulesNoeuds();
	        for (int n = 0; n < reaxels.size(); n++) {
	            if (reaxels.get(n).TrouveEtiquette(etiquette) >= 0) {
	                InstanceReaxel_NetMDyn r = InstanceReaxel_NetMDyn.CreerReaxel(reaxels.get(n));
	                r.setX(i);
	                r.setY(j);
	                r.setZ(k);
	                changed = instances.addReaxel(r);
	                n = reaxels.size();
	            }
	        }
	        return changed;
	    }

	    /**
	     * Get the type of the Entity at these coordinates
	     * @param x : coordinate
	     * @param y : coordinate
	     * @param z : coordinate
	     * @return this type
	     */
	    public String getType(int x, int y, int z) {
	        InstanceReaxel r = instances.getFast(x, y, z);
	        if (r != null) {
	            return r.getNom();
	        } else {
	            return "";
	        }
	    }
	    
	    /**
	     * Add a future Reaxel
	     * @param i : X coordinate
	     * @param j : Y coordinate
	     * @param k : Z coordinate
	     * @param etiquette : name of the future Reaxel
	     * @return if the future Reaxel has been added or not
	     */
	    private boolean AjouterFuturReaxel(int i, int j, int k, String etiquette) {
	        boolean changed = false;
	        ArrayList<Entity_NetMDyn> reaxels = model.getListManipulesNoeuds();
	        for (int n = 0; n < reaxels.size(); n++) {
	            if (reaxels.get(n).TrouveEtiquette(etiquette) >= 0) {
	                InstanceReaxel_NetMDyn r = InstanceReaxel_NetMDyn.CreerReaxel(reaxels.get(n));
	                r.setX(i);
	                r.setY(j);
	                r.setZ(k);
	                changed = instancesFutur.addReaxel(r);
	                n = reaxels.size();
	            }
	        }
	        return changed;
	    }

	    /**
	     * Edit Reaxels of an Entity
	     * @param entity : name of the Entity
	     * @param old_name 
	     */
	    public void ProtoReaxelEdited(Entity_NetMDyn entity, String old_name) {
	        instances.editReaxels(entity, old_name);
	        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
	            listen.matrixUpdate(getInstances(), updateList(), getTime());
	        }
	    }

	    /**
	     * Check if the simulation is running or not
	     * @return true or false
	     */
	    public boolean isRunning() {
	        return timer_play.isRunning();
	    }

	    /**
	     * Check if the simulation is paused or not
	     * @return true or false
	     */
	    public boolean isPause() {
	        return pause;
	    }

	    /**
	     * Inverse the pause status of the simulation 
	     * @param pause : true ou false
	     */
	    public void setPause(boolean pause) {
	        this.pause = pause;
	        if (isPause()) {
	            timer_play.stop();
	        } else {
	            timer_play.start();
	        }
	    }

	    /**
	     * Obtain the speed of the simulation
	     * @return the speed
	     */
	    public int getSpeed() {
	        return speed;
	    }

	    /**
	     * Change the speed of the reaction
	     * @param the speed
	     */
	    public void setSpeed(int speed) {
	        this.speed = speed;
	        if (!isStopped()) {
	            timer_play.stop();
	            initTimer();
	        } else {
	            initTimer();
	        }
	    }

	    /**
	     * Obtain the time of the simulation 
	     * @return the time
	     */
	    public int getTime() {
	        return time;
	    }

	    /**
	     * Change the time of the simulation
	     * @param time : the new time
	     */
	    public void setTime(int time) {
	        this.time = time;
	    }

	    /**
	     * Obtain the number of process to treat
	     * @return this number
	     */
	    public int getNb_processus_a_traiter() {
	        return nb_processus_a_traiter;
	    }

	    /**
	     * Change the number of process to treat
	     * @param nb_processus_a_traiter
	     */
	    public void setNb_processus_a_traiter(int nb_processus_a_traiter) {
	        this.nb_processus_a_traiter = nb_processus_a_traiter;
	    }

	    /**
	     * Decrement the number of process to treat
	     */
	    public synchronized void decrementer_nb_processus_a_traiter() {
	        setNb_processus_a_traiter(getNb_processus_a_traiter() - 1);
	    }

	    public boolean isStopped() {
	        return stopped;
	    }

	    /**
	     * Stop the simulation
	     * @param stopped
	     */
	    public void setStopped(boolean stopped) {
	        this.stopped = stopped;
	    }

	    /**
	     * Obtain the maximum step of the simulation
	     * @return this step
	     */
	    public int getMaxStep() {
	        return maxStep;
	    }

	    /**
	     * Change the maximum step of the simulation
	     * @param maxStep
	     */
	    public void setMaxStep(int maxStep) {
	        this.maxStep = maxStep;
	    }

	    /**
	     * Obtain the instances of the simulation
	     * @return these instances
	     */
	    public AllInstances_NetMDyn getInstances() {
	        return instances;
	    }
	}

	

