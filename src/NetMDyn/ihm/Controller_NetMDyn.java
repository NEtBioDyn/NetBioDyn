/* This file is part of NetMDyn.ihm
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
 * Controller_NetMDyn.java
 *
 * Created on February 12 2016, 14:01
 */

package NetMDyn.ihm;

import NetMDyn.AllInstances_NetMDyn;
import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Compartment;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.InstanceReaxel_NetMDyn;
import NetMDyn.Model_NetMDyn;
import NetMDyn.SbmlParser;
import NetMDyn.Simulator_NetMDyn;
import NetMDyn.util.FileSaverLoader_NetMDyn;
import NetMDyn.util.UtilPoint3D_NetMDyn;
import jadeAgentServer.util.Parameter;

import java.awt.Container; // Component that can contain other Abstract Window Toolkit components. 
import java.awt.Dimension; // Width and height of a component (in integer precision) in a single object
import java.awt.event.ActionEvent; // A semantic event which indicates that a component-defined action occurred
import java.awt.event.KeyEvent; // An event which indicates that a keystroke occurred in a component. 
import java.awt.event.WindowAdapter; // Abstract class to receive window events 
import java.awt.event.WindowEvent; // Possible to signal status changes of a window (opened, close, reduced,...)
import java.io.File; // An abstract representation of file and directory pathnames. 
import java.io.IOException; // Signals that an I/O exception of some sort has occurred
import java.util.ArrayList; // Possible creation of tables
import java.util.HashMap; // Possible creation of hashmaps
import java.util.List; // Possible creation of lists
import java.util.logging.Level; // Set of standard logging levels that can be used to control logging output
import java.util.logging.Logger; //Log messages for a specific system or application component.

import javax.swing.AbstractAction; // Standard behaviors like the get and set methods for Action object properties (icon, text, and enabled) are defined here.
import javax.swing.ActionMap; // Mappings from Objects (called keys or Action names) to Actions.
import javax.swing.InputMap; // Binding between an input event (currently only KeyStrokes are used) and an Object
import javax.swing.JComponent; // The base class for all Swing components except top-level containers.
import javax.swing.JFrame; // Possible creation of windows
import javax.swing.JOptionPane; //Possible creation of dialog windows
import javax.swing.KeyStroke; // Key action on the keyboard, or equivalent input device
import javax.xml.stream.XMLStreamException; // The base exception for unexpected processing errors

import org.sbml.jsbml.SBMLDocument; // Represent the 'sbml' root node of a SBML file.
import org.sbml.jsbml.SBMLReader; // Provide methods for reading SBML from files, text strings or streams. 

import com.jogamp.opengl.awt.GLJPanel; // Provides OpenGL rendering support.
import com.jogamp.opengl.util.FPSAnimator; // Attempt to achieve a target frames-per-second rate to avoid using all CPU time

import netbiodyn.AllInstances;
import netbiodyn.Behavior;
import netbiodyn.InstanceReaxel;
import netbiodyn.Model;
import netbiodyn.Simulator;
import netbiodyn.Entity;
import netbiodyn.ihm.AddCommand;
import netbiodyn.ihm.Command;
import netbiodyn.ihm.Controller;
import netbiodyn.ihm.Env_Parameters;
import netbiodyn.ihm.Environment;
import netbiodyn.ihm.JOGL2Setup_GLJPanel;
import netbiodyn.ihm.RemoveCommand;
import netbiodyn.ihm.SimulationCurves;
import netbiodyn.ihm.WndAbout;
import netbiodyn.ihm.WndDescriptionSimulation;
import netbiodyn.ihm.WndEditEnvironnement;
import netbiodyn.util.FileSaverLoader;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;
import netbiodyn.util.UtilAnimatedGifEncoder;
import netbiodyn.util.UtilDivers;
import netbiodyn.util.UtilFileFilter;
import netbiodyn.util.UtilPoint3D;

