package NetMDyn;

import NetMDyn.ihm.Controller_NetMDyn;

public class Main_NetMDyn {
	//Default size of the software window
	static int FRAME_WIDTH = 900;
	static int FRAME_HEIGHT = 600;

	public static void main(String[] args){
	//The opening of the software causes the creation of the Controller
	new Controller_NetMDyn();
	}
}
