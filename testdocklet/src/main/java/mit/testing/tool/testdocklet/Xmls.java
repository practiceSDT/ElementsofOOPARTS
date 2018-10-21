package mit.testing.tool.testdocklet;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class Xmls {
	public static void generateXmlFile(Document document, String outFileName) throws IOException, TransformerException {
		generateXmlFile(document, outFileName, "UTF-8");
	}

	public static void generateXmlFile(Document document, String outFileName, String encoding)
			throws IOException, TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer out = factory.newTransformer();
		Properties props = new Properties();
		props.put("method", "xml");
		props.put("indent", "yes");
		props.put("encoding", encoding);
		out.setOutputProperties(props);
		out.transform(new DOMSource(document),
				new StreamResult(new BufferedOutputStream(new FileOutputStream(outFileName))));
	}
}