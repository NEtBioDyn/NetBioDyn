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
 * SbmlParser.java
 *
 * Created on February 12 2016, 11:08
 */

package NetMDyn;

import java.io.IOException;
import java.util.ArrayList; // Possible creation of tables
import java.util.HashSet; // The Set interface, backed by a hash table (actually a HashMap instance)
import java.util.List; // Possible creation of lists
import java.util.Set; // A collection that contains no duplicate elements

import javax.xml.stream.XMLStreamException; // The base exception for unexpected processing errors.

import org.sbml.jsbml.Compartment; // Represents the compartment in a model
import org.sbml.jsbml.KineticLaw; // Represents the kinetic law in a model
import org.sbml.jsbml.Model; // Represents the model
import org.sbml.jsbml.Reaction; // Represents the reaction in a model
import org.sbml.jsbml.SBMLDocument; // Represents the 'sbml' root node of a SBML file.
import org.sbml.jsbml.Species; // Represents the species in a model
import org.sbml.jsbml.SpeciesReference; // Represents the speciesreference in a model

/**
 * Parse a SBML document of NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class SbmlParser{
	private ArrayList<Reaction>reactions= new ArrayList<Reaction>();
	private ArrayList<Compartment>compartments=new ArrayList<Compartment>();
	private Set<Species>entities=new HashSet<Species>();
	private ArrayList<String> entities_name= new ArrayList<String>();
	
	
	/**
	 * Initialization of a SbmlParser object
	 */
	public SbmlParser() {
	}
	
	/**
	 * Species parser into the SBML document
	 * @param document : the SBML document
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public void parseSpecies(SBMLDocument document) throws XMLStreamException, IOException{
		Model model = document.getModel();
		for (int k = 0; k <model.getSpeciesCount() ; k++) {
			entities.add(model.getSpecies(k));
		}
	}
	
	/**
	 * Name of the Species parser
	 * @param document : the SBML document
	 */
	public void parseSpeciesName(SBMLDocument document){
		Model model = document.getModel();
		for (int k = 0; k <model.getSpeciesCount() ; k++) {
			entities_name.add(model.getSpecies(k).getName()+"_"+model.getSpecies(k).getCompartment());
		}
	}
	
	/**
	 * Compartment parser into the SBML document
	 * @param document : the SBML document
	 */
	public void parse_compartment(SBMLDocument document){
		Model model = document.getModel();
		for (int j = 0; j < model.getCompartmentCount(); j++) {
		compartments.add(model.getCompartment(j));
		}
	}
	
	/**
	 * Reaction parser into the SBML document
	 * @param document : the SBML document
	 */
	public void parse_reaction(SBMLDocument document){
		Model model = document.getModel();
		for (int i = 0; i <model.getReactionCount() ; i++) {
			reactions.add(model.getReaction(i));
			}
		}
	
	/**
	 * Return all reactants of a reaction
	 * @param reac : the wanted reaction
	 * @return the list of the reactants
	 */
	public List<SpeciesReference> get_reactant(Reaction reac){
		reac.getListOfReactants();
		return reac.getListOfReactants();
	}
	
	/**
	 * Return all products of a reaction
	 * @param reac : the wanted reaction
	 * @return the list of the products
	 */
	public List<SpeciesReference> get_product(Reaction reac){
		reac.getListOfProducts();
		return reac.getListOfProducts();
		}
	
	/**
	 * Return the Kinetic Law of a reaction	
	 * @param reac : the wanted reaction
	 * @return the Kinetic Law
	 */
	public KineticLaw get_kinetic_law(Reaction reac){
		return reac.getKineticLaw();
	}
	
	/**
	 * Return all Reactions
	 * @return the list of the Reactions
	 */
	public ArrayList<Reaction> getReactions() {
		return reactions;
	}
	
	/**
	 * Put new values to Reactions 
	 * @param reactions : the new Reactions
	 */
	public void setReactions(ArrayList<Reaction> reactions) {
		this.reactions = reactions;
	}
	
	/**
	 * Return all compartments
	 * @return the list of the compartments
	 */
	public ArrayList<Compartment> getCompartments() {
		return compartments;
	}
	
	/**
	 * Put new values to Compartments
	 * @param compartments : the new Compartments
	 */
	public void setCompartments(ArrayList<Compartment> compartments) {
		this.compartments = compartments;	
	}
	
	/**
	 * Return all Entities
	 * @return the list of Entities
	 */
	public Set<Species> getEntities() {
		return entities;
	}
	
	/**
	 * Return all Entity names
	 * @return the list of names of the Entities
	 */
	public ArrayList<String> getEntitiesName(){
		return entities_name;
	}
	
	/**
	 * Put new values to Entities
	 * @param entities
	 */
	public void setEntities(Set<Species> entities) {
		this.entities = entities;
	}
	
	/**
	 * Get the name of the Model
	 * @param document : the SBML document
	 * @return the name of the Model
	 */
	public String getmodelname(SBMLDocument document){
		return document.getModel().getName();
	}
	
	
}