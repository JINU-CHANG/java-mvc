package com.techcourse.controller.manual;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class ManualRegisterController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        final User user = new User(
            null,
            request.getParameter("account"),
            request.getParameter("password"),
            request.getParameter("email")
        );
        InMemoryUserRepository.save(user);

        return "redirect:/index.jsp";
    }
}