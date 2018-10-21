package mit.testing.tool.testdocklet;

import lombok.Getter;

class TestMethod {
	private String name;
	@Getter
	private int result;
	private long time;
	private JUnitTestReport jUnitTestReport;

	TestMethod(JUnitTestReport this$0, String name, int result) {
		this.jUnitTestReport = this$0;
		this.name = name;
		this.result = result;
	}
}