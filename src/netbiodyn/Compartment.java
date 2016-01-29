package netbiodyn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

public class Compartment extends ProtoBioDyn{
	private String name;
	private UtilPoint3D center = new UtilPoint3D();
	private int radius;
	private Entity ent= new Entity();
	public Color Couleur = Color.RED;
	public boolean _visibleDansPanel = true;
	public boolean Vidable = true;
	protected JTextPane _description = new JTextPane();
	
	public Compartment(){}
	
    public Compartment clone() {
        Compartment comp = new Compartment();
        comp.center = center;
        comp.radius = radius;
        comp.ent = ent;
        comp.setEtiquette(getEtiquette());
        comp.Couleur = Couleur;
        comp._visibleDansPanel = _visibleDansPanel;
        comp.Vidable = Vidable;
        return comp;
    }	
	
	public JTextPane getDescription() {
		return _description;
	}

	public void setDescription( JTextPane _description) {
		this._description = _description;
	}


	public Entity entity_property(){
		ent.setEtiquettes('m'+name);
		ent.Couleur = this.Couleur;
		ent._compartment = getEtiquette();
		ent._visibleDansPanel=true;
		ent.Vidable = this.Vidable;
		ent.DemieVie = 0;
		ent._forme = 1;
		ent._taille = 0;
		
		return ent;
	}

	public String getEtiquette() {
		return name;
	}

	public void setEtiquette(String name) {
		this.name = name;
	}

	public UtilPoint3D getCenter() {
		return center;
	}

	public void setCenter(UtilPoint3D center) {
		this.center.x = center.x;
		this.center.y = center.y;
		
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Entity getEnt() {
		return ent;
	}

	public void setEnt(Entity ent) {
		this.ent = ent;
	}
	
    public ArrayList<String> toSave() {
        ArrayList<String> toSave = new ArrayList<String>();
        String classe = this.getClass().toString();
        classe = classe.replaceFirst("class BioDynPackage", "biodyn_net");
        toSave.add(classe + "\n");
        toSave.add("\tEtiquettes:" + getEtiquette() + "\n");
        toSave.add(new String("\tcenterX:").concat(Integer.toString(this.getCenter().x)) + "\n");
        toSave.add(new String("\tcenterY:").concat(Integer.toString(this.getCenter().y)) + "\n");
        toSave.add(new String("\tcenterZ:").concat(Integer.toString(this.getCenter().z)) + "\n");
        toSave.add(new String("\tradius:").concat(Integer.toString(this.getRadius())) + "\n");
        toSave.add(new String("\tmenbrane:").concat(getEnt().getEtiquettes()) + "\n");
        return toSave;
    }
	
//    public ArrayList<String> toSave() {
//        ArrayList<String> toSave = super.toSave();
//        toSave.add(new String("\tcouleur:").concat(((Integer) this.Couleur.getRGB()).toString()) + "\n");
//        toSave.add(new String("\tdemie_vie:").concat(((Double) this.DemieVie).toString()) + "\n");
//        toSave.add(new String("\tvidable:").concat(((Boolean) this.Vidable).toString()) + "\n");
//        toSave.add(new String("\tcompartment:").concat(((String) this.Compartment).toString()) + "\n");
//        return toSave;
//    }
	
//	public ArrayList<String> toSave() {
//  ArrayList<String> toSave = super.toSave();
//  toSave.add(new String("\tforme:").concat(((Integer) this._forme).toString()) + "\n");
//  toSave.add(new String("\ttaille:").concat(((Integer) this._taille).toString()) + "\n");
//
//  if (BackgroundImage != null) {
//      toSave.add("\tImage:" + _str_image_deco + "\n");
//  }
//  return toSave;
//}
}