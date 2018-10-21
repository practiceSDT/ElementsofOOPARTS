package mit.testing.tool.testdocklet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class TestItem implements Comparable {
	private String id;
	private String target;
	private String testClass;
	private String testMethod;
	private String name;
	private String description;
	private String author;
	private SortedMap history;
	private boolean isAlive;

	public static TestItem createTestItemFromElement(Element element) throws ParseException {
		String id = element.getAttribute("id");
		String testClass = element.getAttribute("testClass");
		String testMethod = element.getAttribute("testMethod");
		String name = element.getAttribute("name");
		NodeList descriptions = element.getElementsByTagName("description");
		String description = ((Text) descriptions.item(0).getFirstChild()).getData();
		TestItem testItem = new TestItem(id, testClass, testMethod, name, description);
		NodeList targets = element.getElementsByTagName("target");
		Element target = (Element) targets.item(0);
		if (target != null) {
			Text text = (Text) target.getFirstChild();
			testItem.setTarget(text.getData());
		}

		NodeList authors = element.getElementsByTagName("author");
		Element author = (Element) authors.item(0);
		if (author != null) {
			Text text = (Text) author.getFirstChild();
			testItem.setAuthor(text.getData());
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		NodeList history = element.getElementsByTagName("history");

		for (int j = 0; j < history.getLength(); ++j) {
			Element each = (Element) history.item(j);
			testItem.addHistory(dateFormat.parse(each.getAttribute("date")), each.getAttribute("result"));
		}

		testItem.setAlive(false);
		return testItem;
	}

	public TestItem(String id, String testClass, String testMethod, String name, String description) {
		this.id = id;
		this.testClass = testClass;
		this.testMethod = testMethod;
		this.name = name;
		this.description = description;
		this.history = new TreeMap();
	}

	public String getId() {
		return this.id;
	}

	public String getTestClass() {
		return this.testClass;
	}

	public String getTestMethod() {
		return this.testMethod;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getTarget() {
		return this.target;
	}

	public String getAuthor() {
		return this.author;
	}

	public SortedMap getHistory() {
		return this.history;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void addHistory(Date d, String result) {
		Date c = (Date) d.clone();
		this.history.put(c, result);
	}

	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}

	public String toString() {
		return this.name + ":" + this.description + "(" + this.target + ")";
	}

	public int compareTo(Object o) {
		TestItem other = (TestItem) o;
		int result = this.testClass.compareTo(other.testClass);
		if (result != 0) {
			return result;
		} else {
			result = this.testMethod.compareTo(other.testMethod);
			return result != 0 ? result : this.id.compareTo(other.id);
		}
	}
}