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
 * WndEditCompartment.java
 *
 * Created on February 12 2016, 16:00
 */

package NetMDyn.ihm;

import netbiodyn.util.UtilFileFilter;
import netbiodyn.util.UtilPoint3D;

import java.awt.Color; // Encapsulate colors in the default sRGB color space or colors in arbitrary color spaces identified by a ColorSpace
import java.awt.event.KeyEvent; // An event which indicates that a keystroke occurred in a component. 
import java.awt.image.BufferedImage; // An Image with an accessible buffer of image data.
import java.io.File; // An abstract representation of file and directory pathnames. 
import java.net.URL; // A Uniform Resource Locator, a pointer to a "resource" on the World Wide Web
import java.util.ArrayList; // Possible creation of tables
import javax.imageio.ImageIO; // A class containing static convenience methods for locating ImageReaders and ImageWriters, and performing simple encoding and decoding.
import javax.swing.DefaultComboBoxModel; // The default model for combo boxes.
import javax.swing.JColorChooser; // Allow a user to manipulate and select a color
import javax.swing.JFileChooser; //  Allow a user to choose a file
import javax.swing.JOptionPane; // A standard dialog box that prompts users for a value or informs them of something.

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Compartment;
import NetMDyn.Entity_NetMDyn;
import netbiodyn.Behavior;
import netbiodyn.Entity;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;

