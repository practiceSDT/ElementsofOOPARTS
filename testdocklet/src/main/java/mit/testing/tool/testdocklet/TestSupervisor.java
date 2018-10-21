package mit.testing.tool.testdocklet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tdoclet.TestSupervisor.1;
import tdoclet.TestSupervisor.TestItemIdComparator;

public class TestSupervisor {
	public static final String DEFAULT_FILE_NAME = "testlist.xml";
	public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private DateFormat dateFormat;
	private String outputFileName;
	private String junitReportDirectory;
	private DocumentBuilder builder;
	private SortedMap testItemTable;
	private TestReport testReport;
	private XmlTestRecorder recorder;

	public TestSupervisor(String outputFileName, String junitReportDirectory)
			throws ParserConfigurationException, SAXException, IOException, ParseException {
		this(outputFileName, junitReportDirectory, "yyyy/MM/dd HH:mm:ss");
	}

	public TestSupervisor(String outputFileName, String junitReportDirectory, String dateFormat) throws ParserConfigurationException, SAXException, IOException, ParseException {
      this.testItemTable = new TreeMap(new TestItemIdComparator((1)null));
      this.outputFileName = outputFileName;
      this.junitReportDirectory = junitReportDirectory;
      this.testReport = new JUnitTestReport(junitReportDirectory);
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      this.builder = factory.newDocumentBuilder();
      this.dateFormat = new SimpleDateFormat(dateFormat);
      this.parseOldList();
   }

	public TestItem createTestItem(String testClassName, String testMethodName, String testItemName, String description)
			throws IOException, ParseException, SAXException {
		String id;
		if (this.testItemTable.size() == 0) {
			id = "0";
		} else {
			String previousId = (String) this.testItemTable.lastKey();
			id = Integer.toString(Integer.parseInt(previousId) + 1);
		}

		TestItem item = new TestItem(id, testClassName, testMethodName, testItemName, description);
		this.testItemTable.put(id, item);
		return item;
	}

	public TestItem getTestItem(String testClassName, String testMethodName, String testItemName) {
		Iterator i = this.testItemTable.values().iterator();

		TestItem each;
		do {
			if (!i.hasNext()) {
				return null;
			}

			each = (TestItem) i.next();
		} while (!testClassName.equals(each.getTestClass()) || !testMethodName.equals(each.getTestMethod())
				|| !testItemName.equals(each.getName()));

		return each;
	}

	private void parseOldList() throws ParserConfigurationException, SAXException, IOException, ParseException {
		this.recorder = new XmlTestRecorder(this.outputFileName);
		Collection tests = this.recorder.parse();
		Iterator i = tests.iterator();

		while (i.hasNext()) {
			TestItem testItem = (TestItem) i.next();
			this.testItemTable.put(testItem.getId(), testItem);
		}

	}

	public void getJUnitReport(TestItem item) throws IOException, ParseException, SAXException {
		int r = this.testReport.getResult(item);
		if (r != 0) {
			Date d = this.testReport.getDate(item);
			if (d != null) {
				String result = "";
				if (r == 1) {
					result = "success";
				} else if (r == -1) {
					result = "failure";
				} else if (r == -2) {
					result = "error";
				} else {
					result = "unknown";
				}

				item.addHistory(d, result);
				item.setAlive(true);
			}
		}
	}

	protected Document generateTestDomTree() throws IOException, SAXException, ParserConfigurationException {
		Document document = this.builder.newDocument();
		Element test = document.createElement("test");
		document.appendChild(test);
		Element changeList = document.createElement("ChangeList");
		test.appendChild(changeList);
		Iterator i = this.recorder.getUpdateHistory().iterator();

		Element list;
		while (i.hasNext()) {
			String updateRecord = (String) i.next();
			list = document.createElement("update");
			list.appendChild(document.createTextNode(updateRecord));
			changeList.appendChild(list);
		}

		Element update = document.createElement("update");
		update.appendChild(document.createTextNode(this.dateFormat.format(new Date())));
		changeList.appendChild(update);
		list = document.createElement("TestList");
		test.appendChild(list);
		Iterator i = this.testItemTable.values().iterator();

		while (i.hasNext()) {
			TestItem each = (TestItem) i.next();
			Element item = this.createTestItemElement(document, each);
			list.appendChild(item);
		}

		return document;
	}

	private Element createTestItemElement(Document document, TestItem item) {
		Element element = document.createElement("item");
		element.setAttribute("id", item.getId());
		element.setAttribute("testClass", item.getTestClass());
		element.setAttribute("testMethod", item.getTestMethod());
		element.setAttribute("name", item.getName());
		Element author;
		if (item.getTarget() != null) {
			author = document.createElement("target");
			author.appendChild(document.createTextNode(item.getTarget()));
			element.appendChild(author);
		}

		if (item.getDescription() != null) {
			author = document.createElement("description");
			author.appendChild(document.createTextNode(item.getDescription()));
			element.appendChild(author);
		}

		if (item.getAuthor() != null) {
			author = document.createElement("author");
			author.appendChild(document.createTextNode(item.getAuthor()));
			element.appendChild(author);
		}

		Iterator j = item.getHistory().entrySet().iterator();

		while (j.hasNext()) {
			Entry entry = (Entry) j.next();
			Date date = (Date) entry.getKey();
			String result = (String) entry.getValue();
			Element history = document.createElement("history");
			history.setAttribute("date", this.dateFormat.format(date));
			history.setAttribute("result", result);
			element.appendChild(history);
		}

		Element state = document.createElement("state");
		if (item.isAlive()) {
			state.appendChild(document.createTextNode("alive"));
		} else {
			state.appendChild(document.createTextNode("dead"));
		}

		element.appendChild(state);
		return element;
	}
}