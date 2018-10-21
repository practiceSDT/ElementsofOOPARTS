package mit.plugins.getdoclet_maven_plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class MessageObjectTest {

	@Test
	public void testGetMesString() {
		MessageObject sut = new MessageObject();
		String actual = sut.getMesString();
		String expected = "default mes string";
		
		assertThat(actual, is(expected));
	}

	@Test
	public void testSetMesString() {
		MessageObject sut = new MessageObject();
		sut.setMesString("default mes string");
		String actual = sut.getMesString();
		String expected = "default mes string";
		
		assertThat(actual, is(expected));
	}

}
