package mit.testing.tool.testdocklet;

import java.util.Comparator;

class TestItemIdComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		String id1 = (String) o1;
		String id2 = (String) o2;
		int k1 = Integer.parseInt(id1);
		int k2 = Integer.parseInt(id2);
		return k1 - k2;
	}
}