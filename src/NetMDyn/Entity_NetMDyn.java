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
 * Entity_NetMDyn.java
 *
 * Created on February 12 2016, 09:07
 */

package NetMDyn;

import java.awt.image.BufferedImage; // Possibility to create an image with an accessible buffer of image data (a buffered image)
import java.util.ArrayList; //Possible creation of tables
import java.util.HashMap; //Possible creation of hashmaps
import netbiodyn.Entity;


/**
 * Class of Entity management
 * 
 * @author Master 2 Bioinformatique
 */

public class Entity_NetMDyn extends Entity{
	
	public String _compartment = "Cytosol";
	
	/**
	 * Initialization of an Entity object
	 */
    public Entity_NetMDyn() {
        super();
    }

	/**
	 * Clone this object
	 * @return the new Entity
	 */
	public Entity_NetMDyn clone() {
        Entity_NetMDyn  reaxel = new Entity_NetMDyn (); // Creation of a new Entity
        // Retrieval of object parameters
        reaxel.setEtiquettes(getEtiquettes());
        reaxel.Couleur=Couleur;
        reaxel._visibleDansPanel=_visibleDansPanel;
        reaxel._compartment = _compartment;
        reaxel.Vidable=Vidable;
        reaxel.DemieVie=DemieVie;
        reaxel._forme = _forme;
        reaxel._taille = _taille;
        if (BackgroundImage != null) {
            reaxel._str_image_deco=_str_image_deco;
        }
        return reaxel; // Return the new Entity
    }
	
	/**
	 * Save the Entity with all its parameters
	 * @return all saved parameters
	 */
    public ArrayList<String> toSave() {
        ArrayList<String> toSave = super.toSave(); // Creation of the variable that will contain all informations
        toSave.add(new String("\tcompartment:").concat(((String) this._compartment).toString()) + "\n");
        return toSave; // Return the table containing all the informations
    }
    
    /**
     * Return the compartment of the Entity
     * @return the name of the compartment
     */
	public String getCompartment() {
		return _compartment;
	}

	/**
	 * Put the Entity into a new compartment
	 * @param compartment : the new compartment of the Entity
	 */
	public void setCompartment(String compartment) {
		_compartment = compartment;
	}

}
