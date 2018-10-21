package mit.plugins.getdoclet_maven_plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import com.sun.javadoc.RootDoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.Ignore;
import org.junit.Test;

public class EchoMojoTest {

	
	
	/**
	 * @author water
	 * @createdate 2018/10/20
	 * @updatedate 2018/10/20
	 * @subsystem hoge
	 * @processname fuga
	 * @functionname uhyo
	 * @abstract 概要
	 * @testkind 正常テスト
	 * @testkinddetail 初期値
	 * @actual MessageObjectにデフォルト設定されている値
	 * @expected default mes string
	 * @testresult 文字列が一致すること
	 */
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
