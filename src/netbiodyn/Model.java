/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netbiodyn;//On crée un package, cette ligne indiquant que le fichier actuel sera dans ce package 


import netbiodyn.util.Serialized;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.util.SaverLoader;
import netbiodyn.util.FileSaverLoader;
import netbiodyn.ihm.Environment;
import java.util.ArrayList; //Création de listes
import java.util.HashMap; //Création de hashmap
import javax.swing.event.EventListenerList; //Création possible de listeners d'événements

import netbiodyn.ihm.IhmListener;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;
import jadeAgentServer.util.Parameter; //Mise en place de serveur multi-agents ?

/**
 *
 * @author riviere
 */
public class Model {

    private final EventListenerList listeners; //Liste des listeners
    private Env_Parameters parameters; //Paramètres de l'environnement

    private ArrayList<Compartment> compartment; // Compartements
    private ArrayList<Entity> entities; // Types d'entités
    private ArrayList<Behavior> behaviors; // Comportements
    private AllInstances instances; //Instances

    private SaverLoader sl; //Création de la partie import/export

    public Model(Env_Parameters parameters) {
        listeners = new EventListenerList(); //Mise en place des futurs listeners
        this.parameters = parameters; //Mise en place des paramètres du modèle

        compartment = new ArrayList<>(); //Mise en place des futurs compartiments
        entities = new ArrayList<>(); //Mise en place des futures entités
        behaviors = new ArrayList<>(); //Mise en place des futurs comportements
        instances = new AllInstances(parameters.getX(), parameters.getY(), parameters.getZ());
    }

    private Model(Env_Parameters parameters, AllInstances instances, ArrayList<Entity> entities, ArrayList<Behavior> behaviors, ArrayList<Compartment> compartment) {
        this.parameters = parameters;
        this.instances = instances;
        this.entities = entities;
        this.behaviors = behaviors;
        this.compartment = compartment;
        listeners = new EventListenerList();
    }

    public void addListener(IhmListener listen) {
        listeners.add(IhmListener.class, listen);
    }

    public void newModel() {
        entities = new ArrayList<>();
        behaviors = new ArrayList<>();
        compartment = new ArrayList<>();
        instances = new AllInstances(parameters.getX(), parameters.getY(), parameters.getZ());
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }

    public String getType(int x, int y, int z) {
        InstanceReaxel r = instances.getFast(x, y, z);
        if (r != null) {
            return r.getNom();
        } else {
            return "";
        }
    }

    public void clearEnvironment() {
        instances = new AllInstances(parameters.getX(), parameters.getY(), parameters.getZ());
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }

    public void clear_OnlyCleanable() {
    	for(Compartment comp : compartment){
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

        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }

    public void select(int x, int y, int z) {
        instances.select(x, y, z);
    }

    public void unselect(int x, int y, int z) {
        instances.unselect(x, y, z);
    }

    public void unselect(ArrayList<InstanceReaxel> cubes_selectionnes) {
        if (cubes_selectionnes != null) {
            for (InstanceReaxel r : cubes_selectionnes) {
                unselect(r.getX(), r.getY(), r.getZ());
            }
        }
    }

    public void reboot() {
        // TODO charger une ancienne version du modèle

    }

    public void deplacer(ArrayList<InstanceReaxel> _cubes_selectionnes, int new_x, int new_y, int new_z) {
        System.out.println(_cubes_selectionnes.toString());
        this.unselect(_cubes_selectionnes);
        // Calcul du cdg de la liste de reaxels, si toutes les places sont libres
        UtilPoint3D cdg = placeLibre(_cubes_selectionnes, new_x, new_y, new_z);
        if (cdg != null) {
            int dx = cdg.x;
            int dy = cdg.y;
            int dz = cdg.z;
            // Vidage des emplacements initiaux
            for (InstanceReaxel r : _cubes_selectionnes) {
                instances.removeReaxel(r.getX(), r.getY(), r.getZ());
            }
            // Deplacement de tous les reaxels de l'ensemble
            for (InstanceReaxel r : _cubes_selectionnes) {
                if (instances.getFast(r.getX() + dx, r.getY() + dy, r.getZ() + dz) == null) {
                    r.setX(r.getX() + dx);
                    r.setY(r.getY() + dy);
                    r.setZ(r.getZ() + dz);
                    instances.addReaxel(r);
                }
            }
            for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
                listen.matrixUpdate(getInstances(), getInitialState(), 0);
            }
        }
    }

