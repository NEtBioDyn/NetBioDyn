import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.util.SimpleTreeNodeChangeListener;
import org.sbml.jsbml.util.TreeNodeChangeListener;

public class Sbml_parser{
	private SBMLDocument document;
	private ArrayList<Reaction>reactions= new ArrayList<Reaction>();
	private ArrayList<Compartment>compartments=new ArrayList<Compartment>();
	private ArrayList<Species>entities=new ArrayList<Species>();
	
	
	public Sbml_parser() {
	
		
	}
	public void parse_species(SBMLDocument document) throws XMLStreamException, IOException{
	//	System.out.println(document);
	Model model = document.getModel();
//	System.out.println(model);
//	String modelName = model.getName();
//	ListOf<Compartment> compartiments = model.getListOfCompartments();
//	System.out.println(modelName);
//	Map<String, String> attribut = document.getSBMLDocumentAttributes();
//	System.out.println(attribut);
//	System.out.println(compartiments);
//	JTree tree = new JTree(document);
//	System.out.println(tree);
//	Compartment compartment = model.getCompartment(0);
//	System.out.println(compartment);
//	System.out.println( model.getCompartmentCount());
// 
//	System.out.println(model.getReactionCount());
	
//Reaction reac=model.getReaction(2);
//		System.out.println(reac.getListOfReactants());
//		System.out.println(reac.getListOfProducts());
//		System.out.println(reac.getListOfReactants());
//		System.out.println(reac.getListOfModifiers());
//		System.out.println(reac.getKineticLaw());
//		System.out.println(reac.getKineticLaw().getFormula());
//		for (int i = 0; i < reac.getKineticLaw().(); i++) {
//			
//		}
//		for (SpeciesReference entitie : reac.getListOfReactants()) {
//			System.out.println(entitie);
//		}
	for (int k = 0; k <model.getSpeciesCount() ; k++) {
		entities.add(model.getSpecies(k));
		
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
		System.out.println(compartments);
	}
	public ArrayList<Species> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<Species> entities) {
		this.entities = entities;
		
	}
	public String getmodelname(SBMLDocument document){
		return document.getModel().getName();
	}
	
	
}