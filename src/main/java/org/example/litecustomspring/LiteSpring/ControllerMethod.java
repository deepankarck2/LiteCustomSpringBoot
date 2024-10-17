package org.example.litecustomspring.LiteSpring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@Builder
public class ControllerMethod {
    private Class<?> clazz;
    private Object object;
    private Method method;
    private MethodType methodType;
    private String url;
}
