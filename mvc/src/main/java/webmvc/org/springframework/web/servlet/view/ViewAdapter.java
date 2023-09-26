package webmvc.org.springframework.web.servlet.view;

import static webmvc.org.springframework.web.servlet.view.JspView.REDIRECT_PREFIX;
import static webmvc.org.springframework.web.servlet.view.ViewType.JSP;

import webmvc.org.springframework.web.servlet.View;

public class ViewAdapter {

    public View getView(String viewName) {
        ViewType viewType = negotiateViewType(viewName);
        if (viewType.equals(JSP)) {
            return new JspView(viewName);
        }
        return new JsonView();
    }

    private ViewType negotiateViewType(String viewName) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return JSP;
        }

        int index = viewName.indexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException("확장자명이 없습니다.");
        }

        return ViewType.from(viewName.substring(index + 1));
    }
}
