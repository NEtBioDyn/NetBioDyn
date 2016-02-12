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
 * AddCommand_NetMDyn.java
 *
 * Created on February 12 2016, 13:38
 */
package NetMDyn.ihm;

import java.util.ArrayList; //Possible creation of tables

import NetMDyn.Model_NetMDyn;
import NetMDyn.Simulator_NetMDyn;
import netbiodyn.ihm.Command;
import netbiodyn.util.UtilPoint3D;

/**
 * Class of Command visualization
 * 
 * @author Master 2 Bioinformatique
 */

public class AddCommand_NetMDyn implements Command {

    private final Model_NetMDyn model;
    private final Simulator_NetMDyn simulator;
    private final ArrayList<UtilPoint3D> points;
    private final String type;
    private Command opposite;

    /**
     * Initialization of an AddCommand object
     * @param model : the model
     * @param simulator : the simlator
     * @param points : the Reaxels
     * @param type : the type
     */
    public AddCommand_NetMDyn(Model_NetMDyn model, Simulator_NetMDyn simulator, ArrayList<UtilPoint3D> points, String type) {
        this.model = model;
        this.simulator = simulator;
        this.type = type;
        this.points = points;        
    }
    
    /**
     * 
     * @param opposite
     */
     public void setOpposite(Command opposite){
        this.opposite=opposite;
    }

    /**
     * Execute an addition of Entities 
     */
    public void execute() {
        if (simulator.isStopped()) {
            model.addEntityInstances(points, type);
        } else {
            simulator.addEntityInstances(points, type);
        }
    }

    /**
     * Undo the Command
     */
    public void undo() {
        opposite.execute();
    }

}
