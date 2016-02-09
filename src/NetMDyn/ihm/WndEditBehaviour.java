package NetMDyn.ihm;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Entity_NetMDyn;
import NetMDyn.Compartment;
import netbiodyn.util.Lang;
import netbiodyn.util.UtilPointF;

public class WndEditBehaviour extends javax.swing.JDialog{
	boolean _env_affiche3D = false;
    public Behavior_NetMDyn _r3 = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior_NetMDyn> behaviors;
    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Compartment> compartment;

    
    public WndEditBehaviour(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    public void WndEditBehaviour_load(Behavior_NetMDyn behavior){
    	if (behavior == null){
    		_r3 = new Behavior_NetMDyn();
    	}else{
    		_r3 = behavior;
    	}
    	textBoxName.setText(_r3.getEtiquettes());
    	comboBox_S1.setSelectedItem(_r3._produits.get(0));
    	comboBox_S2.setSelectedItem(_r3._produits.get(1));
    	comboBox_P1.setSelectedItem(_r3._reactifs.get(0));
    	comboBox_P2.setSelectedItem(_r3._reactifs.get(1));
    	
    }
    
    private void initComponents() {

        jLabelNom = new javax.swing.JLabel();
        jLabelReaction = new javax.swing.JLabel();
        jLabelKCst = new javax.swing.JLabel();
        jLabelS1 = new javax.swing.JLabel();
        jLabelS2 = new javax.swing.JLabel();
        jLabelP1 = new javax.swing.JLabel();
        jLabelP2 = new javax.swing.JLabel();
       
        textBoxName = new javax.swing.JTextField();
        textBoxKCst = new javax.swing.JTextField();
        
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
        

        textBoxName.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxName.setText("nom");
        textBoxName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                //textBoxNameKeyTyped(evt);
            }
        });
        getContentPane().add(textBoxName);
        textBoxName.setBounds(80, 40, 150, 20);
        
        jLabelReaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelReaction.setText("Reaction");
        getContentPane().add(jLabelReaction);
        jLabelReaction.setBounds(10, 70, 60, 20);
        
        String[] type = {"Reversible", "Irreversible"};
        comboBox_reaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_reaction.setModel(new javax.swing.DefaultComboBoxModel(type));
        getContentPane().add(comboBox_reaction);
        comboBox_reaction.setBounds(80, 70, 150, 20);
      
        jLabelS1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS1.setText("Substrat 1");
        getContentPane().add(jLabelS1);
        jLabelS1.setBounds(10, 100, 60, 20);
        
        String[] ents = new String[entities.size()+1];
        ents[0] = "-";
        for(int i = 1; i< entities.size()+1; i++){
        	ents[i] = entities.get(i-1).getEtiquettes();
        }
        
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
        
                
        
        jLabelP1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP1.setText("Produit 1");
        getContentPane().add(jLabelP1);
        jLabelP1.setBounds(290, 100, 60, 20);
        
        comboBox_P1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_P1.setModel(new javax.swing.DefaultComboBoxModel(ents));
        getContentPane().add(comboBox_P1);
        comboBox_P1.setBounds(360, 100, 150, 20);
        
               
        jLabelP2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP2.setText("Produit 2");
        getContentPane().add(jLabelP2);
        jLabelP2.setBounds(290, 130, 60, 20);
        
        comboBox_P2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_P2.setModel(new javax.swing.DefaultComboBoxModel(ents));
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
    
    private void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
    	if (textBoxName.getText().equals("")) {
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

    
    
    public String getTextBoxName() {
		return textBoxName.getText();
	}

	public void setTextBoxName(String textBoxName) {
		this.textBoxName.setText(textBoxName);
	}

	
	public String getTextBoxKCst() {
		return textBoxKCst.getText();
	}

	public void setTextBoxKCst(String textBoxCoefP1) {
		this.textBoxKCst.setText(textBoxCoefP1);
	}

	public String getComboBox_S1() {
		return (String) comboBox_S1.getSelectedItem();
	}

	public void setComboBox_S1(String comboBox_S1) {
		this.comboBox_S1.setSelectedItem(comboBox_S1);
	}

	public String getComboBox_S2() {
		return (String) comboBox_S2.getSelectedItem();
	}

	public void setComboBox_S2(String comboBox_S2) {
		this.comboBox_S2.setSelectedItem(comboBox_S2);
	}

	
	public String getComboBox_P1() {
		return (String) comboBox_P1.getSelectedItem();
	}

	public void setComboBox_P1(String comboBox_P1) {
		this.comboBox_P1.setSelectedItem(comboBox_P1);
	}

	public String getComboBox_P2() {
		return (String) comboBox_P2.getSelectedItem();
	}

	public void setComboBox_P2(String comboBox_P2) {
		this.comboBox_P2.setSelectedItem(comboBox_P2);
	}

	public String getDialogResult() {
		return DialogResult;
	}

	public void setDialogResult(String dialogResult) {
		DialogResult = dialogResult;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelReaction;
    private javax.swing.JLabel jLabelKCst;
    private javax.swing.JLabel jLabelS1;
    private javax.swing.JLabel jLabelS2;
    private javax.swing.JLabel jLabelP1;
    private javax.swing.JLabel jLabelP2;
    
    private javax.swing.JLabel jLabelReaction2;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JTextField textBoxName;
    private javax.swing.JTextField textBoxKCst;
    
    private javax.swing.JComboBox comboBox_S1;
    private javax.swing.JComboBox comboBox_S2;

    private javax.swing.JComboBox comboBox_P1;
    private javax.swing.JComboBox comboBox_P2;

    private javax.swing.JComboBox comboBox_reaction;

    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_CANCEL;
    // End of variables declaration//GEN-END:variables
    

}
