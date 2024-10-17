package org.example.litecustomspring.LiteSpring;

import org.example.litecustomspring.LiteSpring.Annotation.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class LiteSpringApplicationContext {

    private static final Map<String, Object> beanFactory = new HashMap<>();

    public static void run(Class<?> appClass) throws Exception {
        if (!appClass.isAnnotationPresent(PackageScan.class)) {
            throw new IllegalArgumentException("Application class must be annotated with @PackageScan");
        }

        PackageScan packageScan = appClass.getAnnotation(PackageScan.class);
        String[] packageNames = packageScan.scanPackages();

        List<Class<?>> classes = scanPackages(packageNames);
        createBeans(classes);
        injectDependencies(classes);
        
        // Optional: Display all created beans
//        beanFactory.keySet().forEach(key -> System.out.println("key = " + key));

        TomCatConfig.init();
        List<ControllerMethod> controllerMethodList = createControllerMethodList(classes);
        
        TomCatConfig.registerDispatcherServlet(controllerMethodList);
    }

    private static List<ControllerMethod> createControllerMethodList(List<Class<?>> classes) {
        List<ControllerMethod> controllerMethods = new ArrayList<>();

        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(RestController.class)) continue;

            String baseUrl = "";
            MethodType defaultMethodType = MethodType.GET;  // Default method type

            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = classRequestMapping.url();
                defaultMethodType = classRequestMapping.type();
            }

            Object controllerInstance = beanFactory.get(getSimpleClassName(clazz));

            for (Method method : clazz.getDeclaredMethods()) {
                String url = "";
                MethodType methodType = defaultMethodType;  // Start with class-level default

                // Check method-level RequestMapping and override if needed
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    url = methodRequestMapping.url();
                    methodType = methodRequestMapping.type();  // Override with method-level type
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    url = method.getAnnotation(PostMapping.class).url();
                    methodType = MethodType.POST;
                } else if (method.isAnnotationPresent(GetMapping.class)) {
                    url = method.getAnnotation(GetMapping.class).url();
                    methodType = MethodType.GET;
                }

                controllerMethods.add(ControllerMethod.builder()
                        .clazz(clazz)
                        .object(controllerInstance)
                        .method(method)
                        .methodType(methodType)
                        .url(baseUrl + url)
                        .build());
            }
        }
        return controllerMethods;
    }



    private static void registerServlets(List<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for(Class<?> clz: classes){
            if(clz.isAnnotationPresent(Servlet.class)){
                Servlet sevletAnnotation = clz.getAnnotation(Servlet.class);
                String urlMapping = sevletAnnotation.urlMapping();

                Object servletInstance = beanFactory.get(getSimpleClassName(clz));
                System.out.println("servletInstance = " + servletInstance);

                String servletName = getSimpleClassName(clz);
                TomCatConfig.registerServlet(servletInstance, urlMapping, servletName);

            }
        }
    }

    private static List<Class<?>> scanPackages(String[] packageNames) throws Exception {
        ClassLoader classLoader = LiteSpringApplicationContext.class.getClassLoader();
        List<Class<?>> classes = new ArrayList<>();

        for (String packageName : packageNames) {
            URL resource = classLoader.getResource(packageName.replace(".", "/"));
            if (resource == null) continue;
            File directory = new File(resource.getPath().replace("%20", " "));
            classes.addAll(ClassScanner.scan(directory, packageName));
        }
        return classes;
    }

    private static void createBeans(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Servlet.class) || clazz.isAnnotationPresent(RestController.class)) {
                String beanName = getSimpleClassName(clazz);
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beanFactory.put(beanName, instance);
            }
        }
    }

    private static void injectDependencies(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(Component.class) && !clazz.isAnnotationPresent(Servlet.class) && !clazz.isAnnotationPresent(RestController.class)) {
                continue;
            }

            Object beanInstance = beanFactory.get(getSimpleClassName(clazz));
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String dependencyName = field.getType().getSimpleName().toLowerCase();
                    Object dependency = beanFactory.get(dependencyName);

                    if (dependency != null) {
                        field.setAccessible(true);
                        field.set(beanInstance, dependency);
                    } else {
                        throw new RuntimeException("Unsatisfied dependency: " + dependencyName + " for " + clazz.getName());
                    }
                }
            }
        }
    }

    private static String getSimpleClassName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static Object getBean(String name) {
        return beanFactory.get(name.toLowerCase());
    }

}
