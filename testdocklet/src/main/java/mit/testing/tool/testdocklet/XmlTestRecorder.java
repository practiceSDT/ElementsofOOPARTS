package mit.testing.tool.testdocklet;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XmlTestRecorder {
	private String fileName;
	private List updateHistory = new ArrayList();

	public XmlTestRecorder(String fileName) {
		this.fileName = fileName;
	}

	public Collection parse() throws ParseException {
		ArrayList record = new ArrayList();

		DocumentBuilder builder;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException var14) {
			return record;
		}

		File file = new File(this.fileName);
		if (!file.exists()) {
			return record;
		} else {
			Document document;
			try {
				document = builder.parse("file:" + this.fileName);
			} catch (SAXException var12) {
				return record;
			} catch (IOException var13) {
				return record;
			}

			Element top = document.getDocumentElement();
			NodeList updateList = top.getElementsByTagName("update");

			for (int i = 0; i < updateList.getLength(); ++i) {
				Element update = (Element) updateList.item(i);
				this.updateHistory.add(((Text) update.getFirstChild()).getData());
			}

			NodeList itemList = top.getElementsByTagName("item");

			for (int i = 0; i < itemList.getLength(); ++i) {
				Element item = (Element) itemList.item(i);
				TestItem testItem = TestItem.createTestItemFromElement(item);
				record.add(testItem);
			}

			return record;
		}
	}

	public List getUpdateHistory() {
		return Collections.unmodifiableList(this.updateHistory);
	}
}