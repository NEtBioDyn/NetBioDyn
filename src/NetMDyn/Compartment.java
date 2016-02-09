package NetMDyn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

import netbiodyn.ProtoBioDyn;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

public class Compartment extends ProtoBioDyn{
	private UtilPoint3D center = new UtilPoint3D();
	private int radius;
	//private Entity ent= new Entity();
	public Color Couleur = Color.RED;
	public boolean _visibleDansPanel = true;
	public boolean Vidable = true;
	protected JTextPane _description = new JTextPane();
	
	//Initialization of the Compartment object
	public Compartment(){
		initComponents();
	}
	
	//Clone of this object into a new one
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
	
  //Return the description of the Compartment
	public JTextPane getDescription() {
		return _description;
	}

	//Put a new value to the description of the Compartment
	public void setDescription( JTextPane _description) {
		this._description = _description;
	}

	//Return the center of the UtilPoint3D
	public UtilPoint3D getCenter() {
		return center;
	}
	
	//Put a new value to the center of the UtilPoint3D
	public void setCenter(UtilPoint3D center) {
		this.center.x = center.x;
		this.center.y = center.y;
		
	}
	
	//Return the radius of the Compartment
	public int getRadius() {
		return radius;
	}
	//Put a new value to the radius of the Compartment
	public void setRadius(int radius) {
		this.radius = radius;
	}
/*
	public Entity getEnt() {
		return ent;
	}

	public void setEnt(Entity ent) {
		this.ent = ent;
	}
*/	
	
	//Save the object with all its parameters
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
        //toSave.add(new String("\tmenbrane:").concat(getEnt().getEtiquettes()) + "\n");
        return toSave;
    }
    
    //Initialization of the components of the Compament
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
	
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