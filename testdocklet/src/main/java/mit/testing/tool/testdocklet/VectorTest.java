package mit.testing.tool.testdocklet;

import java.util.Vector;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class VectorTest extends TestCase {
	protected Vector fEmpty;
	protected Vector fFull;

	public VectorTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	protected void setUp() {
		this.fEmpty = new Vector();
		this.fFull = new Vector();
		this.fFull.addElement(new Integer(1));
		this.fFull.addElement(new Integer(2));
		this.fFull.addElement(new Integer(3));
	}

	public static Test suite() {
		return new TestSuite(class$samples$VectorTest == null
				? (class$samples$VectorTest = class$("samples.VectorTest"))
				: class$samples$VectorTest);
	}

	public void testCapacity() {
		int size = this.fFull.size();

		for (int i = 0; i < 100; ++i) {
			this.fFull.addElement(new Integer(i));
		}

		Assert.assertTrue(this.fFull.size() == 100 + size);
	}

	public void testClone() {
		Vector clone = (Vector) this.fFull.clone();
		Assert.assertTrue(clone.size() == this.fFull.size());
		Assert.assertTrue(clone.contains(new Integer(1)));
	}

	public void testContains() {
		Assert.assertTrue(this.fFull.contains(new Integer(1)));
		Assert.assertTrue(!this.fEmpty.contains(new Integer(1)));
	}

	public void testElementAt() {
		Integer i = (Integer) this.fFull.elementAt(0);
		Assert.assertTrue(i == 1);

		try {
			Integer var10000 = (Integer) this.fFull.elementAt(this.fFull.size());
		} catch (ArrayIndexOutOfBoundsException var3) {
			return;
		}

		Assert.fail("Should raise an ArrayIndexOutOfBoundsException");
	}

	public void testRemoveAll() {
		this.fFull.removeAllElements();
		this.fEmpty.removeAllElements();
		Assert.assertTrue(this.fFull.isEmpty());
		Assert.assertTrue(this.fEmpty.isEmpty());
	}

	public void testRemoveElement() {
		this.fFull.removeElement(new Integer(3));
		Assert.assertTrue(!this.fFull.contains(new Integer(3)));
	}
}