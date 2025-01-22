package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        getControllers().forEach(this::registerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlers(Class<?> controller) {
        try {
            Object instanceController = controller.getConstructor().newInstance();
            Method[] declaredMethods = controller.getDeclaredMethods();

            for (Method declaredMethod : declaredMethods) {
                registerHandlersByMethod(instanceController, declaredMethod);
            }
        } catch (NoSuchMethodException exception) {
            log.error("해당 메서드가 존재하지 않습니다.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            log.error("객체를 인스턴스화 할 수 없습니다.");
        }
    }

    private void registerHandlersByMethod(Object instanceController, Method declaredMethod) {
        RequestMapping declaredAnnotation = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = declaredAnnotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(declaredAnnotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(instanceController, declaredMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
