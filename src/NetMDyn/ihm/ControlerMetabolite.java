package NetMDyn.ihm;
import  NetMDyn.ihm.MetaboliteVisualizer;
import NetMDyn.Model_NetMDyn;
import NetMDyn.SbmlParser;
import NetMDyn.util.WriterNbd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import netbiodyn.util.UtilDivers;

import org.sbml.jsbml.SBMLDocument;

public class ControlerMetabolite {
	private SBMLDocument document;
	private MetaboliteVisualizer visualize;
	private NetMDyn.util.WriterNbd writer;
	private SbmlParser parser = new SbmlParser();
	private ArrayList<String> selection= new ArrayList<String>();
	private Environment_NetMDyn env;
	private Model_NetMDyn model;
	public ControlerMetabolite(MetaboliteVisualizer visualize,
			SbmlParser parser, SBMLDocument document, Environment_NetMDyn env, Model_NetMDyn model) {
		this.visualize = visualize;
		this.parser = parser;
		this.document = document;
		this.env= env;
		this.model= model;
	};

	// public Integer newlist() {
	// Integer id = visualize.getShop().newBasket();
	// basketId.add(id);
	// return id;
	// }
	public ActionListener addMetaboliteinSelection() throws XMLStreamException,
			IOException {
		parser.parseSpeciesName(document);
		parser.parseSpecies(document);
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				for (int id : visualize.getselectedmetabolitesIndices()) {
		        		boolean flag=false;
		        		 for (int i = 0; i < selection.size(); i++) {
						if (selection.get(i).equals(parser.getEntitiesName().get(id))) {
							flag=true;
						}
					}
		        		 if(flag==false){
		 					selection.add(parser.getEntitiesName().get(id));
		 				}
		 				if(flag==true){
		 				JOptionPane.showMessageDialog(visualize, "le métabolite"+parser.getEntitiesName().get(id)+ "est déja dans la liste");
		 				flag=false;
		 				}
		        	}
		        
				visualize.renderSelection(selection);
			}
		};
	}

	public ActionListener validateSelection() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writer= new WriterNbd(document);
				JOptionPane.showMessageDialog(visualize,"la liste des entités a bien été prise en compte" );
				System.out.println(selection+"toto");
				writer.setEntities(selection);
//				Controller_NetMDyn c= new Controller_NetMDyn();
//				try {
//					env.controller.loadModel(writer.getFileName(document));
//				} catch (XMLStreamException | IOException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}

				
				
			
				try {
					writer.nbdWrite();
//				Model_NetMDyn	model; 
					model.load(env,writer.getFileName(document));
//					 Environment_NetMDyn env;
//					env.setNom_sauvegarde(UtilDivers.removeExtension( writer.getFileName(document)));
			         
		    	
//				model=	model.load(env, writer.getFileName(document) );
				} catch (IOException | XMLStreamException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		};
	}

	public ActionListener supprime() {
		
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(visualize,"L'entité a bien été enlevé a la liste" );
			}
			
		};
		}

	public WriterNbd getWriter() {
		return writer;
	}

	public void setWriter(WriterNbd writer) {
		this.writer = writer;
	}
	
	}
