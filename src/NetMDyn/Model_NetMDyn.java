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
 * Model_NetMDyn.java
 *
 * Created on February 12 2016, 09:34
 */

package NetMDyn;

import java.util.ArrayList; // Possible creation of tables
import java.util.HashMap; // Possible creation of hashmaps
import javax.swing.event.EventListenerList; // Possible creation of lists of EventListeners

import netbiodyn.AllInstances;
import netbiodyn.Behavior;
import netbiodyn.Entity;
import netbiodyn.InstanceReaxel;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.util.Serialized;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.util.SaverLoader;
import netbiodyn.util.FileSaverLoader;
import netbiodyn.ihm.Environment;
import netbiodyn.ihm.IhmListener;
import netbiodyn.Model;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

import NetMDyn.Compartment;
import NetMDyn.ihm.Environment_NetMDyn;
import NetMDyn.ihm.IhmListener_NetMDyn;
import NetMDyn.util.FileSaverLoader_NetMDyn;
import NetMDyn.util.SaverLoader_NetMDyn;
import NetMDyn.util.Serialized_NetMDyn;
import NetMDyn.util.UtilPoint3D_NetMDyn;

import jadeAgentServer.util.Parameter;

/**
 * Class of Model management
 * 
 * @author Master 2 Bioinformatique
 */

public class Model_NetMDyn {
	
    protected final EventListenerList listeners;
    protected Env_Parameters parameters;
    protected AllInstances_NetMDyn instances;
	private ArrayList<Compartment> compartments; 
	protected ArrayList<Entity_NetMDyn> entities;
	protected ArrayList<Behavior_NetMDyn> behaviors;
	protected SaverLoader_NetMDyn sl;
	
	/**
	 * Initialization of a Model object with Environment parameters
	 * @param parameters : the Environment parameters 
	 */
    public Model_NetMDyn(Env_Parameters parameters) {
    	listeners = new EventListenerList();
        this.parameters = parameters;
        entities = new ArrayList<>();
        behaviors = new ArrayList<>();
        compartments = new ArrayList<>();
        instances = new AllInstances_NetMDyn(parameters.getX(), parameters.getY(), parameters.getZ());
    }

    /**
     * Initialization of a Model object with all possible parameters
     * @param parameters : the Environment parameters
     * @param instances : all the Instances of the Model
     * @param entities : all the Entities of the Model
     * @param behaviors : all the Behaviors of the Model
     * @param compartments : all the Compartments of the Model
     */
    public Model_NetMDyn(Env_Parameters parameters, AllInstances_NetMDyn instances, ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviors, ArrayList<Compartment> compartments) {
    	this.parameters = parameters;
        this.instances = instances;
        this.entities = entities;
        this.behaviors = behaviors;
        this.compartments = compartments;
        listeners = new EventListenerList();
    }
    
    /**
     * Addition of the listener into the Model
     * @param listen : the new listener to add
     */
    public void addListener(IhmListener_NetMDyn listen) {
        listeners.add(IhmListener_NetMDyn.class, listen);
    }
    
    /**
     * Creation of a new Model with no parameters at all, and creation of default listeners
     */
    public void newModel() {
        entities = new ArrayList<>();
        behaviors = new ArrayList<>();
        compartments = new ArrayList<>();
        instances = new AllInstances_NetMDyn(parameters.getX(), parameters.getY(), parameters.getZ());
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }

