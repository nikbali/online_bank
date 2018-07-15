package onlinebanking.users;

import java.util.Objects;

public class User {
    private  int id;//как мне сделать ее final если я при сохранении в DB генерирую ключ и присваиваю
    private final String name;
    private final String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String name, String password) {
        //this.id = 0;
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name) + Objects.hashCode(password);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }
        if (obj==null){
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return user.getName().equals(this.getName())
                && this.getPassword().equals(user.getPassword());
    }

    @Override
    public String toString() {
        return  "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password=" + password +
                '}';
    }
}
