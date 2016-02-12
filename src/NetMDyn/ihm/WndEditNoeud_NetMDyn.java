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
 * WndEditNoeud_NetMDyn.java
 *
 * Created on February 12 2016, 16:08
 */

package NetMDyn.ihm;

import netbiodyn.util.UtilFileFilter;
import java.awt.Color; // Encapsulate colors in the default sRGB color space or colors in arbitrary color spaces identified by a ColorSpace
import java.awt.image.BufferedImage; // Possible creation of an Image with an accessible buffer of image data
import java.io.File; // An abstract representation of file and directory pathnames.
import java.net.URL; // A Uniform Resource Locator, a pointer to a "resource" on the World Wide Web
import java.util.ArrayList; // Possible creation of tables
import javax.imageio.ImageIO;// Static convenience methods for locating ImageReaders and ImageWriters, and performing simple encoding and decoding
import javax.swing.DefaultComboBoxModel; // The default model for combo boxes.
import javax.swing.JColorChooser; // Allow a user to manipulate and select a color
import javax.swing.JFileChooser; //  Allow a user to choose a file
import javax.swing.JOptionPane; // A standard dialog box that prompts users for a value or informs them of something.

import NetMDyn.Behavior_NetMDyn;
import NetMDyn.Compartment;
import NetMDyn.Entity_NetMDyn;
import netbiodyn.util.Lang;
import netbiodyn.util.RandomGen;

/**
 * Edit of the Reaxels in NetMDyn
 * 
 * @author Master 2 Bioinformatique
 */

public class WndEditNoeud_NetMDyn extends javax.swing.JDialog {

    private final ArrayList<Entity_NetMDyn> entities;
    private final ArrayList<Behavior_NetMDyn> behaviours;
    private final ArrayList<Compartment> compartment;    
    private String DialogResult = "";
    public Entity_NetMDyn _cli = null;
    String _old_name = "";
    Color _old_color = Color.black;
    
    
    /**
     * Creates new form WndGererEntites
     * @param entities
     * @param behaviours
     */
    public WndEditNoeud_NetMDyn(ArrayList<Entity_NetMDyn> entities, ArrayList<Behavior_NetMDyn> behaviours, ArrayList<Compartment> compartment) {
        this.setModal(true);
        this.entities=entities;
        this.behaviours=behaviours;
        this.compartment = compartment;
        initComponents();
    }
    
    /**
     * Load a new Reaxel
     * @param reaxel
     */
    public void WndCliValue_Load(Entity_NetMDyn reaxel) {
    	
        String[] comps = new String[compartment.size()+1];
        comps[0] = "Cytosol";
        for(int i = 1; i< compartment.size()+1; i++){
        	comps[i] = compartment.get(i-1).getEtiquettes();
        }
    	
        // Set language
        if (Lang.getInstance().getLang().equals("FR")) {
            jLabelNom.setText("Nom");
            jLabelComp.setText("Compartiment");
            jLabelEntite.setText("Entité");
            jLabelVie.setText("1/2 Vie (0=infini)");
            jLabelApp.setText("Apparence");
            jCheckBox_vidable.setText("Vidable");
            jLabelDescr.setText("Description");
            button_CANCEL.setText("Annuler");
            buttonCouleur.setText("Couleur");
            button_pas_image.setText("Pas d'image");
            
            String[] formes_fr = {"Disque", "Carre", "Triangle", "Losange", "Etoile", "Pois", "Bruit"};
            DefaultComboBoxModel model_fr = new DefaultComboBoxModel(formes_fr);
            DefaultComboBoxModel comp_fr = new DefaultComboBoxModel(comps);
            comboBox_formes.setModel(model_fr);
            comboBox_compartment.setModel(comp_fr);

        } else {
            jLabelNom.setText("Name");
            jLabelComp.setText("Compartment");
            jLabelEntite.setText("Entity");
            jLabelVie.setText("1/2 Life (0=infinite)");
            jLabelApp.setText("Appearance");
            jCheckBox_vidable.setText("Cleanable");
            jLabelDescr.setText("Description");
            button_CANCEL.setText("Cancel");
            buttonCouleur.setText("Color");
            button_pas_image.setText("No image");

            String[] formes_fr = {"Disc", "Square", "Triangle", "Diamon", "Star", "Pots", "Noise"};
            DefaultComboBoxModel model_fr = new DefaultComboBoxModel(formes_fr);
            DefaultComboBoxModel comp_fr = new DefaultComboBoxModel(comps);
            comboBox_formes.setModel(model_fr);
            comboBox_compartment.setModel(comp_fr);
        }

        if (reaxel != null) {
            setCli(reaxel);
        } else {
            _cli = new Entity_NetMDyn();
            _cli.Couleur = new Color(RandomGen.getInstance().nextInt(250),RandomGen.getInstance().nextInt(250), RandomGen.getInstance().nextInt(250));
        }
        
        _old_name = _cli._etiquettes;
        _old_color = _cli.Couleur;

        textBox1.setText(_cli._etiquettes);
        textBox_demie_vie.setText(((Double) _cli.DemieVie).toString());

        // Appearence
        buttonCouleur.setBackground(_cli.Couleur);
        comboBox_formes.setSelectedIndex(_cli._forme);
        comboBox_compartment.setSelectedItem(_cli._compartment);
        richTextBox_description.setText(_cli.getDescription().getText());
        jCheckBox_vidable.setSelected(_cli.Vidable);
    }

