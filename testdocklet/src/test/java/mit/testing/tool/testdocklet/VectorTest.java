package mit.testing.tool.testdocklet;

import junit.framework.*;
import java.util.Vector;

/**
 * A sample test case, testing <code>java.util.Vector</code>.
 *
 * @author 作製者名
 */
public class VectorTest extends TestCase {

	protected Vector fEmpty;
	protected Vector fFull;

	public VectorTest(String name) {
		super(name);
	}

	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}

	protected void setUp() {
		fEmpty= new Vector();
		fFull= new Vector();
		fFull.addElement(new Integer(1));
		fFull.addElement(new Integer(2));
		fFull.addElement(new Integer(3));
	}

	public static Test suite() {
		return new TestSuite(VectorTest.class);
	}

	/**
	 * @target java.util.Vector
	 * @test capacity1 : 数多くの要素(100個まで確認)を追加できるか
	 * @test capacity2 : 数多くの要素(100個まで確認)を追加した場合にサイズは適切に獲得できるか
	 */
	public void testCapacity() {

		int size= fFull.size();
		for (int i = 0; i < 100; i++) fFull.addElement(new Integer(i));
		assertTrue(fFull.size() == 100 + size);

	}

	/**
	 * @target java.util.Vector
	 * @test clone1 : 複製を作成した場合にその要素数はオリジナルの要素数と同一であるか
	 * @test clone2 : 複製にはオリジナルと同じ要素が含まれているか
	 */
	public void testClone() {

		Vector clone= (Vector)fFull.clone(); 
		assertTrue(clone.size() == fFull.size());
		assertTrue(clone.contains(new Integer(1)));

	}

	/**
	 * @target java.util.Vector
	 * @test contains1 : 追加したオブジェクトと同じ値のものが含まれていると判断されるか
	 * @test contains2 : 空のオブジェクトの時、正しく判断されるか
	 */
	public void testContains() {

		assertTrue(fFull.contains(new Integer(1)));
		assertTrue(!fEmpty.contains(new Integer(1)));

	}

	/**
	 * @target java.util.Vector
	 * @test elementAt1 : 最初格納されたオブジェクトを取り出せるか
	 * @test elementAt2 : インデックスは 0 から始まりサイズ - 1 出終わる
	 */
	public void testElementAt() {

		Integer i = (Integer)fFull.elementAt(0);
		assertTrue(i.intValue() == 1);

		try {

			Integer j= (Integer)fFull.elementAt(fFull.size());

		} catch (ArrayIndexOutOfBoundsException e) {

			return;

		}
		fail("Should raise an ArrayIndexOutOfBoundsException");

	}

	/**
	 * @target java.util.Vector
	 * @test removeAll1 : 全ての要素を削除することができ、その後は空と判断されるか
	 * @test removeAll2 : 空のオブジェクトに対しても removeAll() を呼んでも正しく動作するか
	 */
	public void testRemoveAll() {

		fFull.removeAllElements();
		fEmpty.removeAllElements();
		assertTrue(fFull.isEmpty());
		assertTrue(fEmpty.isEmpty()); 

	}

	/**
	 * @test removeElement : 値を指定して削除した場合に、同値と判断されたオブジェクトが削除されて
	 *                       いるか
	 */
	public void testRemoveElement() {

		fFull.removeElement(new Integer(3));
		assertTrue(!fFull.contains(new Integer(3)) ); 

	}

}