    private UtilPoint3D placeLibre(ArrayList<InstanceReaxel> lst, int xg1, int yg1, int zg1) {
        if (lst == null) {
            return null;
        }

        // Calcul du cdg de la liste de reaxels
        UtilPoint3D pt_g0 = UtilPoint3D.centreDeGravite(lst);
        int dx = xg1 - pt_g0.x;
        int dy = yg1 - pt_g0.y;
        int dz = zg1 - pt_g0.z;
        UtilPoint3D new_point = new UtilPoint3D(dx, dy, dz);
        // Verif que toutes les places sont libres
        for (InstanceReaxel r : lst) {
            InstanceReaxel rf = instances.getFast(r.getX() + dx, r.getY() + dy, r.getZ() + dz);
            if (rf != null) {
                if (rf.isSelectionne() == false) {
                    return null;
                }
            }
        }
        return new_point;
    }

    public Behavior getBehaviour(String name) {
        for (int i = behaviors.size() - 1; i >= 0; i--) {
            Behavior moteur = behaviors.get(i);
            if (moteur.getEtiquettes().equals(name)) {
                return moteur.clone();
            }
        }
        return null;
    }

    public Entity getProtoReaxel(String name) {
        for (Entity entity : entities) {
            if (entity._etiquettes.equals(name)) {
                return entity.clone();
            }
        }
        return null;
    }
    
    public Compartment getCompartment(String name) {
        for (int i = compartment.size() - 1; i >= 0; i--) {
            Compartment comp = compartment.get(i);
            if (comp.getEtiquette().equals(name)) {
                return comp.clone();
            }
        }
        return null;
    }

