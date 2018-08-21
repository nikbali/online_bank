package system.entity;


import java.time.LocalDate;

/**
 * pojo класс, с различными активностями свзянанными с Пользователем, различные операции по его аккаунту, напоминания, изменения
 * Используется для вывода на страницу Профиля информации по активносятм, больше никакой цели не имеет
 */
public class Action {
    private final String title;
    private final String discription;
    private final LocalDate date;

    public Action(String title, String discription, LocalDate date) {
        this.title = title;
        this.discription = discription;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDiscription() {
        return discription;
    }

    public LocalDate getDate() {
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
