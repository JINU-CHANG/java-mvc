package webmvc.org.springframework.web.servlet.mvc.handler.adapter;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.handler.adapter.HandlerAdapter;

public class HandlerAdaptorFinder {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdaptorFinder(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter find(final Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.support(handler))
            .findFirst()
            .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}