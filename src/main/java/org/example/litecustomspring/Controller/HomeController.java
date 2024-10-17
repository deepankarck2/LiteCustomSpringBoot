package org.example.litecustomspring.Controller;

import org.example.litecustomspring.LiteSpring.Annotation.GetMapping;
import org.example.litecustomspring.LiteSpring.Annotation.RequestMapping;
import org.example.litecustomspring.LiteSpring.Annotation.RestController;

@RestController
@RequestMapping(url = "/")
public class HomeController {
    @GetMapping
    public String helloWorld(){
        return "hello world";
    }
}
