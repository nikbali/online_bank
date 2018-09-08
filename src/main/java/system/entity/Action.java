package system.entity;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * pojo класс, с различными активностями свзянанными с Пользователем, различные операции по его аккаунту, напоминания, изменения
 * Используется для вывода на страницу Профиля информации по активносятм, больше никакой цели не имеет
 */
public class Action {
    private final String title;
    private final String discription;
    private final String date;


    public Action(String title, String discription, Instant date) {
        this.title = title;
        this.discription = discription;
        this.date =  new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(Date.from(date));
    }

    public String getTitle() {
        return title;
    }

    public String getDiscription() {
        return discription;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Action{" +
                "title='" + title + '\'' +
                ", discription='" + discription + '\'' +
                ", date=" + date +
                '}';
    }
}
