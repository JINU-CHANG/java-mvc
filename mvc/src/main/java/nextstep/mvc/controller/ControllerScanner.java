package nextstep.mvc.controller;

import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(String packageName) {
        this.reflections = new Reflections(packageName);
    }

    public Set<Map.Entry<Class<?>, Object>> getControllers() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<?>, Object> results = new HashMap<>();
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            results.put(controller, controller.getConstructor().newInstance());
        }
        return results.entrySet();
    }
}