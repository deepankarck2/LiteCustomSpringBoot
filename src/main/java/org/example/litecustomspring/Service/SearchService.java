package org.example.litecustomspring.Service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.litecustomspring.LiteSpring.Annotation.Autowired;
import org.example.litecustomspring.LiteSpring.Annotation.Component;
import org.example.litecustomspring.models.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchService {

    @Autowired
    private ProductService productService;

    public List<Product> search(String name) {
        List<Product> filterProducts = new ArrayList<>();

        List<Product> products = productService.getAllProducts();
        System.out.println("products.toString() = " + products.toString());
        for(Product product : products) {
            if(product.getName().toLowerCase().contains(name.toLowerCase())) filterProducts.add(product);
        }
        return filterProducts;
    }
}
