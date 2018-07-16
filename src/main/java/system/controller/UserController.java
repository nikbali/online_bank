package system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import system.entity.User;
import system.service.UserService;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    String getAllUsers() {
        return userService.loadAll().toString();
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ModelAndView validateUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userFromServer", new User());
        modelAndView.setViewName("users_check_page");
        return modelAndView;
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public @ResponseBody
    String checkUser(@ModelAttribute("userFromServer") User user) {
        User u = userService.findByName(user.getName());
        if (u.getPassword().equals(user.getPassword())) {
            return "valid";
        }
        return "invalid";
    }
}