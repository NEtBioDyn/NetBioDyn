/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetMDyn.ihm;

import java.util.ArrayList;

import NetMDyn.Model_NetMDyn;
import NetMDyn.Simulator_NetMDyn;
import netbiodyn.ihm.Command;
import netbiodyn.util.UtilPoint3D;

/**
 *
 * @author riviere
 */
public class AddCommand_NetMDyn implements Command {

    private final Model_NetMDyn model;
    private final Simulator_NetMDyn simulator;
    private final ArrayList<UtilPoint3D> points;
    private final String type;
    private Command opposite;

    public AddCommand_NetMDyn(Model_NetMDyn model, Simulator_NetMDyn simulator, ArrayList<UtilPoint3D> points, String type) {
        this.model = model;
        this.simulator = simulator;
        this.type = type;
        this.points = points;        
    }
    
     public void setOpposite(Command opposite){
        this.opposite=opposite;
    }

    @Override
    public void execute() {
        if (simulator.isStopped()) {
            model.addEntityInstances(points, type);
        } else {
            simulator.addEntityInstances(points, type);
        }
    }

    @Override
    public void undo() {
        opposite.execute();
    }

}
