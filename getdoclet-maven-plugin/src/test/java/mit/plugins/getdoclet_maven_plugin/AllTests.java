package mit.plugins.getdoclet_maven_plugin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <p>[概 要] 全テスト</p>
 * <p>[詳 細] テスト・スイートクラスのAllTests。</p>
 */
@RunWith(Suite.class)
@SuiteClasses({ EchoMojoTest.class, MessageObjectTest.class })
public class AllTests {

}
