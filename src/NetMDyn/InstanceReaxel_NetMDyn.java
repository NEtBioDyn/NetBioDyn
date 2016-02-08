package NetMDyn;

import netbiodyn.Entity;
import netbiodyn.InstanceReaxel;

public class InstanceReaxel_NetMDyn extends InstanceReaxel{
	public String _compartment = "Cytosol";

    public InstanceReaxel_NetMDyn clone() {
        InstanceReaxel_NetMDyn clone = new InstanceReaxel_NetMDyn(); //Creation of a new InstanceReaxel
        //Retrieval of object parameters
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
        return clone; //Return the new InstanceReaxel
    }
    
    //Creation of an InstanceReaxel from an Entity
    public static InstanceReaxel_NetMDyn CreerReaxel(Entity_NetMDyn cli) {
        InstanceReaxel_NetMDyn r = new InstanceReaxel_NetMDyn(); //Creation of a new InstanceReaxel
        // Retrieval of all object parameters from those of the Entity one
        r._couleur = cli.Couleur;
        r._taille = cli._taille;
        r._forme = cli._forme;
        r._demie_vie = cli.DemieVie;
        r._compartment = cli._compartment;
        r._nom = cli.getEtiquettes();
        r._image = cli.BackgroundImage;
        r._vidable = cli.Vidable;
        return r; //Return the new InstanceReaxel
    }
	
    //Return the location (compartment) of the InstanceReaxel
    public String get_compartment() {
		return _compartment;
	}

    //Put a new value to the location (compartment) of the InstanceReaxel
	public void set_compartment(String _compartment) {
		this._compartment = _compartment;
	}

}
