package NetMDyn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

public class SbmlParser{
	private ArrayList<Reaction>reactions= new ArrayList<Reaction>();
	private ArrayList<Compartment>compartments=new ArrayList<Compartment>();
	private Set<Species>entities=new HashSet<Species>();
	private ArrayList<String> entities_name= new ArrayList<String>();
	
	
	
	public SbmlParser() {
	
		
	}
	public void parseSpecies(SBMLDocument document) throws XMLStreamException, IOException{
	Model model = document.getModel();
	for (int k = 0; k <model.getSpeciesCount() ; k++) {
		entities.add(model.getSpecies(k));
	}
	}
	public void parseSpeciesName(SBMLDocument document){
		Model model = document.getModel();
		for (int k = 0; k <model.getSpeciesCount() ; k++) {
			entities_name.add(model.getSpecies(k).getName());
	}
		}
	public void parse_compartment(SBMLDocument document){
		Model model = document.getModel();
		for (int j = 0; j < model.getCompartmentCount(); j++) {
		compartments.add(model.getCompartment(j));
		}
	}
	public void parse_reaction(SBMLDocument document){
		Model model = document.getModel();
		for (int i = 0; i <model.getReactionCount() ; i++) {
			reactions.add(model.getReaction(i));
			}
		}
	public List<SpeciesReference> get_reactant(Reaction reac){
		reac.getListOfReactants();
		return reac.getListOfReactants();
	}
	public List<SpeciesReference> get_product(Reaction reac){
		reac.getListOfProducts();
		return reac.getListOfProducts();
		}
	public KineticLaw get_kinetic_law(Reaction reac){
		return reac.getKineticLaw();
		
	}
	
	public ArrayList<Reaction> getReactions() {
		return reactions;
	}
	public void setReactions(ArrayList<Reaction> reactions) {
		this.reactions = reactions;
	}
	public ArrayList<Compartment> getCompartments() {
		return compartments;
	}
	public void setCompartments(ArrayList<Compartment> compartments) {
		this.compartments = compartments;
		
	}
	public Set<Species> getEntities() {
		return entities;
	}
	public ArrayList<String> getEntitiesName(){
		return entities_name;
		
	}
	public void setEntities(Set<Species> entities) {
		this.entities = entities;
		
	}
	public String getmodelname(SBMLDocument document){
		return document.getModel().getName();
	}
	
	
}