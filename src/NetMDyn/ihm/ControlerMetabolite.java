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
 * ControlerMetabolite.java
 *
 * Created on February 12 2016, 13:49
 */


package NetMDyn.ihm;

import NetMDyn.ihm.MetaboliteVisualizer;
import NetMDyn.Model_NetMDyn;
import NetMDyn.SbmlParser;
import NetMDyn.util.WriterNbd;

import java.awt.event.ActionEvent; // A semantic event which indicates that a component-defined action occurred
import java.awt.event.ActionListener; // The listener interface for receiving action events
import java.io.IOException; // Signals that an I/O exception of some sort has occurred
import java.util.ArrayList; // Possible creation of tables

import javax.swing.JOptionPane; // Pop up a standard dialog box that prompts users for a value or informs them of something
import javax.xml.stream.XMLStreamException; // The base exception for unexpected processing errors

import netbiodyn.util.Lang;
import netbiodyn.util.UtilDivers;

import org.sbml.jsbml.SBMLDocument; /// Represent the 'sbml' root node of a SBML file.

/**
 * Control of the metabolites in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class ControlerMetabolite {
	private SBMLDocument document;
	private MetaboliteVisualizer visualize;
	private NetMDyn.util.WriterNbd writer;
	private SbmlParser parser = new SbmlParser();
	private ArrayList<String> selection= new ArrayList<String>();
	private Environment_NetMDyn env;
	private Model_NetMDyn model;
	
	/**
	 * Initialization of a ControlerMetabolite object
	 * @param visualize : visualization of the metabolites
	 * @param parser : SBML parser
	 * @param document : SBML document 
	 * @param env : Environment
	 * @param model : Model
	 */
	public ControlerMetabolite(MetaboliteVisualizer visualize,
			SbmlParser parser, SBMLDocument document, Environment_NetMDyn env, Model_NetMDyn model) {
		this.visualize = visualize;
		this.parser = parser;
		this.document = document;
		this.env= env;
		this.model= model;
	};
	
	/**
	 * Add a Metabolite in the selection
	 * @return an Action Listener
	 * @throws XMLStreamException
	 * @throws IOException
	 */
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
		 					if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
				 				JOptionPane.showMessageDialog(visualize, "Le métabolite "+parser.getEntitiesName().get(id)+ " est déja dans la liste");
		 					}
		 					else{
				 				JOptionPane.showMessageDialog(visualize, "The metabolite "+parser.getEntitiesName().get(id)+ " is already into the list");
		 					}
		 						
		 				flag=false;
		 				}
		        	}
		        
				visualize.renderSelection(selection);
			}
		};
	}

	/**
	 * Validation of the selection
	 * @return an ActionListener
	 */
	public ActionListener validateSelection() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writer= new WriterNbd(document);
				if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
					JOptionPane.showMessageDialog(visualize,"La liste des entités a bien été prise en compte." );
				}
				else{
					JOptionPane.showMessageDialog(visualize,"The Entities list has been taken into account." );
				}
				
				writer.setEntities(selection);				
			
				try {
					writer.nbdWrite();
					model.load(env,writer.getFileName(document));
				} catch (IOException | XMLStreamException e1) {
					e1.printStackTrace();
				}	
			}
		};
	}

	/**
	 * Remove an Entity
	 * @return an ActionListener
	 */
	public ActionListener supprime() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(visualize,"L'entité a bien été enlevé a la liste" );
			}
			
		};
		}

	/**
	 * Obtain the WriterNbd
	 * @return the writer
	 */
	public WriterNbd getWriter() {
		return writer;
	}

	/**
	 * Change the WriterNbd
	 * @param writer : the new WriterNbd
	 */
	public void setWriter(WriterNbd writer) {
		this.writer = writer;
	}
	
	}
