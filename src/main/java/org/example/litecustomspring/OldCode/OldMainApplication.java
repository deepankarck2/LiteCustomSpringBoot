package org.example.litecustomspring.OldCode;

import org.apache.catalina.LifecycleException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OldMainApplication {

	public static void main(String[] args) throws LifecycleException {
//		Tomcat tomcat = new Tomcat();
//		tomcat.setBaseDir("temp");
//
//		Connector connector = new Connector();
//		connector.setPort(8080);
//		tomcat.setConnector(connector);
//
//		String contextPath = "";
//		String docBase = new File("/").getAbsolutePath();
//
//		Context context = tomcat.addContext(contextPath,docBase);
//
//		HomeServlet homeServlet = new HomeServlet();
//
//		tomcat.addServlet(contextPath, "HomeServlet", homeServlet);
//		context.addServletMappingDecoded("/","HomeServlet");
//
//		ProductRepository productRepository = new ProductRepository();
//		ProductService productService = new ProductService(productRepository);
//		SearchService searchService = new SearchService(productService);
//
//		ProductController productController = new ProductController(productService, searchService);
//		ProductServlet productServlet = new ProductServlet(productController);
//		tomcat.addServlet(contextPath, "ProductServlet", productServlet);
//		context.addServletMappingDecoded("/api/products/*", "ProductServlet");
//
//		tomcat.start();
//		System.out.println("Started server!");
//		tomcat.getServer().await();

		// SpringApplication.run(LiteCustomSpringApplication.class, args);
	}

}
