/* This file is part of NetMDyn.ihm
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
 * IhmListener_NetMDyn.java
 *
 * Created on February 12 2016, 15:19
 */
package NetMDyn.ihm;

import java.util.ArrayList; //Possible creation of tables
import java.util.EventListener; // A tagging interface that all event listener interfaces must extend
import java.util.HashMap; // Possible creation of hashmaps

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
 * Listener of the IHM in NetMDyn (interface)
 * 
 * @author Master 2 Bioinformatique
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
