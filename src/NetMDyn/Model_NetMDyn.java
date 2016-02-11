package NetMDyn;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

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
import java.util.ArrayList; //Création de listes
import java.util.HashMap; //Création de hashmap
import javax.swing.event.EventListenerList; //Création possible de listeners d'événements

import NetMDyn.Compartment;
import NetMDyn.ihm.Environment_NetMDyn;
import NetMDyn.ihm.IhmListener_NetMDyn;
import NetMDyn.util.FileSaverLoader_NetMDyn;
import NetMDyn.util.SaverLoader_NetMDyn;
import NetMDyn.util.Serialized_NetMDyn;
import NetMDyn.util.UtilPoint3D_NetMDyn;
import netbiodyn.ihm.IhmListener;
import netbiodyn.Model;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;
import jadeAgentServer.util.Parameter; //Mise en place de serveur multi-agents ?

public class Model_NetMDyn {
	
    protected final EventListenerList listeners;
    protected Env_Parameters parameters;
    protected AllInstances_NetMDyn instances;
	
	private ArrayList<Compartment> compartments; 
	protected ArrayList<Entity_NetMDyn> entities;
	protected ArrayList<Behavior_NetMDyn> behaviors;
	
	protected SaverLoader_NetMDyn sl;
	
    public Model_NetMDyn(Env_Parameters parameters) {
    	listeners = new EventListenerList();
        this.parameters = parameters;

        entities = new ArrayList<>();
        behaviors = new ArrayList<>();
        compartments = new ArrayList<>();
        instances = new AllInstances_NetMDyn(parameters.getX(), parameters.getY(), parameters.getZ());
    }

    public Model_NetMDyn(Env_Parameters parameters, AllInstances_NetMDyn instances, ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviors, ArrayList<Compartment> compartments) {
    	this.parameters = parameters;
        this.instances = instances;
        this.entities = entities;
        this.behaviors = behaviors;
        this.compartments = compartments;
        listeners = new EventListenerList();
    }
    
    public void addListener(IhmListener_NetMDyn listen) {
        listeners.add(IhmListener_NetMDyn.class, listen);
    }
    
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

    public String getType(int x, int y, int z) {
        InstanceReaxel r = instances.getFast(x, y, z);
        if (r != null) {
            return r.getNom();
        } else {
            return "";
        }
    }
    
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
    
    public void select(int x, int y, int z) {
        instances.select(x, y, z);
    }

    public void unselect(int x, int y, int z) {
        instances.unselect(x, y, z);
    }

    public void unselect(ArrayList<InstanceReaxel_NetMDyn> cubes_selectionnes) {
        if (cubes_selectionnes != null) {
            for (InstanceReaxel_NetMDyn r : cubes_selectionnes) {
                unselect(r.getX(), r.getY(), r.getZ());
            }
        }
    }

    public void reboot() {
        // TODO charger une ancienne version du modèle

    }

