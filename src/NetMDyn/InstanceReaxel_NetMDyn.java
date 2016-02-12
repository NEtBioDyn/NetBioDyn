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
 * InstanceReaxel_NetMDyn.java
 *
 * Created on February 12 2016, 09:17
 */

package NetMDyn;

import netbiodyn.Entity;
import netbiodyn.InstanceReaxel;

public class InstanceReaxel_NetMDyn extends InstanceReaxel{
	public String _compartment = "Cytosol";

	/**
	 * Clone this InstanceReaxel object
	 * @return the new Reaxel
	 */
    public InstanceReaxel_NetMDyn clone() {
        InstanceReaxel_NetMDyn clone = new InstanceReaxel_NetMDyn(); // Creation of a new InstanceReaxel
        // Retrieval of object parameters
        clone._couleur = _couleur;
        clone._taille = _taille;
        clone._demie_vie = _demie_vie;
        clone._compartment = _compartment;
        clone._forme = _forme;
        clone._nom = _nom;
        clone._image = _image;
        clone._vidable = _vidable;
        clone._x=_x;
        clone._y=_y;
        clone._z=_z;
        return clone; // Return the new InstanceReaxel
    }
    
    /**
     * Creation of an InstanceReaxel from an Entity
     * @param cli : the Entity used to create the Reaxel
     * @return the new Reaxel
     */
    public static InstanceReaxel_NetMDyn CreerReaxel(Entity_NetMDyn cli) {
        InstanceReaxel_NetMDyn r = new InstanceReaxel_NetMDyn(); // Creation of a new InstanceReaxel
        // Retrieval of all object parameters from those of the Entity one
        r._couleur = cli.Couleur;
        r._taille = cli._taille;
        r._forme = cli._forme;
        r._demie_vie = cli.DemieVie;
        r._compartment = cli._compartment;
        r._nom = cli.getEtiquettes();
        r._image = cli.BackgroundImage;
        r._vidable = cli.Vidable;
        return r; // Return the new InstanceReaxel
    }
	
    /**
     * Return the location (compartment) of the Reaxel
     * @return the compartment of the Reaxel
     */
    public String get_compartment() {
		return _compartment;
	}

    /**
     * Put a new value to the location (compartment) of the Reaxel
     * @param _compartment : the new compartment of the Reaxel
     */
	public void set_compartment(String _compartment) {
		this._compartment = _compartment;
	}

}
