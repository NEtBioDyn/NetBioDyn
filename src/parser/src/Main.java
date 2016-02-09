import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
public class Main{
	public static void main(String[]arg) throws XMLStreamException, IOException {
		SBMLReader slread = new SBMLReader();
		SBMLDocument document = slread.readSBML(new File("BMID000000118667.xml"));
		new JSBMLvisualizer(document);
		Writer_nbd writer = new Writer_nbd(document);
		writer.nbd_write();
	}
}