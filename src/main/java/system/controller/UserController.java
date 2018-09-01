package system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import system.entity.User;
import system.entity.UserRole;
import system.exceptions.UserExistException;
import system.repository.RoleRepository;
import system.service.UserService;
import system.utils.CryptoUtils;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;


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
        Iterable<User> list = userService.loadAll();
        model.addObject("list", list);
        return model;
    }


    @RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView("index");
        if (error != null) {
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
        log.info("Error during signing in.");
        return new ModelAndView("redirect:/").addObject("error", true);
    }

    @RequestMapping(params = "sign-up", value="/signin", method=RequestMethod.POST)
    public ModelAndView redir(@ModelAttribute("user") User user)
    {
        ModelAndView mv = new ModelAndView("redirect:/registration");
        return mv;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration(@RequestParam(required = false) Map<String, String> userData) {
        ModelAndView model = new ModelAndView("registration");
        if (userData != null) {
            for (Map.Entry<String, String> entry : userData.entrySet()) {
                model.addObject(entry.getKey(), entry.getValue());
            }
        } else {
            model.addObject("email", "");
            model.addObject("login", "");
            model.addObject("documentNumber", "");
            model.addObject("first_name", "");
            model.addObject("last_name", "");
            model.addObject("middle_name", "");
            model.addObject("password", "");
            model.addObject("phone", "");
        }
        return model;
    }

    //изменения внесены для того, чтобы при внесении юзером инвалидых данных или др ошибках данные в форме регистрации не теряются и их не надо вносить полностью заново
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam Map<String, String> dataToSave) {

        try {
            if (userService.checkFieldsBeforeCreate(dataToSave.get("email"), dataToSave.get("login"), dataToSave.get("documentNumber"), dataToSave.get("first_name"),
                    dataToSave.get("last_name"), dataToSave.get("middle_name"), dataToSave.get("password"), dataToSave.get("phone"))) {

                User user = new User(dataToSave.get("email"), Integer.parseInt(dataToSave.get("documentNumber").replace(" ","")), dataToSave.get("login"), dataToSave.get("first_name"),
                        dataToSave.get("last_name"), dataToSave.get("middle_name"), dataToSave.get("password"), dataToSave.get("phone"));
                HashSet<UserRole> userRoles = new HashSet<>();
                userRoles.add(new UserRole(user, roleRepository.findByName("CLIENT")));
                userService.createUser(user, userRoles);
            }
        } catch (Exception ex) {
            ModelAndView model = new ModelAndView("redirect:/registration");
            if (ex instanceof UserExistException) {
                log.error("Failed to save user, user already exists : ", ex);
                UserExistException existException = (UserExistException) ex;
                model.addObject("message", existException.getMessage());
            } else {
                log.error("Failed to save user.", ex);
                dataToSave.put("message", ex.getMessage());
                for (Map.Entry<String, String> entry : dataToSave.entrySet()) {
                    model.addObject(entry.getKey(), entry.getValue());
                }
            }
            return model;
        }

        return new ModelAndView("redirect:/");
    }

}