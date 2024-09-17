package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(handler -> handler.isAnnotationPresent(RequestMapping.class))
                .forEach(this::addHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlers(Method handler) {
        HandlerExecution handlerExecution = new HandlerExecution(handler);
        RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        HandlerKeys handlerKeys = HandlerKeys.from(requestMapping);

        handlerKeys.getKeys()
                .forEach(handlerKey -> addHandler(handlerKey, handlerExecution));
    }

    private void addHandler(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("이미 존재하는 RequestMethod 입니다.");
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.from(method);
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        validateHandlerKey(handlerKey);

        return handlerExecutions.get(handlerKey);
    }

    private void validateHandlerKey(HandlerKey handlerKey) {
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("지원하지 않는 요청입니다.");
        }
    }
}