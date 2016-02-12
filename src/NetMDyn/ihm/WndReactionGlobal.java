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
 * WndReactionGlobal.java
 *
 * Created on February 12 2016, 16:21
 */

package NetMDyn.ihm;

import netbiodyn.util.UtilPointF;
import netbiodyn.util.UtilDivers;

import java.awt.Color;// Encapsulate colors in the default sRGB color space or colors in arbitrary color spaces identified by a ColorSpace
import java.awt.Dimension; // Encapsulate the width and height of a component (in integer precision) in a single object.
import java.awt.Graphics; // The abstract base class for all graphics contexts that allow an application to draw onto components that are realized on various devices, as well as onto off-screen images. 
import java.util.ArrayList; // Possible creation for tables
import javax.swing.DefaultCellEditor; // The default editor for table and tree cells
import javax.swing.JComboBox; //Combine a button or editable field and a drop-down list
import javax.swing.JOptionPane; // A standard dialog box that prompts users for a value or informs them of something.
import javax.swing.table.DefaultTableModel; //An implementation of TableModel that uses a Vector of Vectors to store the cell value objects. 
import javax.swing.table.TableColumn; // All the attributes of a column in a JTable

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Compartment;
import NetMDyn.Entity_NetMDyn;
import netbiodyn.Behavior;
import netbiodyn.Entity;
import netbiodyn.util.Lang;

