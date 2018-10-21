package mit.testing.tool.testdocklet;

import java.util.Date;

public interface TestReport {
	int RESULT_SUCCESS = 1;
	int RESULT_FAILURE = -1;
	int RESULT_ERROR = -2;
	int RESULT_UNKNOWN = 0;

	int getResult(TestItem var1);

	Date getDate(TestItem var1);
}