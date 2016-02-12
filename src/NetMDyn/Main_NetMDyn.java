/* This file is part of NetMDyn.
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
 * Main_NetMDyn.java
 *
 * Created on February 12 2016, 09:28
 */

package NetMDyn;

import NetMDyn.ihm.Controller_NetMDyn;

/**
 * Main Class of NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class Main_NetMDyn {
	//Default size of the software window
	static int FRAME_WIDTH = 900;
	static int FRAME_HEIGHT = 600;
	
	/**
	 * Opening of NetMDyn
	 * @param args
	 */
	public static void main(String[] args){
	//The opening of the software causes the creation of the Controller
	new Controller_NetMDyn();
	}
}
