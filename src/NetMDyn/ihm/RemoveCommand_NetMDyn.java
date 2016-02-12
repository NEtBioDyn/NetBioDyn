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
 * RemoveCommand_NetMDyn.java
 *
 * Created on February 12 2016, 15:42
 */

package NetMDyn.ihm;

import java.util.ArrayList; // Possible creation of tables

import NetMDyn.Model_NetMDyn;
import NetMDyn.Simulator_NetMDyn;
import netbiodyn.Model;
import netbiodyn.Simulator;
import netbiodyn.ihm.Command;
import netbiodyn.util.UtilPoint3D;

/**
 * Class of Command deletion
 * 
 * @author Master 2 Bioinformatique
 */

public class RemoveCommand_NetMDyn implements Command {

    private final Model_NetMDyn model;
    private final Simulator_NetMDyn simulator;
    private UtilPoint3D point;
    private ArrayList<UtilPoint3D> points;
    private Command opposite;

    /**
     * Initialization of a new RemoveCommand object with only one point
     * @param model
     * @param simulator
     * @param point
     */
    public RemoveCommand_NetMDyn(Model_NetMDyn model, Simulator_NetMDyn simulator, UtilPoint3D point) {
        this.points = new ArrayList<>();
        this.model = model;
        this.simulator = simulator;
        this.point = point;
    }
    
    /**
     * Initialization of a new RemoveCommand object with multiple points
     * @param model
     * @param simulator
     * @param points
     */
    public RemoveCommand_NetMDyn(Model_NetMDyn model, Simulator_NetMDyn simulator, ArrayList<UtilPoint3D> points) {
        this.model = model;
        this.simulator = simulator;
        this.points = points;
    }
    
    /**
     * Change this parameter
     * @param opposite
     */
    public void setOpposite(Command opposite){
        this.opposite=opposite;
    }

    /**
     * Execution
     */
    public void execute() {
        if (point != null) {
            int x = point.x;
            int y = point.y;
            int z = point.z;

            if (simulator.isStopped()) {
                model.removeEntityInstance(x, y, z);
            } else {
                simulator.removeEntityInstance(x, y, z);
            }
        } else {
            for (UtilPoint3D p : points) {
                int x = p.x;
                int y = p.y;
                int z = p.z;
                if (simulator.isStopped()) {
                    model.removeEntityInstance(x, y, z);
                } else {
                    simulator.removeEntityInstance(x, y, z);
                }
            }
        }
    }

    /**
     * Undo the Command
     */
    public void undo() {
        opposite.execute();
    }

}
