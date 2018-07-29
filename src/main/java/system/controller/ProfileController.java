package system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import system.entity.User;
import system.service.UserService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @RequestMapping( method = RequestMethod.GET)
    public String profile(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session, Model model)
    {
        UserUtils.deleteUserFromSession(session);
        return "redirect:/";

    }


}