/**
 * Edit of the Compartment in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WndEditCompartment extends javax.swing.JDialog {

    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Behavior_NetMDyn> behaviours;
    private final ArrayList<Compartment> compartment;
    public String DialogResult = "";
    public Compartment _cli = null;
    String _old_name = "";
    Color _old_color = Color.black;
    
    
    /**
     * Creates new form WndGererEntites
     * @param entities
     * @param behaviours
     */
    public WndEditCompartment(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.entities=entities;
        this.behaviours=behaviours;
        this.compartment=compartment;
        initComponents();
    }
    
    /**
     * Load of a compartment
     * @param comp
     */
    public void WndCliValue_Load(Compartment comp) {
        // Set language
        if (Lang.getInstance().getLang().equals("FR")) {
            jLabelNom.setText("Nom");
            jLabelCenter.setText("Centre");
            jLabelRadius.setText("Rayon");
            jLabelApp.setText("Apparence");
            jCheckBox_vidable.setText("Vidable");
            jLabelDescr.setText("Description");
            button_CANCEL.setText("Annuler");
            buttonCouleur.setText("Couleur");

        } else {
            jLabelNom.setText("Name");
            jLabelCenter.setText("Center");
            jLabelRadius.setText("Radius");
            jLabelApp.setText("Appearance");
            jCheckBox_vidable.setText("Cleanable");
            jLabelDescr.setText("Description");
            button_CANCEL.setText("Cancel");
            buttonCouleur.setText("Color");
        }

        if (comp != null) {
            setCli(comp);
        } else {
            _cli = new Compartment();
            _cli.Couleur = new Color(RandomGen.getInstance().nextInt(250),RandomGen.getInstance().nextInt(250), RandomGen.getInstance().nextInt(250));
        }
        
        _old_name = _cli.getEtiquettes();
        _old_color = _cli.Couleur;

        textBox1.setText(_cli.getEtiquettes());
        textBoxCenterX.setText(Integer.toString(_cli.getCenter().x));
        textBoxCenterY.setText(Integer.toString(_cli.getCenter().y));
        textBoxRadius.setText(Integer.toString(_cli.getRadius()));
        // Appearance
        buttonCouleur.setBackground(_cli.Couleur);
        
        richTextBox_description.setText(_cli.getDescription().getText());
        jCheckBox_vidable.setSelected(_cli.Vidable);
        
    }

    /**
     * Get the name of the compartment
     * @return
     */
    public Compartment getCli() {
        return _cli;
    }
    
    /**
     * Put a new value to the name of the compartment
     * @param _cli
     */
    public void setCli(Compartment _cli) {
        this._cli = _cli;
    }

  /**
   * Get the content of DialogResult
   * @return
   */
    public String getDialogResult() {
        return DialogResult;
    }

    /**
     * Initialization of the parameters 
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	
    	jLabelCompartment = new javax.swing.JLabel();
        jLabelNom = new javax.swing.JLabel();
        jLabelCenter = new javax.swing.JLabel();
        textBoxCenterX = new javax.swing.JTextField();
        textBoxCenterY = new javax.swing.JTextField();
        jLabelRadius = new javax.swing.JLabel();
        textBoxRadius = new javax.swing.JTextField();
        jLabelApp = new javax.swing.JLabel();
        textBox1 = new javax.swing.JTextField();
        buttonCouleur = new javax.swing.JButton();
        jCheckBox_vidable = new javax.swing.JCheckBox();
        button_OK = new javax.swing.JButton();
        button_CANCEL = new javax.swing.JButton();
        jLabelDescr = new javax.swing.JLabel();
        button_aide_description = new javax.swing.JButton();
        richTextBox_description = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
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
        if (Lang.getInstance().getLang().equals("FR")) {
        	jLabelNom.setText("Nom");
        }
        else{
        	jLabelNom.setText("Name");
        }
        getContentPane().add(jLabelNom);
        jLabelNom.setBounds(10, 40, 50, 15);
        
        jLabelCenter.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
        	jLabelCenter.setText("Centre");
        }
        else{
        	jLabelCenter.setText("Center");
        }
        getContentPane().add(jLabelCenter);
        jLabelCenter.setBounds(10, 70, 50, 15);
        
        textBoxCenterX.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCenterX.setText("0");
        getContentPane().add(textBoxCenterX);
        textBoxCenterX.setBounds(60, 70, 100, 20);
        
        textBoxCenterY.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBoxCenterY.setText("0");
        getContentPane().add(textBoxCenterY);
        textBoxCenterY.setBounds(170, 70, 100, 20);
        
        jLabelRadius.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabelRadius.setText("0");
        getContentPane().add(jLabelRadius);
        jLabelRadius.setBounds(10, 100, 50, 15);
             
        textBoxRadius.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
        	textBoxRadius.setText("Rayon");
        }
        else{
        	textBoxRadius.setText("Radius");
        }
        
        getContentPane().add(textBoxRadius);
        textBoxRadius.setBounds(60, 100, 220, 20);

        jLabelApp.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
        	jLabelApp.setText("Apparence");
        }
        else{
        	jLabelApp.setText("Appearance");
        }
        getContentPane().add(jLabelApp);
        jLabelApp.setBounds(10, 130, 70, 15);

        textBox1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
        	textBox1.setText("Nom");
        }
        else{
        	textBox1.setText("Name");
        }
        
        textBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBox1KeyTyped(evt);
            }
        });
        getContentPane().add(textBox1);
        textBox1.setBounds(60, 40, 220, 20);

        buttonCouleur.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
            buttonCouleur.setText("Couleur");       	
        }
        else{
            buttonCouleur.setText("Color");       	
        }
        buttonCouleur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonCouleurMouseClicked(evt);
            }
        });
        getContentPane().add(buttonCouleur);
        buttonCouleur.setBounds(80, 130, 80, 21);

        jCheckBox_vidable.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jCheckBox_vidable.setSelected(true);
        jCheckBox_vidable.setText("Vidable");
        jCheckBox_vidable.setToolTipText("");
        jCheckBox_vidable.setContentAreaFilled(false);
        jCheckBox_vidable.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jCheckBox_vidable.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        getContentPane().add(jCheckBox_vidable);
        jCheckBox_vidable.setBounds(10, 190, 110, 20);

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
        button_OK.setBounds(0, 220, 280, 23);

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
        button_CANCEL.setBounds(293, 220, 210, 23);

        jLabelDescr.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
            jLabelDescr.setText("Description de l'entite");        	
        }
        else{
            jLabelDescr.setText("Description of the entity");        	
        }

        getContentPane().add(jLabelDescr);
        jLabelDescr.setBounds(300, 40, 141, 15);

        button_aide_description.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        button_aide_description.setText("?");
        button_aide_description.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_aide_descriptionMouseClicked(evt);
            }
        });
        getContentPane().add(button_aide_description);
        button_aide_description.setBounds(447, 33, 40, 23);
        
        jLabelCompartment.setBackground(new java.awt.Color(153, 153, 255));
        jLabelCompartment.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabelCompartment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        if (Lang.getInstance().getLang().equals("FR")) {
            jLabelCompartment.setText("Compartiment");        
        }
        else{
            jLabelCompartment.setText("Compartment");        	
        }
        getContentPane().add(jLabelCompartment);
        jLabelCompartment.setBounds(0, 0, 533, 28);
        
        richTextBox_description.setColumns(20);
        richTextBox_description.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        richTextBox_description.setLineWrap(true);
        richTextBox_description.setRows(5);
        getContentPane().add(richTextBox_description);
        richTextBox_description.setBounds(300, 69, 190, 130);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(300, 71, 2, 2);

        jLabel19.setBackground(new java.awt.Color(153, 153, 255));
        jLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel19.setOpaque(true);
        getContentPane().add(jLabel19);
        jLabel19.setBounds(0, 0, 533, 30);

        setSize(new java.awt.Dimension(533, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void button_CANCELMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_CANCELMouseClicked
// TODO add your handling code here:

    }//GEN-LAST:event_button_CANCELMouseClicked

    /**
     * Description of the Entity
     * @param evt
     */
    private void button_aide_descriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_aide_descriptionMouseClicked
        if (Lang.getInstance().getLang().equals("FR")) {
            JOptionPane.showMessageDialog(this, "Placer ici la description de cette entité. L'accès à cette description se fera aussi par un clic-droit sur une entité dans le simulateur.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        } else {
            JOptionPane.showMessageDialog(this, "Put a short description of this entity. This description is also displayed by a right-click on a entity in the simulator.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        }

    }//GEN-LAST:event_button_aide_descriptionMouseClicked

    /**
     * Choose a color
     * @param evt
     */
    private void buttonCouleurMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonCouleurMouseClicked
        //List of colors already existing
        ArrayList<Color> lst_col = new ArrayList<Color>();
        for (int ii = 0; ii < entities.size(); ii++) {
            lst_col.add((entities.get(ii)).Couleur);
        }
        for (int ii = 0; ii < compartment.size(); ii++) {
            lst_col.add((compartment.get(ii)).Couleur);
        }
        JColorChooser colorDialog1 = new JColorChooser();
        colorDialog1.setColor(_cli.Couleur);

        Color returnColor = JColorChooser.showDialog(this, "", _cli.Couleur);
        {
            lst_col.remove(_old_color);

            if (lst_col.contains(returnColor) == true) {
                // Case where the color already exists
                if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Cette couleur d'entité existe déjà. Changement non effectué.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                } else {
                    JOptionPane.showMessageDialog(this, "This color is already in use, change not occurred", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }

            } else {
                if (returnColor != null) {
                    if (returnColor.equals(Color.WHITE)) {
                        // Case where it's white color
                        if (Lang.getInstance().getLang().equals("FR")) {
                            JOptionPane.showMessageDialog(this, "La couleur blanche est reservée pour les emplacements vides de l'environnement. Changement non effectué.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(this, "The white color is not usable, because it's reserved for empty spaces of environment, change not occurred.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                        }

                    } else {
                        _cli.Couleur = returnColor;
                        buttonCouleur.setBackground(returnColor);
                        _old_color = _cli.Couleur;
                    }
                }
            }
        }
    }//GEN-LAST:event_buttonCouleurMouseClicked

    private void button_OKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OKMouseClicked


    }//GEN-LAST:event_button_OKMouseClicked

    /**
     * Error method, when a wrong character is put
     * @param evt
     */
    private void textBox1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBox1KeyTyped
        char c = evt.getKeyChar();
        if (c == '\\' || c == '/' || c == ':' || c == ' ' || c == '*' || c == '?' || c == '\"' || c == '<' || c == '>' || c == '|') {
            evt.consume();
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Les caracteres \\ / : ESPACE * ? \" < > , et | sont interdits. Merci de votre comprehension", "ATTENTION", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "Characteres \\ / : SPACE * ? \" < > , and | are forbidden. Thanks for your understanding.", "ATTENTION", JOptionPane.INFORMATION_MESSAGE, null);
            }
        }
        if (c == '\n') {
            button_OKActionPerformed(null);
        }
    }//GEN-LAST:event_textBox1KeyTyped

    
    /**
     * Errors when the OK button is clicked
     * @param evt
     */
    public void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
    	try{
    		Integer.decode(textBoxCenterX.getText());
    		Integer.decode(textBoxCenterX.getText());
    		Integer.decode(textBoxCenterX.getText()); 
    	}catch(Exception  e){
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Les coordonnées du centre et le rayon doivent être des entiers", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "Center coordinates and radius have to be integers.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
    	}
        
        if (textBox1.getText().equals("")) {
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Merci de nommer l'entité.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "Please name the entity.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
        }

        // List of already-existing names 
        ArrayList<String> lst_str = new ArrayList<>();
        lst_str.add("Cytosol");
        for (Compartment comp : compartment) {
            lst_str.add(comp.getEtiquettes());       
        }
        
        lst_str.remove(_old_name);
        
        // Check if the name is not assigned to an entity
        for (int n = 0; n < entities.size(); n++) {
            if (entities.get(n).TrouveEtiquette(textBox1.getText()) >= 0) {
                // Case where a name already exists
                if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Ce nom existe déjà. Veuillez en changer svp.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                } else {
                    JOptionPane.showMessageDialog(this, "This name already exists, please change it.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
            }
        }

        // Check if the name is not already assigned to a behavior
        for (int n = 0; n < behaviours.size(); n++) {
            if (behaviours.get(n).TrouveEtiquette(textBox1.getText()) >= 0) {
                // Case where the name already exists
                if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Ce nom existe déjà. Veuillez en changer svp.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                } else {
                    JOptionPane.showMessageDialog(this, "This name already exists, please change it.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
            }
        }
        
     // Check if the name is not already assigned to a behavior
        if (lst_str.contains(textBox1.getText()) == true) {
            // Case where the name already exists
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Ce nom existe déjà. Veuillez en changer svp.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "This name already exists.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
        }
        try {
            _cli.setEtiquettes(textBox1.getText());
            _cli.setCenter(new UtilPoint3D(Integer.parseInt(textBoxCenterX.getText()), Integer.parseInt(textBoxCenterY.getText()), 0));
            _cli.setRadius(Integer.parseInt(textBoxRadius.getText()));
            _cli.getDescription().setText(richTextBox_description.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        
        // Appearance
        _cli.Couleur = buttonCouleur.getBackground();
        _cli.Vidable = jCheckBox_vidable.isSelected();
        //_cli.entity_property();
        this.DialogResult = new String("OK");
        setVisible(false);
        
    }//GEN-LAST:event_button_OKActionPerformed
    
    
    /**
     * Action when cliked on Cancel Button
     * @param evt
     */
    private void button_CANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_CANCELActionPerformed
        this.DialogResult = new String("CANCEL");
        setVisible(false);
    }//GEN-LAST:event_button_CANCELActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:

        //if()
    }//GEN-LAST:event_formKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCouleur;
    private javax.swing.JButton button_CANCEL;
    private javax.swing.JButton button_OK;
    private javax.swing.JButton button_aide_description;
    private javax.swing.JCheckBox jCheckBox_vidable;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabelApp;
    private javax.swing.JLabel jLabelCompartment;
    private javax.swing.JLabel jLabelDescr;
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelCenter;
    private javax.swing.JLabel jLabelRadius;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea richTextBox_description;
    private javax.swing.JTextField textBox1;
    private javax.swing.JTextField textBoxCenterX;
	private javax.swing.JTextField textBoxCenterY;
    private javax.swing.JTextField textBoxRadius;
    // End of variables declaration//GEN-END:variables
    
    public javax.swing.JTextField getTextBoxCenterX() {
		return textBoxCenterX;
	}


	public void setTextBoxCenterX(String textBoxCenterX) {
		this.textBoxCenterX.setText(textBoxCenterX);
	}


	public javax.swing.JTextField getTextBoxCenterY() {
		return textBoxCenterY;
	}


	public void setTextBoxCenterY(String textBoxCenterY) {
		this.textBoxCenterY.setText(textBoxCenterY);
	}


	public javax.swing.JTextField getTextBoxRadius() {
		return textBoxRadius;
	}


	public void setTextBoxRadius(String textBoxRadius) {
		this.textBoxRadius.setText(textBoxRadius);
	}

}
