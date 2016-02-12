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
 * WndEditTraverse.java
 *
 * Created on February 12 2016, 16:11
 */

package NetMDyn.ihm;

import java.awt.Dimension; // Width and height of a component (in integer precision) in a single object
import java.util.ArrayList; // Possible creation of tables

import javax.swing.JOptionPane; // Possible creation of dialog windows
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JOptionPane; // Possible creation of dialog windows

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.Compartment;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

/**
 * Edit of the Traverse in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WndEditTraverse extends javax.swing.JDialog {
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    public Behavior_NetMDyn _rNS = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior_NetMDyn> behaviors;
    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Compartment> compartment;
    
    /**
     * Initialization of a WndEditTraverse object
     * @param entities
     * @param behaviours
     * @param compartment
     */
    public WndEditTraverse(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    /**
     * Load a new traverse reaction
     * @param behavior
     */
    public void WndEditTraverse_load(Behavior_NetMDyn behavior){
    	if (behavior == null){
    		_r3 = new Behavior_NetMDyn();
    		_rNS = new Behavior_NetMDyn();

    	}else{
    		_r3 = behavior;
    		for(Behavior_NetMDyn bev : behaviors){
    			if (bev.getEtiquettes().equals(_r3.getEtiquettes()+"NS")){
    				_rNS = bev;
    			}
    		}
    		comboBox_compartment.setSelectedItem(_r3._reactifs.get(0).split("Membrane_")[1]);
        	comboBox_OriginEntity.setSelectedItem(_r3._reactifs.get(1));
        	comboBox_TargetEntity.setSelectedItem(_r3._produits.get(2));
        	
        	textBoxProba.setText(((Double) _r3.getProba()).toString());
    	}
    }
    
    /**
     * Initialization of parameters
     */
    private void initComponents() {
    	 jLabelNomOrigine = new javax.swing.JLabel();
    	 jLabelNomTarget = new javax.swing.JLabel();
    	 jLabelProba = new javax.swing.JLabel();
    	 jLabelCompartment = new javax.swing.JLabel();
    	 comboBox_OriginEntity = new javax.swing.JComboBox();
    	 comboBox_TargetEntity = new javax.swing.JComboBox();
    	 comboBox_compartment = new javax.swing.JComboBox();
    	 textBoxProba = new javax.swing.JTextField();
    	 
    	 jLabelPlaceDuTitre = new javax.swing.JLabel();
    	 jLabelTitre = new javax.swing.JLabel();
    	 
    	 button_OK = new javax.swing.JButton();
         button_CANCEL = new javax.swing.JButton();
         
         setAlwaysOnTop(true);
         setBackground(new java.awt.Color(204, 204, 255));
         setResizable(false);
         addKeyListener(new java.awt.event.KeyAdapter() {
             public void keyPressed(java.awt.event.KeyEvent evt) {
                 formKeyPressed(evt);
             }
         });
         getContentPane().setLayout(null);

         jLabelNomOrigine.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
        	 jLabelNomOrigine.setText("Entité d'origine");
         }
         else{
        	 jLabelNomOrigine.setText("Origin entity");
         }
         getContentPane().add(jLabelNomOrigine);
         jLabelNomOrigine.setBounds(10, 40, 100, 20);
         
        
         ArrayList<String> comps = new ArrayList<String>();
         comps.add("-");
         
         for(int i = 1; i< entities.size()+1; i++){
        	 boolean present = false;
        	 if (entities.get(i-1).getEtiquettes().contains("Membrane_")){
        		 continue;
        	 }        	
        	 comps.add(entities.get(i-1).getEtiquettes());        	 
         }
         
         String[] ent = new String[comps.size()];
         for (int i =0; i<comps.size();i++){
        	 ent[i]=comps.get(i);
         }
         
         comboBox_OriginEntity.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_OriginEntity.setModel(new javax.swing.DefaultComboBoxModel(ent));
         getContentPane().add(comboBox_OriginEntity);
         comboBox_OriginEntity.setBounds(120, 40, 150, 20);

         jLabelNomTarget.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N

         if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
             jLabelNomTarget.setText("Entité à l'arrivée");
         }
         else{
        	 jLabelNomTarget.setText("Target entity");
         }
         
         getContentPane().add(jLabelNomTarget);
         jLabelNomTarget.setBounds(10, 80, 100, 20);
         
         comboBox_TargetEntity.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_TargetEntity.setModel(new javax.swing.DefaultComboBoxModel(ent));
         getContentPane().add(comboBox_TargetEntity);
         comboBox_TargetEntity.setBounds(120, 80, 150, 20);
         
         jLabelCompartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
        	 jLabelCompartment.setText("Compartiment");
         }
         else{
        	 jLabelCompartment.setText("Compartment");
         }
         getContentPane().add(jLabelCompartment);
         jLabelCompartment.setBounds(10, 120, 100, 20);
         
         String[] compart = new String[compartment.size()+1];
         compart[0] = "-";
         for(int i = 1; i< compartment.size()+1; i++){
         	compart[i] = compartment.get(i-1).getEtiquettes();
         }
         
         comboBox_compartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_compartment.setModel(new javax.swing.DefaultComboBoxModel(compart));
         getContentPane().add(comboBox_compartment);
         comboBox_compartment.setBounds(120, 120, 150, 20);
         
         jLabelProba.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
             jLabelProba.setText("Probabilité");        	 
         }
         else{
             jLabelProba.setText("Probability");        	 
         }
         getContentPane().add(jLabelProba);
         jLabelProba.setBounds(10, 160, 60, 20);
         
         textBoxProba.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         textBoxProba.setText("0.5");
         textBoxProba.addKeyListener(new java.awt.event.KeyAdapter() {
             public void keyTyped(java.awt.event.KeyEvent evt) {
                 //textBoxNameKeyTyped(evt);
             }
         });
         getContentPane().add(textBoxProba);
         textBoxProba.setBounds(80, 160, 50, 20);
         
         button_OK.setBackground(new java.awt.Color(153, 255, 153));
         button_OK.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         button_OK.setText("OK");
         button_OK.addMouseListener(new java.awt.event.MouseAdapter() {
             public void mouseClicked(java.awt.event.MouseEvent evt) {
                 button_OKMouseClicked(evt);
             }
         });
         button_OK.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 button_OKActionPerformed(evt);
             }
         });
         getContentPane().add(button_OK);
         button_OK.setBounds(0, 200, 250, 30);

         button_CANCEL.setBackground(new java.awt.Color(255, 153, 153));
         button_CANCEL.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
        	 button_CANCEL.setText("Annuler");
         }
         else{
        	 button_CANCEL.setText("Cancel");
         }
         button_CANCEL.addMouseListener(new java.awt.event.MouseAdapter() {
             public void mouseClicked(java.awt.event.MouseEvent evt) {
                 button_CANCELMouseClicked(evt);
             }
         });
         button_CANCEL.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 button_CANCELActionPerformed(evt);
             }
         });
         getContentPane().add(button_CANCEL);
         button_CANCEL.setBounds(280, 200, 250, 30);
         
         jLabelTitre.setBackground(new java.awt.Color(153, 153, 255));
         jLabelTitre.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
         jLabelTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
         if (Lang.getInstance().getLang().equalsIgnoreCase("FR")) {
        	 jLabelTitre.setText("Traversée de membrane");
         }
         else{
        	 jLabelTitre.setText("Membrane crossing");
         }
         
         getContentPane().add(jLabelTitre);
         jLabelTitre.setBounds(0, 0, 533, 28);
         
         jLabelPlaceDuTitre.setBackground(new java.awt.Color(153, 153, 255));
         jLabelPlaceDuTitre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
         jLabelPlaceDuTitre.setOpaque(true);
         getContentPane().add(jLabelPlaceDuTitre);
         jLabelPlaceDuTitre.setBounds(0, 0, 533, 30);
         
         setSize(new java.awt.Dimension(533, 300));
         setLocationRelativeTo(null);
    }
    
    /**
     * Action when OK button is pushed
     * @param evt
     */
    private void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
    	if (getComboBox_OriginEntity().equals("-")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci de choisir une entité à déplacer", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please choose an entity to move.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    	
    	if (getComboBox_TargetEntity().equals("-")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci de choisir une entité à déplacer", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please choose an entity to move.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    	if (getComboBox_TargetEntity().equals(getComboBox_OriginEntity())) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Les deux entités ne peuvent pas être identiques", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Both entities can't be identical", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    	
    	if (getComboBox_compartment().equals("-")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci de choisir un compartiment", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please choose a compartment.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    	
    	boolean entite1=false;
    	boolean entite2=false;
    	for (Entity_NetMDyn entity : entities){
    		if (entity.getCompartment().equals(getComboBox_compartment()) && entity.getEtiquettes().equals(getComboBox_OriginEntity())){
    			entite1=true;
    		}
    		if (entity.getCompartment().equals(getComboBox_compartment()) && entity.getEtiquettes().equals(getComboBox_TargetEntity())){
    			entite2=true;
    		}
    	}
    		if (entite1==false && entite2==false){
    			if (Lang.getInstance().getLang().equals("FR")) {
        			JOptionPane.showMessageDialog(this, "Au moins l'un des entités doit appartenir au compartiment!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "At least one entity must be an element of the chosen compartment!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
    		}
    		if (entite1==true && entite2==true){
    			if (Lang.getInstance().getLang().equals("FR")) {
        			JOptionPane.showMessageDialog(this, "Les deux entités ne peuvent pas appartenir au compartiment!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Both entities can't be elements of the chosen compartment!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
    	}
    	
    	
    	try{
        	Double.parseDouble(textBoxProba.getText());
        }catch(Exception e){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "La probabilité de traverser la membrane doit être un nombre", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "The probability to cross the membrane must be a number.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
        	}        
        
        _r3._reactifs.clear();
        _r3._produits.clear();
        _r3._positions.clear();
    	_r3.setEtiquettes("Traverse_"+ comboBox_OriginEntity.getSelectedItem() + "_"+ comboBox_compartment.getSelectedItem());
    	_r3._reactifs.add("Membrane_" + (String) comboBox_compartment.getSelectedItem());
    	_r3._reactifs.add((String) comboBox_OriginEntity.getSelectedItem());
    	_r3._reactifs.add("0");
    	_r3._produits.add("Membrane_" + (String) comboBox_compartment.getSelectedItem());
    	_r3._produits.add("0");
    	_r3._produits.add((String) comboBox_TargetEntity.getSelectedItem());
    	_r3._positions.add("122222222");
    	_r3._positions.add("202100210");
    	_r3._positions.add("202100210");
    	_r3._positions.add("212101210");
    	_r3._positions.add("212101210");
    	_r3._positions.add("212101210");
    	_r3._positions.add("212101210");
    	_r3._positions.add("212101210");
    	_r3._positions.add("212101210");
    	_r3.setType_behavior(2);
    	_r3.setProba(Double.parseDouble(textBoxProba.getText()));
    	
    	
        _rNS._reactifs.clear();
        _rNS._produits.clear();
        _rNS._positions.clear();
    	_rNS.setEtiquettes("Traverse_"+ comboBox_OriginEntity.getSelectedItem() + "_"+ comboBox_compartment.getSelectedItem()+"NS");
    	_rNS._reactifs.add("Membrane_" + (String) comboBox_compartment.getSelectedItem());
    	_rNS._reactifs.add((String) comboBox_OriginEntity.getSelectedItem());
    	_rNS._reactifs.add("0");
    	_rNS._produits.add("Membrane_" + (String) comboBox_compartment.getSelectedItem());
    	_rNS._produits.add("0");
    	_rNS._produits.add((String) comboBox_TargetEntity.getSelectedItem());
    	_rNS._positions.add("122222222");
    	_rNS._positions.add("212001200");
    	_rNS._positions.add("212001200");
    	_rNS._positions.add("212101210");
    	_rNS._positions.add("212101210");
    	_rNS._positions.add("212101210");
    	_rNS._positions.add("212101210");
    	_rNS._positions.add("212101210");
    	_rNS._positions.add("212101210");
    	_rNS.setType_behavior(2);
    	_rNS.setProba(Double.parseDouble(textBoxProba.getText()));
    	_rNS._visibleDansPanel = false;
    	
    	
    	DialogResult = "OK";
    	setVisible(false);
    }
    
   

	private void button_OKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OKMouseClicked


    }//GEN-LAST:event_button_OKMouseClicked
    
    private void button_CANCELMouseClicked(java.awt.event.MouseEvent evt) {
    	
    }
    
    /**
     * Action when Cancel button is pushed
     * @param evt
     */
	private void button_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CANCELActionPerformed
        // TODO add your handling code here:
        this.DialogResult = new String("CANCEL");
        setVisible(false);
    }//GEN-LAST:event_button_CANCELActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:

        //if()
    }//GEN-LAST:event_formKeyPressed
    
    public String getComboBox_OriginEntity() {
		return (String) comboBox_OriginEntity.getSelectedItem();
	}

	public void setComboBox_OriginEntity(String comboBox_entity) {
		this.comboBox_OriginEntity.setSelectedItem(comboBox_entity);
	}
	
	public String getComboBox_TargetEntity() {
		return (String) comboBox_TargetEntity.getSelectedItem();
	}

	public void setComboBox_TargetEntity(String comboBox_mvt) {
		this.comboBox_TargetEntity.setSelectedItem(comboBox_mvt);
	}
	
	 public String getComboBox_compartment() {
			return (String) comboBox_compartment.getSelectedItem();
		}

		public void setComboBox_compartment(String comboBox_compartment) {
			this.comboBox_compartment.setSelectedItem(comboBox_compartment);
		}

	 public String getDialogResult() {
			return DialogResult;
		}

		public void setDialogResult(String dialogResult) {
			DialogResult = dialogResult;
		}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelNomOrigine;
    private javax.swing.JLabel jLabelNomTarget;
    private javax.swing.JLabel jLabelProba;
    private javax.swing.JLabel jLabelCompartment;
    private javax.swing.JComboBox comboBox_OriginEntity;
    private javax.swing.JComboBox comboBox_TargetEntity;
    private javax.swing.JComboBox comboBox_compartment;
   
	private javax.swing.JTextField textBoxProba;
    
    private javax.swing.JLabel jLabelPlaceDuTitre;
    private javax.swing.JLabel jLabelTitre;
    
    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_CANCEL;
    // End of variables declaration//GEN-END:variables

	public Double getTextBoxProba() {
		return Double.parseDouble(textBoxProba.getText());
	}

	public void setTextBoxProba(Double textBoxProba) {
		this.textBoxProba.setText(textBoxProba.toString());;
	}
    
}