/**
 * Edit of the global reaction in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WndReactionGlobal extends javax.swing.JDialog {
    
    boolean _env_affiche3D = false;
    public Behavior _r3 = null;
    private String DialogResult = "";
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior_NetMDyn> behaviors;
    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Compartment> compartment;


    /**
     * Initialization of WndReactionGlobal object
     * @param entities
     * @param behaviours
     * @param compartment
     */
    public WndReactionGlobal(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    /**
     * Initialization of the parameters
     */
    private void initComponents() {
        
        jLabelNom = new javax.swing.JLabel();
        jLabelReaction = new javax.swing.JLabel();
        jLabelCompartment = new javax.swing.JLabel();
        jLabelK1 = new javax.swing.JLabel();
        jLabelKmoins1 = new javax.swing.JLabel();
        textBoxName = new javax.swing.JTextField();
        textBoxReaction = new javax.swing.JTextField();
        textBoxK1 = new javax.swing.JTextField();
        textBoxKmoins1 = new javax.swing.JTextField();
        
        jLabelS1 = new javax.swing.JLabel();
        jLabelS2 = new javax.swing.JLabel();
        jLabelP1 = new javax.swing.JLabel();
        jLabelP2 = new javax.swing.JLabel();
        
        comboBox_compartment = new javax.swing.JComboBox();
        comboBox_TypeS1 = new javax.swing.JComboBox();
        comboBox_TypeS2 = new javax.swing.JComboBox();
        comboBox_TypeP1 = new javax.swing.JComboBox();
        comboBox_TypeP2 = new javax.swing.JComboBox();
        jCheckBox_reversible = new javax.swing.JCheckBox();
        
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
        
        
        textBoxName.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxName.setText("");
        textBoxName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBoxKeyTyped(evt);
            }
        });
        getContentPane().add(textBoxName);
        textBoxName.setBounds(80, 40, 140, 20);
        
        jLabelCompartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelCompartment.setText("Compartment");
        getContentPane().add(jLabelCompartment);
        jLabelCompartment.setBounds(270, 40, 80, 20);
        
        String[] comps = new String[compartment.size()+1];
        comps[0] = "Cytosol";
        for(int i = 1; i< compartment.size()+1; i++){
            comps[i] = compartment.get(i-1).getEtiquettes();
        }
        
        comboBox_compartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_compartment.setModel(new javax.swing.DefaultComboBoxModel(comps));
        getContentPane().add(comboBox_compartment);
        comboBox_compartment.setBounds(350, 40, 150, 20);
        
        
        jLabelReaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelReaction.setText("Reaction");
        getContentPane().add(jLabelReaction);
        jLabelReaction.setBounds(10, 70, 60, 20);
        
        String[] type = {"-","Protéine", "Métabolite"};
        
        textBoxReaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxReaction.setText("");
        textBoxReaction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBoxKeyTyped(evt);
            }
        });
        getContentPane().add(textBoxReaction);
        textBoxReaction.setBounds(80, 70, 420, 20);

        jLabelS1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS1.setText("Substrat 1");
        getContentPane().add(jLabelS1);
        jLabelS1.setBounds(10, 100, 60, 20);
        
        comboBox_TypeS1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_TypeS1.setModel(new javax.swing.DefaultComboBoxModel(type));
        getContentPane().add(comboBox_TypeS1);
        comboBox_TypeS1.setBounds(80, 100, 150, 20);
        
        jLabelP1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP1.setText("Produits 1");
        getContentPane().add(jLabelP1);
        jLabelP1.setBounds(250, 100, 60, 20);
        
        comboBox_TypeP1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_TypeP1.setModel(new javax.swing.DefaultComboBoxModel(type));
        getContentPane().add(comboBox_TypeP1);
        comboBox_TypeP1.setBounds(320, 100, 150, 20);
        
        jLabelS2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS2.setText("Substrat 2");
        getContentPane().add(jLabelS2);
        jLabelS2.setBounds(10, 130, 60, 20);
        
        comboBox_TypeS2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_TypeS2.setModel(new javax.swing.DefaultComboBoxModel(type));
        getContentPane().add(comboBox_TypeS2);
        comboBox_TypeS2.setBounds(80, 130, 150, 20);
        
        jLabelP2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP2.setText("Produits 2");
        getContentPane().add(jLabelP2);
        jLabelP2.setBounds(250, 130, 60, 20);
        
        comboBox_TypeP2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_TypeP2.setModel(new javax.swing.DefaultComboBoxModel(type));
        getContentPane().add(comboBox_TypeP2);
        comboBox_TypeP2.setBounds(320, 130, 150, 20);
        
        jLabelK1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelK1.setText("KCat");
        getContentPane().add(jLabelK1);
        jLabelK1.setBounds(10, 160, 60, 20);
        
        
        textBoxK1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxK1.setText("0.0");
        getContentPane().add(textBoxK1);
        textBoxK1.setBounds(80, 160, 150, 20);
        
        jLabelKmoins1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelKmoins1.setText("K-1");
        getContentPane().add(jLabelKmoins1);
        jLabelKmoins1.setBounds(250, 160, 60, 20);
        
        
        textBoxKmoins1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxKmoins1.setText("0.0");
        getContentPane().add(textBoxKmoins1);
        textBoxKmoins1.setBounds(320, 160, 150, 20);
        
        jCheckBox_reversible.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jCheckBox_reversible.setSelected(true);
        jCheckBox_reversible.setText("Reversible");
        jCheckBox_reversible.setToolTipText("");
        jCheckBox_reversible.setContentAreaFilled(false);
        jCheckBox_reversible.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jCheckBox_reversible.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        getContentPane().add(jCheckBox_reversible);
        jCheckBox_reversible.setBounds(10, 190, 110, 20);
        
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
        button_OK.setBounds(0, 220, 250, 30);
        
        button_CANCEL.setBackground(new java.awt.Color(255, 153, 153));
        button_CANCEL.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
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
        button_CANCEL.setBounds(280, 220, 250, 30);
        
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
     * Errors when OK button is pushed
     * @param evt
     */
    private void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
    	if (textBoxName.getText().equals("")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci de nommer la réaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);

            }else {
                JOptionPane.showMessageDialog(this, "Please name the reaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;

        }
        if (textBoxReaction.getText().equals("")) {
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Merci d'ecrire une réaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please write a reaction equation.", "Information", JOptionPane.INFORMATION_MESSAGE, null);

    	}
    	if (textBoxReaction.getText().equals("")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci d'écrire une réaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please name the reaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
        }
        
        for (Behavior_NetMDyn bev: behaviors){
        	if (bev.getType_behavior() == 3){
        		if ((bev.getEtiquettes().split("React_")[1]).equals(getTextBoxName())){
        			if (Lang.getInstance().getLang().equals("FR")) {
        				JOptionPane.showMessageDialog(this, "Cette enzyme est deja engagé dans une reaction", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        			}else {
        				JOptionPane.showMessageDialog(this, "This enzyme is already used in a reaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        			}
        			return;
        		}
        	}
        }
        
        String equation = getTextBoxReaction();
        String substrats;
        String produits;
        try{
        	substrats = equation.split("=")[0];
        	produits = equation.split("=")[1];
        }catch(Exception e){
        	if (Lang.getInstance().getLang().equals("FR")) {
        		JOptionPane.showMessageDialog(this, "L'équation doit s'écrire de la facon suivante A+B=C+D.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        	}else {
        		JOptionPane.showMessageDialog(this, "Equation must be written with the shape A+B=C+D.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
        }
        
        String[] substrat;
        int nbSubstrats;
        try{
        	substrat = substrats.split("\\+");
        	nbSubstrats = substrat.length;
        }catch(Exception e){
        	nbSubstrats = 1;
        }
        
        String[] produit;
        int nbPrduits;
        try{
        	produit = produits.split("\\+");
        	nbPrduits = produit.length;
        }catch(Exception e){
        	nbPrduits = 1;
        }
        
        if(nbSubstrats > 2 || nbPrduits > 2){
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "L'équation ne peut contenir que deux susbstrats et deux produits.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Equation can contain only two substrates and two products.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
        }
        if(nbSubstrats == 2){
        	if(getComboBox_TypeS1().equals("-") || getComboBox_TypeS2().equals("-")){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Donnez la catégories des deux susbstrats", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Please choose a type for all the substrates.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
        	}
        }else if(nbSubstrats == 1){
        	if(getComboBox_TypeS1().equals("-") || (!getComboBox_TypeS2().equals("-"))){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Ne donne la catégorie qu'au premier substrats", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Please choose a type for all the substrates.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
        	}
        }
        if(nbPrduits == 2){
        	if(getComboBox_TypeP1().equals("-") || getComboBox_TypeP2().equals("-")){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Donnez la catégories des deux produits", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Please choose a type for all the products.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
        	}
        }else if(nbPrduits == 1){
        	if(getComboBox_TypeP1().equals("-") || (!getComboBox_TypeP2().equals("-"))){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Ne donne la catégorie qu'au premier produit", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Please choose a type for all the products.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
        	}
        }
        
        try{
        	Double.parseDouble(getTextBoxK1());
        	Double.parseDouble(getTextBoxKmoins1());
        }catch(Exception e){
        		if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Les constantes catalytiques doivent etre de type 0.0", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }else {
                    JOptionPane.showMessageDialog(this, "Catalytic constant must be numbers only.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
        	}
        }
        
        DialogResult = "OK";
        setVisible(false);
    }
    
    private void button_OKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OKMouseClicked
        this.DialogResult = new String("CANCEL");
        setVisible(false);
        
    }//GEN-LAST:event_button_OKMouseClicked
    
    private void button_CANCELMouseClicked(java.awt.event.MouseEvent evt) {
        this.DialogResult = new String("CANCEL");
        setVisible(false);
    }
    
  /**
   * Errors when Cancel button is pushed
   * @param evt
   */
	private void button_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CANCELActionPerformed
        this.DialogResult = new String("CANCEL");
        setVisible(false);
    }//GEN-LAST:event_button_CANCELActionPerformed
    
    private void textBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBox1KeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (c == '\\' || c == '/' || c == ',' || c == ':' || c == ' ' || c == '*' || c == '?' || c == '\"' || c == '<' || c == '>' || c == '|') {
            evt.consume();
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Les caracteres \\ / , : ESPACE * ? \" < > , et | sont interdits. Merci de votre comprehension", "ATTENTION", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "Characteres \\ / : SPACE * ? \" < > , and | are forbiden.", "ATTENTION", JOptionPane.INFORMATION_MESSAGE, null);
            }
        }
    }//GEN-LAST:event_textBox1KeyTyped
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed
    
    
    public String getTextBoxName() {
        return textBoxName.getText();
    }
    
    public void setTextBoxName(String textBoxName) {
        this.textBoxName.setText(textBoxName);
    }
    
    public String getTextBoxReaction() {
        return textBoxReaction.getText();
    }
    
    public void setTextBoxReaction(String textBoxReaction) {
        this.textBoxReaction.setText(textBoxReaction);
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
    
    public Boolean getjCheckBox_reversible() {
        return jCheckBox_reversible.isSelected();
    }
    
    public void setjCheckBox_reversible(Boolean jCheckBox_reversible) {
        this.jCheckBox_reversible.setSelected(jCheckBox_reversible);
    }
    
    public String getComboBox_TypeS1() {
		return (String) comboBox_TypeS1.getSelectedItem();
	}

	public String getTextBoxK1() {
		return textBoxK1.getText();
	}

	public void setTextBoxK1(String textBoxK1) {
		this.textBoxK1.setText(textBoxK1);
	}

	public String getTextBoxKmoins1() {
		return textBoxKmoins1.getText();
	}

	public void setTextBoxKmoins1(String textBoxKmoins1) {
		this.textBoxKmoins1.setText(textBoxKmoins1);
	}

	public void setComboBox_TypeS1(String comboBox_TypeS1) {
		this.comboBox_TypeS1.setSelectedItem(comboBox_TypeS1);
	}

	public String getComboBox_TypeS2() {
		return (String) comboBox_TypeS2.getSelectedItem();
	}

	public void setComboBox_TypeS2(String comboBox_TypeS2) {
		this.comboBox_TypeS2.setSelectedItem(comboBox_TypeS2);
	}

	public String getComboBox_TypeP1() {
		return (String) comboBox_TypeP1.getSelectedItem();
	}

	public void setComboBox_TypeP1(String comboBox_TypeP1) {
		this.comboBox_TypeP1.setSelectedItem(comboBox_TypeP1);
	}

	public String getComboBox_TypeP2() {
		return (String) comboBox_TypeP2.getSelectedItem();
	}

	public void setComboBox_TypeP2(String comboBox_TypeP2) {
		this.comboBox_TypeP2.setSelectedItem(comboBox_TypeP2);
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelReaction;
    private javax.swing.JLabel jLabelCompartment;
    private javax.swing.JLabel jLabelS1;
    private javax.swing.JLabel jLabelS2;
    private javax.swing.JLabel jLabelP1;
    private javax.swing.JLabel jLabelP2;
    private javax.swing.JLabel jLabelK1;
    private javax.swing.JLabel jLabelKmoins1;
    private javax.swing.JLabel jLabelReaction2;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JTextField textBoxName;
    private javax.swing.JTextField textBoxReaction;
    private javax.swing.JTextField textBoxK1;
    private javax.swing.JTextField textBoxKmoins1;
    private javax.swing.JComboBox comboBox_compartment;
    
    private javax.swing.JComboBox comboBox_TypeS1;
    private javax.swing.JComboBox comboBox_TypeS2;
    private javax.swing.JComboBox comboBox_TypeP1;
    private javax.swing.JComboBox comboBox_TypeP2;
    
    private javax.swing.JCheckBox jCheckBox_reversible;
    
    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_CANCEL;
    // End of variables declaration//GEN-END:variables
    
}
