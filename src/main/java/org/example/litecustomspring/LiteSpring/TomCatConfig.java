package org.example.litecustomspring.LiteSpring;

import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.util.List;

public class TomCatConfig {
    private static Tomcat tomcat;
    private static Context context;
    private static int port = 8080;
    private static String contextPath = "";

    public static void init() throws LifecycleException {
        tomcat = new Tomcat();
		tomcat.setBaseDir("temp");

		Connector connector = new Connector();
		connector.setPort(port);
		tomcat.setConnector(connector);

        Host host = tomcat.getHost();
        host.setName("localhost");
        host.setAppBase("webapps");

		tomcat.start();

        String docBase = new File(".").getAbsolutePath();
        context = tomcat.addContext(contextPath,docBase);

        System.out.println("Started server!");
    }

    public static void registerServlet(Object servletInstance, String urlMapping, String servletName) {
        HttpServlet servletInstanceNew = (HttpServlet) servletInstance;
        tomcat.addServlet(contextPath, servletName, servletInstanceNew);
        context.addServletMappingDecoded(urlMapping, servletName);

        System.out.println("servletName = " + servletName + "with url: " + urlMapping + " registered");

    }

    public static void registerDispatcherServlet(List<ControllerMethod> controllerMethodList) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMethodList);
        String servletName = "dispatcherServlet";
        tomcat.addServlet(contextPath,servletName , dispatcherServlet);
        context.addServletMappingDecoded("/", servletName);
    }
}
