package system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import system.entity.Role;
import system.entity.User;
import system.entity.UserRole;
import system.exceptions.UserExistException;
import system.repository.RoleRepository;
import system.service.UserService;
import system.utils.CryptoUtils;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashSet;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView("user_list");
        Iterable<User> list =  userService.loadAll();
        model.addObject("list", list);
        return model;
    }


    @RequestMapping(value = {"/", "index"} , method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value="error", required = false) String error)
    {
        ModelAndView model = new ModelAndView("index");
        if(error != null){
            model.addObject("msg", "The username or password is incorrect!");
        }
        User user = new User();
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(params = "sign-in", value="/signin", method=RequestMethod.POST)
    public ModelAndView auth(@ModelAttribute("user") User user, HttpSession session)
    {

        try {
            if (userService.checkLoginExists(user.getLogin())) {
                User cur_user = userService.findByLogin(user.getLogin());
                if (cur_user.getPassword().equals(CryptoUtils.getHash(user.getPassword())))
                {
                    session.setAttribute("user", cur_user);
                    return new ModelAndView("redirect:/main");
                }
            }
        }
        catch (Exception ex)
        {
            return new ModelAndView("redirect:/").addObject("error", true);

        }
        return new ModelAndView("redirect:/").addObject("error", true);


    }

    @RequestMapping(params = "sign-up", value="/signin", method=RequestMethod.POST)
    public ModelAndView redir(@ModelAttribute("user") User user)
    {
        ModelAndView mv = new ModelAndView("redirect:/registration");
        return mv;
    }

    @RequestMapping(value="/registration", method=RequestMethod.GET)
    public ModelAndView registration(@RequestParam(value="error", required = false) String error, @RequestParam(value="message", required = false) String error_message )
    {
        ModelAndView model = new ModelAndView("registration");
        if(error != null)
        {
            model.addObject("msg", error_message);
        }
        model.addObject("email","");
        model.addObject("login","");
        model.addObject("documentNumber","");
        model.addObject("first_name","");
        model.addObject("last_name","");
        model.addObject("middle_name","");
        model.addObject("password","");
        model.addObject("phone","");

        return model;
    }

    @RequestMapping(value="/save", method=RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("email") String email,
                             @ModelAttribute("login") String login,
                             @ModelAttribute("documentNumber") String documentNumber,
                             @ModelAttribute("first_name") String first_name,
                             @ModelAttribute("last_name") String last_name,
                             @ModelAttribute("middle_name") String middle_name,
                             @ModelAttribute("password") String password,
                             @ModelAttribute("phone") String phone){

        try
        {
            if (userService.checkFieldsBeforeCreate(email, login, documentNumber, first_name, last_name, middle_name, password, phone))
            {
                User user = new User(email, Integer.parseInt(documentNumber), login, first_name,  last_name,  middle_name, password, phone);
                HashSet<UserRole> userRoles = new HashSet<>();
                userRoles.add(new UserRole(user, roleRepository.findByName("CLIENT")));
                userService.createUser(user, userRoles);
            }

        }
        catch (Exception ex)
        {
            ModelAndView model = new ModelAndView("redirect:/registration");
            model.addObject("error", true);
            if(ex instanceof UserExistException)
            {
                UserExistException existException = (UserExistException) ex;
                model.addObject("message", existException.getMessage());
            }
            else {
                log.info("Error: " + ex.getMessage());
                model.addObject("message", "Error create User, try again!");
            }
            return model;
        }

        return new ModelAndView("redirect:/");
    }

}