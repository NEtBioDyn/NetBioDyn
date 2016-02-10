/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author riviere
 */
public class RemoveCommand_NetMDyn implements Command {

    private final Model_NetMDyn model;
    private final Simulator_NetMDyn simulator;
    private UtilPoint3D point;
    private ArrayList<UtilPoint3D> points;
    private Command opposite;

    //Initialization of a new RemoveCommand object with only one point
    public RemoveCommand_NetMDyn(Model_NetMDyn model, Simulator_NetMDyn simulator, UtilPoint3D point) {
        this.points = new ArrayList<>();
        this.model = model;
        this.simulator = simulator;
        this.point = point;
    }
    
    //Initialization of a new RemoveCommand object with multiple points
    public RemoveCommand_NetMDyn(Model_NetMDyn model, Simulator_NetMDyn simulator, ArrayList<UtilPoint3D> points) {
        this.model = model;
        this.simulator = simulator;
        this.points = points;
    }
    
    //Change this parameter
    public void setOpposite(Command opposite){
        this.opposite=opposite;
    }

    @Override
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

    @Override
    public void undo() {
        opposite.execute();
    }

}
