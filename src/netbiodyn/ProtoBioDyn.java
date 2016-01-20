/* This file is part of NetBioDyn.
 *
 *   NetBioDyn is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   any later version.
 *
 *   NetBioDyn is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with NetBioDyn; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package netbiodyn;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/*
 * BioDyn.java
 *
 * Created on 14 octobre 2007, 11:22
 */
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author ballet
 */
public class ProtoBioDyn extends javax.swing.JPanel{

    /**
     * Creates new form BioDyn
     */
    public ProtoBioDyn() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * 
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LblTitre = new javax.swing.JLabel();
        boxNames = new javax.swing.JTextField();
        boxManipulated = new javax.swing.JTextField();
        boxRelais = new javax.swing.JTextField();
        button_move = new javax.swing.JLabel();
        button_timer = new javax.swing.JButton();
        button_display_relations = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMaximumSize(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(null);

        LblTitre.setBackground(javax.swing.UIManager.getDefaults().getColor("RadioButtonMenuItem.selectionBackground"));
        LblTitre.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        LblTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LblTitre.setText("Titre");
        add(LblTitre);
        LblTitre.setBounds(40, 0, 89, 20);
        add(boxNames);
        boxNames.setBounds(40, 20, 80, 20);
        add(boxManipulated);
        boxManipulated.setBounds(40, 40, 40, 20);
        add(boxRelais);
        boxRelais.setBounds(80, 40, 40, 20);

        button_move.setBackground(new java.awt.Color(102, 255, 255));
        button_move.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        button_move.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_moveMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button_moveMouseReleased(evt);
            }
        });
        button_move.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                button_moveMouseDragged(evt);
            }
        });
        add(button_move);
        button_move.setBounds(0, 20, 40, 40);

        button_timer.setBackground(new java.awt.Color(51, 255, 0));
        add(button_timer);
        button_timer.setBounds(0, 0, 20, 20);

        button_display_relations.setBackground(new java.awt.Color(255, 255, 51));
        button_display_relations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_display_relationsMouseClicked(evt);
            }
        });
        add(button_display_relations);
        button_display_relations.setBounds(20, 0, 20, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void button_moveMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_moveMouseDragged
        // TODO add your handling code here:
        if (_move_box == true) {
            _x1 = evt.getX();
            _y1 = evt.getY();

            int dx = _x1 - _x0;
            int dy = _y1 - _y0;

            int newLeft = getX() + dx;
            int newTop = getY() + dy;

            setLocation(newLeft, newTop);

            _x1 = _x0;
            _y1 = _y0;

        }
    }//GEN-LAST:event_button_moveMouseDragged

    private void button_moveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_moveMouseReleased
        // TODO add your handling code here:
        if (evt.getButton() == MouseEvent.BUTTON1) {
            _move_box = false;
        }
    }//GEN-LAST:event_button_moveMouseReleased

    private void button_moveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_moveMousePressed
