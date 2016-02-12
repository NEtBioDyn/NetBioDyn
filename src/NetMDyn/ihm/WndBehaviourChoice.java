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
 * WndBehaviourChoice.java
 *
 * Created on February 12 2016, 15:52
 */

package NetMDyn.ihm;

import java.awt.Dimension; // Encapsulate the width and height of a component (in integer precision) in a single object.
import java.awt.GridLayout; // A layout manager that lays out a container's components in a rectangular grid
import java.awt.BorderLayout; // Lay out a container, arranging and resizing its components to fit in five regions: north, south, east, west, and center
import java.awt.TextField; // A text component that allows for the editing of a single line of text

import javax.swing.ButtonGroup; //Create a multiple-exclusion scope for a set of buttons
import javax.swing.JFrame; // Possible creation of windows
import javax.swing.JPanel; // A generic lightweight container
import javax.swing.JRadioButton; // An implementation of a radio button -- an item that can be selected or deselected, and which displays its state to the user

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;

import javax.swing.AbstractButton; // Define common behaviors for buttons and menu items

import java.util.ArrayList; //Possible creation of tables

import javax.swing.JOptionPane; // A standard dialog box that prompts users for a value or informs them of something.

import NetMDyn.Compartment;
import netbiodyn.Entity;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

/**
 * Choice of the Behavior in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WndBehaviourChoice extends javax.swing.JDialog{
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;

    
    /**
     * Initialization of WndBehaviorChoice object
     * @param entities
     * @param behaviours
     * @param arrayList
     */
    public WndBehaviourChoice(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> arrayList) {
        this.setModal(true);
        setSize(new Dimension(640, 440));
        initComponents();
    }  
    
    /**
     * Initialization of the parameters of the object
     */
    private void initComponents() {
    	
   	 jLabelPlaceDuTitre = new javax.swing.JLabel();
   	 jLabelTitre = new javax.swing.JLabel();
        	
        	button_OK = new javax.swing.JButton();
            button_CANCEL = new javax.swing.JButton();
        	
            String textmvt="";
            String texttraverse="";
            String textgroupe="";
            mvt = new JRadioButton(textmvt, true);
        	traverse = new JRadioButton(texttraverse);
            reaction = new JRadioButton(textgroupe);
            if (Lang.getInstance().getLang().equals("FR")) {
            	textmvt="Mouvement";
            	texttraverse="Traversée";
            	textgroupe="Réaction";
            }
            else{
            	textmvt="Movement";
            	texttraverse="Crossing";
            	textgroupe="Reaction";            	
            }
            groupe = new ButtonGroup();
            
            
        	setAlwaysOnTop(true);
            setBackground(new java.awt.Color(204, 204, 255));
            setResizable(false);
            addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    formKeyPressed(evt);
                }
            });
            getContentPane().setLayout(null);
            groupe.add(mvt);
            groupe.add(traverse);
            groupe.add(reaction);
            
            getContentPane().add(mvt);
            mvt.setBounds(215, 50, 150, 20);
            getContentPane().add(traverse);
            traverse.setBounds(215, 90, 150, 20);
            getContentPane().add(reaction);
            reaction.setBounds(215, 130, 150, 20);
            
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
            button_CANCEL.setBounds(280, 200, 250, 30);
            
            jLabelTitre.setBackground(new java.awt.Color(153, 153, 255));
            jLabelTitre.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
            jLabelTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            if (Lang.getInstance().getLang().equals("FR")) {
            	jLabelTitre.setText("Choix du comportement");
            }
            else{
            	jLabelTitre.setText("Choice of compartment");
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
        
        public void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
        	
        	if (mvt.isSelected()){
        		type= 1;
        	}
        	else if (traverse.isSelected()){
        		type=2;
        	}
        	else{
        		type=3;
        	}
        	
        	
        	DialogResult = "OK";
        	setVisible(false);
        }
        
        private void button_OKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OKMouseClicked


        }//GEN-LAST:event_button_OKMouseClicked
        
        private void button_CANCELMouseClicked(java.awt.event.MouseEvent evt) {
        	
        }
        
    	private void button_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CANCELActionPerformed
            // TODO add your handling code here:
            String textdialogresult="";
            this.DialogResult = new String(textdialogresult);
    		if (Lang.getInstance().getLang().equals("FR")) {
            	textdialogresult="Annuler";
            }
            else{
            	textdialogresult="Cancel";
            }
            setVisible(false);
        }//GEN-LAST:event_button_CANCELActionPerformed

        private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
            // TODO add your handling code here:

            //if()
        }//GEN-LAST:event_formKeyPressed
        
        /**
         * Return the type of this behavior
         * @return
         */
        public int getBehaviourType(){
        	return type;
        }
        
       public javax.swing.JRadioButton mvt;
        private javax.swing.JRadioButton reaction;
        private javax.swing.JRadioButton traverse;
        private javax.swing.ButtonGroup groupe;
        
        private javax.swing.JLabel jLabelPlaceDuTitre;
        private javax.swing.JLabel jLabelTitre;
        
        private javax.swing.JButton button_OK;
        private javax.swing.JButton button_CANCEL;
        private int type;
        
        /**
         * Return the String into DialogResult
         * @return
         */
        public String getDialogResult() {
            return DialogResult;
        }
}