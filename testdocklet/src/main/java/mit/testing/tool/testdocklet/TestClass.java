package mit.testing.tool.testdocklet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.Getter;

class TestClass {
	private String name;
	
	@Getter
	private Map methodTable;
	@Getter
	private Date runTime;
	private JUnitTestReport jUnitTestReport;

	TestClass(JUnitTestReport this$0, String name) {
      this.jUnitTestReport = this$0;
      this.methodTable = new HashMap();
      File file = this.getResultXmlFile(name);
      if (file.exists()) {
         this.runTime = new Date(file.lastModified());

         try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Element element = document.getDocumentElement();
            NodeList resultsElement = element.getElementsByTagName("testcase");

            for(int i = 0; i < resultsElement.getLength(); ++i) {
               TestMethod testMethod = null;
               Element testCase = (Element)resultsElement.item(i);
               String methodName = testCase.getAttribute("name");
               NodeList failureList = testCase.getElementsByTagName("failure");
               if (failureList.getLength() > 0) {
                  testMethod = new TestMethod(this$0, methodName, -1);
                  this.methodTable.put(methodName, testMethod);
               } else {
                  NodeList errorList = testCase.getElementsByTagName("error");
                  if (errorList.getLength() > 0) {
                     testMethod = new TestMethod(this$0, methodName, -2);
                     this.methodTable.put(methodName, testMethod);
                  } else {
                     testMethod = new TestMethod(this$0, methodName, 1);
                     this.methodTable.put(methodName, testMethod);
                  }
               }
            }
         } catch (ParserConfigurationException var15) {
            ;
         } catch (SAXException var16) {
            ;
         } catch (IOException var17) {
            ;
         }

      }
   }

	private File getResultXmlFile(String name) {
		String fileName = this.jUnitTestReport.getReportPath() + "TEST-" + name + ".xml";
		return new File(fileName);
	}
}