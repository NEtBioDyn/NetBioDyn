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
 * Compartment.java
 *
 * Created on February 11 2016, 20:05
 */

package NetMDyn;

import java.awt.Color; // Encapsulate colors in the default sRGB color space or colors in arbitrary color spaces identified by a ColorSpace
import java.awt.image.BufferedImage; // An Image with an accessible buffer of image data
import java.util.ArrayList; // Possible creation of tables
import java.util.HashMap; // Possible creation of hashmaps

import javax.swing.JTextArea; // A multi-line area that displays plain text
import javax.swing.JTextPane; //A text component that can be marked up with attributes that are represented graphically

import netbiodyn.ProtoBioDyn;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

/**
 * Class of Compartment management
 * 
 * @author Master 2 Bioinformatique
 */

public class Compartment extends ProtoBioDyn{
	private UtilPoint3D center = new UtilPoint3D();
	private int radius;
	public Color Couleur = Color.RED;
	public boolean _visibleDansPanel = true;
	public boolean Vidable = true;
	protected JTextPane _description = new JTextPane();
	
	/**
	 * Initialization of the Compartment object
	 */
	public Compartment(){
		initComponents();
	}
	
	/**
	 * Clone of this object into a new one
	 */
    public Compartment clone() {
        Compartment comp = new Compartment();
        comp.center = center;
        comp.radius = radius;
        comp.setEtiquettes(getEtiquettes());
        comp.Couleur = Couleur;
        comp._visibleDansPanel = _visibleDansPanel;
        comp.Vidable = Vidable;
        return comp;
    }	

  /**
   * Return the description of the Compartment
   * @return this description
   */
	public JTextPane getDescription() {
		return _description;
	}

	/**
	 * Put a new value to the description of the Compartment
	 * @param _description : the description to replace
	 */
	public void setDescription( JTextPane _description) {
		this._description = _description;
	}

	/**
	 * Return the center of the Compartment
	 * @return the point representing the center
	 */
	public UtilPoint3D getCenter() {
		return center;
	}
	
	/**
	 * Put a new value to the center of the Compartment
	 * @param center : the point representing the center
	 */
	public void setCenter(UtilPoint3D center) {
		this.center.x = center.x;
		this.center.y = center.y;
	}
	
	/**
	 * Return the radius of the Compartment
	 * @return this radius
	 */
	public int getRadius() {
		return radius;
	}
	/**
	 * Put a new value to the radius of the Compartment
	 * @param radius : the radius to replace
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 *  Save the object with all its parameters
	 *  @return all saved parameters 
	 */
    public ArrayList<String> toSave() {
        ArrayList<String> toSave = new ArrayList<String>();
        String classe = this.getClass().toString();
        classe = classe.replaceFirst("class BioDynPackage", "biodyn_net");
        toSave.add(classe + "\n");
        toSave.add("\tEtiquettes:" + getEtiquettes() + "\n");
        toSave.add(new String("\tcenterX:").concat(Integer.toString(this.getCenter().x)) + "\n");
        toSave.add(new String("\tcenterY:").concat(Integer.toString(this.getCenter().y)) + "\n");
        toSave.add(new String("\tcenterZ:").concat(Integer.toString(this.getCenter().z)) + "\n");
        toSave.add(new String("\tradius:").concat(Integer.toString(this.getRadius())) + "\n");
        return toSave;
    }
    
    /**
     * Initialization of the components of the Compartment
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
    }
    
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    
}