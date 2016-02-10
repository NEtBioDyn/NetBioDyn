package NetMDyn.ihm;

import java.util.ArrayList; //Possible creation of tables
import java.util.EventListener; // All event listener interfaces must extend this interface
import java.util.HashMap; //Possible creation of hashmaps

import NetMDyn.AllInstances_NetMDyn;
import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Compartment;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.util.Serialized_NetMDyn;
import netbiodyn.AllInstances;
import netbiodyn.util.Serialized;
import netbiodyn.Behavior;
import netbiodyn.Entity;
import netbiodyn.ihm.Env_Parameters;

/**
 *
 * @author riviere
 */


//Interface of a particular type of Listener
public interface IhmListener_NetMDyn extends EventListener{
    
    public void newEnvLoaded(Serialized_NetMDyn saved,HashMap<String, Integer> entitesBook);
    public void newEnvParameters(Env_Parameters parameters);
    public void protoEntityUpdate(ArrayList<Entity_NetMDyn> entities, HashMap<String, Integer> entitesBook);
    public void moteurReactionUpdate(ArrayList<Behavior_NetMDyn> behaviours);
    public void CompartmentUpdate(ArrayList<Compartment> compartment);
    public void matrixUpdate(AllInstances_NetMDyn instances, HashMap<String, Integer> initialState, int i);
    public void ready();
    
}