// TODO add your handling code here:
        int xx = LeftDansParentForm();
        int yy = TopDansParentForm();

        if (evt.getButton() == MouseEvent.BUTTON1) {
            if (_move_box == false) {
                _move_box = true;
                _x0 = evt.getX();
                _y0 = evt.getY();
            }
        }
        _selected = true;
    }//GEN-LAST:event_button_moveMousePressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        return;
               ///     if (getParent() == null) // The ctrl is alone (not in a window)
        /// return;
        // Below, the ctrl is init. & in a wnd

            // GESTIONNAIRE EVENEMENTS DESIGN_MODE
        // ***********************************
            /*if (isDesignTime() == true) // The ctrl is in DESIGN mode //////////////////
         {
         if (_init == false) // The ctrl is in a wnd but not yet init.
         {
         BioDynDesign_OnInit();
         }

         //if (DesignBehaviors == true)
         //{
         int xx = ParentForm.PointToClient(Cursor.Position).X;
         int yy = ParentForm.PointToClient(Cursor.Position).Y;

         // Resize
         if (Width != _oldWidth || Height != _oldHeight)
         {
         BioDynDesign_OnResize(Width - _oldWidth, Height - _oldHeight);
         }
         // Deplacement
         else if (Left != _oldLeft || Top != _oldTop)
         {
         BioDynDesign_OnMove(Left - _oldLeft, Top - _oldTop);
         }
         // Clic
         else if (xx >= Left && yy >= Top)
         {
         if (xx < Left + Size.Width && yy < Top + Size.Height)
         {
         BioDynDesign_OnClick(xx, yy);
         AfficherRelationsPartout();
         }
         }
         //}
         BioDynDesign_OnPaint(sender, e);

         }
         // **************************************************
         else // The ctrl is in EXECUTION mode *******************
         // **************************************************
         {*/
        /*
         if (_init == false) // The ctrl is in a wnd but not yet init.
         {
         BioDynExec_OnInit();
         } *///
        //int xx = ParentForm.PointToClient(Cursor.Position).X;
        //int yy = ParentForm.PointToClient(Cursor.Position).Y;
                //BioDynExec_OnPaint(Graphics.FromHwnd(ParentForm.Handle));
        // }
            // MAJ des changements
            /*_oldLeft = getX();
         _oldTop = getY();
         _oldWidth = getWidth();
         _oldHeight = getHeight();
         *///
    }//GEN-LAST:event_formComponentShown

    private void button_display_relationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_display_relationsMouseClicked
        // TODO add your handling code here:
        /*
            Graphics fg = null;//GEN-LAST:event_button_display_relationsMouseClicked

        // Affichage dans la fenetre parent
        Component f = _env; //MainForm();
        if (f == null) {
            fg = this.getGraphics(); // Graphics.FromHwnd(f.Handle);
        } else {
            fg = f.getGraphics();    // Graphics.FromHwnd(Parent.Handle);
        }            //DisplayRelations(fg, 0, 0);
        AfficherRelationsPartout();

        
         // Affichage dans tous les controles (evite aux relations d'etre cachees)
         List<BioDyn> lst = BioDyn_GetAllBiodynControls();
         for (int i = 0; i < lst.Count; i++)
         {
         fg = Graphics.FromHwnd(lst[i].Handle);
         int dx = ParentPositionXVersControlPositionX
         DisplayRelations(fg, 

         }*/
            //BioDynDisplayInDesigner();
            /*if (_display_relations_in_exec == false)
         _display_relations_in_exec = true;
         else
         _display_relations_in_exec = false;
         */
        //RePaintAll();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //if (_init == false) // The ctrl is in a wnd but not yet init.
        //{
        //    BioDynExec_OnInit();
        //}

        if (getParent() == null) // The ctrl is alone (not in a window)
        {
            return;
        }
            // Below, the ctrl is init. & in a wnd

            // GESTIONNAIRE EVENEMENTS DESIGN_MODE
        // ***********************************
            /* if (DesignMode == true) // The ctrl is in DESIGN mode //////////////////
         {
         if (_init == false) // The ctrl is in a wnd but not yet init.
         {
         BioDynDesign_OnInit();
         }

         //if (DesignBehaviors == true)
         //{
         int xx = ParentForm.PointToClient(Cursor.Position).X;
         int yy = ParentForm.PointToClient(Cursor.Position).Y;

         // Resize
         if (Width != _oldWidth || Height != _oldHeight)
         {
         BioDynDesign_OnResize(Width - _oldWidth, Height - _oldHeight);
         }
         // Deplacement
         else if (Left != _oldLeft || Top != _oldTop)
         {
         BioDynDesign_OnMove(Left - _oldLeft, Top - _oldTop);
         }
         // Clic
         else if (xx >= Left && yy >= Top)
         {
         if (xx < Left + Size.Width && yy < Top + Size.Height)
         {
         BioDynDesign_OnClick(xx, yy);
         AfficherRelationsPartout();
         }
         }
         //}
         BioDynDesign_OnPaint(sender, e);

         }
         // **************************************************
         else // The ctrl is in EXECUTION mode *******************
         // **************************************************
         {*/
        if (_init == false) // The ctrl is in a wnd but not yet init.
        {
            BioDynExec_OnInit();
        }

        // MAJ des changements
        _oldLeft = getX();
        _oldTop = getY();
        _oldWidth = getWidth();
        _oldHeight = getHeight();

    }

    public int LeftDansParentForm() {
        Component parent_ = this.getParent();
        int dxx = 0;
        while (parent_ != null) {
            dxx += parent_.getX();
            parent_ = parent_.getParent();
        }
        return dxx + this.getX();
    }

    public int TopDansParentForm() {
        Component parent_ = this.getParent();
        int dyy = 0;
        while (parent_ != null) {
            dyy += parent_.getX();
            parent_ = parent_.getParent();
        }
        return dyy + this.getY();
    }
    /*public int Distance(BioDyn ctrl1, BioDyn ctrl2)        {
     // Prerequists
     if (ctrl1 == null || ctrl2 == null)
     return -1;

     double dx = 1.0 * (ctrl2.getX() - ctrl1.getX());
     double dy = 1.0 * (ctrl2.getY() - ctrl1.getY());
     return (int)Math.sqrt(dx * dx + dy * dy);
     }*/

    public Component MainForm() {
//        Component parent = _env; //getParent();
        return null;
        /*
         if (parent == null)
         return null;

         while (parent.getParent() != null)
         parent = parent.getParent();
         return parent;*/
    }

    // Gestion des evenements BioDyn en mode ee�cution

    public void BioDynExec_OnInit() {
        InterfaceToRealLocation();

        _oldLeft = this.getX();
        _oldTop = getY();
        LblTitre.setText(getTitre());
        BioDyn_MajListManipules(ListFromString(getManipules()));
        _ListManipulesCourante = _ListManipules;

        //List<String> lst = ListFromString(Relais);
        //BioDyn_MajListRelais(lst);
        BioDyn_MajAllControls();

        _init = true;
    }

    public void BioDynExec_Relayer(ArrayList<ProtoBioDyn> lstManip) {
        if (getRelais().equals("")) {
            return;
        }

        if (_ListRelais.size() <= 0) {
            return;
        } else {
            _ListRelais.get(ProtoBioDyn.rnd.nextInt(_ListRelais.size())).BioDynExec_Principale(null, lstManip); // UN SEUL relais
        }
    }

    public void BioDynExec_Principale(String msg, ArrayList<ProtoBioDyn> lstManip) {
//        assert (_env != null);

        _temps_ecoule += _timeStep;

        Component f = this.getParent();
        if (f == null) {
            return;
        }

        if (lstManip == null) {
            //List<String> lst_noms_manip = ListFromString(Manipules);
            //BioDyn_MajListManipules(lst_noms_manip);
            _ListManipulesCourante = _ListManipules;
        } else {
            if (getManipules() != null) {
                if (!getManipules().equals("")) {
                    _ListManipulesCourante = BioDyn_GetBiodynControls(ListFromString(getManipules()), lstManip);
                } else {
                    _ListManipulesCourante = /*_ListManipules =*/ lstManip;
                }
            } else {
                _ListManipulesCourante = /*_ListManipules =*/ lstManip;
            }
        }
    }

        
    public void InterfaceToRealLocation() {
        _real_size_x = getWidth() * _scaleSize;
        _real_size_y = getHeight() * _scaleSize;
        _real_size_z = _size_z * _scaleSize;

        _real_position_x = getX() * _scaleSize;
        _real_position_y = getY() * _scaleSize;
        _real_position_z = _deep * _scaleSize;
    }

    public void AfficherRelationsPartout() {
//        Graphics gfx = null;
//            ///Pen pblue = new Pen(Color.blue, 1/*this.BackColor*/); // Pen(br, 1);
//        ///Pen pred = new Pen(Color.red, 2/*this.BackColor*/); // Pen(br, 1);
//
//        Component f = _env; //this.MainForm();
//        if (f == null) {
//            return;
//        }
//
//        ArrayList<Component> lst = PrendreTousLesSousControles(MainForm());
//        lst.add(MainForm());
//
//        BioDyn_MajListRelais(ListFromString(getRelais()));
//        ArrayList<ProtoBioDyn> list_ = new ArrayList<ProtoBioDyn>();
//        for (int i = 0; i < _ListManipulesCourante.size(); i++) {
//            list_.add(_ListManipulesCourante.get(i));
//        }
//        for (int i = 0; i < _ListRelais.size(); i++) {
//            list_.add(_ListRelais.get(i));
//        }
//
//        Point P0;
//
//            // Affichage dans tous les autres ctrl
//        // -----------------------------------
//        P0 = pointToScreen(this, new Point());
//        // Chaque lien doit etre affiche au travers de tous les autres ctrl
//        for (int c = 0; c < list_.size(); c++) {
//            Point P1 = pointToScreen(list_.get(c), new Point());
//
//            for (int cc = 0; cc < lst.size(); cc++) {
//                Point c0 = pointToClient(lst.get(cc), P0);
//                Point c1 = pointToClient(lst.get(cc), P1);
//
//                gfx = lst.get(cc).getGraphics(); //Graphics.FromHwnd(lst.get(cc).Handle);
//
//                if (c >= _ListManipulesCourante.size()) {
//                    gfx.setColor(Color.red);
//                } else {
//                    gfx.setColor(Color.blue);
//                }
//                gfx.drawLine(c0.x, c0.y, c1.x, c1.y);
//            }
//        }
    }

    public ArrayList<Component> PrendreTousLesSousControles(Component ctrl) {
        ArrayList<Component> lst = new ArrayList<Component>();
        for (int cti = 0; cti < ((Container) ctrl).getComponents().length; cti++) {
            lst.add(((Container) ctrl).getComponent(cti));
        }
        // Ajout de tous les sous-controles
        ArrayList<Component> lst2 = null;
        for (int cti = lst.size() - 1; cti >= 0; cti--) {
            lst2 = PrendreTousLesSousControles(lst.get(cti));
                // Concatenation de lst et lst2
            //if(lst2 != null) {
            for (int j = 0; j < lst2.size(); j++) {
                lst.add(lst2.get(j));
            }
            //}
        }

        return lst;
    }

    public void BioDyn_MajAllControls() {
        ArrayList<ProtoBioDyn> lst = BioDyn_GetAllBiodynControls();
        // Le nouveau control est ajoute aux autres ctrl deja presents
        for (int i = 0; i < lst.size(); i++) {
            lst.get(i).BioDyn_MajListManipules(ListFromString(lst.get(i).getManipules()), lst);
            lst.get(i).BioDyn_MajListRelais(ListFromString(lst.get(i).getRelais()), lst);
        }
    }


    
    Point pointToScreen(Component c, Point pt) {
        Point scr_pt = new Point();
        scr_pt.x = pt.x + c.getLocationOnScreen().x;
        scr_pt.y = pt.y + c.getLocationOnScreen().y;
        return scr_pt;
    }

    Point pointToClient(Component c, Point scr_pt) {
        Point pt = new Point();
        pt.x = scr_pt.x - c.getLocationOnScreen().x;
        pt.y = scr_pt.y - c.getLocationOnScreen().y;
        return pt;
    }

    public ArrayList<ProtoBioDyn> BioDyn_GetBiodynControls(ArrayList<String> lst_names, ArrayList<ProtoBioDyn> lst_ctrl) {
        ArrayList<ProtoBioDyn> lst_res = new ArrayList<ProtoBioDyn>();
        Component f = MainForm();// this.ParentForm;
        if (f == null) {
            return lst_res;
        }

        // Forward and backward
        for (int i = 0; i < lst_ctrl.size(); i++) {

            ProtoBioDyn ctrl = lst_ctrl.get(i);

            for (int s = 0; s < lst_names.size(); s++) {
                if (lst_names.get(s).equals("*")) {
                    lst_res.add(ctrl);
                } else if (ctrl.TrouveEtiquette(lst_names.get(s)) >= 0) {
                    lst_res.add(ctrl);
                }
            }

        }
        return lst_res;

    }

    public int TrouveEtiquette(String etiquette) {
        ArrayList<String> names = ListFromString(getEtiquettes());

        int pos = 0;

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(etiquette)) {
                return pos;
            }
            pos += names.get(i).length() + 1; // le + 1 est pour la virgule separatrice
        }

        return -1;
    }

    public ArrayList<String> toSave() {
        ArrayList<String> toSave = new ArrayList<String>();
        String classe = this.getClass().toString();
        classe = classe.replaceFirst("class BioDynPackage", "biodyn_net");
        toSave.add(classe + "\n");
        toSave.add("\tEtiquettes:" + getEtiquettes() + "\n");
        toSave.add(new String("\tvisibleDansPanel:").concat(((Boolean) this._visibleDansPanel).toString()) + "\n");
        return toSave;
    }

    public void BioDyn_MajListManipules(ArrayList<String> lst_names) {
        // All the manipulated ctrl
        _ListManipules = BioDyn_GetAllBiodynControls(lst_names);
    }

    public void BioDyn_MajListRelais(ArrayList<String> lst_names) {
        // All the manipulated ctrl
        _ListRelais = BioDyn_GetAllBiodynControls(lst_names);
    }

    public void BioDyn_MajListManipules(ArrayList<String> lst_names, ArrayList<ProtoBioDyn> lst) {
        // All the manipulated ctrl
        _ListManipules = BioDyn_GetBiodynControls(lst_names, lst);
    }

    public void BioDyn_MajListRelais(ArrayList<String> lst_names, ArrayList<ProtoBioDyn> lst) {
        // All the manipulated ctrl
        _ListRelais = BioDyn_GetBiodynControls(lst_names, lst);
    }

    /**
     * Renvoie la liste de tous les controles de type BioDyn presents dans la
     * fenêtre principale
     *
     * @return
     */
    public ArrayList<ProtoBioDyn> BioDyn_GetAllBiodynControls() {
        ArrayList<ProtoBioDyn> lst = new ArrayList<ProtoBioDyn>();
//        Container f = _env; //(Container)this.MainForm(); // MainForm();// this.ParentForm;
//        if (f == null) {
//            return lst;
//        }
//        Component[] tab_components = f.getComponents();
//        for (int c = 0; c < f.getComponentCount(); c++) {
//            Component co = tab_components[c];
//            if (co instanceof ProtoBioDyn) {
//                lst.add((ProtoBioDyn) co);
//            }
//        }
        return lst;
    }

    public ArrayList<ProtoBioDyn> BioDyn_GetAllBiodynControls(ArrayList<String> lst_names) {
        ArrayList<ProtoBioDyn> lst = new ArrayList<ProtoBioDyn>();
//        Container f = _env; //(Container)this.MainForm(); // MainForm();// this.ParentForm;
//        if (f == null) {
//            return lst;
//        }
//        Component[] tab_components = f.getComponents();
//        for (int c = 0; c < f.getComponentCount(); c++) {
//            Component co = tab_components[c];
//            if (co instanceof ProtoBioDyn) {
//                for (int s = 0; s < lst_names.size(); s++) {
//                    if (lst_names.get(s).equals("*")) {
//                        return BioDyn_GetAllBiodynControls();
//                    } else if (((ProtoBioDyn) co).TrouveEtiquette(lst_names.get(s)) >= 0) {
//                        lst.add((ProtoBioDyn) co);
//                    }
//                }
//            }
//        }
        return lst;

    }
   
    public ArrayList<String> ListFromString(String str) {
        ArrayList<String> lst = new ArrayList<String>();
        int pos_deb_champ = 0;
        if (str == null) {
            return lst;
        }
        int pos_fin_champ = str.length();
        if (pos_fin_champ == 0) {
            return lst;
        }
        for (int i = pos_deb_champ; i < pos_fin_champ; i++) {
            if (i == pos_fin_champ - 1) {
                String name = new String(str.substring(pos_deb_champ, i + 1));
                //String name = new String(str.substring(pos_deb_champ, i - pos_deb_champ + 1));
                lst.add(name);
            } else if (str.charAt(i) == ',') {
                String name = new String(str.substring(pos_deb_champ, i));
                lst.add(name);
                pos_deb_champ = i + 1;
            }
        }
        return lst;
    }

    public ArrayList<String> ListFromString(String champ, String str) {
        ArrayList<String> lst = new ArrayList<String>();
        String champ_ = champ + "(";
        if (str.contains(champ_) == true) {
            int pos_deb_champ = str.indexOf(champ_) + champ_.length();
            int pos_fin_champ = str.indexOf(')', pos_deb_champ);
            for (int i = pos_deb_champ; i <= pos_fin_champ; i++) {
                if (str.charAt(i) == ',' || str.charAt(i) == ')') {
                    lst.add(str.substring(pos_deb_champ, i - pos_deb_champ));
                    pos_deb_champ = i + 1;
                }
            }
        }
        return lst;
    }

    boolean _move_box = false;
    int _x0 = 0, _y0 = 0, _x1 = 0, _y1 = 0;
    boolean _selected = false;
    public double _timeStep = 1.0;
    public double _temps_ecoule;
    public boolean _lance = false;

    public static Random rnd = new Random();

    public ArrayList<ProtoBioDyn> _ListManipulesCourante = new ArrayList<ProtoBioDyn>();
    public ArrayList<ProtoBioDyn> _ListManipules = new ArrayList<ProtoBioDyn>();
    public ArrayList<ProtoBioDyn> _ListRelais = new ArrayList<ProtoBioDyn>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel LblTitre;
    protected javax.swing.JTextField boxManipulated;
    protected javax.swing.JTextField boxNames;
    protected javax.swing.JTextField boxRelais;
    protected javax.swing.JButton button_display_relations;
    protected javax.swing.JLabel button_move;
    protected javax.swing.JButton button_timer;
    // End of variables declaration//GEN-END:variables

    /**
     * Holds value of property manipules.
     */
    // Gestion des relations
    public String _etiquettes = "";
    public String _manipules = "";
    public String _relais = "";

    public float biodyn_test1 = 3.14159f;

    public float getbiodyn_test1() {
        return biodyn_test1;
    }

    public void setbiodyn_test1(float v) {
        biodyn_test1 = v;
    }

    public String getEtiquettes() {
        return _etiquettes;
    }

    public void setEtiquettes(String value) {
        boxNames.setText(value);
        _etiquettes = value;
        BioDyn_MajAllControls();
    }

    public String getManipules() {
        return _manipules;
    }

    public void setManipules(String value) {
        _manipules = value;
        boxManipulated.setText(value);
        BioDyn_MajAllControls();
    }

    public String getRelais() {
        return _relais;
    }

    public void setRelais(String value) {
        _relais = value;
        boxRelais.setText(value);
        BioDyn_MajAllControls();
    }

    public double getTimeStep() {
        return _timeStep;
    }

    public void setTimeStep(double value) {
        _timeStep = value;
    }

    public double getScaleSize() {
        return _scaleSize;
    }

    public void setScaleSize(double value) {
        _scaleSize = value;
    }

    public boolean getDisplayClockBox() {
        return _displayClockBox;
    }

    public void setDisplayClockBox(boolean value) {
        _displayClockBox = value;
    }

    public boolean getDisplayManipulatedField() {
        return _displayManipulatedField;
    }

    public void setDisplayManipulatedField(boolean value) {
        _displayManipulatedField = value;
    }

    public boolean getDisplayMoveBox() {
        return _displayMoveBox;
    }

    public void setDisplayMoveBox(boolean value) {
        _displayMoveBox = value;
    }

    public boolean getDisplayNamesField() {
        return _displayNamesField;
    }

    public void setDisplayNamesField(boolean value) {
        _displayNamesField = value;
    }

    public boolean getDisplayRelaisField() {
        return _displayRelaisField;
    }

    public void setDisplayRelaisField(boolean value) {
        _displayRelaisField = value;
    }

    public boolean getDisplayRelationsBox() {
        return _displayRelationsBox;
    }

    public void setDisplayRelationsBox(boolean value) {
        _displayRelationsBox = value;
    }

    public String getTitre() {
        return _titre;
    }

    public void setTitre(String value) {
        _titre = value;
        LblTitre.setText(value);
    }

    public String _titre = "";

    public boolean _displayRelationsBox = true;
    public boolean _displayClockBox = true;
    public boolean _displayMoveBox = true;
    public boolean _displayNamesField = true;
    public boolean _displayManipulatedField = true;
    public boolean _displayRelaisField = true;

    public double _real_size_x;
    public double _real_size_y;
    public double _real_size_z;

    public double _real_position_x;
    public double _real_position_y;
    public double _real_position_z;

    // Scales
    public double _scaleSize = 1.0;
    public int _timerInterval = 1;
    protected int _size_z = 0;

    public int _oldLeft = 0;
    public int _oldTop = 0;
    public int _oldWidth = 0;
    public int _oldHeight = 0;
    public int _deep = 0;

    // Attributs divers
    protected boolean _init = false;
    public boolean _visibleDansPanel = true;

      
    @Override 
    public boolean equals(Object p) {
        if(p instanceof ProtoBioDyn)
            return ((ProtoBioDyn)p)._etiquettes.equals(_etiquettes);
        else return false;
    }

}
