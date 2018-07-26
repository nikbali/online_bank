package system.controller;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
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

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    /**
    * Обрабатывает запрос по выводу списка всех юзеров
     * @return user_list.jsp заполненую юзерами
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView getAllUsers() {
        ModelAndView model = new ModelAndView("user_list");
        Iterable<User> list =  repository.findAll();
        model.addObject("list", list);
        return model;
    }

    /**
     * Запрос к корневой странице, с формой входа
     * @return index.jsp
     */
    @RequestMapping("/")
    public ModelAndView home()
    {
        ModelAndView model = new ModelAndView("index");
        User user = new User();
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(value="/add", method=RequestMethod.GET)
    public ModelAndView add(){
        ModelAndView model = new ModelAndView("add_user");
        User user = new User();
        model.addObject("user", user);
        return model;
    }

    /**
    * Запрос по созданию пользователя
     */
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("user") User user){

        try
        {
            repository.save(user);
        }
        catch (Exception ex)
        {
            ModelAndView model = new ModelAndView("redirect:/");
            model.addObject("error", 2);
            return model;
        }

        return new ModelAndView("redirect:/list");
    }

    @RequestMapping(params = "sign-in", value="/signin", method=RequestMethod.POST)
    public ModelAndView auth(@ModelAttribute("user") User user)
    {

        if(userService.checkLoginExists(user.getLogin()))
        {
            User cur_user = userService.findByLogin(user.getLogin());
            if(cur_user.getPassword().equals(user.getPassword())) return new ModelAndView("redirect:/list");
            return new ModelAndView("redirect:/");
        }
        else
        {
            return new ModelAndView("redirect:/");
        }

    }

    /**
     * Переход с главной на страницу Регистрации
     */
    @RequestMapping(params = "sign-up", value="/signin", method=RequestMethod.POST)
    public ModelAndView redir(@ModelAttribute("user") User user)
    {
        ModelAndView mv = new ModelAndView("redirect:/registration");
        return mv;
    }

    /**
    * Страница с регистрацией
     */
    @RequestMapping(value="/registration", method=RequestMethod.GET)
    public ModelAndView registration()
    {
        ModelAndView model = new ModelAndView("registration");
        User user = new User();
        model.addObject("user", user);
        return model;
    }

}