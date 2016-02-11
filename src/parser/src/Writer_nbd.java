import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;

public class Writer_nbd{
	private ArrayList<Integer>colorlist= new ArrayList();
	private SBMLDocument document;
	private Sbml_parser parse=new Sbml_parser();
	
	
	public Writer_nbd(SBMLDocument document) {
		this.document=document;
	}
	public void nbd_write() throws IOException, XMLStreamException{
		ArrayList<Species> entities= new ArrayList<Species>();
		boolean error=false;
		FileWriter testSave = null;
		
		System.out.println(parse.getEntities());
	    try {
	        testSave = new FileWriter(this.parse.getmodelname(document)+".nbd");
	       
	    } catch (Exception e) {
	    	System.out.println("bbbbb");
	        error = true;
	    }
	    System.out.println("ccc");
	    BufferedWriter out_file = new BufferedWriter(testSave);
	    try {
	    	System.out.println("");
            out_file.write("version:3d-1.0\n");
            write_entities( out_file,document);
            write_compartment( out_file,  document);
            write_environement( out_file,  document);
        } catch (Exception e) {
            error = true;
        }
	    System.out.println("dddd");
	    
	out_file.close();
	}
	public void write_entities(BufferedWriter out_file, SBMLDocument document ) throws XMLStreamException, IOException{
		boolean visible=true;
	    parse.parse_species(document);
	for (int i = 0; i < parse.getEntities(). size(); i++) {
		String compartmenttmp = parse.getEntities().get(i).getCompartment();
		System.out.println("ee");
//		 String tab_separe = ":";
//	        str = str.replaceAll("\t", ":");
		out_file.write("class netbiodyn.Entity\n");
		out_file.write("\tEtiquettes:"+parse.getEntities().get(i).getName()+"\n");
		out_file.write("\tvisibleDansPanel:"+visible+"\n");
		out_file.write("\tcouleur:-"+create_color()+"\n");
		out_file.write("\tdemie_vie:0.0\n");
		out_file.write("\tvidable:true\n");
		if (compartmenttmp.equals("default")){
		out_file.write("\tcompartment:"+"cytosol"+"\n");
		}
		else{
			out_file.write("\tcompartment:"+compartmenttmp+"\n");
		}
		
		out_file.write("\tforme:1\n");
		out_file.write("\ttaille:0\n");
		out_file.write("fin\n");
	    out_file.newLine();
	    }
	visible=false;
	for (int i = 0; i < parse.getCompartments().size(); i++) {
		if(!parse.getCompartments().get(i).getName().equals("default")){
			System.out.println(parse.getCompartments().get(i).getName());
			out_file.write("class netbiodyn.Entity\n");
			out_file.write("\tEtiquettes:"+"m"+parse.getCompartments().get(i).getName()+"\n");
			out_file.write("\tvisibleDansPanel:"+visible+"\n");
			out_file.write("\tcouleur:-"+create_color()+"\n");
			out_file.write("\tdemie_vie:0.0\n");
			out_file.write("\tvidable:true\n");
			out_file.write("\tcompartment:"+parse.getCompartments().get(i).getName()+"\n");
			out_file.write("\tforme:1\n");
			out_file.write("\ttaille:0\n");
			out_file.write("fin\n");
		    out_file.newLine();
		}
	}
	}
	
public void write_compartment(BufferedWriter out_file, SBMLDocument document) throws IOException{
	 parse.parse_compartment(document);
		for (int i = 0; i < parse.getCompartments().size(); i++) {
			if(!parse.getCompartments().get(i).getName().equals("default")){
				System.out.println(parse.getCompartments().get(i).getName());
			out_file.write("class netbiodyn.Compartment\n");
			out_file.write("\tEtiquettes:"+parse.getCompartments().get(i).getName()+"\n");
			out_file.write("\tcenterX:"+"\n");
			out_file.write("\tcenterY:"+"\n");
			out_file.write("\tcenterZ:"+"\n");
			out_file.write("\tradius:"+"\n");
			out_file.write("\tmenbrane:m"+parse.getCompartments().get(i).getName()+"\n");
			out_file.write("fin\n");
		    out_file.newLine();
			
			}
		}
	}
public void write_environement(BufferedWriter out_file, SBMLDocument document) throws IOException{
	out_file.write("netbiodyn.Environnement\n");
	out_file.write("\ttailleX:1000\n");
	out_file.write("\ttailleY:1000\n");
	out_file.write("\ttailleZ:100\n");
	out_file.write("fin\n");
	 out_file.newLine();
	
}
public void write_behaviors(BufferedWriter out_file, SBMLDocument document) throws IOException{
	parse.parse_compartment(document);
	for (int i = 0; i < parse.getReactions().size(); i++) {
		out_file.write("class netbiodyn.Behavior\n");
		out_file.write("Etiquettes:"+parse.getReactions().get(i).getName());
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
		System.out.println(color);
		System.out.println(color2);
		if ((color<color2+100)&&(color>color2-100)){
			System.out.println(color);
			System.out.println(color2);
			create_color();
		
		}
		else{
		}
	}
	colorlist.add(color);
		return color;
	}
}