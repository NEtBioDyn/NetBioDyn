package NetMDyn.ihm;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.Compartment;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

public class WndEditTraverse extends javax.swing.JDialog {
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior_NetMDyn> behaviors;
    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Compartment> compartment;
    
    public WndEditTraverse(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    public void WndEditTraverse_load(Behavior_NetMDyn behavior){
    	if (behavior == null){
    		_r3 = new Behavior_NetMDyn();
    	}else{
    		_r3 = behavior;
    	}
    	comboBox_compartment.setSelectedItem(_r3._reactifs.get(0).split("_")[1]);
    	comboBox_OriginEntity.setSelectedItem(_r3._produits.get(1));
    	comboBox_TargetEntity.setSelectedItem(_r3._reactifs.get(1));
    	
    	textBoxProba.setText(((Double) _r3.get_k()).toString());
    }
    
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
         jLabelNomOrigine.setText("Entité d'origine");
         getContentPane().add(jLabelNomOrigine);
         jLabelNomOrigine.setBounds(10, 40, 100, 20);
         
         String[] ent = new String[entities.size()+1];
         ent[0] = "-";
         for(int i = 1; i< entities.size()+1; i++){
         	ent[i] = entities.get(i-1).getEtiquettes();
         }
         
         comboBox_OriginEntity.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_OriginEntity.setModel(new javax.swing.DefaultComboBoxModel(ent));
         getContentPane().add(comboBox_OriginEntity);
         comboBox_OriginEntity.setBounds(120, 40, 150, 20);

         jLabelNomTarget.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         jLabelNomTarget.setText("Entité d'origine");
         getContentPane().add(jLabelNomTarget);
         jLabelNomTarget.setBounds(10, 80, 100, 20);
         
         comboBox_TargetEntity.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_TargetEntity.setModel(new javax.swing.DefaultComboBoxModel(ent));
         getContentPane().add(comboBox_TargetEntity);
         comboBox_TargetEntity.setBounds(120, 80, 150, 20);
         
         jLabelCompartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         jLabelCompartment.setText("Compartment");
         getContentPane().add(jLabelCompartment);
         jLabelCompartment.setBounds(10, 120, 100, 20);
         
         String[] compart = new String[compartment.size()+1];
         compart[0] = "Cytosol";
         for(int i = 1; i< compartment.size()+1; i++){
         	compart[i] = compartment.get(i-1).getEtiquettes();
         }
         
         comboBox_compartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         comboBox_compartment.setModel(new javax.swing.DefaultComboBoxModel(compart));
         getContentPane().add(comboBox_compartment);
         comboBox_compartment.setBounds(120, 120, 150, 20);
         
         jLabelProba.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
         jLabelProba.setText("Probability");
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
         jLabelTitre.setText("Traversée de membrane");
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
    	
    	
    	String origin_entity = getComboBox_OriginEntity();
    	String target_entity = getComboBox_TargetEntity();
    	
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
    
}

