package system.exceptions;


public class IncorrectedFieldsException extends Exception {

    private String field;
    public IncorrectedFieldsException(String nameField, String discription){
        super("Field: " + nameField+ " " + discription);
        this.field=nameField;
    }
}
