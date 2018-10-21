package mit.testing.tool.testdocklet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class JUnitTestReport implements TestReport {

	@Getter
	private String reportPath;
	private Map table;

	public JUnitTestReport(String path) {
		this.reportPath = path;
		this.table = new HashMap<String, TestClass>();
	}

	public int getResult(TestItem item) {
		TestClass testClass = this.getTestClass(item);
		if (testClass == null) {
			return 0;
		} else {
			TestMethod testMethod = (TestMethod) testClass.getMethodTable().get(item.getTestMethod());
			return testMethod.getResult();
		}
	}

	public Date getDate(TestItem item) {
		TestClass testClass = this.getTestClass(item);
		return testClass.getRunTime();
	}

	private TestClass getTestClass(TestItem item) {
      String testClassName = item.getTestClass();
      TestClass testClass = (TestClass)this.table.get(testClassName);
      if (testClass != null) {
         return testClass;
      } else {
         testClass = new TestClass(this, testClassName);
         this.table.put(testClassName, testClass);
         return testClass;
      }
   }
}