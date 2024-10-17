package org.example.litecustomspring.OldCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.litecustomspring.Controller.ProductController;
import org.example.litecustomspring.LiteSpring.Annotation.Autowired;
import org.example.litecustomspring.LiteSpring.Annotation.Servlet;
import org.example.litecustomspring.models.Product;
import org.example.litecustomspring.models.dto.AddProductRequest;
import org.example.litecustomspring.models.dto.AddProductResponse;
import org.example.litecustomspring.models.dto.SearchResponse;

import java.io.BufferedReader;
import java.io.IOException;

@Servlet(urlMapping = "/api/products/*")
public class OldProductServlet extends HttpServlet {

    @Autowired
    private ProductController productController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI(); // e.g., /products/123
        System.out.println("uri = " + uri);
        String id = extractIdFromUri(uri); // Extract the id from the URI
        System.out.println("id = " + id);
        ObjectMapper objectMapper = new ObjectMapper();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if(uri.contains("/search")){
            String searchQuery = req.getParameter("query");
            SearchResponse response = productController.search(searchQuery);
            resp.getWriter().write(objectMapper.writeValueAsString(response));
        } else {
            Product product = productController.getProduct(id);
            resp.getWriter().write(objectMapper.writeValueAsString(product));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = readJson(req);
        System.out.println("body = " + body);

        ObjectMapper objectMapper = new ObjectMapper();
        AddProductRequest addProductRequest = objectMapper.readValue(body, AddProductRequest.class);
        AddProductResponse addProductResponse = productController.addProduct(addProductRequest);

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(addProductResponse));
    }

    private String extractIdFromUri(String uri) {
        // Split the URI to extract the id
        String[] segments = uri.split("/");
        return segments[segments.length - 1]; // The last segment is the ID
    }

    public String readJson(HttpServletRequest httpServletRequest){
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        try {
            BufferedReader bufferedReader = httpServletRequest.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }

        return jsonBuilder.toString();
    }
}
