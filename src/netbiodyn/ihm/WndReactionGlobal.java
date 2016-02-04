package netbiodyn.ihm;

import netbiodyn.util.UtilPointF;
import netbiodyn.util.UtilDivers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import netbiodyn.Behavior;
import netbiodyn.Compartment;
import netbiodyn.Entity;
import netbiodyn.util.Lang;

public class WndReactionGlobal extends javax.swing.JDialog {
	
    boolean _env_affiche3D = false;
    public Behavior _r3 = null;
    private String DialogResult = null;
    private int _mouseX = 0, _mouseY = 0;
    private final ArrayList<Behavior> behaviors;
    private final ArrayList<Entity> entities;
    private final ArrayList<Compartment> compartment;

    
    public WndReactionGlobal(ArrayList<Entity> entities, ArrayList<Behavior> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.behaviors = behaviours;
        this.entities = entities;
        this.compartment = compartment;
        setSize(new Dimension(640, 440));
        initComponents();
    }
    
    private void initComponents() {

        jLabelNom = new javax.swing.JLabel();
        jLabelReaction = new javax.swing.JLabel();
        jLabelCompartment = new javax.swing.JLabel();
        jLabelS1 = new javax.swing.JLabel();
        jLabelS2 = new javax.swing.JLabel();
        jLabelS3 = new javax.swing.JLabel();
        jLabelP1 = new javax.swing.JLabel();
        jLabelP2 = new javax.swing.JLabel();
        jLabelP3 = new javax.swing.JLabel();
        textBoxName = new javax.swing.JTextField();
        textBoxReaction = new javax.swing.JTextField();
        textBoxCoefS1 = new javax.swing.JTextField();
        textBoxCoefS2 = new javax.swing.JTextField();
        textBoxCoefS3 = new javax.swing.JTextField();
        textBoxCoefP1 = new javax.swing.JTextField();
        textBoxCoefP2 = new javax.swing.JTextField();
        textBoxCoefP3 = new javax.swing.JTextField();
        comboBox_compartment = new javax.swing.JComboBox();
        
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
        jLabelNom.setText("Nom");
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
        

        textBoxReaction.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxReaction.setText("");
        textBoxReaction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                //textBoxNameKeyTyped(evt);
            }
        });
        getContentPane().add(textBoxReaction);
        textBoxReaction.setBounds(80, 70, 420, 20);

        jLabelS1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS1.setText("Substrat 1");
        getContentPane().add(jLabelS1);
        jLabelS1.setBounds(10, 100, 60, 20);
        

        textBoxCoefS1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCoefS1.setText("");
        getContentPane().add(textBoxCoefS1);
        textBoxCoefS1.setBounds(80, 100, 150, 20);
        
        jLabelS2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS2.setText("Substrat 2");
        getContentPane().add(jLabelS2);
        jLabelS2.setBounds(10, 130, 60, 20);
        

        textBoxCoefS2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCoefS2.setText("");
        getContentPane().add(textBoxCoefS2);
        textBoxCoefS2.setBounds(80, 130, 150, 20);
        
        jLabelS3.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelS3.setText("Substrat 3");
        getContentPane().add(jLabelS3);
        jLabelS3.setBounds(10, 160, 60, 20);
        

        textBoxCoefS3.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCoefS3.setText("");
        getContentPane().add(textBoxCoefS3);
        textBoxCoefS3.setBounds(80, 160, 150, 20);
        
        jLabelP1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP1.setText("Produit 1");
        getContentPane().add(jLabelP1);
        jLabelP1.setBounds(290, 100, 60, 20);
        

        textBoxCoefP1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCoefP1.setText("");
        getContentPane().add(textBoxCoefP1);
        textBoxCoefP1.setBounds(350, 100, 150, 20);
        
        jLabelP2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP2.setText("Produit 2");
        getContentPane().add(jLabelP2);
        jLabelP2.setBounds(290, 130, 60, 20);
        

        textBoxCoefP2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCoefP2.setText("");
        getContentPane().add(textBoxCoefP2);
        textBoxCoefP2.setBounds(350, 130, 150, 20);
        
        jLabelP3.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelP3.setText("Produit 3");
        getContentPane().add(jLabelP3);
        jLabelP3.setBounds(290, 160, 60, 20);
        

        textBoxCoefP3.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCoefP3.setText("");
        getContentPane().add(textBoxCoefP3);
        textBoxCoefP3.setBounds(350, 160, 150, 20);


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
    			JOptionPane.showMessageDialog(this, "Merci de nommer la reaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please name the reaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
    	if (textBoxReaction.getText().equals("")) {
    		if (Lang.getInstance().getLang().equals("FR")) {
    			JOptionPane.showMessageDialog(this, "Merci d'ecrire une réaction.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }else {
                JOptionPane.showMessageDialog(this, "Please name the behaviour.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}

    	String equation = textBoxReaction.getText();
    	
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

	public String getTextBoxReaction() {
		return textBoxReaction.getText();
	}

	public void setTextBoxReaction(String textBoxReaction) {
		this.textBoxReaction.setText(textBoxReaction);
	}

	public String getTextBoxCoefP1() {
		return textBoxCoefP1.getText();
	}

	public void setTextBoxCoefP1(String textBoxCoefP1) {
		this.textBoxCoefP1.setText(textBoxCoefP1);
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
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelReaction;
    private javax.swing.JLabel jLabelCompartment;
    private javax.swing.JLabel jLabelS1;
    private javax.swing.JLabel jLabelS2;
    private javax.swing.JLabel jLabelS3;
    private javax.swing.JLabel jLabelP1;
    private javax.swing.JLabel jLabelP2;
    private javax.swing.JLabel jLabelP3;
    private javax.swing.JLabel jLabelReaction2;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JTextField textBoxName;
    private javax.swing.JTextField textBoxReaction;
    private javax.swing.JTextField textBoxCoefS1;
    private javax.swing.JTextField textBoxCoefS2;
    private javax.swing.JTextField textBoxCoefS3;
    private javax.swing.JTextField textBoxCoefP1;
    private javax.swing.JTextField textBoxCoefP2;
    private javax.swing.JTextField textBoxCoefP3;
    private javax.swing.JComboBox comboBox_compartment;
    
    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_CANCEL;
    // End of variables declaration//GEN-END:variables
    
}
