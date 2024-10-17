package org.example.litecustomspring.Controller;

import lombok.NoArgsConstructor;
import org.example.litecustomspring.LiteSpring.Annotation.*;
import org.example.litecustomspring.Service.ProductService;
import org.example.litecustomspring.Service.SearchService;
import org.example.litecustomspring.models.Product;
import org.example.litecustomspring.models.dto.AddProductRequest;
import org.example.litecustomspring.models.dto.AddProductResponse;
import org.example.litecustomspring.models.dto.SearchResponse;

import java.util.List;

@NoArgsConstructor
@RestController
@RequestMapping(url = "/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private SearchService searchService;

    @PostMapping(url = "/add")
    public AddProductResponse addProduct(@RequestBody AddProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());

        String id = productService.addProduct(product);

        AddProductResponse addProductResponse = new AddProductResponse();
        addProductResponse.setId(id);
        System.out.println("Product added. id: " + id);
        return addProductResponse;
    }

    @GetMapping(url = "/{id}")
    public Product getProduct(@PathVariable("id") String id) {
        System.out.println("id in getProduct{id) = " + id);
        return productService.getProduct(id);
    }

    @GetMapping(url = "/{id}/category/{CategoryId}")
    public Product twogetProducttwo(@PathVariable("id")String id, @PathVariable("CategoryId") String CategoryId) {
        return productService.getProduct(id);
    }

    public SearchResponse search(String name) {
        List<Product> productList = searchService.search(name);
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setProducts(productList);
        return searchResponse;
    }
}