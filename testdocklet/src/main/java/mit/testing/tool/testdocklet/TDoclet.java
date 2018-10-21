package mit.testing.tool.testdocklet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TDoclet {
	public static final String DEFAULT_TAG_TEST = "test";
	public static final String DEFAULT_TAG_TARGET = "target";
	public static final String DEFAULT_ENCODING = "UTF-8";
	private String junitReportDirectory;
	private String outFileName;
	private String encoding;

	public static boolean start(RootDoc root) {
		
		Stream<ClassDoc> stream = Arrays.stream(root.classes());
		stream.forEach(c -> 
		Stream.of(c.getClass().getMethods())
		.forEach(m -> System.out.println(m.getName()))
				);
		
		try {
			TDoclet doclet = new TDoclet(root.options());
			doclet.startGeneration(root.classes());
			return true;
		} catch (ParserConfigurationException var6) {
			var6.printStackTrace();
			System.err.println(var6.getMessage());
			return false;
		} catch (TransformerException var7) {
			var7.printStackTrace();
			System.err.println(var7.getMessage());
			return false;
		} catch (SAXException var8) {
			var8.printStackTrace();
			System.err.println(var8.getMessage());
			return false;
		} catch (IOException var9) {
			var9.printStackTrace();
			System.err.println(var9.getMessage());
			return false;
		} catch (ParseException var10) {
			var10.printStackTrace();
			System.err.println(var10.getMessage());
			return false;
		}
	}

	public static int optionLength(String option) {
		if (option.equals("-reportdir")) {
			return 2;
		} else if (option.equals("-destfile")) {
			return 2;
		} else {
			return option.equals("-tdocencoding") ? 2 : 0;
		}
	}

	public TDoclet(String[][] options) {
		for (int i = 0; i < options.length; ++i) {
			if (options[i][0].equals("-reportdir")) {
				this.junitReportDirectory = options[i][1] + File.separator;
			}

			if (options[i][0].equals("-destfile")) {
				this.outFileName = options[i][1];
			}

			if (options[i][0].equals("-tdocencoding")) {
				this.encoding = options[i][1];
			}
		}

		if (this.junitReportDirectory == null) {
			this.junitReportDirectory = "";
		}

		if (this.outFileName == null) {
			this.outFileName = "testlist.xml";
		}

		if (this.encoding == null) {
			this.encoding = "UTF-8";
		}

	}

	private void startGeneration(ClassDoc[] classes)
			throws ParserConfigurationException, TransformerException, IOException, SAXException, ParseException {
		TestSupervisor supervisor = new TestSupervisor(this.outFileName, this.junitReportDirectory);
		this.generateTestItems(supervisor, classes);
		Document doc = supervisor.generateTestDomTree();
		Xmls.generateXmlFile(doc, this.outFileName, this.encoding);
	}

	private void generateTestItems(TestSupervisor supervisor, ClassDoc[] classes)
			throws ParserConfigurationException, SAXException, IOException, ParseException {
		for (int i = 0; i < classes.length; ++i) {
			String author = null;
			Tag[] authors = classes[i].tags("author");
			if (authors.length > 0) {
				author = authors[0].text();
			}

			MethodDoc[] methods = classes[i].methods();

			for (int j = 0; j < methods.length; ++j) {
				String target = null;
				Tag[] targets = methods[j].tags("target");
				if (targets.length > 0) {
					target = targets[0].text();
				}

				Tag[] descriptions = methods[j].tags("test");

				for (int k = 0; k < descriptions.length; ++k) {
					StringTokenizer tokenizer = new StringTokenizer(descriptions[k].text(), ":");
					String name = tokenizer.nextToken().trim();
					String des = name;
					if (tokenizer.hasMoreTokens()) {
						des = tokenizer.nextToken().trim();
					}

					TestItem item = supervisor.getTestItem(classes[i].toString(), methods[j].name(), name);
					if (item == null) {
						des = des.replaceAll("\\p{Cntrl}\\p{Space}+", "");
						des = des.replaceAll("\\p{Blank}\\p{Space}+", " ");
						item = supervisor.createTestItem(classes[i].toString(), methods[j].name(), name, des);
						item.setTarget(target);
						item.setAuthor(author);
					}

					supervisor.getJUnitReport(item);
				}
			}
		}

	}
}