package system.exceptions;

import system.entity.User;

public class UserExistException  extends Exception{
    private User user;
    public UserExistException(String message, User user){
        super(message);
        this.user=user;
    }
}
