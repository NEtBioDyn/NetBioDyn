package NetMDyn;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

import NetMDyn.Compartment;
import netbiodyn.AllInstances;
import netbiodyn.util.Serialized;
import netbiodyn.Behavior;
import netbiodyn.Entity;
import netbiodyn.ihm.Env_Parameters;

/**
 *
 * @author riviere
 */
public interface IhmListener_NetMDyn extends EventListener{
    
    public void newEnvLoaded(Serialized_NetMDyn saved,HashMap<String, Integer> entitesBook);
    public void newEnvParameters(Env_Parameters parameters);
    public void protoEntityUpdate(ArrayList<Entity_NetMDyn> entities, HashMap<String, Integer> entitesBook);
    public void moteurReactionUpdate(ArrayList<Behavior_NetMDyn> behaviours);
    public void CompartmentUpdate(ArrayList<Compartment> compartment);
    public void matrixUpdate(AllInstances_NetMDyn instances, HashMap<String, Integer> initialState, int i);
    public void ready();
    
}
