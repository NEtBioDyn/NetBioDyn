/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetMDyn.util;

import netbiodyn.ihm.Env_Parameters;
import java.util.ArrayList;
import java.util.HashMap;

import NetMDyn.AllInstances_NetMDyn;
import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Compartment;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.InstanceReaxel_NetMDyn;
import netbiodyn.AllInstances;
import netbiodyn.InstanceReaxel;
import netbiodyn.Behavior;
import netbiodyn.Entity;

/**
 *
 * @author riviere
 */
public class Serialized_NetMDyn {

    private ArrayList<Entity_NetMDyn> _ListManipulesNoeuds; // Entity types
    private ArrayList<Behavior_NetMDyn> _ListManipulesReactions; // Behaviour
    private ArrayList<Compartment> _ListManipulesCompartments; //Compartments
    private AllInstances_NetMDyn instances;

    private HashMap<String, Integer> entitesBook;

    //Environnement
    private Env_Parameters parameters;

    public Serialized_NetMDyn() {
        _ListManipulesNoeuds = new ArrayList<>();
        _ListManipulesReactions = new ArrayList<>();
        _ListManipulesCompartments = new ArrayList<>();
        parameters = new Env_Parameters("FR", 0, 0, 0, "", null, "");
        instances = new AllInstances_NetMDyn(parameters.getX(), parameters.getY(), parameters.getZ());
        entitesBook = new HashMap<>();
    }

    public Serialized_NetMDyn(Env_Parameters parameters) {
        _ListManipulesNoeuds = new ArrayList<>();
        _ListManipulesReactions = new ArrayList<>();
        _ListManipulesCompartments = new ArrayList<>();
    }

    public ArrayList<Entity_NetMDyn> getListManipulesNoeuds() {
        ArrayList<Entity_NetMDyn> proto = new ArrayList<>();
        for (Entity_NetMDyn r : _ListManipulesNoeuds) {
            proto.add(r.clone());
        }
        return proto;
    }

    public void setListManipulesNoeuds(ArrayList<Entity_NetMDyn> _ListManipulesNoeuds) {
        this._ListManipulesNoeuds = _ListManipulesNoeuds;
    }

    public void addProtoReaxel(Entity_NetMDyn r) {
        _ListManipulesNoeuds.add(r);
    }

    public void addMoteurReaction(Behavior_NetMDyn m) {
        _ListManipulesReactions.add(m);
    }
    
    public void addProtoCompartment(Compartment c) {
        _ListManipulesCompartments.add(c);
    }

    public ArrayList<Behavior_NetMDyn> getListManipulesReactions() {
        ArrayList<Behavior_NetMDyn> moteurs = new ArrayList<>();
        for (Behavior_NetMDyn r : _ListManipulesReactions) {
            moteurs.add(r.clone());
        }
        return moteurs;
    }

    public void setListManipulesReactions(ArrayList<Behavior_NetMDyn> _ListManipulesReactions) {
        this._ListManipulesReactions = _ListManipulesReactions;
    }
    
    
    public ArrayList<Compartment> getListManipulesCompartment() {
        ArrayList<Compartment> comp = new ArrayList<>();
        for (Compartment r : _ListManipulesCompartments) {
            comp.add(r.clone());
        }
        return comp;
    }

    public void setListManipulesCompartment(ArrayList<Compartment> _ListManipulesCompartments) {
        this._ListManipulesCompartments = _ListManipulesCompartments;
    }
    
    
    
    

    public AllInstances_NetMDyn getInstances() {
        return instances;
    }

    public void setInstances(AllInstances_NetMDyn instances) {
        this.instances = instances;
    }

    public Env_Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Env_Parameters parameters) {
        this.parameters = parameters;
    }

    public void initMatriceAndList() {
        instances = new AllInstances_NetMDyn(parameters.getX(), parameters.getY(), parameters.getZ());
    }

    public HashMap<String, Integer> getEntitesBook() {
        return entitesBook;
    }

    public void setEntitesBook(HashMap<String, Integer> entitesBook) {
        this.entitesBook = entitesBook;
    }

    public InstanceReaxel_NetMDyn CreerReaxel(String etiquette) {
        for (Entity_NetMDyn reaxel : _ListManipulesNoeuds) {
            if (reaxel.TrouveEtiquette(etiquette) >= 0) {
                return InstanceReaxel_NetMDyn.CreerReaxel(reaxel);
            }
        }
        return null;
    }

    public void AjouterReaxel(int i, int j, int k, String etiquette) {
        for (Entity_NetMDyn _ListManipulesNoeud : _ListManipulesNoeuds) {
            if (_ListManipulesNoeud.TrouveEtiquette(etiquette) >= 0) {
                AjouterReaxel(i, j, k, _ListManipulesNoeud);
            }
        }
    }

    public void AjouterReaxel(int i, int j, int k, Entity_NetMDyn cli) {
        InstanceReaxel_NetMDyn r = InstanceReaxel_NetMDyn.CreerReaxel(cli);
        while (i < 0) {
            i += parameters.getX();
        }
        while (i >= parameters.getX()) {
            i -= parameters.getX();
        }
        while (j < 0) {
            j += parameters.getY();
        }
        while (j >= parameters.getY()) {
            j -= parameters.getY();
        }
        while (k < 0) {
            k += parameters.getZ();
        }
        while (k >= parameters.getZ()) {
            k -= parameters.getZ();
        }
        r.setX(i);
        r.setY(j);
        r.setZ(k);
        instances.add(r);
    }

    public void setTaille(String dimension, String value) {
        switch (dimension) {
            case "tailleX": {
                int v;
                try {
                    v = Integer.parseInt(value);
                    parameters.setX(v);
                } catch (Exception e) {
                }
                break;
            }
            case "tailleY": {
                int v;
                try {
                    v = Integer.parseInt(value);
                    parameters.setY(v);
                } catch (Exception e) {
                }
                break;
            }
            case "tailleZ": {
                int v;
                try {
                    v = Integer.parseInt(value);
                    parameters.setZ(v);
                } catch (Exception e) {
                }
                break;
            }
        }
    }

}
