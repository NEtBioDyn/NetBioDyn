package NetMDyn.ihm;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.Compartment;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

public class WndEditMvt extends javax.swing.JDialog {
	
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior_NetMDyn> behaviors;
    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Compartment> compartment;
    
    public WndEditMvt(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    public void WndEditMvt_load(Behavior_NetMDyn behavior){
    	if (behavior == null){
    		_r3 = new Behavior_NetMDyn();
    	}else{
    		_r3 = behavior;
    		comboBox_entity.setSelectedItem(_r3._reactifs.get(0));
        	if (_r3.getProba()==0.5){
        		comboBox_mvt.setSelectedItem("Protéine");
        	}
        	else{
        		comboBox_mvt.setSelectedItem("Métabolite");
        	}
    	}
    
    }
    
    private void initComponents() {
    	 jLabelNom = new javax.swing.JLabel();
    	 comboBox_entity = new javax.swing.JComboBox();
    	 comboBox_mvt = new javax.swing.JComboBox();
    	 
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

         jLabelNom.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         jLabelNom.setText("Nom");
         getContentPane().add(jLabelNom);
         jLabelNom.setBounds(80, 100, 30, 20);
         
         ArrayList<String> comps = new ArrayList<String>();
         comps.add("-");
         
         for(int i = 1; i< entities.size()+1; i++){
        	 if (entities.get(i-1).getEtiquettes().contains("Membrane_")){
        		 continue;
        	 }
         	comps.add(entities.get(i-1).getEtiquettes());
         }
         
         String[] remain_entity = new String[comps.size()];
         for (int i =0; i<comps.size();i++){
        	 remain_entity[i]=comps.get(i);
         }
         
         comboBox_entity.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_entity.setModel(new javax.swing.DefaultComboBoxModel(remain_entity));
         getContentPane().add(comboBox_entity);
         comboBox_entity.setBounds(120, 100, 150, 20);

         String[] mouvt = {"Protéine", "Métabolite"};
         
         comboBox_mvt.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_mvt.setModel(new javax.swing.DefaultComboBoxModel(mouvt));
         getContentPane().add(comboBox_mvt);
         comboBox_mvt.setBounds(280, 100, 150, 20);
         
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
         jLabelTitre.setText("Mouvement");
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
    	if (getComboBox_entity().equals("-")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci de choisir une entité à déplacer", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please choose an entity to move.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
        _r3._reactifs.clear();
        _r3._produits.clear();
    	_r3.setEtiquettes("Move_"+ comboBox_entity.getSelectedItem());
    	_r3._reactifs.add((String) comboBox_entity.getSelectedItem());
    	_r3._reactifs.add("0");
    	_r3._produits.add("0");
    	_r3._produits.add((String) comboBox_entity.getSelectedItem());
    	_r3._positions.add("122222222");
    	_r3._positions.add("212111211");
    	_r3.setType_behavior(1);
    	if (comboBox_mvt.getSelectedItem().equals("Protéine")){
    		_r3.setProba(0.5);
    	}
    	else{
    		_r3.setProba(0.8);
    	}
    	String moving_entity = getComboBox_entity();
    	String entity_type = getComboBox_mvt();
    	
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
    public String getComboBox_entity() {
		return (String) comboBox_entity.getSelectedItem();
	}

	public void setComboBox_entity(String comboBox_entity) {
		this.comboBox_entity.setSelectedItem(comboBox_entity);
	}
	
	public String getComboBox_mvt() {
		return (String) comboBox_mvt.getSelectedItem();
	}

	public void setComboBox_mvt(String comboBox_mvt) {
		this.comboBox_mvt.setSelectedItem(comboBox_mvt);
	}
	
    public String getDialogResult() {
		return DialogResult;
	}

	public void setDialogResult(String dialogResult) {
		DialogResult = dialogResult;
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JComboBox comboBox_entity;
    private javax.swing.JComboBox comboBox_mvt;
    
    private javax.swing.JLabel jLabelPlaceDuTitre;
    private javax.swing.JLabel jLabelTitre;
    
    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_CANCEL;
    // End of variables declaration//GEN-END:variables
    
}