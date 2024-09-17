package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.container.BeanContainer;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private final Method handler;

    public HandlerExecution(Method handler) {
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Class<?> clazz = handler.getDeclaringClass();
        Object controller = BeanContainer.getInstance().getBean(clazz);
        Object[] parameters = createParameters(request, response);

        return (ModelAndView) handler.invoke(controller, parameters);
    }

    private Object[] createParameters(HttpServletRequest request, HttpServletResponse response) {
        return Arrays.stream(handler.getParameterTypes())
                .map(type -> getParameter(request, response, type))
                .toArray();
    }

    private Object getParameter(HttpServletRequest request, HttpServletResponse response, Class<?> type) {
        if (type.equals(HttpServletRequest.class)) {
            return request;
        }
        if (type.equals(HttpServletResponse.class)) {
            return response;
        }
        throw new IllegalArgumentException("지원하지 않는 파라미터 입니다.");
    }
}