    public void deplacer(ArrayList<InstanceReaxel_NetMDyn> _cubes_selectionnes, int new_x, int new_y, int new_z) {
        System.out.println(_cubes_selectionnes.toString());
        this.unselect(_cubes_selectionnes);
        // Calcul du cdg de la liste de reaxels, si toutes les places sont libres
        UtilPoint3D cdg = placeLibre(_cubes_selectionnes, new_x, new_y, new_z);
        if (cdg != null) {
            int dx = cdg.x;
            int dy = cdg.y;
            int dz = cdg.z;
            // Vidage des emplacements initiaux
            for (InstanceReaxel_NetMDyn r : _cubes_selectionnes) {
                instances.removeReaxel(r.getX(), r.getY(), r.getZ());
            }
            // Deplacement de tous les reaxels de l'ensemble
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

    protected UtilPoint3D_NetMDyn placeLibre(ArrayList<InstanceReaxel_NetMDyn> lst, int xg1, int yg1, int zg1) {
        if (lst == null) {
            return null;
        }

        // Calcul du cdg de la liste de reaxels
        UtilPoint3D_NetMDyn pt_g0 = UtilPoint3D_NetMDyn.centreDeGravite(lst, true);
        int dx = xg1 - pt_g0.x;
        int dy = yg1 - pt_g0.y;
        int dz = zg1 - pt_g0.z;
        UtilPoint3D_NetMDyn new_point = new UtilPoint3D_NetMDyn(dx, dy, dz);
        // Verif que toutes les places sont libres
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

    public void setParameters(Env_Parameters parameters) {
        this.parameters = parameters;
        for (final IhmListener listen : listeners.getListeners(IhmListener.class)) {
            listen.newEnvParameters(parameters);
        }

    }
    
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

    public void removeEntityInstance(int x, int y, int z) {
        instances.removeReaxel(x, y, z);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    public HashMap<String, Integer> getInitialState() {
        HashMap<String, Integer> init = instances.getBook();
        for (Entity_NetMDyn entity : entities) {
            if (init.containsKey(entity._etiquettes) == false) {
                init.put(entity._etiquettes, 0);
            }
        }

        return init;
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


    public AllInstances_NetMDyn getInstances() {
        return instances;
    }

    
    public Compartment getCompartment(String name) {
        for (int i = compartments.size() - 1; i >= 0; i--) {
            Compartment comp = compartments.get(i);
            if (comp.getEtiquettes().equals(name)) {
                return comp.clone();
            }
        }
        return null;
    }
    
    public void addCompartment(Compartment comp) {
        compartments.add(comp);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    public void delCompartment(ArrayList<String> compartments) {
        for (String name : compartments) {
            Compartment comp = this.getCompartment(name);
            this.removeCompart(comp);
        }
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.CompartmentUpdate(getCopyListManipulesCompartment());
        }
    }
    
    public void removeCompart(Compartment comp){
         for (int i = 0; i < compartments.size(); i++) {
             if (compartments.get(i).getEtiquettes().equals(comp.getEtiquettes())) { 
             		compartments.remove(i);
             }
    	}
    }
    
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
    
    public Entity_NetMDyn getProtoReaxel(String name) {
        for (Entity_NetMDyn entity : entities) {
            if (entity._etiquettes.equals(name)) {
                return entity.clone();
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
    
    public void addProtoReaxel(Entity_NetMDyn entity) {
        entities.add(entity);
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
        }
    }

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
    
    public void delProtoReaxel(ArrayList<String> entities) {
        for (String r : entities) {
            Entity_NetMDyn rea = getProtoReaxel(r);
            this.entities.remove(rea);
            instances.removeEntityType(r);
//            this.removeInBehaviours(r.getEtiquettes());
        }

        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.protoEntityUpdate(getCopyListManipulesNoeuds(), getInitialState());
            listen.matrixUpdate(getInstances(), getInitialState(), 0);
        }
    }
    
    public void editEntitiesHalfLife(String nom, double value) {
        Entity_NetMDyn entity = getProtoReaxel(nom);
        if (entity != null) {
            entity.DemieVie = value;
            this.editProtoReaxel(entity, nom, 0);
        }
    }
    

    public ArrayList<Entity_NetMDyn> getListManipulesNoeuds() {
        return entities;
    }
    
    
    public Behavior_NetMDyn getBehaviour(String name) {
        for (int i = behaviors.size() - 1; i >= 0; i--) {
            Behavior_NetMDyn moteur = behaviors.get(i);
            if (moteur.getEtiquettes().equals(name)) {
                return moteur.clone();
            }
        }
        return null;
    }
    
    protected void editInBehaviors(String entity, String old_name) {
        for (Behavior_NetMDyn moteur : behaviors) {
            moteur.protoReaxelNameChanged(old_name, entity);
        }
    }
    
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

    public void addMoteurReaction(Behavior_NetMDyn behaviour) {
        behaviors.add(behaviour);

        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }

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

    public void delMoteurReaction(ArrayList<String> reactions) {
        for (String name : reactions) {
            Behavior r = this.getBehaviour(name);
            behaviors.remove(r);
        }
        for (final IhmListener_NetMDyn listen : listeners.getListeners(IhmListener_NetMDyn.class)) {
            listen.moteurReactionUpdate(getCopyListManipulesReactions());
        }
    }
    
    public void load(Environment_NetMDyn env, String nomFichier) {
    	System.out.println("blip");
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
    
    public ArrayList<Compartment> getListManipulesCompartment() {
        return compartments;
    }
    
    public ArrayList<Compartment> getCopyListManipulesCompartment() {
        ArrayList<Compartment> comps = new ArrayList<>();
        for (Compartment r : compartments) {
            comps.add(r.clone());
        }
        return comps;
    }
    


    public ArrayList<Behavior_NetMDyn> getListManipulesReactions() {
        return behaviors;
    }

    public ArrayList<Entity_NetMDyn> getCopyListManipulesNoeuds() {
        ArrayList<Entity_NetMDyn> proto = new ArrayList<>();
        for (Entity_NetMDyn r : entities) {
            proto.add(r.clone());
        }
        return proto;
    }

    public ArrayList<Behavior_NetMDyn> getCopyListManipulesReactions() {
        ArrayList<Behavior_NetMDyn> moteurs = new ArrayList<>();
        for (Behavior_NetMDyn r : behaviors) {
            moteurs.add(r.clone());
        }
        return moteurs;
    }
    
    public Model_NetMDyn clone() {
        return new Model_NetMDyn(getParameters(), getInstances().clone(), getListManipulesNoeuds(), getListManipulesReactions(), getListManipulesCompartment());
    }
}
