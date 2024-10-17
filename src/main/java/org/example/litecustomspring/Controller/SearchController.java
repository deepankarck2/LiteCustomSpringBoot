package org.example.litecustomspring.Controller;
import org.example.litecustomspring.LiteSpring.Annotation.*;
import org.example.litecustomspring.LiteSpring.MethodType;
import org.example.litecustomspring.Service.ProductService;
import org.example.litecustomspring.Service.SearchService;
import org.example.litecustomspring.models.Product;
import org.example.litecustomspring.models.dto.SearchResponse;

import java.util.List;
@RestController
@RequestMapping(url = "/api")
public class SearchController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SearchService searchService;

    @RequestMapping(url = "/search", type = MethodType.GET)
//    @GetMapping(url = "/search")
    public SearchResponse search(@RequestParam("query") String query) {
        System.out.println("query = " + query);
        List<Product> productList = searchService.search(query);
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setProducts(productList);
        return searchResponse;
    }
}