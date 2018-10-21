package mit.plugins.getdoclet_maven_plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class EchoMojoTest {

	@Test
	public void testMessageHead_case_normal() {
		
		MessageObject messageObject = new MessageObject();
		EchoMojo sut = new EchoMojo();
		String actual = sut.messageHead(messageObject);
		String expected = "default mes string";
		
		assertThat(actual, is(expected));
	}

	@Test
	public void testMessageHead_case_change() {
		
		MessageObject messageObject = new MessageObject();
		messageObject.setMesString("Changed");
		EchoMojo sut = new EchoMojo();
		String actual = sut.messageHead(messageObject);
		String expected = "Changed";
		
		assertThat(actual, is(expected));
	}

	@Ignore
	@Test
	public void testMessageHead_case_error() {
		
		MessageObject messageObject = new MessageObject();
		messageObject.setMesString("Changed");
		EchoMojo sut = new EchoMojo();
		String actual = sut.messageHead(messageObject);
		String expected = "";
		
		assertThat(actual, is(expected));
	}

}
