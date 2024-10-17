package org.example.litecustomspring;

import org.example.litecustomspring.LiteSpring.Annotation.PackageScan;
import org.example.litecustomspring.LiteSpring.LiteSpringApplicationContext;
import org.example.litecustomspring.repository.ProductRepository;

@PackageScan(scanPackages = {"org.example.litecustomspring"})
public class MainApplication {


	public static void main(String[] args) throws Exception {

		LiteSpringApplicationContext.run(MainApplication.class);
		ProductRepository productRepository = (ProductRepository) LiteSpringApplicationContext.getBean("productRepository");
//		productRepository.addProduct( new Product("sjd", "jkhdsf", "hgh"));
//		System.out.println("productRepository.getProducts() = " + productRepository.getProducts());
//		ProductController productController = (ProductController) LiteSpringApplicationContext.getBean("productController");
//		AddProductResponse res =  productController.addProduct(new AddProductRequest("dkjds"));
//		System.out.println("res.toString() = " + res.toString());
////
//		String packageName = "org.example.litecustomspring";
//		ClassLoader classLoader = MainApplication.class.getClassLoader();
//
//		String path = packageName.replace(".", "/");
//
//		URL resource = classLoader.getResource(path);
//		System.out.println("resource = " + resource);
//
//        assert resource != null;
//          File resourceFile = new File(resource.getPath().replace("%20", " "));
//
//		File[] files1 = resourceFile.listFiles();
//		// System.out.println("files = " + Arrays.toString(files1));
//
//		System.out.println("resourceFile.getAbsolutePath() = " + resourceFile.getAbsolutePath());
        // resourceFile.getAbsolutePath() = C:\Users\dip2l\Documents\digital Studio\CodeMaster\
		// LiteCustomSpring\target\classes\org\example\litecustomspring

//		List<String> files = recursiveFiles(resourceFile.getAbsolutePath());
//		System.out.println("files = " + files);
		
//		 SpringApplication.run(LiteCustomSpringApplication.class, args);
	}

}
