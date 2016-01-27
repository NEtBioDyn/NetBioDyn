package netbiodyn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilPoint3D;

public class Compartment{
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
}