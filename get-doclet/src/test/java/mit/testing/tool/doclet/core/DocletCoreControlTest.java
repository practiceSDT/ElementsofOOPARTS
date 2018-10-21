package mit.testing.tool.doclet.core;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Test;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

public class DocletCoreControlTest {

	@Test
	public void testStartRootDoc() {
		
		//シリアライズ実装されていなかったので別途
	    try(ObjectInputStream objInStream 
	    = new ObjectInputStream(
	      new FileInputStream("src/main/resources/data/rootDoc.bin"))){
	        RootDoc rootDoc = (RootDoc) objInStream.readObject();
			Stream<ClassDoc> stream = Arrays.stream(rootDoc.classes());
			stream.forEach(c -> {
				System.out.println(c.qualifiedName());
				Stream.of(c.methods(true)).forEach(
						m -> {
							System.out.println("->" + m.name());
							Stream.of(m.tags()).forEach(t ->  
							System.out.println("-->" + t.name() + ":" + t.text()));
						}
						);
			});
	    } catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testLanguageVersion() {
	}

	@Test
	public void testWriteTo() {
	}

}