    /**
     * Return the type of the Reaxel at these coordinates
     * @param x : coordinate
     * @param y : coordinate
     * @param z : coordinate
     * @return the type of the Reaxel 
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
     * Remove all the Compartments and Reaxels into the Environnement of the Model 
     */
    public void clearEnvironment() {
    	for(Compartment comp : compartments){
    		comp.setRadius(0);
    		comp.setCenter(new UtilPoint3D());
    	}
        instances = new AllInstances_NetMDyn(parameters.getX(), parameters.getY(), parameters.getZ());
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    /**
     * Remove all the Compartments and Reaxels into the Environnement of the Model, and update the Listeners
     */
    public void clear_OnlyCleanable() {
    	for(Compartment comp : compartments){
    		if(comp.Vidable){
    			comp.setRadius(0);
    			comp.setCenter(new UtilPoint3D());
    		}
    	}
        for (int i = 0; i < parameters.getX(); i++) {
            for (int j = 0; j < parameters.getY(); j++) {
                for (int k = 0; k < parameters.getZ(); k++) {
                    instances.removeOnlyCleanable(i, j, k);
                }
            }
        }

        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    /**
     * Select Reaxels at these coordinates
     * @param x : coordinate 
     * @param y : coordinate
     * @param z : coordinate
     */
    public void select(int x, int y, int z) {
        instances.select(x, y, z);
    }

    /**
     * Unselect Reaxels at these coordinates
     * @param x : coordinate
     * @param y : coordinate
     * @param z : coordinate
     */
    public void unselect(int x, int y, int z) {
        instances.unselect(x, y, z);
    }

    /**
     * Unselect the list of selected Reaxels
     * @param cubes_selectionnes : selected Reaxels
     */
    public void unselect(ArrayList<InstanceReaxel_NetMDyn> cubes_selectionnes) {
        if (cubes_selectionnes != null) {
            for (InstanceReaxel_NetMDyn r : cubes_selectionnes) {
                unselect(r.getX(), r.getY(), r.getZ());
            }
        }
    }

    /**
     * Move the selected Reaxels to these coordinates 
     * @param _cubes_selectionnes : selected Reaxels
     * @param new_x : coordinate
     * @param new_y : coordinate
     * @param new_z : coordinate
     */
    public void deplacer(ArrayList<InstanceReaxel_NetMDyn> _cubes_selectionnes, int new_x, int new_y, int new_z) {
        this.unselect(_cubes_selectionnes);
        // Check if the new new position has not already reaxels
        UtilPoint3D cdg = placeLibre(_cubes_selectionnes, new_x, new_y, new_z);
        if (cdg != null) {
            int dx = cdg.x;
            int dy = cdg.y;
            int dz = cdg.z;
            // Empty all initial locations
            for (InstanceReaxel_NetMDyn r : _cubes_selectionnes) {
                instances.removeReaxel(r.getX(), r.getY(), r.getZ());
            }
            // Move all reaxels of the list
            for (InstanceReaxel_NetMDyn r : _cubes_selectionnes) {
                if (instances.getFast(r.getX() + dx, r.getY() + dy, r.getZ() + dz) == null) {
                    r.setX(r.getX() + dx);
                    r.setY(r.getY() + dy);
                    r.setZ(r.getZ() + dz);
                    instances.addReaxel(r);
                }
            }
            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
                listen.matrixUpdate(getInstances(), getInitialState(), 0);
            }
        }
    }
    
    /**
     * Calculation of the new location and check if there is already no Reaxels at them 
     * @param lst : list of selected Reaxels
     * @param xg1 : coordinate
     * @param yg1 : coordinate
     * @param zg1 : coordinate
     * @return the new location if it's empty of Reaxels
     */
    protected UtilPoint3D_NetMDyn placeLibre(ArrayList<InstanceReaxel_NetMDyn> lst, int xg1, int yg1, int zg1) {
        if (lst == null) {
            return null;
        }
        // Check if the new new position has not already reaxels
        UtilPoint3D_NetMDyn pt_g0 = UtilPoint3D_NetMDyn.centreDeGravite(lst, true);
        int dx = xg1 - pt_g0.x;
        int dy = yg1 - pt_g0.y;
        int dz = zg1 - pt_g0.z;
        UtilPoint3D_NetMDyn new_point = new UtilPoint3D_NetMDyn(dx, dy, dz);
        // Check if all locations are empty
        for (InstanceReaxel_NetMDyn r : lst) {
            InstanceReaxel_NetMDyn rf = instances.getFast(r.getX() + dx, r.getY() + dy, r.getZ() + dz);
            if (rf != null) {
                if (rf.isSelectionne() == false) {
                    return null;
                }
            }
        }
        return new_point;
    }

    /**
     * Put new values to Environment parameters
     * @param parameters : Environment parameters
     */
    public void setParameters(Env_Parameters parameters) {
        this.parameters = parameters;
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.newEnvParameters(parameters);
        }

    }
    
    /**
     * Add a Reaxel at these coordinates
     * @param i : X coordinate 
     * @param j : Y coordinate
     * @param k : Z coordinate
     * @param etiquette : name of the Reaxel
     * @return if the Rexal has been added
     */
    protected boolean AjouterReaxel(int i, int j, int k, String etiquette) {
        boolean changed = false;
        for (int n = 0; n < entities.size(); n++) {
            if (entities.get(n).TrouveEtiquette(etiquette) >= 0) {
                InstanceReaxel_NetMDyn r = InstanceReaxel_NetMDyn.CreerReaxel(entities.get(n));
                r.setX(i);
                r.setY(j);
                r.setZ(k);
                changed = instances.addReaxel(r);
                n = entities.size();
            }
        }
        return changed;
    }
    
