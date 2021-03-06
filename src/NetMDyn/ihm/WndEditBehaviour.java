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
 * WndEditBehaviour.java
 *
 * Created on February 12 2016, 15:52
 */

package NetMDyn.ihm;

import java.awt.Dimension;// Encapsulate the width and height of a component (in integer precision) in a single object.
import java.awt.FlowLayout; // A flow layout arranges components in a directional flow, much like lines of text in a paragraph
import java.util.ArrayList; // Possible creation of tables

import javax.swing.JOptionPane; // Possible creation of dialog windows

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.Compartment;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

/**
 * Edit of the Behavior in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WndEditBehaviour extends javax.swing.JDialog{
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    public Behavior_NetMDyn _r3rev = null;
    private String DialogResult = "";
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior_NetMDyn> behaviors;
    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Compartment> compartment;

    /**
     * Initialization of WndEditBehavior object
     * @param entities
     * @param behaviours
     * @param compartment
     */
    public WndEditBehaviour(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    /**
     * Load of a behavior
     * @param behavior
     * @param behavior2
     */
    public void WndEditBehaviour_load(Behavior_NetMDyn behavior, Behavior_NetMDyn behavior2){
    	if (behavior == null && behavior2 == null){

    		_r3 = new Behavior_NetMDyn();
    		_r3rev = new Behavior_NetMDyn();
    	}
    	else if (behavior2 == null){
    		_r3 = behavior;
    		_r3rev = new Behavior_NetMDyn();
    		comboBox_reaction.setSelectedItem("Irreversible");
    		if (_r3.getEtiquettes().contains("_vers_")){
    			comboBoxName.setSelectedItem("*");
    			comboBox_S1.setSelectedItem(_r3._reactifs.get(0));
            	comboBox_S2.setSelectedItem(_r3._reactifs.get(1));
            	comboBox_P1.setSelectedItem(_r3._produits.get(0));
            	comboBox_P2.setSelectedItem(_r3._produits.get(1));
    		}
    		else{
    			comboBoxName.setSelectedItem(_r3.getEtiquettes().split("React_")[1]);
    			comboBox_S1.setSelectedItem(_r3._reactifs.get(1));
            	comboBox_S2.setSelectedItem(_r3._reactifs.get(2));
            	comboBox_P1.setSelectedItem(_r3._produits.get(1));
            	comboBox_P2.setSelectedItem(_r3._produits.get(2));
    		}
        	
        	textBoxKCst.setText(((Double) _r3.getK()).toString());
    	}
    	else{
    		_r3 = behavior;
    		_r3rev = behavior2;
    		comboBox_reaction.setSelectedItem("Reversible");
    		if (_r3.getEtiquettes().contains("_vers_")){
    			comboBoxName.setSelectedItem("*");
    			comboBox_S1.setSelectedItem(_r3._reactifs.get(0));
            	comboBox_S2.setSelectedItem(_r3._reactifs.get(1));
            	comboBox_P1.setSelectedItem(_r3._produits.get(0));
            	comboBox_P2.setSelectedItem(_r3._produits.get(1));
    		}
    		else{
    			comboBoxName.setSelectedItem(_r3.getEtiquettes().split("React_")[1]);
    			comboBox_S1.setSelectedItem(_r3._reactifs.get(1));
            	comboBox_S2.setSelectedItem(_r3._reactifs.get(2));
            	comboBox_P1.setSelectedItem(_r3._produits.get(1));
            	comboBox_P2.setSelectedItem(_r3._produits.get(2));
    		}    		
        	textBoxKCst.setText(((Double) _r3.getK()).toString());
        	textBoxKCst2.setText(((Double) _r3rev.getK()).toString());
    	}
    	
    	
    }
    
    /**
     * Initialization of the parameters of WndEditBehavior 
     */
    private void initComponents() {

        jLabelNom = new javax.swing.JLabel();
        jLabelReaction = new javax.swing.JLabel();
        jLabelKCst = new javax.swing.JLabel();
        jLabelKCst2 = new javax.swing.JLabel();
        jLabelS1 = new javax.swing.JLabel();
        jLabelS2 = new javax.swing.JLabel();
        jLabelP1 = new javax.swing.JLabel();
        jLabelP2 = new javax.swing.JLabel();
       
        comboBoxName = new javax.swing.JComboBox();
        textBoxKCst = new javax.swing.JTextField();
        textBoxKCst2 = new javax.swing.JTextField();
        
        comboBox_S1 = new javax.swing.JComboBox();
        comboBox_S2 = new javax.swing.JComboBox();
        comboBox_P1 = new javax.swing.JComboBox();
        comboBox_P2 = new javax.swing.JComboBox();
        comboBox_reaction = new javax.swing.JComboBox();
                
        button_OK = new javax.swing.JButton();
        button_CANCEL = new javax.swing.JButton();
        jLabelReaction2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();

        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(204, 204, 255));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabelNom.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelNom.setText("Enzyme");
        getContentPane().add(jLabelNom);
        jLabelNom.setBounds(10, 40, 60, 20);
        
        
        ArrayList<String> comps = new ArrayList<String>();
        comps.add("*");
        
        for(int i = 1; i< entities.size()+1; i++){
       	 if (entities.get(i-1).getEtiquettes().contains("Membrane_")){
       		 continue;
       	 }
        	comps.add(entities.get(i-1).getEtiquettes());
        }
        
        String[] ents = new String[comps.size()];
        for (int i =0; i<comps.size();i++){
       	 ents[i]=comps.get(i);
        }
        

        comboBoxName.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBoxName.setModel(new javax.swing.DefaultComboBoxModel(ents));
        getContentPane().add(comboBoxName);
        comboBoxName.setBounds(80, 40, 150, 20);
        
        jLabelReaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelReaction.setText("Reaction");
        getContentPane().add(jLabelReaction);
        jLabelReaction.setBounds(10, 70, 60, 20);
        
        String[] type = {"Reversible", "Irreversible"};
        comboBox_reaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_reaction.setModel(new javax.swing.DefaultComboBoxModel(type));
        getContentPane().add(comboBox_reaction);
        comboBox_reaction.setBounds(80, 70, 150, 20);
        
        jLabelKCst.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelKCst.setText("Kcat");
        getContentPane().add(jLabelKCst);
        jLabelKCst.setBounds(290, 40, 60, 20);
        
        textBoxKCst.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxKCst.setText("0.0");
        textBoxKCst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                //textBoxNameKeyTyped(evt);
            }
        });
        getContentPane().add(textBoxKCst);
        textBoxKCst.setBounds(360, 40, 150, 20);
        
        jLabelKCst2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelKCst2.setText("Kcat");
        getContentPane().add(jLabelKCst2);
        jLabelKCst2.setBounds(290, 70, 60, 20);
        
        textBoxKCst2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxKCst2.setText("0.0");
        textBoxKCst2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                //textBoxNameKeyTyped(evt);
            }
        });
        getContentPane().add(textBoxKCst2);
        textBoxKCst2.setBounds(360, 70, 150, 20);
      
        jLabelS1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS1.setText("Substrat 1");
        getContentPane().add(jLabelS1);
        jLabelS1.setBounds(10, 100, 60, 20);

        comboBox_S1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_S1.setModel(new javax.swing.DefaultComboBoxModel(ents));
        getContentPane().add(comboBox_S1);
        comboBox_S1.setBounds(80, 100, 150, 20);

                
        jLabelS2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS2.setText("Substrat 2");
        getContentPane().add(jLabelS2);
        jLabelS2.setBounds(10, 130, 60, 20);
        
        comboBox_S2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_S2.setModel(new javax.swing.DefaultComboBoxModel(ents));
        getContentPane().add(comboBox_S2);
        comboBox_S2.setBounds(80, 130, 150, 20);
        
        ArrayList<String> comps2 = new ArrayList<String>();
        comps2.add("-");
        
        for(int i = 1; i< entities.size()+1; i++){
       	 if (entities.get(i-1).getEtiquettes().contains("Membrane_")){
       		 continue;
       	 }
        	comps2.add(entities.get(i-1).getEtiquettes());
        }
        
        String[] entp = new String[comps2.size()];
        for (int i =0; i<comps2.size();i++){
       	 entp[i]=comps2.get(i);
        }   
        
        jLabelP1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP1.setText("Produit 1");
        getContentPane().add(jLabelP1);
        jLabelP1.setBounds(290, 100, 60, 20);
        
        comboBox_P1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_P1.setModel(new javax.swing.DefaultComboBoxModel(entp));
        getContentPane().add(comboBox_P1);
        comboBox_P1.setBounds(360, 100, 150, 20);
        
               
        jLabelP2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP2.setText("Produit 2");
        getContentPane().add(jLabelP2);
        jLabelP2.setBounds(290, 130, 60, 20);
        
        comboBox_P2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_P2.setModel(new javax.swing.DefaultComboBoxModel(entp));
        getContentPane().add(comboBox_P2);
        comboBox_P2.setBounds(360, 130, 150, 20);

        
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
        button_CANCEL.setText("Annuler");
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

        jLabelReaction2.setBackground(new java.awt.Color(153, 153, 255));
        jLabelReaction2.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabelReaction2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelReaction2.setText("Reaction");
        getContentPane().add(jLabelReaction2);
        jLabelReaction2.setBounds(0, 0, 530, 30);


        jLabel19.setBackground(new java.awt.Color(153, 153, 255));
        jLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel19.setOpaque(true);
        getContentPane().add(jLabel19);
        jLabel19.setBounds(0, 0, 530, 30);

        setSize(new java.awt.Dimension(530, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Action when OK button is used
     * @param evt
     */
     public void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
    	if (comboBoxName.getSelectedItem().equals("")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci de nommer l'enzyme.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please name the enzyme.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    
    	if (getComboBox_S1().equals(getComboBox_P1()) || getComboBox_S1().equals(getComboBox_P2())|| getComboBox_S2().equals(getComboBox_P1()) || getComboBox_S2().equals(getComboBox_P2()) ) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Les substrats et produits ne peuvent pas être identiques.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Substrates and products must be different.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    	
    	try{
        	Double.parseDouble(textBoxKCst.getText());
        	Double.parseDouble(textBoxKCst2.getText());
        }catch(Exception e){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Les constantes catalytiques doivent etre de type 0.0", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Catalytic constant must be numbers only.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;        	
        }
    	
    	ArrayList<String> compartment = new ArrayList<String>();
    	for (Entity_NetMDyn entity : entities){
    		if (entity.getEtiquettes().equals(getComboBox_S1()) && !getComboBox_S1().equals("*")){
    			compartment.add(entity.getCompartment());
    		}
    		else if (entity.getEtiquettes().equals(getComboBox_S2()) && !getComboBox_S2().equals("*")){
    			compartment.add(entity.getCompartment());
    		}
    		else if (entity.getEtiquettes().equals(getComboBox_P1()) && !getComboBox_P1().equals("-")){
    			compartment.add(entity.getCompartment());
    		}
    		else if (entity.getEtiquettes().equals(getComboBox_P2()) && !getComboBox_P2().equals("-")){
    			compartment.add(entity.getCompartment());
    		}
    		else if (entity.getEtiquettes().equals((String) comboBoxName.getSelectedItem()) && !comboBoxName.getSelectedItem().equals("*")){
    			compartment.add(entity.getCompartment());
    		}
    	}
    	System.out.println(compartment);
    	boolean erreur=false;
    	for (int i=1; i<compartment.size();i++){
    		if (compartment.get(0)!=compartment.get(i)){
    			erreur=true;
    			continue;
    		}
    	}
		if (erreur==true){
			if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Les entités doivent toutes appartenir au même compartiment", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "All entities must be elements of the same compartment!", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}	
		
    	
        _r3._reactifs.clear();
        _r3._produits.clear();
        _r3._positions.clear();
        if (comboBoxName.getSelectedItem().equals("*")){
        	_r3.setEtiquettes("React_"+ comboBox_S1.getSelectedItem() + "_vers_" + comboBox_P1.getSelectedItem());
        }
        else{
    	_r3.setEtiquettes("React_"+ comboBoxName.getSelectedItem());
    	_r3._reactifs.add((String)comboBoxName.getSelectedItem());
    	_r3._produits.add((String)comboBoxName.getSelectedItem());
        }
    	_r3._reactifs.add((String) comboBox_S1.getSelectedItem());
    	_r3._reactifs.add((String) comboBox_S2.getSelectedItem());    	
    	_r3._produits.add((String) comboBox_P1.getSelectedItem());
    	_r3._produits.add((String) comboBox_P2.getSelectedItem());
    	_r3._positions.add("122222222");
    	_r3._positions.add("212111211");
    	_r3._positions.add("212111211");
    	_r3.setType_behavior(3);
    	_r3.setK(Double.parseDouble(textBoxKCst.getText()));
    	if (comboBox_reaction.getSelectedItem()=="Reversible"){
            _r3rev._reactifs.clear();
            _r3rev._produits.clear();
            _r3rev._positions.clear();
            if (!comboBoxName.getSelectedItem().equals("*")){
            	_r3rev.setEtiquettes("React_"+ comboBoxName.getSelectedItem()+"_reverse");
            	_r3rev._reactifs.add((String)comboBoxName.getSelectedItem());
            	_r3rev._produits.add((String)comboBoxName.getSelectedItem());
            }
            else{
            	_r3rev.setEtiquettes("React_"+ comboBox_S1.getSelectedItem() + "_vers_" + comboBox_P1.getSelectedItem()+"_reverse");           	
            }
             
        	_r3rev._reactifs.add((String) comboBox_P1.getSelectedItem());
        	if (comboBox_P2.getSelectedItem().equals("-")){
        		_r3rev._reactifs.add("*");
        	}
        	else{
        		_r3rev._reactifs.add((String) comboBox_P2.getSelectedItem());
        	}
        	_r3rev._produits.add((String) comboBox_S1.getSelectedItem());
        	if (comboBox_S2.getSelectedItem().equals("*")){
        		_r3rev._produits.add("-");
        	}
        	else{
        	_r3rev._produits.add((String) comboBox_S2.getSelectedItem());
        	}
        	_r3rev._positions.add("122222222");
        	_r3rev._positions.add("212111211");
        	_r3rev._positions.add("212111211");
        	_r3rev.setType_behavior(3);
        	_r3rev.setK(Double.parseDouble(textBoxKCst2.getText()));
    	}
    	DialogResult = "OK";
    	setVisible(false);
    }

	private void button_OKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OKMouseClicked


    }//GEN-LAST:event_button_OKMouseClicked
    
    private void button_CANCELMouseClicked(java.awt.event.MouseEvent evt) {
    	
    }
    
  /**
   * Action when Cancel button is used
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

    
    /**
     *  Return the text inside this JField
     * @return
     */
    public String getTextBoxName() {
		return (String) comboBoxName.getSelectedItem();
	}

    /**
     *  Put a new value inside this JField
     * @param textBoxName
     */
	public void setTextBoxName(String textBoxName) {
		this.comboBoxName.setSelectedItem(textBoxName);
	}

	/**
	 *  Return the text inside this JField
	 * @return
	 */
	public String getTextBoxKCst() {
		return textBoxKCst.getText();
	}

	/**
	 *  Put a new value inside this JField
	 * @param textBoxCoefP1
	 */
	public void setTextBoxKCst(String textBoxCoefP1) {
		this.textBoxKCst.setText(textBoxCoefP1);
	}

	/**
	 *  Return the text inside this ComboBox
	 * @return
	 */
	
    public String getTextBoxKCst2() {
		return textBoxKCst2.getText();
	}

	public void setTextBoxKCst2(String textBoxKCst2) {
		this.textBoxKCst2 .setText(textBoxKCst2);
	}


	/**
	 *  Return the text inside this ComboBox
	 * @return
	 */

	public String getComboBox_S1() {
		return (String) comboBox_S1.getSelectedItem();
	}

	/**
	 *  Put a new value inside this ComboBox
	 * @param comboBox_S1
	 */
	public void setComboBox_S1(String comboBox_S1) {
		this.comboBox_S1.setSelectedItem(comboBox_S1);
	}
	
	/**
	 *  Return the text inside this ComboBox
	 * @return
	 */
	public String getComboBox_S2() {
		return (String) comboBox_S2.getSelectedItem();
	}
	
	/**
	 *  Put a new value inside this ComboBox
	 * @param comboBox_S2
	 */
	public void setComboBox_S2(String comboBox_S2) {
		this.comboBox_S2.setSelectedItem(comboBox_S2);
	}

	/**
	 *  Return the text inside this ComboBox
	 * @return
	 */
	public String getComboBox_P1() {
		return (String) comboBox_P1.getSelectedItem();
	}

	/**
	 *  Put a new value inside this ComboBox
	 * @param comboBox_P1
	 */
	public void setComboBox_P1(String comboBox_P1) {
		this.comboBox_P1.setSelectedItem(comboBox_P1);
	}
	
	/**
	 *  Return the text inside this ComboBox
	 * @return
	 */
	public String getComboBox_P2() {
		return (String) comboBox_P2.getSelectedItem();
	}

	/**
	 *  Put a new value inside this ComboBox
	 * @param comboBox_P2
	 */
	public void setComboBox_P2(String comboBox_P2) {
		this.comboBox_P2.setSelectedItem(comboBox_P2);
	}


	public String getComboBox_reaction() {
		return (String) comboBox_reaction.getSelectedItem();
	}

	public void setComboBox_reaction(String comboBox_reaction) {
		this.comboBox_reaction.setSelectedItem(comboBox_reaction);
	}
	
	/**
	 *  Return this DialogResult
	 * @return
	 */

	public String getDialogResult() {
		return DialogResult;
	}

	/**
	 *  Put a new value to this DialogResult
	 * @param dialogResult
	 */
	public void setDialogResult(String dialogResult) {
		DialogResult = dialogResult;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelReaction;
    private javax.swing.JLabel jLabelKCst;
    private javax.swing.JLabel jLabelKCst2;
    
    private javax.swing.JLabel jLabelS1;
    private javax.swing.JLabel jLabelS2;
    private javax.swing.JLabel jLabelP1;
    private javax.swing.JLabel jLabelP2;
    
    private javax.swing.JLabel jLabelReaction2;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JComboBox comboBoxName;
    private javax.swing.JTextField textBoxKCst;
    private javax.swing.JTextField textBoxKCst2;
    
    private javax.swing.JComboBox comboBox_S1;
    private javax.swing.JComboBox comboBox_S2;

    private javax.swing.JComboBox comboBox_P1;
    private javax.swing.JComboBox comboBox_P2;

    private javax.swing.JComboBox comboBox_reaction;

    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_CANCEL;
    // End of variables declaration//GEN-END:variables
    

}
