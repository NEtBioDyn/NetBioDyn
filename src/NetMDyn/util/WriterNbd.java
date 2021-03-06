/* This file is part of NetMDyn.util
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
 * WriterNbd.java
 *
 * Created on February 12 2016, 16:35
 */

package NetMDyn.util;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import NetMDyn.SbmlParser;
import NetMDyn.ihm.MetaboliteVisualizer;
import netbiodyn.util.Lang;

/**
 * Writer of nbd files in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WriterNbd{
	private ArrayList<Integer>colorlist= new ArrayList();
	private SBMLDocument document;
	private SbmlParser parse=new SbmlParser();
	private ArrayList<String>entityName= new ArrayList<String>();
	private ArrayList<Species>entities=new ArrayList<Species>(); 
	private ArrayList<Compartment>compartments=new ArrayList<Compartment>();
	private MetaboliteVisualizer visualize;
	
	
	
	public WriterNbd(SBMLDocument document) {
		this.document=document;
	}
	public void nbdWrite() throws IOException, XMLStreamException{
		FileWriter testSave = null;
		
		
	    try {
	        testSave = new FileWriter(this.parse.getmodelname(document)+".nbd");
	       
	    } catch (Exception e) {
	    	
	    }
	  
	    BufferedWriter out_file = new BufferedWriter(testSave);
	    try {
	    	
            out_file.write("version:NetMDyn\n");
            checkEntities(document);
            write_entities( out_file,document);
            write_compartment( out_file,  document);
            write_behaviors( out_file,  document);
            writeMovement( out_file, document);
            writeTraversee(out_file, document);
            
            write_environement( out_file,  document);
            out_file.write("Reaxels\n");
            out_file.write("Fin");
        } catch (Exception e) {
        }
	   
	    
	out_file.close();
	
	}
	public void checkReactantProductNumber(){
		
	}
	public void setEntities(ArrayList<String>entitiesName ) {
//		System.out.println(entitiesName+"titi");	

		this.entityName=entitiesName;
	}
	public void checkEntities(SBMLDocument document ) throws XMLStreamException, IOException{
		 parse.parseSpecies(document);
		 parse.parse_reaction(document);
		 for (Species entity : parse.getEntities()) {
			 for (String name : entityName) {
				 String test= entity.getName()+"_"+entity.getCompartment();
				 if (test.equals(name)) {
						entities.add(entity);
					}
				}
			}
		

		
	}
	
	public void write_entities(BufferedWriter out_file, SBMLDocument document ) throws XMLStreamException, IOException{
		boolean visible=true;
	    parse.parseSpecies(document);
	    parse.parse_reaction(document);
	    parse.parse_compartment(document);
	for (Species entity : entities) {

		
	
		String compartmenttmp = entity.getCompartment();
		
		out_file.write("class NetMDyn.Entity_NetMDyn\n");
		out_file.write("\tEtiquettes:"+entity.getName()+"_"+compartmenttmp+"\n");
		out_file.write("\tvisibleDansPanel:"+visible+"\n");
		out_file.write("\tcouleur:-"+create_color()+"\n");
		out_file.write("\tdemie_vie:0.0\n");
		out_file.write("\tvidable:true\n");
		if (compartmenttmp.equals("default")){
		out_file.write("\tcompartment:"+"Cytosol"+"\n");
		}
		else{
			out_file.write("\tcompartment:"+compartmenttmp+"\n");
		}
		
		out_file.write("\tforme:1\n");
		out_file.write("\ttaille:0\n");
		out_file.write("Fin\n");
	    out_file.newLine();
	    }
	visible=true;
	System.out.println(parse.getCompartments()+"chabada");
	for (int i = 0; i < parse.getCompartments().size(); i++) {
		System.out.println(parse.getCompartments().get(i)+" han solo");
	if((!parse.getCompartments().get(i).getName().equals("default"))&&(!parse.getCompartments().get(i).getName().equals("Cytosol"))){
		
			out_file.write("class NetMDyn.Entity_NetMDyn\n");
			out_file.write("\tEtiquettes:"+"Membrane_"+parse.getCompartments().get(i).getName()+"\n");
			out_file.write("\tvisibleDansPanel:"+visible+"\n");
		out_file.write("\tcouleur:-"+create_color()+"\n");
			out_file.write("\tdemie_vie:0.0\n");
			out_file.write("\tvidable:true\n");
			out_file.write("\tcompartment:"+parse.getCompartments().get(i).getName()+"\n");
		out_file.write("\tforme:1\n");
			out_file.write("\ttaille:0\n");
			out_file.write("Fin\n");
		    out_file.newLine();
		}
	}
	
	}
	
public void write_compartment(BufferedWriter out_file, SBMLDocument document) throws IOException{
//	 parse.parse_compartment(document);
	 System.out.println(parse.getCompartments()+"chabada2");
		for (int i = 0; i < parse.getCompartments().size(); i++) {
			if((!parse.getCompartments().get(i).getName().equals("default"))&&(!parse.getCompartments().get(i).getName().equals("Cytosol"))){
				
			out_file.write("class NetMDyn.Compartment\n");
			out_file.write("\tEtiquettes:"+parse.getCompartments().get(i).getName()+"\n");
			out_file.write("\tcenterX:"+"0"+"\n");
			out_file.write("\tcenterY:"+"0"+"\n");
			out_file.write("\tcenterZ:"+"0"+"\n");
			out_file.write("\tradius:"+"0"+"\n");
			out_file.write("Fin\n");
		    out_file.newLine();
			
			}
			
		}
	}
public void write_environement(BufferedWriter out_file, SBMLDocument document) throws IOException{
	out_file.write("netbiodyn.Environnement\n");
	out_file.write("\ttailleX:100\n");
	out_file.write("\ttailleY:100\n");
	out_file.write("\ttailleZ:10\n");
	out_file.write("Fin\n");
	 out_file.newLine();
	
}
public void write_behaviors(BufferedWriter out_file, SBMLDocument document) throws IOException{
	parse.parse_reaction(document);
	for (int i = 0; i < parse.getReactions().size(); i++) {
		ArrayList<SpeciesReference>productList=new ArrayList<SpeciesReference>();
		for (SpeciesReference product: parse.getReactions().get(i).getListOfProducts()) {
			System.out.println(product);
			for (Species entity : entities) {
				if(product.getSpeciesInstance().equals(entity)){
					productList.add(product);
				}
			}
		}
		ArrayList<SpeciesReference>reactantList=new ArrayList<SpeciesReference>();
		
		for (SpeciesReference reactant: parse.getReactions().get(i).getListOfReactants()) {
			for (Species entity : entities) {
				if(reactant.getSpeciesInstance().equals(entity)){
					reactantList.add(reactant);
				}
			}
		}
		if (!((productList.isEmpty())||(reactantList.isEmpty()))) {
			out_file.write("class NetMDyn.Behavior_NetMDyn\n");
			out_file.write("\tEtiquettes:React_"+parse.getReactions().get(i).getName()+"\n");
		
			out_file.write("\tvisibleDansPanel:true\n");
			out_file.write("\tDescription:\n");
			
			
		
			
		
			for (SpeciesReference reactant:reactantList){
				out_file.write("\treactif:"+reactant.getSpeciesInstance().getName()+"_"+parse.getReactions().get(i).getCompartment()+"\n");
			}
			if (reactantList.size()<=2) {
				out_file.write("\treactif:*\n");
						}
		
			for (SpeciesReference product:productList){
				out_file.write("\tproduit:"+product.getSpeciesInstance().getName()+"_"+parse.getReactions().get(i).getCompartment()+"\n");
			}
			if (productList.size()<=2) {
				out_file.write("\tproduit:-\n");
						}
		out_file.write("\tk:"+0+"\n");
		out_file.write("\tpos:122222222\n");
		for (int l = 0; l < 8; l++) {
			out_file.write("\tpos:212111211\n");			
		}
			out_file.write("\tType_Behaviour:3\n");
			for (int j = 0; j < parse.getReactions().get(i).getKineticLaw().getListOfLocalParameters().size(); j++){
				if(parse.getReactions().get(i).getKineticLaw().getListOfLocalParameters().get(j).getId().equals("kon")){
					out_file.write("\tK:"+parse.getReactions().get(i).getKineticLaw().getListOfLocalParameters().get(j).getValue()+"\n");
					}
				}
			out_file.write("\tProba:0.0\n");
			out_file.write("Fin\n");
			out_file.write("\n");
		} 
		
		if(parse.getReactions().get(i).getReversible()==true){
			if (!((productList.isEmpty())||(reactantList.isEmpty()))) {
				out_file.write("class NetMDyn.Behavior_NetMDyn\n");
				out_file.write("\tEtiquettes:React_"+parse.getReactions().get(i).getName()+"_reverse\n");
				out_file.write("\tvisibleDansPanel:true\n");
				out_file.write("\tDescription:\n");					
				for (SpeciesReference product:productList){
					out_file.write("\treactif:"+product.getSpeciesInstance().getName()+"\n");
				}
				if (productList.size()<=2) {
					out_file.write("\treactif:*\n");
				}
				for (SpeciesReference reactant:reactantList){
					out_file.write("\tproduit:"+reactant.getSpeciesInstance().getName()+"\n");
				}
				if (reactantList.size()<=2) {
					out_file.write("\tproduit:-\n");
				}
				out_file.write("\tk:"+0+"\n");
				out_file.write("\tpos:122222222\n");
				for (int l = 0; l < 8; l++) {
					out_file.write("\tpos:212111211\n");
				}
				out_file.write("\tType_Behaviour:3\n");
				for (int j = 0; j < parse.getReactions().get(i).getKineticLaw().getListOfLocalParameters().size(); j++){
					if(parse.getReactions().get(i).getKineticLaw().getListOfLocalParameters().get(j).getId().equals("koff")){
						out_file.write("\tK:"+parse.getReactions().get(i).getKineticLaw().getListOfLocalParameters().get(j).getValue()+"\n");
						}
					}
				out_file.write("\tProba:0.0\n");
				out_file.write("Fin\n");
				out_file.write("\n");
			}				
		
		}
	}
	}
public void writeMovement(BufferedWriter out_file, SBMLDocument document) throws IOException{
	for (String entityname : entityName) {
		out_file.write("class NetMDyn.Behavior_NetMDyn\n");
		out_file.write("\tEtiquettes:Move_"+entityname+"\n");
		out_file.write("\tvisibleDansPanel:true\n");
		out_file.write("\tDescription:\n");
		out_file.write("\treactif:"+entityname+"\n");
		out_file.write("\treactif:0\n");
		out_file.write("\treactif:*\n");
		out_file.write("\tproduit:0\n");
		out_file.write("\tproduit:"+entityname+"\n");
		out_file.write("\tproduit:-\n");
		out_file.write("\tk:"+0.75+"\n");
		out_file.write("\tpos:122222222\n");
		for (int l = 0; l < 8; l++) {
			out_file.write("\tpos:212111211\n");
		}
		out_file.write("\tType_Behaviour:1\n");
		out_file.write("\tK:0.0\n");
		boolean reactif=false;
		boolean produit=false;
		boolean enzyme=false;
		for (int i = 0; i < parse.getReactions().size(); i++) {
			reactif=false;
			produit=false;
			for(int j = 0; j < parse.getReactions().get(i).getListOfReactants().size(); j++){
				if(parse.getReactions().get(i).getListOfReactants().get(j).getSpeciesInstance().getName().equals(entityname)){
					reactif=true;
				}
			}
			for(int j = 0; j < parse.getReactions().get(i).getListOfProducts().size(); j++){
				if(parse.getReactions().get(i).getListOfProducts().get(j).getSpeciesInstance().getName().equals(entityname)){
					produit=true;
				}
			}
			if (reactif==true && produit==true){
				enzyme=true;
			}			
		}
		if (enzyme==true){
			out_file.write("\tProba:0.5\n");
		}
		else {
			out_file.write("\tProba:0.8\n");
		}

		out_file.write("Fin\n");
		out_file.write("\n");
	}
}

//public void writeTraversee(BufferedWriter out_file, SBMLDocument document) throws IOException{
//	for (String entityname : entityName) {
//		out_file.write("class NetMDyn.Behavior_NetMDyn\n");
//		out_file.write("\tEtiquettes:Traverse_"+entityname+"_"+"\n");
//		out_file.write("\tvisibleDansPanel:true\n");
//		out_file.write("\tDescription:\n");
//		out_file.write("\treactif:"+entityname+"\n");
//		out_file.write("\treactif:"+entityname+"\n");
//		out_file.write("\treactif:*\n");
//		out_file.write("\tproduit:"+entityname+"\n");
//		out_file.write("\tproduit:0\n");
//		out_file.write("\tproduit:"+entityname+"\n");
//		out_file.write("\tk:"+0.75+"\n");
//		out_file.write("\tpos:122222222\n");
//		out_file.write("\tpos:202100210\n");
//		out_file.write("\tpos:202100210\n");
//		for (int l = 0; l < 6; l++) {
//			out_file.write("\tpos:212101210\n");
//		}
//		out_file.write("\tType_Behaviour:1\n");
//		out_file.write("\tK:0.0\n");
//		out_file.write("\tProba:0.5\n");
//		out_file.write("Fin\n");
//		out_file.write("\n");
//			
//		out_file.write("class NetMDyn.Behavior_NetMDyn\n");
//		out_file.write("\tEtiquettes:Traverse_"+entityname+"_"+"NS\n");
//		out_file.write("\tvisibleDansPanel:true\n");
//		out_file.write("\tDescription:\n");
//		out_file.write("\treactif:"+entityname+"\n");
//		out_file.write("\treactif:"+entityname+"\n");
//		out_file.write("\treactif:*\n");
//		out_file.write("\tproduit:"+entityname+"\n");
//		out_file.write("\tproduit:0\n");
//		out_file.write("\tproduit:"+entityname+"\n");
//		out_file.write("\tk:"+0.75+"\n");
//		out_file.write("\tpos:122222222\n");
//		out_file.write("\tpos:212001200\n");
//		out_file.write("\tpos:212001200\n");
//		for (int l = 0; l < 6; l++) {
//			out_file.write("\tpos:212101210\n");
//		}
//		out_file.write("\tType_Behaviour:1\n");
//		out_file.write("\tK:0.0\n");
//		out_file.write("\tProba:0.5\n");
//		out_file.write("Fin\n");
//		out_file.write("\n");		
//	}
//}

public void writeTraversee(BufferedWriter out_file, SBMLDocument document) throws IOException, XMLStreamException{
//	visualize=new MetaboliteVisualizer(document);
	//parse.parse_compartment(document);
	boolean bool=false;
	int i = 0;
	for (String entityname : entityName) {
		i++;
		int j=0;
		for (String entityname2 : entityName) {
			j++;
			String[] tmp1 = entityname.split("_");
			String[]tmp2 = entityname2.split("_");
			if((tmp1[0].equals(tmp2[0]))&&(!tmp1[1].equals(tmp2[1]))&&(i<j)) {
				if(tmp1[1].equals("Cytosol")||tmp2[1].equals("Cytosol")){
//					System.out.println(tmp1[0]+tmp1[1]);
//					System.out.println(tmp2[0]+tmp2[1]);
					out_file.write("class NetMDyn.Behavior_NetMDyn\n");
					out_file.write("\tEtiquettes:Traverse_"+tmp1[0]+"_"+"NS\n");
					out_file.write("\tvisibleDansPanel:false\n");
					out_file.write("\tDescription:\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\treactif:Membrane_"+tmp2[1]+"\n");
						}
					else{
						out_file.write("\treactif:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\treactif:"+tmp1[0]+"_"+tmp1[1]+"\n");
					out_file.write("\treactif:*\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\tproduit:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\tproduit:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\tproduit:0\n");
					out_file.write("\tproduit:"+tmp2[0]+"_"+tmp2[1]+"\n");
					out_file.write("\tk:"+0.75+"\n");
					out_file.write("\tpos:122222222\n");
					out_file.write("\tpos:202100210\n");
					out_file.write("\tpos:202100210\n");
					for (int l = 0; l < 6; l++) {
						out_file.write("\tpos:212101210\n");
					}
					out_file.write("\tType_Behaviour:2\n");
					out_file.write("\tK:0.0\n");
					out_file.write("\tProba:0.5\n");
					out_file.write("Fin\n");
					out_file.write("\n");
					
					
					
					out_file.write("class NetMDyn.Behavior_NetMDyn\n");
					out_file.write("\tEtiquettes:Traverse_"+tmp1[0]+"\n");
					out_file.write("\tvisibleDansPanel:true\n");
					out_file.write("\tDescription:\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\treactif:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\treactif:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\treactif:"+tmp1[0]+"_"+tmp1[1]+"\n");
					out_file.write("\treactif:*\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\tproduit:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\tproduit:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\tproduit:0\n");
					out_file.write("\tproduit:"+tmp2[0]+"_"+tmp2[1]+"\n");
					out_file.write("\tk:"+0.75+"\n");
					out_file.write("\tpos:122222222\n");
					out_file.write("\tpos:212001200\n");
					out_file.write("\tpos:212001200\n");
					for (int l = 0; l < 6; l++) {
						out_file.write("\tpos:212101210\n");
					}
					out_file.write("\tType_Behaviour:2\n");
					out_file.write("\tK:0.0\n");
					out_file.write("\tProba:0.5\n");
					out_file.write("Fin\n");
					out_file.write("\n");
					
					
					
					out_file.write("class NetMDyn.Behavior_NetMDyn\n");
					out_file.write("\tEtiquettes:Traverse_"+tmp1[0]+"_reverse\n");
					out_file.write("\tvisibleDansPanel:true\n");
					out_file.write("\tDescription:\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\treactif:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\treactif:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\treactif:"+tmp2[0]+"_"+tmp2[1]+"\n");
					out_file.write("\treactif:*\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\tproduit:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\tproduit:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\tproduit:0\n");
					out_file.write("\tproduit:"+tmp1[0]+"_"+tmp1[1]+"\n");
					out_file.write("\tk:"+0.75+"\n");
					out_file.write("\tpos:122222222\n");
					out_file.write("\tpos:212001200\n");
					out_file.write("\tpos:212001200\n");
					for (int l = 0; l < 6; l++) {
						out_file.write("\tpos:212101210\n");
					}
					out_file.write("\tType_Behaviour:2\n");
					out_file.write("\tK:0.0\n");
					out_file.write("\tProba:0.5\n");
					out_file.write("Fin\n");
					out_file.write("\n");
					
					
					out_file.write("class NetMDyn.Behavior_NetMDyn\n");
					out_file.write("\tEtiquettes:Traverse_"+tmp1[0]+"_"+"NS_reverse\n");
					out_file.write("\tvisibleDansPanel:false\n");
					out_file.write("\tDescription:\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\treactif:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\treactif:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\treactif:"+tmp2[0]+"_"+tmp2[1]+"\n");
					out_file.write("\treactif:*\n");
					if(tmp1[1].equals("Cytosol")){
						out_file.write("\tproduit:Membrane_"+tmp2[1]+"\n");
					}
					else{
						out_file.write("\tproduit:Membrane_"+tmp1[1]+"\n");
					}
					out_file.write("\tproduit:0\n");
					out_file.write("\tproduit:"+tmp1[0]+"_"+tmp1[1]+"\n");
					out_file.write("\tk:"+0.75+"\n");
					out_file.write("\tpos:122222222\n");
					out_file.write("\tpos:202100210\n");
					out_file.write("\tpos:202100210\n");
					for (int l = 0; l < 6; l++) {
						out_file.write("\tpos:212101210\n");
					}
					out_file.write("\tType_Behaviour:2\n");
					out_file.write("\tK:0.0\n");
					out_file.write("\tProba:0.5\n");
					out_file.write("Fin\n");
					out_file.write("\n");
					
				}
				else{
					bool=true;
				}
			}
		}
	}
	if (bool==true){		
		if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
		          JOptionPane.showMessageDialog(visualize, " Attention il est probablement nécessaire de créer des traversées de membrane supplémentaire" );
		       } else {
		           JOptionPane.showMessageDialog(visualize, " warning you probably need to create new membrane crossing !");
		       }
	}
}




	public int create_color(){
		int color=0;
	Random rnd=new Random();
	int val1=rnd.nextInt(254)+1;
	int val2=rnd.nextInt(254)+1;
	int val3=rnd.nextInt(254)+1;
	color=val1*val2*val3;

	for (Integer color2 : colorlist) {
		if ((color<color2+100)&&(color>color2-100)){
			create_color();
		
		}
		else{
		}
	}
	colorlist.add(color);
		return color;
	}
	public String getFileName(SBMLDocument document){
		return parse.getmodelname(document)+".nbd";
		
	}
}