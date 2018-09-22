package system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import system.entity.User;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;

/**
 * Контроллер обрабатывает /admin/*  запросы, для пользователей с ролью ADMIN
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping( method = RequestMethod.GET)
    public ModelAndView toMainAdminView(HttpSession session)
    {
        ModelAndView model = new ModelAndView("admin");
        User user = UserUtils.getUserFromSession(session);
        model.addObject("user", user);
        return model;
    }
}