    /**
     * Add Reaxels of one Entity 
     * @param points : all Reaxels to add
     * @param etiquette : the name of the Entity
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
                listen.matrixUpdate(getInstances(), getInitialState(), 0);
            }
        }
    }

    /**
     * Remove Reaxels of one Entity at these coordinates
     * @param x : coordinate
     * @param y : coordinate
     * @param z : coordinate
     */
    public void removeEntityInstance(int x, int y, int z) {
        instances.removeReaxel(x, y, z);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    /**
     * Return the state of the Model
     * @return the hashmap 
     */
    public HashMap<String, Integer> getInitialState() {
        HashMap<String, Integer> init = instances.getBook();
        for (Entity_NetMDyn entity : entities) {
            if (init.containsKey(entity._etiquettes) == false) {
                init.put(entity._etiquettes, 0);
            }
        }

        return init;
    }
    
    /**
     * Return the Environment parameters
     * @return the Environment parameters
     */
    public Env_Parameters getParameters() {
        return parameters;
    }

    /**
     * Put new values to parameters
     * @param param : hashmap containing all new parameters
     * @param maxPoint 
     */
    public void changeParameters(HashMap<String, ArrayList<Parameter>> param, UtilPoint3D maxPoint) {
        ArrayList<Parameter> ent = param.get("Entities");
        for (Parameter p : ent) {
            editEntitiesHalfLife(p.getRelated().get(0), p.getValue());
        }

        ArrayList<Parameter> beh = param.get("Behaviours");
        for (Parameter p : beh) {
            editBehaviourProba(p.getName(), p.getValue());
        }

        ArrayList<Parameter> init = param.get("InitialState");
        for (Parameter p : init) {
            editInstancesInitialState(p.getRelated().get(0), p.getValue().intValue(), maxPoint);
        }

        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.ready();
        }
    }
    
    /**
     * Change one state of the Model
     * @param nom : the name of the Entity
     * @param value : 
     * @param maxPoint
     */
    public void editInstancesInitialState(String nom, int value, UtilPoint3D maxPoint) {
        int delta = value - getInitialState().get(nom);
        if (delta > 0) {
            int maxX = maxPoint.x;
            int maxY = maxPoint.y;
            int maxZ = maxPoint.z;
            ArrayList<UtilPoint3D> toAdd = new ArrayList<>();
            for (int i = 0; i < delta; i++) {
                int x = RandomGen.getInstance().nextInt(maxX);
                int y = RandomGen.getInstance().nextInt(maxY);
                int z = RandomGen.getInstance().nextInt(maxZ);
                if (instances.getFast(x, y, z) == null) {
                    UtilPoint3D point = new UtilPoint3D(x, y, z);
                    if (!toAdd.contains(point)) {
                        toAdd.add(point);
                    } else {
                        i = i - 1;
                    }
                } else {
                    i = i - 1;
                }
            }
            this.addEntityInstances(toAdd, nom);
        } else {
            ArrayList<InstanceReaxel_NetMDyn> entities = instances.getByName(nom);
            delta = delta * -1;
            int size = entities.size();
            if (delta >= size) {
                instances.removeByName(nom);
            } else {
                for (int i = 0; i < delta; i++) {
                    instances.removeReaxel(entities.get(i));
                    size = size - 1;
                }
            }
        }

    }

    /**
     * Get the instances of the Model 
     * @return the Instances of the Model
     */
    public AllInstances_NetMDyn getInstances() {
        return instances;
    }

    /**
     * Get the compartments of the Model
     * @param name : the wanted compartment 
     * @return a clone of the wanted compartment
     */
    public Compartment getCompartment(String name) {
        for (int i = compartments.size() - 1; i >= 0; i--) {
            Compartment comp = compartments.get(i);
            if (comp.getEtiquettes().equals(name)) {
                return comp.clone();
            }
        }
        return null;
    }
    
