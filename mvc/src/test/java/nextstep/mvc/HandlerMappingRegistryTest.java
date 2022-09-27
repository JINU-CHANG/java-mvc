package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.exception.HandlerAdapterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HandlerMappingRegistryTest {

    @DisplayName("적절한 Handler를 반환한다.")
    @Test
    void getHandler() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI())
                .thenReturn("/get-test");
        Mockito.when(request.getMethod())
                .thenReturn("GET");

        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @DisplayName("적절한 Handler가 없을 경우 예외를 발생시킨다.")
    @Test
    void getHandler_HandlerNotFoundException() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        HttpServletRequest invalidRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(invalidRequest.getRequestURI())
                .thenReturn("/invalid-uri");
        Mockito.when(invalidRequest.getMethod())
                .thenReturn("TRACE");

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(invalidRequest))
                .isInstanceOf(HandlerAdapterNotFoundException.class);
    }
}