    public Entity_NetMDyn getCli() {
        return _cli;
    }

    public void setCli(Entity_NetMDyn _cli) {
        this._cli = _cli;
    }

    public String getDialogResult() {
        return DialogResult;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelNom = new javax.swing.JLabel();
        jLabelComp = new javax.swing.JLabel();
        jLabelVie = new javax.swing.JLabel();
        jLabelApp = new javax.swing.JLabel();
        textBox1 = new javax.swing.JTextField();
        textBox_demie_vie = new javax.swing.JTextField();
        comboBox_formes = new javax.swing.JComboBox();
        comboBox_compartment = new javax.swing.JComboBox();
        buttonCouleur = new javax.swing.JButton();
        jCheckBox_vidable = new javax.swing.JCheckBox();
        button_img = new javax.swing.JButton();
        button_pas_image = new javax.swing.JButton();
        button_OK = new javax.swing.JButton();
        button_CANCEL = new javax.swing.JButton();
        jLabelDescr = new javax.swing.JLabel();
        button_aide_description = new javax.swing.JButton();
        jLabelEntite = new javax.swing.JLabel();
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
        
        jLabelComp.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
        	jLabelComp.setText("Compartiment");
        }
        else{
        	jLabelComp.setText("Compartment");
        }
        getContentPane().add(jLabelComp);
        jLabelComp.setBounds(10, 70, 80, 15);
        
