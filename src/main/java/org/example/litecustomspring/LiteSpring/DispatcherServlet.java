package org.example.litecustomspring.LiteSpring;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.litecustomspring.LiteSpring.Annotation.PathVariable;
import org.example.litecustomspring.LiteSpring.Annotation.RequestBody;
import org.example.litecustomspring.LiteSpring.Annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.reflect.Method;
import java.util.stream.Collectors;

public class DispatcherServlet extends HttpServlet {
    private final List<ControllerMethod> controllerMethods;
    private final ObjectMapper objectMapper;

    public DispatcherServlet(List<ControllerMethod> controllerMethods) {
        this.controllerMethods = controllerMethods;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, MethodType.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, MethodType.POST);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, MethodType.DELETE);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp, MethodType.PUT);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp, MethodType methodType) {
        try {
            dispatch(req, resp, methodType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp, MethodType methodType) throws Exception {
        String requestUrl = req.getRequestURI().replaceAll("/+$", ""); // Remove trailing slashes

        for (ControllerMethod controllerMethod : controllerMethods) {
            Map<String, Object> regexAndNames = createRegexAndVariableNames(controllerMethod.getUrl());
            Pattern pattern = Pattern.compile((String) regexAndNames.get("regexPattern"));
            Matcher matcher = pattern.matcher(requestUrl);

            if (matcher.matches() && methodType == controllerMethod.getMethodType()) {
                Map<String, String> pathVariables = extractPathVariables(matcher, (List<String>) regexAndNames.get("variableNames"));
                String body = readRequestBody(req, methodType);
                System.out.println("controllerMethod.getMethod().getName() = " + controllerMethod.getMethod().getName());
                
                Object responseObject = invokeMethod(req, controllerMethod, pathVariables, body);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(responseObject));
                break;
            }
        }
    }

    private String readRequestBody(HttpServletRequest req, MethodType methodType) {
        if (methodType == MethodType.POST || methodType == MethodType.PUT) {
            try (BufferedReader reader = req.getReader()) {
                return reader.lines().collect(Collectors.joining());
            } catch (IOException e) {
                System.err.println("Error reading request body: " + e);
            }
        }
        return null;
    }

    private Object invokeMethod(HttpServletRequest req, ControllerMethod controllerMethod, Map<String, String> pathVariables, String body) throws Exception {
        Method method = controllerMethod.getMethod();
        Object[] args = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            args[i] = resolveArgument(parameters[i], req, pathVariables, body);
        }
        return method.invoke(controllerMethod.getObject(), args);
    }

    private Object resolveArgument(Parameter parameter, HttpServletRequest req, Map<String, String> pathVariables, String body) throws IOException {
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return pathVariables.get(parameter.getAnnotation(PathVariable.class).value());
        }
        if (parameter.isAnnotationPresent(RequestBody.class) && body != null) {
            return objectMapper.readValue(body, parameter.getType());
        }
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            return req.getParameter(parameter.getAnnotation(RequestParam.class).value());
        }
        return null;
    }

    private Map<String, String> extractPathVariables(Matcher matcher, List<String> variableNames) {
        Map<String, String> pathVariables = new HashMap<>();
        for (int i = 0; i < variableNames.size(); i++) {
            pathVariables.put(variableNames.get(i), matcher.group(i + 1));
        }
        return pathVariables;
    }

    private Map<String, Object> createRegexAndVariableNames(String uri) {
        List<String> variableNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{([^/]+)\\}");
        Matcher matcher = pattern.matcher(uri);
        StringBuffer regexPattern = new StringBuffer();

        while (matcher.find()) {
            variableNames.add(matcher.group(1));
            matcher.appendReplacement(regexPattern, "([^/]+)");
        }
        matcher.appendTail(regexPattern);

        Map<String, Object> result = new HashMap<>();
        result.put("regexPattern", regexPattern.toString());
        result.put("variableNames", variableNames);
        return result;
    }
}

