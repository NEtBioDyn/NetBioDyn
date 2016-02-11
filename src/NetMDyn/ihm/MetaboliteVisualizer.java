package NetMDyn.ihm;


import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;

import NetMDyn.SbmlParser;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.xml.stream.XMLStreamException;

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

	public MetaboliteVisualizer(SBMLDocument document)
			throws XMLStreamException, IOException {
		// this.setModal(true);
		this.document = document;
		// parse.parse_species(document);

		bt2 = new JButton("ok");
		jtp = new JTabbedPane();
		list = new JList<String>();
		initComponents();
		JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		pnlTab.setOpaque(false);

		jtp.addTab("liste des métabolites", metabList);
		setSize(400, 500);

		JPanel jp2 = new JPanel();
		jp2.setLayout(new BorderLayout());
		JLabel metab_list2 = new JLabel(
				"choisissez les métabolites que vous voulez visualizer");
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
		bt1 = new JButton("validate");
		bt1.addActionListener(bt1AL);
		bt3=new JButton("supress");
		bt3.addActionListener(bt3AL);
		JPanel jb = new JPanel();
		jb.add(bt1);
		jb.add(bt3);
		metabList.add(jb);
		metabList.setSize(300,400);
		metabList.setVisible(true);
		this.pack();
	}

	public void setController(ControlerMetabolite c) throws XMLStreamException,
			IOException {
		this.c = c;
		bt2AL = this.c.addMetaboliteinSelection();
		bt1AL = this.c.validateSelection();
		bt3AL = this.c.supprime();
		bt2.addActionListener(bt2AL);
		
	}

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

	public int getSelectedmetaboliteIndex() {
		System.out.println(list.getSelectedIndex());
		return list.getSelectedIndex();
	}

	public int[] getselectedmetabolitesIndices() {
		
		return list.getSelectedIndices();
	}

	public int getSelectedselectionId() {
		return (jtp.getSelectedIndex() == 0 ? -1 : jtp.getSelectedIndex() - 1);
	}

	public  void alert(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

}