    /**
     * Add a new compartment to the Model
     * @param comp : name of the compartment
     */
    public void addCompartment(Compartment comp) {
        compartments.add(comp);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    /**
     * Delete compartments to the Model
     * @param compartments : list of all the compartments
     */
    public void delCompartment(ArrayList<String> compartments) {
        for (String name : compartments) {
            Compartment comp = this.getCompartment(name);
            this.removeCompart(comp);
        }
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    /**
     * Delete a compartment to the Model
     * @param comp : the compartment to remove
     */
    public void removeCompart(Compartment comp){
         for (int i = 0; i < compartments.size(); i++) {
             if (compartments.get(i).getEtiquettes().equals(comp.getEtiquettes())) { 
             		compartments.remove(i);
             }
    	}
    }
    
    /**
     * Edit the name of a compartment
     * @param m : the compartment to edit
     * @param old_name : the name of the compartment to edit
     */
    public void editCompartment(Compartment m, String old_name) {
        int index1 = 0;
        for (int i = 0; i < compartments.size(); i++) {
            if (compartments.get(i).getEtiquettes().equals(old_name)) {
            	compartments.remove(i);
                index1 = i;
                i = compartments.size();
            }
       }
        compartments.add(index1, m);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    /**
     * Check if there is collision between the Reaxels
     * @param name : the name of the compartment
     * @param points
     * @return if there is collision or not
     */
    public boolean verifCollision(String name, ArrayList<UtilPoint3D> points){
    	boolean rep = false;
    	for (Compartment comp: compartments){
    		if (comp.getEtiquettes().equals(name)){
    			continue;
    		}
    		 ArrayList<UtilPoint3D> lst_pts_tmp = UtilPoint3D_NetMDyn.BresenhamRond3D(comp.getCenter().x,comp.getCenter().y, comp.getCenter().z, comp.getRadius(), getParameters().getZ());
    		 for (UtilPoint3D point : points){
    			 for (UtilPoint3D point_tmp : lst_pts_tmp){
    				 rep = point.equals(point_tmp);
    				 if (rep == true){
    					 return false;
    				 }
    			 }
    		 }
    	}
    	return true;
    }
    
    /**
     * Return an Entity
     * @param name : name of the wanted Entity
     * @return the Entity
     */
    public Entity_NetMDyn getProtoReaxel(String name) {
        for (Entity_NetMDyn entity : entities) {
            if (entity._etiquettes.equals(name)) {
                return entity.clone();
            }
        }
        return null;
    }

    /**
     * Return all Entity names
     * @return the list of Entity names
     */
    public ArrayList<String> getEntitiesNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Entity r : entities) {
            names.add(r.getEtiquettes());
        }
        return names;
    }
    
    /**
     * Add a new Entity
     * @param entity : the Entity to add
     */
    public void addProtoReaxel(Entity_NetMDyn entity) {
        entities.add(entity);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
        }
    }

