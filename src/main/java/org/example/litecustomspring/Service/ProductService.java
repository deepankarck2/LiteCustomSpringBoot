package org.example.litecustomspring.Service;

import org.example.litecustomspring.LiteSpring.Annotation.Autowired;
import org.example.litecustomspring.LiteSpring.Annotation.Component;
import org.example.litecustomspring.models.Product;
import org.example.litecustomspring.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Component
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public String addProduct(Product product){
        String id = UUID.randomUUID().toString();
        product.setId(id);

        boolean success = productRepository.addProduct(product);
        if(success) return id;

        return "";
    }

    public Product getProduct(String id){
        if(id != null){
            return productRepository.getProduct(id);
        }
        return null;
    }

    public List<Product> getAllProducts(){
        return productRepository.getProducts();
    }
}
