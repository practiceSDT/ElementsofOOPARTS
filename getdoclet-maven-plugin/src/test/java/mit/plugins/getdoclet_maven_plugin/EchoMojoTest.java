package mit.plugins.getdoclet_maven_plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class EchoMojoTest {

	@Test
	public void testMessageHead() {
		
		MessageObject messageObject = new MessageObject();
		EchoMojo sut = new EchoMojo();
		String actual = sut.messageHead(messageObject);
		String expected = "default mes string";
		
		assertThat(actual, is(expected));
	}

}
