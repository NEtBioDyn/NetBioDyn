/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package netbiodyn; // Creation of a package, this line indicating that the actual file will be in this package

import netbiodyn.ihm.Controller;

/**
 *
 * @author pascalballet
 */
public class Main {

	//Default size of the software window
    static int FRAME_WIDTH = 900;
    static int FRAME_HEIGHT = 600;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
    	//The opening of the software causes the creation of the Controller
        new Controller();
    }

}

