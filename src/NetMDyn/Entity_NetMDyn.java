package NetMDyn;

import java.awt.image.BufferedImage; // Possibility to create an image with an accessible buffer of image data (a buffered image)
import java.util.ArrayList; //Possible creation of tables
import java.util.HashMap; //Possible creation of hashmaps
import netbiodyn.Entity;

public class Entity_NetMDyn extends Entity{
	
	public String _compartment = "Cytosol";
	
	//Initialization of the Entity object
    public Entity_NetMDyn() {
        super();
    }

  //Clone of this object into a new one
	public Entity_NetMDyn clone() {
        Entity_NetMDyn  reaxel = new Entity_NetMDyn (); // Creation of a new Entity
        //Retrieval of object parameters
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
        return reaxel; //Return the new Entity
    }
	
    public ArrayList<String> toSave() {
        ArrayList<String> toSave = super.toSave(); //creation of the variable that will contain all informations
        toSave.add(new String("\tcompartment:").concat(((String) this._compartment).toString()) + "\n");
        return toSave; //return the table containing all the informations
    }
    
    //Return the Compartment
	public String getCompartment() {
		return _compartment;
	}

	//Put a new value to the Compartment
	public void setCompartment(String compartment) {
		_compartment = compartment;
	}

}
