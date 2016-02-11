package NetMDyn.ihm;


import java.awt.BorderLayout; // Arrange and resize the container components to fit in five regions: north, south, east, west, and center
import java.awt.Checkbox; // Graphical component that can be in either an "on" (true) or "off" (false) state
import java.awt.Dimension; // Encapsulate the width and height of a component in a single object
import java.awt.FlowLayout; // Arrange components in a directional flow, much like lines of text in a paragraph
import java.awt.GridLayout; // Layout manager that lays out a container's components in a rectangular grid
import java.awt.event.ActionListener; // The listener interface for receiving action events
import java.awt.event.MouseAdapter; // An abstract adapter class for receiving mouse events
import java.awt.event.MouseEvent; // Indicates that a mouse action occurred in a component
import java.io.IOException; // Signals that an I/O exception of some sort has occurred
import java.util.ArrayList; // Possible creation of tables
import java.util.List; // Possible creation of lists

import javax.swing.JFrame; //Possible creation of windows

import netbiodyn.util.Lang;

import org.sbml.jsbml.SBMLDocument; // Represents the 'sbml' root node of a SBML file.
import org.sbml.jsbml.Species; // Represents the species XML element of a SBML file.

import NetMDyn.SbmlParser;

import javax.swing.BoxLayout; // A layout manager that allows multiple components to be laid out either vertically or horizontally
import javax.swing.ButtonGroup; // Create a multiple-exclusion scope for a set of buttons
import javax.swing.DefaultListModel;
import javax.swing.JButton; // An implementation of a "push" button
import javax.swing.JCheckBox; // An implementation of a check box -- an item that can be selected or deselected, and which displays its state to the user
import javax.swing.JDialog; // The main class for creating a dialog window
import javax.swing.JLabel; // A display area for a short text string or an image, or both
import javax.swing.JList; // A component that displays a list of objects and allows the user to select one or more items
import javax.swing.JOptionPane; // Possible creation of windows
import javax.swing.JPanel; // A generic lightweight container
import javax.swing.JRadioButton; // An implementation of a radio button -- an item that can be selected or deselected, and which displays its state to the user
import javax.swing.JScrollPane; // Provides a scrollable view of a lightweight component
import javax.swing.JTabbedPane; //A component that lets the user switch between a group of components by clicking on a tab with a given title and/or icon
import javax.swing.JTextField; //Lightweight component that allows the editing of a single line of text
import javax.swing.JTree; // A control that displays a set of hierarchical data as an outline
import javax.xml.stream.XMLStreamException; // The base exception for unexpected processing errors

public class MetaboliteVisualizer extends javax.swing.JFrame {
	private SBMLDocument document;
	private JList<String> list;
	private JTabbedPane jtp;
	private ActionListener bt1AL;
	private ActionListener bt2AL;
	private ActionListener bt3AL;
	private SbmlParser parse = new SbmlParser();
	private JButton bt1;
	private JButton bt2;
	private JButton bt3;
	private JPanel metabList = new JPanel();
	private ControlerMetabolite c;
	private static final long serialVersionUID = 6864318867423022411L;

	//Initialization of MetaboliteVisualizer object
	public MetaboliteVisualizer(SBMLDocument document)
			throws XMLStreamException, IOException {
		// this.setModal(true);
		this.document = document;
		// parse.parse_species(document);

		bt2 = new JButton("Ok");
		jtp = new JTabbedPane();
		list = new JList<String>();
		initComponents();
		JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		pnlTab.setOpaque(false);
		
		if (Lang.getInstance().getLang().equals("FR")) {
			jtp.addTab("Liste des métabolites", metabList);
		}
		else{
			jtp.addTab("List of metabolites", metabList);
		}
		setSize(400, 500);

		JPanel jp2 = new JPanel();
		jp2.setLayout(new BorderLayout());
		String textmetab_list2 = "";
		if (Lang.getInstance().getLang().equals("FR")) {
			textmetab_list2="Choisissez les métabolites que vous voulez visualiser";
		}
		else{
			textmetab_list2="Choose the metabolites you want to show";
		}
		JLabel metab_list2 = new JLabel(textmetab_list2);
		jp2.add(metab_list2, BorderLayout.NORTH);
		jp2.add(new JScrollPane(list), BorderLayout.CENTER);
		jp2.add(bt2, BorderLayout.SOUTH);
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(jtp);
		getContentPane().add(jp2);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);

	}

	//Retrieve selected metabolites and put them into left list
	public void renderSelection(ArrayList<String> title) {
		metabList.removeAll();
		metabList.setLayout(new BoxLayout(metabList, BoxLayout.Y_AXIS));
		JPanel content = new JPanel();
		
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		for (int i = 0; i < title.size(); i++) {
			
			JPanel jpline1 = new JPanel();
			jpline1.add(new JLabel(title.get(i)));
			content.add(jpline1, 0);
		}
		metabList.add(new JScrollPane(content));
		String textbt1 = "";
		if (Lang.getInstance().getLang().equals("FR")) {
			textbt1="Valider";
		}
		else{
			textbt1="Validate";
		}
		bt1 = new JButton(textbt1);
		bt1.addActionListener(bt1AL);
		JPanel jb = new JPanel();
		jb.add(bt1);
		metabList.add(jb);
		metabList.setSize(300,400);
		metabList.setVisible(true);
		this.pack();
	}

	//Put a new Controller to the MetaboliteVisualizer
	public void setController(ControlerMetabolite c) throws XMLStreamException, IOException {
		this.c = c;
		bt2AL = this.c.addMetaboliteinSelection();
		bt1AL = this.c.validateSelection();
		bt3AL = this.c.supprime();
		bt2.addActionListener(bt2AL);
	}

	//Initialization of the elements into the window of MetaboliteVisualizer
	public void initComponents() throws XMLStreamException, IOException {
		parse.parseSpeciesName(document);
		DefaultListModel<String> lm = new DefaultListModel<String>();
		for (String entity : parse.getEntitiesName()) {
			lm.addElement(entity);
		}
		System.out.println(lm);
		list.setModel(lm);
		list.setPreferredSize(new Dimension(550, 700));

	}

	//Return a table of indices of selected metabolites
	public int[] getselectedmetabolitesIndices() {
		return list.getSelectedIndices();
	}

	public int getSelectedselectionId() {
		return (jtp.getSelectedIndex() == 0 ? -1 : jtp.getSelectedIndex() - 1);
	}

	//Error message
	public void alert(String msg) {
		if (Lang.getInstance().getLang().equals("FR")) {
			JOptionPane.showMessageDialog(this, msg, "Erreur",JOptionPane.ERROR_MESSAGE);
		}
		else{
			JOptionPane.showMessageDialog(this, msg, "Error",JOptionPane.ERROR_MESSAGE);
		}
	}

}