    public ArrayList<String> getEntitiesNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Entity r : entities) {
            names.add(r.getEtiquettes());
        }
        return names;
    }

    public void setParameters(Env_Parameters parameters) {
        this.parameters = parameters;
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.newEnvParameters(parameters);
        }

    }

    
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
            for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
                listen.matrixUpdate(getInstances(), getInitialState(), 0);
            }
        }
    }

    public void removeEntityInstance(int x, int y, int z) {
        instances.removeReaxel(x, y, z);
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    
    public void addCompartment(Compartment comp) {
        compartment.add(comp);
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    public void delCompartment(ArrayList<String> compartments) {
        for (String name : compartments) {
            Compartment comp = this.getCompartment(name);
            Entity entity = comp.getEnt();
            this.entities.remove(entity);
            this.instances.removeEntityType(entity.getEtiquettes());
            this.removeCompart(comp);
        }
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
        	listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    public void removeCompart(Compartment comp){
         for (int i = 0; i < compartment.size(); i++) {
             if (compartment.get(i).getEtiquette().equals(comp.getEtiquette())) { 
             		compartment.remove(i);
             }
    	}
    }
    
    public void editCompartment(Compartment m, String old_name) {
        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < compartment.size(); i++) {
            if (compartment.get(i).getEtiquette().equals(old_name)) {
                for (int j = 0; j < entities.size(); j++) {
                    if (entities.get(j).getEtiquettes().equals('m'+old_name)) {
                    	entities.remove(j);
                        index2 = j;
                        j = entities.size();
                    }
                }
                Entity entity = m.entity_property();
                entities.add(index2, entity);
            	compartment.remove(i);
                index1 = i;
                i = compartment.size();
            }
        }
        compartment.add(index1, m);
        
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    
    public boolean verifCollision(String name, ArrayList<UtilPoint3D> points){
    	boolean rep = false;
    	for (Compartment comp: compartment){
    		if (comp.getEtiquette().equals(name)){
    			continue;
    		}
    		 ArrayList<UtilPoint3D> lst_pts_tmp = UtilPoint3D.BresenhamRond3D(comp.getCenter().x,comp.getCenter().y, comp.getCenter().z, comp.getRadius(), getParameters().getZ());
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
    
    public void addProtoReaxel(Entity entity) {
        entities.add(entity);
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
        }
    }

    public void editProtoReaxel(Entity entity, String old_name, int time) {
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
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            if (time == 0) {
                listen.matrixUpdate(getInstances(), getInitialState(), 0);
            }
        }
        if (!entity.getEtiquettes().equals(old_name)) {
            editInBehaviors(entity.getEtiquettes(), old_name);
            for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
                listen.moteurReactionUpdate(getCopyListManipulesReactions());
            }
        }
    }

    public void delProtoReaxel(ArrayList<String> entities) {
        for (String r : entities) {
            Entity rea = getProtoReaxel(r);
            this.entities.remove(rea);
            instances.removeEntityType(r);
//            this.removeInBehaviours(r.getEtiquettes());
        }

        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }

    private void editInBehaviors(String entity, String old_name) {
        for (Behavior moteur : behaviors) {
            moteur.protoReaxelNameChanged(old_name, entity);
        }
    }

    private boolean AjouterReaxel(int i, int j, int k, String etiquette) {
        boolean changed = false;
        for (int n = 0; n < entities.size(); n++) {
            if (entities.get(n).TrouveEtiquette(etiquette) >= 0) {
                InstanceReaxel r = InstanceReaxel.CreerReaxel(entities.get(n));
                r.setX(i);
                r.setY(j);
                r.setZ(k);
                changed = instances.addReaxel(r);
                n = entities.size();
            }
        }
        return changed;
    }

    public void editEntitiesHalfLife(String nom, double value) {
        Entity entity = getProtoReaxel(nom);
        if (entity != null) {
            entity.DemieVie = value;
            this.editProtoReaxel(entity, nom, 0);
        }
    }

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
            ArrayList<InstanceReaxel> entities = instances.getByName(nom);
            delta = delta * -1;
            int size = entities.size();
            if (delta >= size) {
                instances.removeByName(nom);
//                for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
//                    listen.matrixUpdate(getInstances(), getInitialState(), 0);
//                }
            } else {
                for (int i = 0; i < delta; i++) {
                    instances.removeReaxel(entities.get(i));
                    size = size - 1;
                }
            }
        }

    }

    public void editBehaviourProba(String name, double value) {
        for (int i = behaviors.size() - 1; i >= 0; i--) {
            Behavior moteur = behaviors.get(i);
            if (moteur.getEtiquettes().equals(name)) {
                behaviors.get(i).set_k(value);
            }
        }

        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    public void addMoteurReaction(Behavior behaviour) {
        behaviors.add(behaviour);

        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    public void editMoteurReaction(Behavior m, String old_name) {
        int index = 0;
        for (int i = 0; i < behaviors.size(); i++) {
            if (behaviors.get(i)._etiquettes.equals(old_name)) {
                behaviors.remove(i);
                index = i;
                i = behaviors.size();
            }
        }
        behaviors.add(index, m);
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    public void delMoteurReaction(ArrayList<String> reactions) {
        for (String name : reactions) {
            Behavior r = this.getBehaviour(name);
            behaviors.remove(r);
        }
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

    public void load(Environment env, String nomFichier) {
        sl = new FileSaverLoader(env, nomFichier);
        Serialized saved = sl.load();
        if (saved != null) {
            clearEnvironment();
            this.parameters = saved.getParameters();
            Lang.getInstance().setLang(parameters.getLang());
            instances = new AllInstances(saved.getInstances());
            entities = saved.getListManipulesNoeuds();
            behaviors = saved.getListManipulesReactions();
            for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
                listen.newEnvLoaded(saved, getInitialState());
            }
        }
    }

    public void save(Environment env, String nomFichier, String path) {
        sl = new FileSaverLoader(env, nomFichier);
        ((FileSaverLoader) (sl)).setParentPath(path);

        Serialized toSave = new Serialized(parameters);
        toSave.setListManipulesNoeuds(entities);
        toSave.setListManipulesReactions(behaviors);
        toSave.setListManipulesCompartment(compartment);
        toSave.setInstances(getInstances());
        toSave.setParameters(parameters);
        sl.save(toSave);
    }

    /**
     *
     * @return
     */
    public HashMap<String, Integer> getInitialState() {
        HashMap<String, Integer> init = instances.getBook();
        for (Entity entity : entities) {
            if (init.containsKey(entity._etiquettes) == false) {
                init.put(entity._etiquettes, 0);
            }
        }

        return init;
    }
    
    public ArrayList<Entity> getListManipulesNoeuds() {
        return entities;
    }

    public ArrayList<Behavior> getListManipulesReactions() {
        return behaviors;
    }
    
    public ArrayList<Compartment> getListManipulesCompartment() {
        return compartment;
    }

    public ArrayList<Entity> getCopyListManipulesNoeuds() {
        ArrayList<Entity> proto = new ArrayList<>();
        for (Entity r : entities) {
            proto.add(r.clone());
        }
        return proto;
    }

    public ArrayList<Behavior> getCopyListManipulesReactions() {
        ArrayList<Behavior> moteurs = new ArrayList<>();
        for (Behavior r : behaviors) {
            moteurs.add(r.clone());
        }
        return moteurs;
    }
    
    public ArrayList<Compartment> getCopyListManipulesCompartment() {
        ArrayList<Compartment> comps = new ArrayList<>();
        for (Compartment r : compartment) {
            comps.add(r.clone());
        }
        return comps;
    }

    public Env_Parameters getParameters() {
        return parameters;
    }

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

    public AllInstances getInstances() {
        return instances;
    }

    @Override
    public Model clone() {
        return new Model(getParameters(), getInstances().clone(),getListManipulesNoeuds(), getListManipulesReactions(), getListManipulesCompartment());
    }

}

