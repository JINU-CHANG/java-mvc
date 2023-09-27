package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ForwardController {

    private static final String PATH = "/index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return PATH;
    }
}
