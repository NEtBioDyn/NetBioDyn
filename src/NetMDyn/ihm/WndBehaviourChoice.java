package NetMDyn.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.TextField;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;

import javax.swing.AbstractButton;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import NetMDyn.Compartment;
import netbiodyn.Entity;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

public class WndBehaviourChoice extends javax.swing.JDialog{
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;

    
    public WndBehaviourChoice(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> arrayList) {
        this.setModal(true);
        setSize(new Dimension(640, 440));
        initComponents();
    }  
    
    private void initComponents() {
    	
   	 jLabelPlaceDuTitre = new javax.swing.JLabel();
   	 jLabelTitre = new javax.swing.JLabel();
        	
        	button_OK = new javax.swing.JButton();
            button_CANCEL = new javax.swing.JButton();
        	
            mvt = new JRadioButton("Mouvement", true);
        	traverse = new JRadioButton("Traversée");
            reaction = new JRadioButton("Reaction");
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
            
            jLabelTitre.setBackground(new java.awt.Color(153, 153, 255));
            jLabelTitre.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
            jLabelTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabelTitre.setText("Choix du comportement");
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
            this.DialogResult = new String("CANCEL");
            setVisible(false);
        }//GEN-LAST:event_button_CANCELActionPerformed

        private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
            // TODO add your handling code here:

            //if()
        }//GEN-LAST:event_formKeyPressed
        
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
        
        public String getDialogResult() {
            return DialogResult;
        }
}
