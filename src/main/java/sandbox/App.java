package sandbox;

import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
//import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import sandbox.rest.qr.RestQRCode;

public class App {
	private static Logger logger = Logger.getLogger(App.class.getName());

	public static void main(String[] args) throws Exception {

		final int PORT = 8080;

		// 1. Create Server
		Server server = new Server(PORT);

		// 2. Create WebAppContext
		WebAppContext ctx = new WebAppContext();
		ctx.setContextPath("/");
		ctx.setResourceBase("src/main/webapp");

		// 3. Including the JSTL jars for the wrebapp.
		ctx.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*jstl.*\\.jar$");
		
		// 4. Enabling the Annotation based configuration
		Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");

		// *************************** jersey... *************************** //
		ServletHolder servletHolder = ctx.addServlet(
				org.glassfish.jersey.servlet.ServletContainer.class,
				"/rest/*");
		servletHolder.setInitOrder(1);
		servletHolder.setInitParameter(
				"jersey.config.server.provider.packages", 
				"sandbox.rest");
		servletHolder.setInitParameter(
				"jersey.config.server.provider.classnames",
				MultiPartFeature.class.getCanonicalName()+","+
				RestQRCode.class.getCanonicalName());
		// ***************************************************************** //
		
		// 5. Setting the handler and starting the Server
		try {
			server.setHandler(ctx);
			server.start();
			server.join();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
}
