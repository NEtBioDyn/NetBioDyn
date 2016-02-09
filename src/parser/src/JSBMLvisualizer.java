import javax.swing.JFrame;

import org.sbml.jsbml.SBMLDocument;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class JSBMLvisualizer extends JFrame 
{

	private static final long serialVersionUID = 6864318867423022411L;

	public JSBMLvisualizer(SBMLDocument document) {
		super(document.getModel().getId());
		 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().add(new JScrollPane(new JTree(document)));
		pack();
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
//	super(document.getModel().getId());
//    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//    getContentPane().add(new JScrollPane(new JTree(document)));
//    pack(); // make the frame compact
//    setAlwaysOnTop(true);
//    setLocationRelativeTo(null); // Place it at the center of the screen
//    setVisible(true);
}