/**
 * Controller in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class Controller_NetMDyn{
	
	protected final static int FRAME_WIDTH = 1220;
    protected final static int FRAME_HEIGHT = 820;
    protected final int init_x = 100, init_y = 100, init_z = 1;
	
    protected JFrame frame3D;
	protected final Model_NetMDyn model;
	protected final Environment_NetMDyn env;
	protected final Simulator_NetMDyn simulator;
    protected final JFrame frame;
    protected final ArrayList<Command> lastCommand; 
    protected final int maxMemory = 20;
    
    /**
     * Initialization of the Controller
     */
	public Controller_NetMDyn(){
		 	this.lastCommand = new ArrayList<>();
	        frame = new JFrame();
	        Env_Parameters parameters = new Env_Parameters("EN", init_x, init_y, init_z, "Empty Environment", null, "");
	        env = new Environment_NetMDyn(this, parameters);
	        model = new Model_NetMDyn(parameters);
	        model.addListener(env);
	        simulator = new Simulator_NetMDyn(model);
	        simulator.addListener(env);

	        frame.add(env);
	        frame.setName("NetMDyn");
	        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
	        frame.setTitle("NetMDyn - University of Bordeaux - Master 2 of Bioinformatics - Free Software under GPL License");
	        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        frame.addWindowListener(new WindowAdapter() {

	            @Override
	            public void windowClosing(WindowEvent e) {
	                int res;
	                if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
	                    res = JOptionPane.showConfirmDialog(env, "Voulez-vous sauvegarder le modèle avant de quitter ?", "Question", JOptionPane.INFORMATION_MESSAGE);
	                } else {
	                    res = JOptionPane.showConfirmDialog(env, "Do you want to save your model ?", "Question", JOptionPane.INFORMATION_MESSAGE);
	                }

	                if (res == JOptionPane.YES_OPTION) {
	                    int s = saveModel(env.getNom_sauvegarde());
	                    if (s == 0) {
	                        System.exit(0);
	                    }
	                } else if (res == JOptionPane.NO_OPTION) {
	                    System.exit(0);
	                }
	            }

	        });
	        env.launch();
	        init3D();
	        this.addKeyListener(frame);
	        frame.setVisible(true);
	}
	
		/**
		 * Initialization of the 3DView
		 */
	   protected void init3D() {
		   if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
			   frame3D = new JFrame("3D View");
		   }
		   else{
			   frame3D = new JFrame("Vue 3D");
		   }
	        Container topAncestor = env.getTopLevelAncestor();
	        frame3D.setBounds(topAncestor.getBounds().x + topAncestor.getBounds().width, topAncestor.getBounds().y, 640, topAncestor.getBounds().height);

	        try {
	            JOGL2Setup_GLJPanel_NetMDyn panel3d = new JOGL2Setup_GLJPanel_NetMDyn(env.getTailleX(), env.getTailleY(), env.getTailleZ(), model.getInstances().getList());
	            GLJPanel canvas = panel3d;
	            canvas.setPreferredSize(new Dimension(640, topAncestor.getBounds().height - 24));

	            frame3D.setContentPane(canvas);  // GLJPanel is a Container
	            frame3D.pack();
	            model.addListener(panel3d);
	            simulator.addListener(panel3d);

	            // Create a animator that drives canvas' display() at the specified FPS. 
	            final FPSAnimator animator = new FPSAnimator(canvas, 60, true);
	            // Start the animation loop
	            animator.start();
	            frame3D.setVisible(false);
	            this.addKeyListener(frame3D);
	        } catch (UnsatisfiedLinkError e) {
	            disable3D(e.toString());
	        }
	    }
	   
	   /**
	    * Remove the 3D view
	    * @param message
	    */
	    protected void disable3D(String message) {
	        env.disabled3D();
	        File file = new File("./log_netbiodyn.txt");
	        try {
	            file.createNewFile();
	            FileSaverLoader.saveAsText(file.getPath(), message);
	        } catch (IOException ex) {
	            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
	        }

	        if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
	            JOptionPane.showMessageDialog(env, "Impossible d'initialiser la 3D - Démarrage en 2D. Pour plus de détails sur l'erreur, voir le fichier log_netbiodyn.txt", "Attention", JOptionPane.ERROR_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(env, "3D impossible to initialize - Staying in 2D. For more details, see the file log_netbiodyn.txt", "Warning", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    /**
	     * Makes visible / invisible the 3D frame. Called by Environment
	     */
	    public void hideShow3DView() {
	        frame3D.setVisible(!frame3D.isVisible());
	    }
	
	 /**
	  * Add an Entity into the Controller
	  */
	 public void addEntity() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        WndEditNoeud_NetMDyn wc = new WndEditNoeud_NetMDyn(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
	        wc.WndCliValue_Load(null);
	        wc.setVisible(true);
	        if (wc.getDialogResult().equals("OK") && !wc._cli._etiquettes.equals("")) {
	            model.addProtoReaxel(wc._cli);
	        }
	    }
	 //Add a Behavior into the Controller
	 public void addBehaviour() {
		 if (simulator.isRunning()) {
			 this.pauseSimulation();
		 }

		 WndBehaviourChoice wC = new WndBehaviourChoice(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
		 wC.setVisible(true);
		 if (wC.getDialogResult().equals("OK")) {
			 int reaction_type = wC.getBehaviourType();
			 if (reaction_type==1){
				 WndEditMvt wC1 = new WndEditMvt(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
				 wC1.WndEditMvt_load(null);
				 wC1.setVisible(true);
				 String r = wC1.getDialogResult();
			        if (r != null) {
			            if (r.equals("OK")) {
			                model.addMoteurReaction(wC1._r3);
			            }
			        }
			 }
			 else if (reaction_type==2){
				 WndEditTraverse wC1 = new WndEditTraverse(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
				 wC1.WndEditTraverse_load(null);
				 wC1.setVisible(true);
				 String r = wC1.getDialogResult();
			        if (r != null) {
			            if (r.equals("OK")) {
			                model.addMoteurReaction(wC1._r3);
			                model.addMoteurReaction(wC1._rNS);
			            }
			        }
			 }
			 else{
				 WndEditBehaviour wC1 = new WndEditBehaviour(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
				 wC1.WndEditBehaviour_load(null, null);
				 wC1.setVisible(true);
				 String r = wC1.getDialogResult();
			        if (r != null) {
			            if (r.equals("OK")) {
			                model.addMoteurReaction(wC1._r3);
			                if (wC1.getComboBox_reaction()=="Reversible"){
			                	model.addMoteurReaction(wC1._r3rev);
			                }
			            }
			        }
				 
			 }
		 }
	 }
	 /**
	  * Edition of an Entity into the Controller
	  */
	    public void editEntity() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        if (env.getDataGridView_entites().getSelectedIndex() >= 0) {
	            String name = UtilDivers.str_originale(env.getDataGridView_entites().getSelectedValue().toString());
	            Entity_NetMDyn p = model.getProtoReaxel(name);

	            WndEditNoeud_NetMDyn wc = new WndEditNoeud_NetMDyn(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
	            wc.WndCliValue_Load(p);
	            wc.setVisible(true);
	            if (wc.getDialogResult().equals("OK") && !p._etiquettes.equals("")) {
	                int time = simulator.getTime();
	                model.editProtoReaxel(p, name, time);
	                if (!simulator.isStopped()) {
	                    simulator.ProtoReaxelEdited(p, name);
	                }
	            }
	        }
	    }

	    /**
	     * Edition of the probabilities of a Behavior
	     * @param name : the name of the Behavior
	     * @param value : new probability of the Behavior
	     */
	    public void changeProba(String name, double value) {
	        model.editBehaviourProba(name, value);
	    }
	    
	    /**
	     * Edition of parameters of the Controller
	     * @param param : new parameters
	     */

	    public void changeParameters(HashMap<String, ArrayList<Parameter>> param) {
	        model.changeParameters(param, env.getPictureBoxDimensions());
	    }
	 
	 /**
	  * Edition of a Behavior
	  */
	    public void editBehaviour() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        int i = env.getDataGridView_comportements().getSelectedIndex();
	        if (i >= 0) {
	            String name = env.getDataGridView_comportements().getSelectedValue().toString();
	            Behavior_NetMDyn cpt = model.getBehaviour((String) env.getDataGridView_comportements().getModel().getElementAt(i));
	            
	            if (cpt.getType_behavior()==1){
					 WndEditMvt wC1 = new WndEditMvt(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
					 wC1.WndEditMvt_load(cpt);
					 wC1.setVisible(true);
					 String r = wC1.getDialogResult();
				        if (r != null) {
				            if (r.equals("OK")) {
				                model.editMoteurReaction(wC1._r3,name);
				            }
				        }
				 }
				 else if (cpt.getType_behavior()==2){
					 WndEditTraverse wC1 = new WndEditTraverse(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
					 wC1.WndEditTraverse_load(cpt);
					 wC1.setVisible(true);
					 String r = wC1.getDialogResult();
				        if (r != null) {
				            if (r.equals("OK")) {
				                model.editMoteurReaction(wC1._r3,name);
				                model.editMoteurReaction(wC1._rNS,name+"NS");
				            }
				        }
				 }
				 else{
					 WndEditBehaviour wC1 = new WndEditBehaviour(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
					 if (name.contains("_reverse")){
						 for (Behavior_NetMDyn bev : model.getCopyListManipulesReactions()){
							 if (bev.getEtiquettes().equals(name.split("_reverse")[0])){
								 wC1.WndEditBehaviour_load(bev, cpt);
								 break;
							 }
						 }
						 wC1.setVisible(true);
						 String r = wC1.getDialogResult();
					        if (r != null) {
					            if (r.equals("OK")) {
					                model.editMoteurReaction(wC1._r3,name.split("_reverse")[0]);
					                if (wC1.getComboBox_reaction()=="Reversible"){
					                	model.editMoteurReaction(wC1._r3rev,name);
					                }
					                else{
					                	ArrayList<String> suppr = new ArrayList<String>();
					                	suppr.add(name+"_reverse");
					                	model.delMoteurReaction(suppr);
					                }
					            }
					        }
					 }
					 else{
						 boolean test = false;
						 for (Behavior_NetMDyn bev : model.getCopyListManipulesReactions()){
							 if (bev.getEtiquettes().equals(name+"_reverse")){
								 wC1.WndEditBehaviour_load(cpt, bev);
								 test = true;
								 wC1.setVisible(true);
								 String r = wC1.getDialogResult();
							        if (r != null) {
							            if (r.equals("OK")) {
							                model.editMoteurReaction(wC1._r3,name);
							                if (wC1.getComboBox_reaction()=="Reversible"){
							                	model.editMoteurReaction(wC1._r3rev, name+"_reverse");
							                }
							                else{
							                	ArrayList<String> suppr = new ArrayList<String>();
							                	suppr.add(name+"_reverse");
							                	model.delMoteurReaction(suppr);
							                }
							            }
							        }							
							 break;
							 }
						 }
						 if (test==false){
						 wC1.WndEditBehaviour_load(cpt, null);
						 wC1.setVisible(true);
						 String r = wC1.getDialogResult();
					        if (r != null) {
					            if (r.equals("OK")) {
					                model.editMoteurReaction(wC1._r3,name);
					                if (wC1.getComboBox_reaction()=="Reversible"){
					                	model.addMoteurReaction(wC1._r3rev);
					                }
					            }
					        }
						 }
					 }					 					
				 }
	        }
	    }
	 
	 	/**
	 	 * Remove a Behavior
	 	 * @param tab 
	 	 */
	    public void delBehaviour(int[] tab) {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        ArrayList<String> reactions = new ArrayList<>();
	        for (int k = tab.length - 1; k >= 0; k--) {
	            int i = tab[k];
	            if (i >= 0) {
	                String name = (String) env.getDataGridView_comportements().getModel().getElementAt(i);
	                reactions.add(name);
	                if (model.getBehaviour(name).getType_behavior() == 2){
	                	reactions.add(name+"NS");
	                }
	            }
	        }
	        model.delMoteurReaction(reactions);
	    }

	   /**
	    * Remove an Entity
	    * @param tab
	    */
	    public void delEntity(int[] tab) {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }
	        ArrayList<String> entities = new ArrayList<>();
	        for (int k = tab.length - 1; k >= 0; k--) {
	            int i = tab[k];
	            if (i >= 0) {
	                String name = UtilDivers.str_originale((String) env.getDataGridView_entites().getModel().getElementAt(i));
	                entities.add(name);
	            }
	        }
	        model.delProtoReaxel(entities);
	        if (!simulator.isStopped()) {
	            simulator.ProtoReaxelDeleted(entities);
	        }
	    }
	    
	    /**
	     * Add the Reaxels of an Entity randomly into a zone
	     * @param bottom_rightX : coordinate
	     * @param bottom_rightY : coordinate
	     * @param top_leftX : coordinate
	     * @param top_leftY : coordinate
	     * @param z : depth
	     */
	    public void randomlyPopulate(int bottom_rightX, int bottom_rightY, int top_leftX, int top_leftY, int z) {
	        int num_col = env.getDataGridView_entites().getSelectedIndex();
	        if (num_col >= 0) {
	            String etiquette = UtilDivers.str_originale(env.getDataGridView_entites().getModel().getElementAt(num_col).toString());
	            ArrayList<UtilPoint3D> presents = this.instancesInSelection(top_leftX, top_leftY, bottom_rightX, bottom_rightY, z);
	            int max = (Math.max(top_leftX, bottom_rightX) - Math.min(top_leftX, bottom_rightX))
	                    * (Math.max(top_leftY, bottom_rightY) - Math.min(top_leftY, bottom_rightY)) - presents.size();
	            if (max > 0) {
	                if (simulator.isRunning()) {
	                    this.pauseSimulation();
	                }
	                String res;
	                int nbr = max + 1;
	                while (nbr > max) {
	                    if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
	                        res = JOptionPane.showInputDialog(env, "Combien d'entités " + etiquette + " voulez-vous ajouter ? Max : " + max, "Ajouter aléatoirement", JOptionPane.OK_CANCEL_OPTION);
	                    } else {
	                        res = JOptionPane.showInputDialog(env, "How many " + etiquette + " entities do you want to add ? Max : " + max, "Populate randomly", JOptionPane.OK_CANCEL_OPTION);
	                    }
	                    if (res != null) {
	                        nbr = (int) Double.parseDouble(res);
	                        if (nbr > max) {
	                            if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
	                                JOptionPane.showMessageDialog(env, "Nombre d'entités supérieur au maximum possible !", "Attention !", JOptionPane.ERROR_MESSAGE);
	                            } else {
	                                JOptionPane.showMessageDialog(env, "The entities number is greater than the maximum !", "Warning !", JOptionPane.ERROR_MESSAGE);
	                            }
	                        } else {
	                            ArrayList<UtilPoint3D> points = this.populate(top_leftX, top_leftY, bottom_rightX, bottom_rightY, z, nbr, presents);
	                            addEntityInstances(points);
	                        }
	                    } else {
	                        nbr = 0;
	                    }
	                }
	            }
	        }
	    }

	    /**
	     * Select Reaxels into a zone
	     * @param top_leftX : coordinate
	     * @param top_leftY : coordinate
	     * @param bottom_rightX : coordinate
	     * @param bottom_rightY : coordinate
	     * @param z : depth
	     * @return the list of Reaxels
	     */
	    protected ArrayList<UtilPoint3D> instancesInSelection(int top_leftX, int top_leftY, int bottom_rightX, int bottom_rightY, int z) {
	        ArrayList<UtilPoint3D> points = new ArrayList<>();
	        AllInstances_NetMDyn instances;
	        if (simulator.isRunning()) {
	            instances = simulator.getInstances();
	        } else {
	            instances = model.getInstances();
	        }

	        for (int i = Math.min(top_leftX, bottom_rightX); i < Math.max(top_leftX, bottom_rightX); i++) {
	            for (int j = Math.min(top_leftY, bottom_rightY); j < Math.max(top_leftY, bottom_rightY); j++) {
	                InstanceReaxel r = instances.getFast(i, j, z);
	                if (r != null) {
	                    points.add(new UtilPoint3D(r.getX(), r.getY(), r.getZ()));
	                }
	            }
	        }
	        return points;
	    }

	    /**
	     * Populate the zone with Reaxels
	     * @param top_leftX : coordinate
	     * @param top_leftY : coordinate
	     * @param bottom_rightX : coordinate
	     * @param bottom_rightY : coordinate
	     * @param z : depth
	     * @param nbr : number of Reaxels
	     * @param existing : number of present Reaxels
	     * @return the list of Reaxels
	     */
	    protected ArrayList<UtilPoint3D> populate(int top_leftX, int top_leftY, int bottom_rightX, int bottom_rightY, int z, int nbr, ArrayList<UtilPoint3D> existing) {
	        ArrayList<UtilPoint3D> points = new ArrayList<>();
	        int maxX = Math.max(top_leftX, bottom_rightX);
	        int minX = Math.min(top_leftX, bottom_rightX);
	        int maxY = Math.max(top_leftY, bottom_rightY);
	        int minY = Math.min(top_leftY, bottom_rightY);
	        for (int i = 0; i < nbr; i++) {
	            int x = RandomGen.getInstance().nextInt(maxX - minX) + minX;
	            int y = RandomGen.getInstance().nextInt(maxY - minY) + minY;
	            UtilPoint3D p = new UtilPoint3D(x, y, z);
	            if ((points.contains(p)) || (existing.contains(p))) {
	                i = i - 1;
	            } else {
	                points.add(p);
	            }
	        }
	        return points;
	    }

	    /**
	     * Delete the Reaxel at these coordinates
	     * @param x : coordinate
	     * @param y : coordinate
	     * @param z : coordinate
	     */
	    public void removeEntityInstance(int x, int y, int z) {
	        ArrayList<UtilPoint3D> points = new ArrayList<>();
	        UtilPoint3D point = new UtilPoint3D(x, y, z);
	        points.add(point);
	        RemoveCommand_NetMDyn command = new RemoveCommand_NetMDyn(model, simulator, point);
	        if (simulator.isStopped()) {
	            command.setOpposite(new AddCommand_NetMDyn(model, simulator, points, model.getType(x, y, z)));
	        } else {
	            command.setOpposite(new AddCommand_NetMDyn(model, simulator, points, simulator.getType(x, y, z)));
	        }
	        this.memorizeCommand(command);
	        command.execute();
	    }
	    
	    /**
	     * Clear the Reaxels and the Compartments of the Environment
	     */
	    public void clearEnvironment() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        int res = 0;
	        if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
	            res = JOptionPane.showConfirmDialog(env, "Voulez-vous vider tout l'environnement ?", "Attention !", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            res = JOptionPane.showConfirmDialog(env, "Do you want to clean all the environment ?", "Warning !", JOptionPane.INFORMATION_MESSAGE);
	        }

	        if (res == JOptionPane.YES_OPTION) {
	            model.clear_OnlyCleanable();
	            this.stopWithoutAsking();
	        }
	    }
	    
	    /**
	     * Load a Model
	     * @param nameSaved : name of the file
	     * @throws XMLStreamException
	     * @throws IOException
	     */
	    public void loadModel(String nameSaved) throws XMLStreamException, IOException {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        UtilFileFilter filtre = new UtilFileFilter("NetMDyn", "nbd");
	        UtilFileFilter filtre2 = new UtilFileFilter("sbml", "xml");
	        File file = FileSaverLoader_NetMDyn.chooseFileToLoad(nameSaved, filtre,filtre2);
	        String extensionFile=file.getPath().substring( file.getPath().indexOf('.'));
	        System.out.println(extensionFile);
	        if ((file != null)&&(extensionFile.equals(".nbd"))) {
	        	System.out.println(extensionFile);
	            env.setNom_sauvegarde(UtilDivers.removeExtension(file.getPath()));
	            this.stopWithoutAsking();
	            model.load(env, file.getPath());
	        }
	        if ((file != null)&&(extensionFile.equals(".xml"))){
	        	System.out.println(extensionFile);
	        	SBMLReader slread = new SBMLReader();
	    		SBMLDocument document = slread.readSBML(file);
	    		MetaboliteVisualizer visualize = new MetaboliteVisualizer(document);
	    		SbmlParser parser=new SbmlParser();
	    		ControlerMetabolite c =new ControlerMetabolite(visualize, parser, document,env,model) ;
	    		visualize.setController(c);
	        }
	    }

	    /**
	     * Export the recorded curves of the simulation. Called by Environment.
	     *
	     * @param nameSaved : name of the simulation
	     * @param curves : curves recorded from the Environment
	     */
	    public void exportCurves(String nameSaved, SimulationCurves curves) {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        File file = FileSaverLoader.chooseFileToSave(nameSaved, "R files, CSV (Excel) files - .r by default", new String[]{"r", "csv"});

	        if (file != null) {
	            curves.export(file.getPath(), model.getEntitiesNames());
	        }
	    }
	    
	    /**
	     * Save the Model
	     * @param nameSaved : name of the saved file
	     * @return if the file has been saved (0 if success, -1 if not)
	     */
	    public int saveModel(String nameSaved) {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        File file = FileSaverLoader.chooseFileToSave(nameSaved, "NetMDyn", new String[]{"nbd"});

	        if (file != null) {
	            env.setNom_sauvegarde(UtilDivers.removeExtension(UtilDivers.fichier(file.getPath())));
	            model.save(env, file.getPath(), file.getParent());
	            return 0;
	        } else {
	            return -1;
	        }
	    }

	    /**
	     * Change the environment parameters (size, image ...). Called by Environment
	     */
	    public void updateEnvParameters() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        WndEditEnvironnement w = new WndEditEnvironnement(env.getParameters());
	        w.setVisible(true);

	        if (w.getDialogResult().equals("OK")) {
	            model.clearEnvironment();
	            model.setParameters(w.getParameters());
	            this.stopWithoutAsking();
	        }
	    }
	    
	    /**
	     * Play the Simulation
	     */
	    public void play() {
	        if (simulator.isRunning() && !simulator.isPause()) { // Pause of the simulation
	            this.pauseSimulation();
	        } else if (!simulator.isRunning() && simulator.isPause()) { // Run the simulation, pause before
	            env.unpause_simulation();
	            simulator.setPause(false);
	        } else { // Run the simulation, stop before
	            env.simulationStarted();
	            simulator.start();
	        }
	    }

	    /**
	     * Launch just one step of simulation. Called by Environment
	     */
	    public void play_one() {
	        if (!simulator.isRunning()) {
	            simulator.play_one();
	        }
	    }
	    
	    /**
	     * Stop the Simulation
	     */
	    public void stop() {
	        if (env.isFreezed()) {
	            adjustmentStopped(false);
	        }
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        if (simulator.getTime() != 0) {
	            int res = 0;
	            if (Lang.getInstance().getLang().equals("FR")) {
	                res = JOptionPane.showConfirmDialog(env, "Voulez-vous vraiment relancer la simulation depuis le debut ?", "Question", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                res = JOptionPane.showConfirmDialog(env, "Do you really want to relaunch the simulation from the beginning ?", "Question", JOptionPane.INFORMATION_MESSAGE);
	            }

	            if (res == JOptionPane.YES_OPTION) {
	                stopWithoutAsking();
	            }
	        }
	    }
	    
	    /**
	     * Stop the adjustment
	     */
	    public void adjustmentStopped(boolean finished) {
	        env.freeze(false);
	        stopWithoutAsking();
	        if (!finished) {
	        } else {
	            int res;
	        }
	    }
	    
	    /**
	     * Stop automatically the simulation
	     */
	    public void stopWithoutAsking() {
	        if (simulator.getTime() != 0) {
	            env.stopSimulation();
	            simulator.stop();
	        }
	    }

	    /**
	     * Pause the simulation in both the Simulator and the Environment.
	     */
	    protected void pauseSimulation() {
	        env.pauseSimulation();
	        simulator.setPause(true);
	    }
	    
	    /**
	     * Export all
	     */
	    public void export() {
	        String str_model = "\\fs32 \\b Entites:\n\\par\n\\b0";

	        // Entities
	        ArrayList<Entity_NetMDyn> lstc = model.getListManipulesNoeuds();
	        for (Entity_NetMDyn cli : lstc) {
	            str_model += cli.getEtiquettes() + "\\b:\\b0";
	            str_model += " 1/2 Vie =";
	            if (cli.DemieVie > 0) {
	                str_model += ((Double) cli.DemieVie).toString();
	            } else {
	                str_model += "+00";
	            }
	            str_model += "\n\\par\n";
	        }
	        str_model += "\n\\par\n";

	        // Behaviors
	        str_model += "\\b Comportements:\n\\par\n\\b0";

	        List<Behavior_NetMDyn> lst = model.getListManipulesReactions();
	        for (Behavior_NetMDyn reac : lst) {
	            str_model += reac.getEtiquettes() + " \\b : \\b0 ";
	            str_model += reac._reactifs.get(0);
	            int nb_reactifs = 0;
	            for (int r = 1; r < 3; r++) {
	                if (reac._reactifs.get(r) != null) {
	                    if (!reac._reactifs.get(r).equals("*")) {
	                        str_model += " \\b+ \\b0 " + reac._reactifs.get(r);
	                        nb_reactifs++;
	                    }
	                }
	            }
	            str_model += " \\b=" + ((Double) reac.get_k()).toString() + "=> \\b0 ";
	            if (!reac._produits.get(0).equals("")) {
	                str_model += reac._produits.get(0);
	            } else {
	                str_model += "_";
	            }
	            for (int p = 1; p < 3; p++) {
	                String prod = "";
	                if (reac._produits.get(p) != null) {
	                    if (!reac._produits.get(p).equals("-")) {
	                        prod = " \\b+ \\b0 " + reac._produits.get(p);
	                    }
	                }
	                if (prod.equals("") && p < nb_reactifs) {
	                    prod = " \\b+\\b0 _";
	                }
	                str_model += prod;
	            }
	            str_model += "\n\\par\n";
	        }
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        WndDescriptionSimulation w = new WndDescriptionSimulation();
	        w.jEditorPane1.setContentType("text/rtf");
	        w.jEditorPane1.setText("{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1036{\\fonttbl{\\f0\\fnil\\fcharset0 Microsoft Sans Serif;}}" + str_model);
	        w.setVisible(true);
	    }

	    /**
	     * Explanations
	     */
	    public void about() {
	        WndAbout wnd = new WndAbout();
	        wnd.setVisible(true);
	    }

	    /**
	     * Record an animation
	     */
	    public void record_animation() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }

	        String nomFichier = "film.gif";
	        File file = FileSaverLoader.chooseFileToSave(nomFichier, "Animated Gif", new String[]{"gif"});

	        if (file != null) {
	            nomFichier = file.getPath();
	            int reponse;
	            if (Lang.getInstance().getLang().equals("FR")) {
	                reponse = JOptionPane.showConfirmDialog(env, "Voulez-vous enregistrer les courbes en plus ?");
	            } else {
	                reponse = JOptionPane.showConfirmDialog(env, "Do you want to record the curves as well ?");
	            }

	            if (reponse == JOptionPane.OK_OPTION || reponse == JOptionPane.YES_OPTION) {
	                env.setAnimation_courbes(true);
	            } else {
	                env.setAnimation_courbes(false);
	            }
	            if (nomFichier.endsWith(".gif")) {
	                UtilAnimatedGifEncoder gif = new UtilAnimatedGifEncoder();
	                gif.setFrameRate(30);
	                gif.start(nomFichier);
	                gif.setFrameRate(30);
	                env.setAnimation_gif(gif);
	            }
	        } else {
	            env.unselectCheckBoxAvi();
	        }
	    }

	    /**
	     * Change the speed of the simulation
	     * @param value : the speed of the simulation
	     */
	    public void changeSpeed(int value) {
	        int v;
	        switch (value) {
	            case 0:
	                v = 1000;
	                break;
	            case 1:
	                v = 500;
	                break;
	            case 2:
	                v = 100;
	                break;
	            default:
	                v = 1;
	                break;
	        }
	        simulator.setSpeed(v);
	    }

	    /**
	     * Create a new Model
	     */
	    public void newModel() {
	        if (simulator.isRunning()) {
	            this.pauseSimulation();
	        }
	        int res = 0;
	        if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
	            res = JOptionPane.showConfirmDialog(env, "Voulez-vous vraiment créer une nouvelle simulation ?", "Question", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            res = JOptionPane.showConfirmDialog(env, "Do you really want to create a new simulation ?", "Question", JOptionPane.INFORMATION_MESSAGE);
	        }

	        if (res == JOptionPane.YES_OPTION) {
	            this.stopWithoutAsking();
	            model.newModel();
	        }
	    }

	    /**
	     * Select Reaxels at these coordinates
	     * @param x : coordinate
	     * @param y : coordinate
	     * @param z : coordinate
	     */
	    public void select(int x, int y, int z) {
	        if (simulator.isStopped()) {
	            InstanceReaxel_NetMDyn r = model.getInstances().getFast(x, y, z);
	            if (r == null) {
	                model.unselect(env.getCubes_selectionnes());
	                env.setCubes_selectionnes(new ArrayList<InstanceReaxel_NetMDyn>());
	            } else if (r.isSelectionne()) {
	                model.unselect(x, y, z);
	                env.unselect(r);
	            } else {
	                env.addCube_selectionnes(r);
	                model.select(x, y, z);
	            }
	        }
	    }

	    /**
	     * Move the selected Reaxels at these coordinates
	     * @param _cubes_selectionnes : selected Reaxels
	     * @param new_x : new coordinate
	     * @param new_y : new coordinate
	     * @param new_z : new coordinate
	     */
	    public void deplacer(ArrayList<InstanceReaxel_NetMDyn> _cubes_selectionnes, int new_x, int new_y, int new_z) {
	        model.deplacer(_cubes_selectionnes, new_x, new_y, new_z);
	        env.setCubes_selectionnes(new ArrayList<InstanceReaxel_NetMDyn>());
	    }

	    /**
	     * Add a listener based on the keyboard
	     * @param f
	     */
	    protected void addKeyListener(JFrame f) {
	        final Controller_NetMDyn controller = this;
	        final String space = "Space";
	        InputMap map = f.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), space);
	        ActionMap action = f.getRootPane().getActionMap();
	        action.put(space, new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                controller.play();
	            }
	        }
	        );

	        final String escape = "Escape";
	        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), escape);
	        action.put(escape, new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                controller.stop();
	            }
	        }
	        );

	        final String undo = "Undo";
	        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), undo);
	        action.put(undo, new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                controller.undo();
	            }
	        }
	        );
	    }

	    /**
	     * Get the Model linked to this Controller
	     * @return the Model
	     */
	    public Model_NetMDyn getModel() {
	        return model;
	    }

	    /**
	     * Get the Simulator linked to this Controller
	     * @return the Simulator
	     */
	    public Simulator_NetMDyn getSimulator() {
	        return simulator;
	    }

	    /**
	     * Undo a Command
	     */
	    protected void undo() {
	        if (lastCommand.size() > 0) {
	            lastCommand.get(lastCommand.size() - 1).undo();
	            lastCommand.remove(lastCommand.size() - 1);
	        }
	    }

	    /**
	     * Memorize a Command
	     * @param command
	     */
	    protected void memorizeCommand(Command command) {
	        lastCommand.add(command);
	        if (lastCommand.size() > maxMemory) {
	            lastCommand.remove(0);
	        }
	    }

	    /**
	     * Get the Environment linked to the Controller
	     * @return the Environment
	     */
	    public Environment_NetMDyn getEnv() {
	        return env;
	    }

	/**
	 * Remove an Entity
	 * @param name : name of the Entity
	 * @param center : center of the Reaxel
	 * @param radius 
	 */
    public void enleverMauvaisEntite(String name, UtilPoint3D center, int radius){
    	ArrayList<UtilPoint3D> listAEnlever= new ArrayList<UtilPoint3D>();
		AllInstances_NetMDyn allInstances = model.getInstances();
		for (InstanceReaxel_NetMDyn reaxel : allInstances){
			if (!(reaxel.get_compartment().equals(name))){
				int x = reaxel.getX();
				int y = reaxel.getY();
				int z = reaxel.getZ();
				int distance = (int) Math.sqrt(Math.pow(x-center.x, 2) + Math.pow(y-center.y, 2));
				if (radius > distance){
					listAEnlever.add(new UtilPoint3D(x,y,z));
				}
			}
		}
		for(UtilPoint3D point : listAEnlever){
			removeEntityInstance(point.x, point.y,point.z);
		}
    }
    
    /**
     * Remove an old Entity
     * @param name : name of the Entity
     * @param new_center : center of the new thing
     * @param new_radius : radius of the new thing
     */
    public void enleverAncienneEntite(String name, UtilPoint3D new_center, int new_radius){
    	ArrayList<UtilPoint3D> listAEnlever= new ArrayList<UtilPoint3D>();
		AllInstances_NetMDyn allInstances = model.getInstances();
		for (InstanceReaxel_NetMDyn reaxel : allInstances){
			if ((reaxel.get_compartment().equals(name))){
				int x = reaxel.getX();
				int y = reaxel.getY();
				int z = reaxel.getZ();
				int new_distance = (int) Math.sqrt(Math.pow(x-new_center.x, 2) + Math.pow(y-new_center.y, 2));
				if (new_radius < new_distance){
					listAEnlever.add(new UtilPoint3D(x,y,z));
				}
			}
		}
		for(UtilPoint3D point : listAEnlever){
			removeEntityInstance(point.x, point.y,point.z);
		}
    }
    
    /**
     * Remove a Compartment
     * @param name : name of the compartment
     */
    public void enleverEntiteCompartment(String name){
    	ArrayList<UtilPoint3D> listAEnlever= new ArrayList<UtilPoint3D>();
		AllInstances_NetMDyn allInstances = model.getInstances();
		for (InstanceReaxel_NetMDyn reaxel : allInstances){
			if ((reaxel.get_compartment().equals(name))){
				listAEnlever.add(new UtilPoint3D(reaxel.getX(),reaxel.getY(),reaxel.getZ()));
			}
		}
		for(UtilPoint3D point : listAEnlever){
			removeEntityInstance(point.x, point.y,point.z);
		}
    }
    /**
     * Add a reaction
     */
 public void addReaction(){
        WndReactionGlobal wC = new WndReactionGlobal(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wC.setVisible(true);
        if (wC.getDialogResult().equals("OK") && !wC.getTextBoxName().equals("")) {
            String equation = wC.getTextBoxReaction();
            String compartment = wC.getComboBox_compartment();
            String enzymeName = wC.getTextBoxName();
            Boolean reverse = wC.getjCheckBox_reversible();
            String[] typeS = {wC.getComboBox_TypeS1(), wC.getComboBox_TypeS2()};
            String[] typeP = {wC.getComboBox_TypeP1(), wC.getComboBox_TypeP2()};
            Double kcat = (Double) Double.parseDouble(wC.getTextBoxK1());
            Double kcat_1 = (Double) Double.parseDouble(wC.getTextBoxKmoins1());
            
            String []substratsTmp;
            String []produitsTmp;
            
            substratsTmp = equation.split("=")[0].split("\\+");
            produitsTmp = equation.split("=")[1].split("\\+");
            
            ArrayList<String> listSubstrats = new ArrayList<String>();
            for (int i = 0; i<substratsTmp.length; i++){
                listSubstrats.add(substratsTmp[i]);
                Boolean testExistence = false;
                for (Entity_NetMDyn ent: model.getListManipulesNoeuds()){
                	if (ent.getEtiquettes().equals(substratsTmp[i])){
                		testExistence = true;
                	}
                }
                if(testExistence){
                	continue;
                }
                addEntityReaction(substratsTmp[i], compartment, true);
                addBehaviorMoveReaction(substratsTmp[i], typeS[i] );
            }
            
            if (listSubstrats.size() == 1){
                listSubstrats.add("*");
            }
            
            addEntityReaction(enzymeName, compartment, true);
            addBehaviorMoveReaction(enzymeName, "Protéine");
            
            ArrayList<String> listProduits = new ArrayList<String>();
            for (int i = 0; i<produitsTmp.length; i++){
                listProduits.add(produitsTmp[i]);
                Boolean testExistence = false;
                for (Entity_NetMDyn ent: model.getListManipulesNoeuds()){
                	if (ent.getEtiquettes().equals(produitsTmp[i])){
                		testExistence = true;
                	}
                }
                if(testExistence){
                	continue;
                }
                addEntityReaction(produitsTmp[i], compartment, true);
                addBehaviorMoveReaction(produitsTmp[i], typeP[i]);
            }
            
            if (listProduits.size() == 1){
                listProduits.add("-");
            }
            
            if (reverse){
            	addBehaviorReaction(enzymeName, "Reversible", listSubstrats, listProduits, kcat, kcat_1);
            }else{
            	addBehaviorReaction(enzymeName, "Irreversible", listSubstrats, listProduits, kcat, 0.0);
            }
        }	
    }
 /**
  * Add an Entity in the Reaction
  * @param entName : name of the Entity
  * @param comp : name of the Compartment
  * @param visible 
  */
    public void addEntityReaction(String entName, String comp, boolean visible){
        WndEditNoeud_NetMDyn wN = new WndEditNoeud_NetMDyn(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wN.setVisible(false);
        wN.WndCliValue_Load(null);
        
        wN.setjCheckBox_vidable(true);
        wN.setTextBox1(entName);
        wN.setComboBox_compartment(comp);
        wN._cli._visibleDansPanel = visible;
        
        wN.button_OKActionPerformed(null);
        model.addProtoReaxel(wN._cli);
    }
    
    /**
     * Add a move reaction
     * @param entName : name of the Entity
     * @param type : type of the Behavior
     */
    public void addBehaviorMoveReaction(String entName, String type){
        WndEditMvt wR = new WndEditMvt(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wR.WndEditMvt_load(null);
        wR.setVisible(false);
        
        wR.setComboBox_entity(entName);
        wR.setComboBox_mvt(type);
        
        wR.button_OKActionPerformed(null);
        model.addMoteurReaction(wR._r3);
    }
    
    /**
     * Add a Behavior Reaction
     * @param name : name of the Behavior
     * @param reverse : is the reaction reversible or not
     * @param listSubstrats : list of the reagents of the reaction
     * @param listProduits : list of the products of the reaction
     * @param kcat : Kinetic Law
     * @param kcat_1 : Kinetic Law
     */
    public void addBehaviorReaction(String name, String reverse, ArrayList<String> listSubstrats, ArrayList<String> listProduits, Double kcat, Double kcat_1){
    	WndEditBehaviour wR = new WndEditBehaviour(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wR.WndEditBehaviour_load(null, null);
        wR.setVisible(false);
        
        wR.setTextBoxName(name);
        wR.setComboBox_S1(listSubstrats.get(0));
        wR.setComboBox_S2(listSubstrats.get(1));
        wR.setComboBox_P1(listProduits.get(0));
        wR.setComboBox_P2(listProduits.get(1));
        wR.setComboBox_reaction(reverse);
        wR.setTextBoxKCst(kcat.toString());
        wR.setTextBoxKCst2(kcat_1.toString());
        
        wR.button_OKActionPerformed(null);
        model.addMoteurReaction(wR._r3);
        if (reverse.equals("Reversible")){
        	model.addMoteurReaction(wR._r3rev);
        }
    }
    
    /**
     * Add a compartment
     */
    public void addCompartment() {
        if (simulator.isRunning()) {
            this.pauseSimulation();
        }
   
        WndEditCompartment wC = new WndEditCompartment(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wC.WndCliValue_Load(null);
        wC.setVisible(true);
        if (wC.getDialogResult().equals("OK") && !wC._cli.getEtiquettes().equals("")) {
        	addCompartmentMembrane(wC._cli);
        	if (wC._cli.getCenter().x != 0 && wC._cli.getCenter().x != 0 && wC._cli.getRadius() != 0){
            	UtilPoint3D center = wC._cli.getCenter();
            	int radius = wC._cli.getRadius();
            	ArrayList<UtilPoint3D> lst_pts = UtilPoint3D_NetMDyn.BresenhamRond3D(center.x,center.y, center.z, radius, env.getTailleZ());
            	if(model.verifCollision(wC._cli.getEtiquettes(),lst_pts )){
            		if(center.x - radius >= 0 && center.y - radius >= 0 && center.x + radius <= env.getTailleX() && center.y + radius <= env.getTailleY()){
            			enleverMauvaisEntite(wC._cli.getEtiquettes(), center, radius);
            			delMembrane(lst_pts);
            			addEntityInstances2(wC._cli, lst_pts);
            		}else{
            			wC.setTextBoxCenterX("0");
                        wC.setTextBoxCenterY("0");
                        wC.setTextBoxRadius("0");
                        wC.button_OKActionPerformed(null);
                		JOptionPane jop = new JOptionPane();
                		if (Lang.getInstance().getLang().equals("FR")) {
                			jop.showMessageDialog(null, "Vous ne pouvez créer un compartiment en-dehors de l'environnement", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                		}
                		else{
                			jop.showMessageDialog(null, "You can't create a compartment outside the Envionment", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                		}
                		
            		}
            	}else{
            		wC.setTextBoxCenterX("0");
                    wC.setTextBoxCenterY("0");
                    wC.setTextBoxRadius("0");
                    wC.button_OKActionPerformed(null);
            		JOptionPane jop = new JOptionPane();
            		if (Lang.getInstance().getLang().equals("FR")) {
            			jop.showMessageDialog(null, "Vous ne pouvez créer un compartiment sur un autre", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            		}
            		else{
            			jop.showMessageDialog(null, "You can't create a compartment on an other one", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            		}
            		
            	}
           	}   
        	model.addCompartment(wC._cli);
        }
    }
    
    /**
     * Add the membrane of a compartment
     * @param comp : the name of the compartment
     */
    public void addCompartmentMembrane(Compartment comp){
        WndEditNoeud_NetMDyn wN = new WndEditNoeud_NetMDyn(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wN.WndCliValue_Load(null);
        wN.setVisible(false);
        wN.setjCheckBox_vidable(comp.Vidable);
        wN.setTextBox1("Membrane_"+comp.getEtiquettes());
        wN.setButtonCouleur(comp.Couleur);
        wN.setComboBox_compartment(comp.getEtiquettes());
        wN.setVisible(false);
        wN.button_OKActionPerformed(null);
        model.addProtoReaxel(wN._cli);
    }
   
    /**
     * Edit the grapgic of a compartment
     * @param index
     * @param center
     * @param radius
     */
    public void editCompartmentGraphique(int index, UtilPoint3D center, int radius){
    	String name = (String) env.getDataGridView_Compartment().getModel().getElementAt(index);
        Compartment cpt = model.getCompartment(name);
        int old_centerX = cpt.getCenter().x;
        int old_centerY = cpt.getCenter().y;
        int old_centerZ = cpt.getCenter().z;
        int old_radius = cpt.getRadius();
        WndEditCompartment wC = new WndEditCompartment(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wC.WndCliValue_Load(cpt);
        wC.setVisible(false);
        ArrayList<UtilPoint3D> old_lst_pts = UtilPoint3D_NetMDyn.BresenhamRond3D(old_centerX, old_centerY, old_centerZ, old_radius, getEnv().getTailleZ());
        ArrayList<UtilPoint3D> lst_pts = UtilPoint3D_NetMDyn.BresenhamRond3D(center.x,center.y, center.z, radius, env.getTailleZ());
    	if (model.verifCollision(wC._cli.getEtiquettes(), lst_pts)){
    		if(center.x - radius >= 0 && center.y - radius >= 0 && center.x + radius <= env.getTailleX() && center.y + radius <= env.getTailleY()){
    			wC.setTextBoxCenterX(Integer.toString(center.x));
    			wC.setTextBoxCenterY(Integer.toString(center.y));
    			wC.setTextBoxRadius(Integer.toString(radius));
    			wC.button_OKActionPerformed(null);
    			
    			enleverAncienneEntite(wC._cli.getEtiquettes(), center, radius);
    			delMembrane(old_lst_pts);
    			enleverMauvaisEntite(wC._cli.getEtiquettes(), center, radius);
    			delMembrane(lst_pts);
    			addEntityInstances2(wC._cli, lst_pts);
    		}else{
    			wC.setTextBoxCenterX(Integer.toString(old_centerX));
                wC.setTextBoxCenterY(Integer.toString(old_centerY));
                wC.setTextBoxRadius(Integer.toString(old_radius));
                wC.button_OKActionPerformed(null);
        		JOptionPane jop = new JOptionPane();
        		if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
        			jop.showMessageDialog(null, "Vous ne pouvez créer un compartiment en-dehors de l'environnement", "Information", JOptionPane.INFORMATION_MESSAGE, null);	
        		}
        		else{
        			jop.showMessageDialog(null, "You can't create a compartment outside the environment", "Information", JOptionPane.INFORMATION_MESSAGE, null);	
        		}
        		
    		}
    	}else{
    		wC.setTextBoxCenterX(Integer.toString(old_centerX));
            wC.setTextBoxCenterY(Integer.toString(old_centerY));
            wC.setTextBoxRadius(Integer.toString(old_radius));
            wC.button_OKActionPerformed(null);
    		JOptionPane jop = new JOptionPane();
    		if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
    			jop.showMessageDialog(null, "Vous ne pouvez créer un compartiment sur un autre", "Information", JOptionPane.INFORMATION_MESSAGE, null);
    		}
    		else{
    			jop.showMessageDialog(null, "You can't create a compartment on an other one", "Information", JOptionPane.INFORMATION_MESSAGE, null);
    		}
    		
    	}
    	model.editCompartment(wC._cli, name);
        editCompartmentMembrane(wC._cli, name);
    }
    
    /**
     * Check the membrane
     * @param index
     * @return true or false
     */
    public boolean verificationPourMembrane(int index){
        String name = (String) env.getDataGridView_Compartment().getModel().getElementAt(index);
        Compartment comp = model.getCompartment(name);
        UtilPoint3D center = comp.getCenter();
        if(center.x == 0 && center.y == 0){
        	return true;
        }
        else{
        	return false;
        }
    }
    
    /**
     * Delete the membrane
     * @param lst_pts : lists of the Reaxels of the membrane
     */
    public void delMembrane(ArrayList<UtilPoint3D> lst_pts){
    	for (UtilPoint3D point: lst_pts){
    		removeEntityInstance(point.x, point.y,point.z);
    	}
    }
    
    /**
     * Delete the compartment
     * @param tab
     */
    public void delCompartment(int[] tab) {
        if (simulator.isRunning()) {
            this.pauseSimulation();
        }
        ArrayList<String> compartments = new ArrayList<>();
        ArrayList<String> entities = new ArrayList<>();
        ArrayList<String> behaviors = new ArrayList<>();
        for (int k = tab.length - 1; k >= 0; k--) {
            int i = tab[k];
            if (i >= 0) {
                String name = (String) env.getDataGridView_Compartment().getModel().getElementAt(i);
                compartments.add(name);
                for (Entity_NetMDyn ent : model.getCopyListManipulesNoeuds()){
                	if(ent.getCompartment().equals(name)){
                		entities.add(ent.getEtiquettes());
                	}
                }
                for (Behavior_NetMDyn bev : model.getCopyListManipulesReactions()){
                	if(bev.getType_behavior() == 2 && (bev._reactifs.get(0).split("Membrane_")[1].equals(name) && bev._produits.get(0).split("Membrane_")[1].equals(name))){
                		behaviors.add(bev.getEtiquettes());
                	}
                }
            }
        }
        
        model.delCompartment(compartments);
        model.delProtoReaxel(entities);
        model.delMoteurReaction(behaviors);
        if (!simulator.isStopped()) {
            simulator.ProtoReaxelDeleted(entities);
        }
    }
    
    /**
     * Add reaxels of the Entity
     * @param points : lists of the Reaxels to add
     */
    public void addEntityInstances(ArrayList<UtilPoint3D> points) {
        int num_col = env.getDataGridView_entites().getSelectedIndex();
        if (num_col >= 0) {
        	ArrayList<UtilPoint3D> pointsVerifier = new ArrayList<UtilPoint3D>();
            String etiquette = UtilDivers.str_originale(env.getDataGridView_entites().getModel().getElementAt(num_col).toString());
            Entity_NetMDyn entity = model.getProtoReaxel(etiquette);
            String nameComp = entity.getCompartment();
            if (nameComp.equals("Cytosol")){
            	for(UtilPoint3D point : points){
            		boolean rep = true;
            		for (Compartment comp : model.getCopyListManipulesCompartment()){
            			int distance = (int) Math.sqrt(Math.pow(point.x-comp.getCenter().x, 2) + Math.pow(point.y-comp.getCenter().y, 2));
        				if (comp.getRadius() > distance){
        					rep = false;
        					break;
        				}	
            		}
            		if (rep){
            			pointsVerifier.add(point);
            		}
            	}
            }else{
            	for(UtilPoint3D point : points){
            		boolean rep = true;
            		for (Compartment comp : model.getCopyListManipulesCompartment()){
        				int distance = (int) Math.sqrt(Math.pow(point.x-comp.getCenter().x, 2) + Math.pow(point.y-comp.getCenter().y, 2));
            			if(comp.getEtiquettes().equals(nameComp)){
            				if (comp.getRadius() > distance){
            					rep = true;
            				}else{
            					rep = false;
            					break;
            				}
            			}else{
            				if (comp.getRadius() < distance){
            					rep = true;
            				}else{
            					rep = false;
            					break;
            				}
            			}
            		}
            		if (rep == false){
        				continue;
        			}else{
        				pointsVerifier.add(point);
        			}
            	}
            }
			AddCommand_NetMDyn command = new AddCommand_NetMDyn(model, simulator, pointsVerifier, etiquette);
			command.setOpposite(new RemoveCommand_NetMDyn(model, simulator, pointsVerifier));
			this.memorizeCommand(command);
			command.execute();
        }
    }
    
    public void addEntityInstances2(Compartment comp, ArrayList<UtilPoint3D> points) {
       String etiquette = "Membrane_"+comp.getEtiquettes();
       AddCommand_NetMDyn command = new AddCommand_NetMDyn(model, simulator, points, etiquette);
       command.setOpposite(new RemoveCommand_NetMDyn(model, simulator, points));
       this.memorizeCommand(command);
       command.execute();
    }
    


    public void editCompartment() {
        if (simulator.isRunning()) {
            this.pauseSimulation();
        }

        int i = env.getDataGridView_Compartment().getSelectedIndex();
        if (i >= 0) {
            String name = env.getDataGridView_Compartment().getSelectedValue().toString();
            Compartment cpt = model.getCompartment((String) env.getDataGridView_Compartment().getModel().getElementAt(i));
            int old_centerX = cpt.getCenter().x;
            int old_centerY = cpt.getCenter().y;
            int old_centerZ = cpt.getCenter().z;
            int old_radius = cpt.getRadius();
            WndEditCompartment wc = new WndEditCompartment(model.getListManipulesNoeuds(), model.getListManipulesReactions(), model.getListManipulesCompartment());
            wc.WndCliValue_Load(cpt);
            wc.setVisible(true);
            String r = wc.getDialogResult();
            if (r != null && r.equals("OK")) {
                UtilPoint3D center = wc._cli.getCenter();
                int radius = wc._cli.getRadius();
                if (old_centerX != center.x || old_centerY != center.y || old_radius != radius ){
                	ArrayList<UtilPoint3D> old_lst_pts = UtilPoint3D_NetMDyn.BresenhamRond3D(old_centerX, old_centerY, old_centerZ, old_radius, getEnv().getTailleZ());
                	ArrayList<UtilPoint3D> lst_pts = UtilPoint3D_NetMDyn.BresenhamRond3D(center.x, center.y, center.z, radius, getEnv().getTailleZ());
                	if(model.verifCollision(wc._cli.getEtiquettes(), lst_pts)){
                		if(center.x - radius >= 0 && center.y - radius >= 0 && center.x + radius <= env.getTailleX() && center.y + radius <= env.getTailleY()){
                			enleverAncienneEntite(wc._cli.getEtiquettes(), center, radius);
                			delMembrane(old_lst_pts);
                			enleverMauvaisEntite(wc._cli.getEtiquettes(), center, radius);
                			delMembrane(lst_pts);
                			addEntityInstances2(wc._cli, lst_pts);
                		}else{
                			wc.setTextBoxCenterX(Integer.toString(old_centerX));
                            wc.setTextBoxCenterY(Integer.toString(old_centerY));
                            wc.setTextBoxRadius(Integer.toString(old_radius));
                            wc.button_OKActionPerformed(null);
                    		JOptionPane jop = new JOptionPane();
                    		if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
                    			jop.showMessageDialog(null, "Vous ne pouvez créer un compartiment en-dehors de l'environnement", "Information", JOptionPane.INFORMATION_MESSAGE, null);	
                    		}
                    		else{
                    			jop.showMessageDialog(null, "You can't create a compartment outside the environment", "Information", JOptionPane.INFORMATION_MESSAGE, null);	
                    		}                		}
                	}else{
                		wc.setTextBoxCenterX(Integer.toString(old_centerX));
                        wc.setTextBoxCenterY(Integer.toString(old_centerY));
                        wc.setTextBoxRadius(Integer.toString(old_radius));
                        wc.button_OKActionPerformed(null);
                		JOptionPane jop = new JOptionPane();
                		if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
                			jop.showMessageDialog(null, "Vous ne pouvez créer un compartiment sur un autre", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                		}
                		else{
                			jop.showMessageDialog(null, "You can't create a compartment on an other one", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                		}                	}
                }
                
                model.editCompartment(wc._cli, name);
                editCompartmentMembrane(wc._cli, name);
                for (Entity_NetMDyn ent : model.getCopyListManipulesNoeuds()){
                	if (ent._compartment.equals(name)){
                		Entity_NetMDyn entTmp = model.getProtoReaxel(ent.getEtiquettes());
                		entTmp._compartment = wc._cli.getEtiquettes();
                		model.editProtoReaxel(entTmp, ent.getEtiquettes(), 0);
                	}
                }
            }
        }
        
    }
  
    /**
     * Edit the membrane of a compartment
     * @param comp : the wanted compartment
     * @param name : the name of the Entity
     */
    public void editCompartmentMembrane(Compartment comp, String name){
    	Entity_NetMDyn p = model.getProtoReaxel("Membrane_"+name);
    	
        WndEditNoeud_NetMDyn wN = new WndEditNoeud_NetMDyn(model.getListManipulesNoeuds(), model.getListManipulesReactions(),model.getListManipulesCompartment());
        wN.WndCliValue_Load(p);
        wN.setVisible(false);
        wN.setjCheckBox_vidable(comp.Vidable);
        wN.setTextBox1("Membrane_"+comp.getEtiquettes());
        wN.setButtonCouleur(comp.Couleur);
        wN.setComboBox_compartment(comp.getEtiquettes());
        wN.button_OKActionPerformed(null);
        model.editProtoReaxel(wN._cli, "Membrane_"+name, 0);
        if (!simulator.isStopped()) {
            simulator.ProtoReaxelEdited(p, name);
        }
        
    }

    /**
     * 
     */
	public void launchSelfAdjustment() {
	}
	
}