        jLabelVie.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
        	jLabelVie.setText("Demi-vie (0=infinie)");
        }
        else{
        	jLabelVie.setText("Half life (0=infinite)");
        }
        getContentPane().add(jLabelVie);
        jLabelVie.setBounds(10, 100, 92, 15);

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

        textBox_demie_vie.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        textBox_demie_vie.setText("0");
        getContentPane().add(textBox_demie_vie);
        textBox_demie_vie.setBounds(150, 100, 130, 20);

        comboBox_formes.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        String textcombobox1, textcombobox2, textcombobox3,textcombobox4,textcombobox5,textcombobox6,textcombobox7;
        if (Lang.getInstance().getLang().equals("FR")) {
        	textcombobox1="Disque";
        	textcombobox2="Carré";
        	textcombobox3="Triangle";
        	textcombobox4="Losange";
        	textcombobox5="Etoile";
        	textcombobox6="Pois";
        	textcombobox7="Bruit";
        }
        else{
        	textcombobox1="Disc";
        	textcombobox2="Square";
        	textcombobox3="Triangle";
        	textcombobox4="Diamond";
        	textcombobox5="Star";
        	textcombobox6="Pea";
        	textcombobox7="Noise";
        }
        comboBox_formes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { textcombobox1, textcombobox2, textcombobox3,textcombobox4,textcombobox5,textcombobox6,textcombobox7}));
        getContentPane().add(comboBox_formes);
        comboBox_formes.setBounds(160, 130, 120, 21);
        
        String[] comps = new String[compartment.size()+1];
        comps[0] = "Cytosol";
        for(int i = 1; i< compartment.size()+1; i++){
        	comps[i] = compartment.get(i-1).getEtiquettes();
        }
        
        comboBox_compartment.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        comboBox_compartment.setModel(new javax.swing.DefaultComboBoxModel(comps));
        getContentPane().add(comboBox_compartment);
        comboBox_compartment.setBounds(120, 70, 120, 21);

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

        button_img.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        button_img.setText("Image");
        button_img.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_imgMouseClicked(evt);
            }
        });
        getContentPane().add(button_img);
        button_img.setBounds(80, 160, 80, 23);

        button_pas_image.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        if (Lang.getInstance().getLang().equals("FR")) {
            button_pas_image.setText("Sans image");
        }
        else{
            button_pas_image.setText("No image");        	
        }

        button_pas_image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_pas_imageMouseClicked(evt);
            }
        });
        getContentPane().add(button_pas_image);
        button_pas_image.setBounds(160, 160, 120, 23);

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
            jLabelDescr.setText("Description de l'entité");        	
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

        jLabelEntite.setBackground(new java.awt.Color(153, 153, 255));
        jLabelEntite.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabelEntite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        if (Lang.getInstance().getLang().equals("FR")) {
        	jLabelEntite.setText("Entite");
        }
        else{
        	jLabelEntite.setText("Entity");
        }
        getContentPane().add(jLabelEntite);
        jLabelEntite.setBounds(0, 0, 533, 28);

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
     * Action when user wants help
     * @param evt
     */
    private void button_aide_descriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_aide_descriptionMouseClicked
        // TODO add your handling code here:
        if (Lang.getInstance().getLang().equals("FR")) {
            JOptionPane.showMessageDialog(this, "Placer ici du texte decrivant cette entite. L'accès a cette description se fera aussi par un clic droit sur une entite dans le simulateur.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        } else {
            JOptionPane.showMessageDialog(this, "Put a short description of this entity. This description is also displayed by a right-click in the simulator.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
        }

    }//GEN-LAST:event_button_aide_descriptionMouseClicked

    /**
     * Action when we want no image in the background of the box
     * @param evt
     */
    private void button_pas_imageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_pas_imageMouseClicked
        _cli.BackgroundImage = null;
        _cli._str_image_deco = "";
    }//GEN-LAST:event_button_pas_imageMouseClicked

    /**
     * Action when we want an image in the background of the box
     * @param evt
     */
    private void button_imgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_imgMouseClicked
        JFileChooser openFileDialog1 = new JFileChooser();
        UtilFileFilter filtre = new UtilFileFilter("Images", "bmp", "gif", "jpeg", "jpg", "png");
        openFileDialog1.setFileFilter(filtre);
        openFileDialog1.setDialogTitle("Image");
        openFileDialog1.setSelectedFile(new File(_cli._str_image_deco));
        int returnval = openFileDialog1.showOpenDialog(this);
        if (returnval == JFileChooser.APPROVE_OPTION) {
            String fileName = openFileDialog1.getCurrentDirectory().getAbsolutePath() + File.separatorChar + openFileDialog1.getSelectedFile().getName();
            if (!fileName.startsWith("http") && !fileName.startsWith("file")) {
                if (fileName.startsWith("/")) {
                    fileName = "file://" + fileName; // Under Linux
                } else {
                    fileName = "file:///" + fileName; // Uner Windows
                }
            }
            try {
                BufferedImage img = ImageIO.read(new URL(fileName));//.getImage();
                _cli.BackgroundImage = img; //.getImage();
                _cli._str_image_deco = openFileDialog1.getSelectedFile().getName(); //fileName; //Path.GetFileName(openFileDialog1.FileName);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }//GEN-LAST:event_button_imgMouseClicked

    /**
     * Change of color
     * @param evt
     */
    private void buttonCouleurMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonCouleurMouseClicked
        // List of already-existing colors
        ArrayList<Color> lst_col = new ArrayList<Color>();
        for (int ii = 0; ii < entities.size(); ii++) {
            lst_col.add((entities.get(ii)).Couleur);
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
                    JOptionPane.showMessageDialog(this, "This color is already in use, no change done", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }

            } else {
                if (returnColor != null) {
                    if (returnColor.equals(Color.WHITE)) {
                        // Case where the color is white
                        if (Lang.getInstance().getLang().equals("FR")) {
                            JOptionPane.showMessageDialog(this, "La couleur blanche est reservée pour les emplacements vides de l'environnement. Changement non effectue.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                        } else {
                            JOptionPane.showMessageDialog(this, "The white color is not usable because of its use for empty spaces in the environment. No change done.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
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
     * Check if one forbidden character is typed
     * @param evt
     */
    private void textBox1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBox1KeyTyped
        char c = evt.getKeyChar();
        if (c == '\\' || c == '/' || c == ',' || c == ':' || c == ' ' || c == '*' || c == '?' || c == '\"' || c == '<' || c == '>' || c == '|') {
            evt.consume();
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Les caractères \\ / , : ESPACE * ? \" < > , et | sont interdits. Merci de votre compréhension", "ATTENTION", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "Characters \\ / : SPACE * ? \" < > , and | are forbidden.", "ATTENTION", JOptionPane.INFORMATION_MESSAGE, null);
            }
        }
        if (c == '\n') {
            button_OKActionPerformed(null);
        }
    }//GEN-LAST:event_textBox1KeyTyped

    /**
     * Errors when OK button is pushed
     * @param evt
     */
    public void button_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OKActionPerformed
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
        for (Entity_NetMDyn entity : entities) {
            lst_str.add(entity._etiquettes);       
        }

        lst_str.remove(_old_name);

        // Check if the name is not already attributed to a behavior
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
        
        // Check if the name is not already attributed to a compartment
        for (int n = 0; n < compartment.size(); n++) {
            if (compartment.get(n).getEtiquettes().equals(textBox1.getText())) {
                // Case where the name already exists
                if (Lang.getInstance().getLang().equals("FR")) {
                    JOptionPane.showMessageDialog(this, "Ce nom existe déjà. Veuillez en changer svp.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                } else {
                    JOptionPane.showMessageDialog(this, "This name already exists, please change it.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
                }
                return;
            }
        }

        if (lst_str.contains(textBox1.getText()) == true) {
            // Case where the name already exists
            if (Lang.getInstance().getLang().equals("FR")) {
                JOptionPane.showMessageDialog(this, "Ce nom existe déjà. Veuillez en changer svp.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                JOptionPane.showMessageDialog(this, "This name already exists, please change it.", "Information", JOptionPane.INFORMATION_MESSAGE, null);
            }
            return;
        }
        try {
            _cli.setEtiquettes(textBox1.getText());
            _cli.getDescription().setText(richTextBox_description.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        // Appearance
        _cli.Couleur = buttonCouleur.getBackground();
        _cli._forme = comboBox_formes.getSelectedIndex();
        _cli._compartment = (String) comboBox_compartment.getSelectedItem();
        
        try {
            _cli.DemieVie = Double.parseDouble(textBox_demie_vie.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
            _cli.DemieVie = 0;
        }

        _cli.Vidable = jCheckBox_vidable.isSelected();

        this.DialogResult = new String("OK");
        setVisible(false);
    }//GEN-LAST:event_button_OKActionPerformed
    
    
    public Color getButtonCouleur() {
		return buttonCouleur.getBackground();
	}


	public void setButtonCouleur(Color bg ) {
		this.buttonCouleur.setBackground(bg); 
	}


	public String getComboBox_compartment() {
		return comboBox_compartment.getSelectedItem().toString();
	}


	public void setComboBox_compartment(String comboBox_compartment) {
		this.comboBox_compartment.setSelectedItem(comboBox_compartment);
	}


	public boolean getjCheckBox_vidable() {
		return jCheckBox_vidable.isSelected();
	}


	public void setjCheckBox_vidable(boolean b) {
		this.jCheckBox_vidable.setSelected(b);
	}


	public String getTextBox1() {
		return textBox1.getText();
	}


	public void setTextBox1(String textBox1) {
		this.textBox1.setText(textBox1);
	}


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
    private javax.swing.JButton button_img;
    private javax.swing.JButton button_pas_image;
    private javax.swing.JComboBox comboBox_formes;
    private javax.swing.JComboBox comboBox_compartment;
    private javax.swing.JCheckBox jCheckBox_vidable;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabelApp;
    private javax.swing.JLabel jLabelDescr;
    private javax.swing.JLabel jLabelEntite;
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelComp;
    private javax.swing.JLabel jLabelVie;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea richTextBox_description;
    private javax.swing.JTextField textBox1;
    private javax.swing.JTextField textBox_demie_vie;
    // End of variables declaration//GEN-END:variables

}
