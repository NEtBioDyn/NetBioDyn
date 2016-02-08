/* This file is part of NetBioDyn.
 *
 *   NetBioDyn is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   any later version.
 *
 *   NetBioDyn is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with NetBioDyn; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * Reaxel.java
 * 
 * Created on 23 oct. 2007, 10:22:08
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netbiodyn; // Creation of a package, this line indicating that the actual file will be in this package

import java.awt.Color; //Encapsulate colors in the default sRGB color space or colors in arbitrary color spaces
import java.awt.Image; //Provides classes for creating and modifying images.

/**
 *
 * @author ballet
 * Heritage of InstanceSimplexel
 * Creation of reaxels with coordinates, color and half-life
 */

public class InstanceReaxel extends InstanceSimplexel implements Cloneable{

    protected int _x, _y, _z; //Coordinates of the InstanceReaxel
    protected Color _couleur = Color.BLUE; //Default color of the InstanceReaxel
    protected boolean _vidable = true; //Possibility to empty the InstanceReaxel
    protected double _taille = 1; // Size of the InstanceReaxel
    protected int _forme = 0; //Shape of the InstanceReaxel, depending of the number (0 : circle, 1 : square, 2 : triangle, 3 : diamond, 4 : star, 5 : pea, 6 : noise)
    protected double _demie_vie = 0; //Time of half-time of the InstanceReaxel (0:infinite)
    protected boolean _selectionne = false; //Know if the InstanceReaxel is selected or not
    protected String _nom; //name of the InstanceReaxel
    protected Image _image = null; //Know if the InstanceReaxel is represented by an Image, and if yes, which one
    
    //Creation of an objecf of type InstanceReaxel, with no parameters
    public InstanceReaxel(){
    	//Creation of an object with only the coordinates
        _x=-1;
        _y=-1;
        _z=-1;
    }

    //Clone of the InstanceReaxel already existing (with "this" in parameter of this function)
    public InstanceReaxel clone() {
        InstanceReaxel clone = new InstanceReaxel(); //Creation of a new InstanceReaxel
        //Retrieval of object parameters
        clone._couleur = _couleur;
        clone._taille = _taille;
        clone._demie_vie = _demie_vie;
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
    public static InstanceReaxel CreerReaxel(Entity cli) {
        InstanceReaxel r = new InstanceReaxel(); //Creation of a new InstanceReaxel
        // Retrieval of all object parameters from those of the Entity one
        r._couleur = cli.Couleur;
        r._taille = cli._taille;
        r._forme = cli._forme;
        r._demie_vie = cli.DemieVie;
        r._nom = cli.getEtiquettes();
        r._image = cli.BackgroundImage;
        r._vidable = cli.Vidable;
        return r; //Return the new InstanceReaxel
    }
    
	//Verify if an Object put in parameter is equal to one InstanceReaxel already created
    public boolean equals(Object o){
    	//The function will have an effect only if there is an equality
        if(o instanceof InstanceReaxel){ 
            InstanceReaxel r=(InstanceReaxel)o; //Creation of a new Instance Reaxel from the parameter Object
            return ((r.getX() == getX())&&(r.getY()==getY())&&(r.getZ()==getZ())&&(r.getNom().equals(getNom()))); //Return of the coordinates and the name of the new InstanceReaxel (equal to the parameter Object)
        }
        return false; //If there is no equality, return of the boolean False
    }

    //Return the coordinate X of the InstanceReaxel
    public int getX() {
        return _x;
    }

    //Put a new value to the coordinate X of the InstanceReaxel
    public void setX(int _x) {
        this._x = _x;
    }

    //Return the coordinate Y of the InstanceReaxel
    public int getY() {
        return _y;
    }

    //Put a new value to the coordinate Y of the InstanceReaxel
    public void setY(int _y) {
        this._y = _y;
    }
    
    //Return the coordinate Z of the InstanceReaxel
    public int getZ() {
        return _z;
    }

    //Put a new value to the coordinate Z of the InstanceReaxel
    public void setZ(int _z) {
        this._z = _z;
    }

    //Return the color of the InstanceReaxel
    public Color getCouleur() {
        return _couleur;
    }

    //Put a new value to the color of the InstanceReaxel
    public void setCouleur(Color _couleur) {
        this._couleur = _couleur;
    }
    
    //Return if the InstanceReaxel can be emptied or not
    public boolean isVidable() {
        return _vidable;
    }

    //Put a new value to the fact that InstanceReaxel can be emptied or not
    public void setVidable(boolean _vidable) {
        this._vidable = _vidable;
    }

    //Return the size of the InstanceReaxel
    public double getTaille() {
        return _taille;
    }

    //Put a new value to the size of the InstanceReaxel
    public void setTaille(double _taille) {
        this._taille = _taille;
    }

    //Return the shape of the InstanceReaxel
    public int getForme() {
        return _forme;
    }

    //Put a new value to the shape of the InstanceReaxel
    public void setForme(int _forme) {
        this._forme = _forme;
    }

    //Return the half-time of the InstanceReaxel
    public double getDemie_vie() {
        return _demie_vie;
    }

    //Put a new value to the half-time of the InstanceReaxel
    public void setDemie_vie(double _demie_vie) {
        this._demie_vie = _demie_vie;
    }

    //Return if the InstanceReaxel is selected or not
    public boolean isSelectionne() {
        return _selectionne;
    }

    //Put a new value to fact that the InstanceReaxel is selected or not
    public void setSelectionne(boolean _selectionne) {
        this._selectionne = _selectionne;
    }

    //Return the name of the InstanceReaxel
    public String getNom() {
        return _nom;
    }

    //Put a new value to the name of the InstanceReaxel
    public void setNom(String _nom) {
        this._nom = _nom;
    }

    //Return the image representing the InstanceReaxel (if it has one : if not, return null)
    public Image getImage() {
        return _image;
    }

    //Put a new value to the image of the InstanceReaxel
    public void setImage(Image _image) {
        this._image = _image;
    }
    
    
    
}
