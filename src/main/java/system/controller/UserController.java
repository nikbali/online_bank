package system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import system.entity.User;
import system.repository.UserRepository;
import system.service.UserService;

import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView("user_list");
        Iterable<User> list =  repository.findAll();
        model.addObject("list", list);
        return model;
    }

    @RequestMapping("/home")
    public String home() {
        return "users_page";
    }

    @RequestMapping(value="/add", method=RequestMethod.GET)
    public ModelAndView add(){
        ModelAndView model = new ModelAndView("add_user");
        User user = new User();
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(value="/save", method=RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("user") User user){
        repository.save(user);
        return new ModelAndView("redirect:/users/list");
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ModelAndView validateUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userFromServer", new User());
        modelAndView.setViewName("users_check_page");
        return modelAndView;
    }


}