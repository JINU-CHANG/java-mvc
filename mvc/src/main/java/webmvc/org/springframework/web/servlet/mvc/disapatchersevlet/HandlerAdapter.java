package webmvc.org.springframework.web.servlet.mvc.disapatchersevlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean isSupport(final Object handler);

    ModelAndView handle(final HttpServletRequest request,
                        final HttpServletResponse response,
                        final Object handler) throws Exception;
}
