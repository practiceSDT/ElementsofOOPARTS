package mit.plugins.getdoclet_maven_plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Echos an object string to the output screen.
 * @goal echo
 * @requiresProject false
 */
public class EchoMojo extends AbstractMojo
{
    /**
     * Any Object to print out.
     * @parameter property="echo.message" default-value="Hello World..."
     */
    private Object message;
    
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        getLog().info( message.toString() );
        getLog().info( messageHead(new MessageObject()) );
    }
    
    /**
     * Get header message from messageHead.
     * 
     * @parameter message output. type {@code MessageObject}
     * @return Returns not {@code null} when object is created set default words.
     * 
     */
    protected String messageHead(MessageObject mes){
		return mes.getMesString();
    	
    }
    
}