    /**
     * Edit an Entity
     * @param entity : new Entity to add instead of the old one
     * @param old_name : the name of the Entity to edit
     * @param time
     */
    public void editProtoReaxel(Entity_NetMDyn entity, String old_name, int time) {
        int index = 0;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i)._etiquettes.equals(old_name)) {
                entities.remove(i);
                index = i;
                i = entities.size();
            }
        }
        entities.add(index, entity);
        instances.editReaxels(entity, old_name);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            if (time == 0) {
                listen.matrixUpdate(getInstances(), getInitialState(), 0);
            }
        }
        if (!entity.getEtiquettes().equals(old_name)) {
            editInBehaviors(entity.getEtiquettes(), old_name);
            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
                listen.moteurReactionUpdate(getCopyListManipulesReactions());
            }
        }
    }
    
    /**
     * Remove Entities
     * @param entities : list of Entities 
     */
    public void delProtoReaxel(ArrayList<String> entities) {
        for (String r : entities) {
            Entity_NetMDyn rea = getProtoReaxel(r);
            this.entities.remove(rea);
            instances.removeEntityType(r);
        }

        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    /**
     * Edit the half-life of Entities
     * @param nom : name of the wanted Entity
     * @param value : new half-life value
     */
    public void editEntitiesHalfLife(String nom, double value) {
        Entity_NetMDyn entity = getProtoReaxel(nom);
        if (entity != null) {
            entity.DemieVie = value;
            this.editProtoReaxel(entity, nom, 0);
        }
    }
    
    /**
     * Return the list of Entities
     * @return Entities list
     */
    public ArrayList<Entity_NetMDyn> getListManipulesNoeuds() {
        return entities;
    }
    
    /**
     * Return a Behavior
     * @param name : name of the wanted Behavior
     * @return a clone of the Behavior
     */
    public Behavior_NetMDyn getBehaviour(String name) {
        for (int i = behaviors.size() - 1; i >= 0; i--) {
            Behavior_NetMDyn moteur = behaviors.get(i);
            if (moteur.getEtiquettes().equals(name)) {
                return moteur.clone();
            }
        }
        return null;
    }
    
    /**
     * Edit Entity into behaviors
     * @param entity : new name of the Entity to edit
     * @param old_name : old name of the Entity to edit
     */
    protected void editInBehaviors(String entity, String old_name) {
        for (Behavior_NetMDyn moteur : behaviors) {
            moteur.protoReaxelNameChanged(old_name, entity);
        }
    }
    
    /**
     * Edit the probability of the Behavior
     * @param name : the name of the wanted Behavior
     * @param value : new value of the probability of the Behavior
     */
    public void editBehaviourProba(String name, double value) {
        for (int i = behaviors.size() - 1; i >= 0; i--) {
            Behavior_NetMDyn moteur = behaviors.get(i);
            if (moteur.getEtiquettes().equals(name)) {
                behaviors.get(i).setProba(value);
            }
        }

        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    /**
     * Add a new Reaction
     * @param behaviour : new Behavior to add
     */
    public void addMoteurReaction(Behavior_NetMDyn behaviour) {
        behaviors.add(behaviour);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    /**
     * Edit a Reaction
     * @param m : new Behavior to add instead of the old one
     * @param old_name : name of the Behavior to edit
     */
    public void editMoteurReaction(Behavior_NetMDyn m, String old_name) {
        int index = 0;
        for (int i = 0; i < behaviors.size(); i++) {
            if (behaviors.get(i)._etiquettes.equals(old_name)) {
                behaviors.remove(i);
                index = i;
                i = behaviors.size();
            }
        }
        behaviors.add(index, m);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    /**
     * Delete a Reaction
     * @param reactions : list of Reactions
     */
    public void delMoteurReaction(ArrayList<String> reactions) {
        for (String name : reactions) {
            Behavior r = this.getBehaviour(name);
            behaviors.remove(r);
        }
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }
    
    /**
     * Load a Model
     * @param env : Environment
     * @param nomFichier : name of the file to load
     */
    public void load(Environment_NetMDyn env, String nomFichier) {
        sl = new FileSaverLoader_NetMDyn(env, nomFichier);
        Serialized_NetMDyn saved = sl.load();
        if (saved != null) {
            clearEnvironment();
            this.parameters = saved.getParameters();
            Lang.getInstance().setLang(parameters.getLang());
            instances = new AllInstances_NetMDyn(saved.getInstances());
            entities = saved.getListManipulesNoeuds();
            behaviors = saved.getListManipulesReactions();
            compartments= saved.getListManipulesCompartment();            
            
            for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
                listen.newEnvLoaded(saved, getInitialState());
                listen.CompartmentUpdate(getCopyListManipulesCompartment());
            }
        }
    }
    
    /**
     * Save the Model
     * @param env : Environment
     * @param nomFichier : name of the file to load
     * @param path : path where to save the file
     */
    public void save(Environment_NetMDyn env, String nomFichier, String path) {
        sl = new FileSaverLoader_NetMDyn(env, nomFichier);
        ((FileSaverLoader_NetMDyn) (sl)).setParentPath(path);

        Serialized_NetMDyn toSave = new Serialized_NetMDyn(parameters);
        toSave.setListManipulesNoeuds(entities);
        toSave.setListManipulesReactions(behaviors);
        toSave.setListManipulesCompartment(compartments);
        toSave.setInstances(getInstances());
        toSave.setParameters(parameters);
        sl.save(toSave);
    }
    
    /**
     * Return the Compartments of the Model
     * @return the list of the Compartments
     */
    public ArrayList<Compartment> getListManipulesCompartment() {
        return compartments;
    }
    
    /**
     * Return a copy of the Compartments of the Model
     * @return a list of the Compartments
     */
    public ArrayList<Compartment> getCopyListManipulesCompartment() {
        ArrayList<Compartment> comps = new ArrayList<>();
        for (Compartment r : compartments) {
            comps.add(r.clone());
        }
        return comps;
    }
    
    /**
     * Return the Behaviors of the Model
     * @return the list of the Behaviors
     */
    public ArrayList<Behavior_NetMDyn> getListManipulesReactions() {
        return behaviors;
    }

    /**
     * Return a copy of the Entities of the Model
     * @return a list of the Entities
     */
    public ArrayList<Entity_NetMDyn> getCopyListManipulesNoeuds() {
        ArrayList<Entity_NetMDyn> proto = new ArrayList<>();
        for (Entity_NetMDyn r : entities) {
            proto.add(r.clone());
        }
        return proto;
    }

    /**
     * Return a copy of the Behaviors of the Model
     * @return a list of the Behaviors
     */
    public ArrayList<Behavior_NetMDyn> getCopyListManipulesReactions() {
        ArrayList<Behavior_NetMDyn> moteurs = new ArrayList<>();
        for (Behavior_NetMDyn r : behaviors) {
            moteurs.add(r.clone());
        }
        return moteurs;
    }
    
    /**
     * Clone this Model object
     * @return the new Model
     */
    public Model_NetMDyn clone() {
        return new Model_NetMDyn(getParameters(), getInstances().clone(), getListManipulesNoeuds(), getListManipulesReactions(), getListManipulesCompartment());
    }
}
