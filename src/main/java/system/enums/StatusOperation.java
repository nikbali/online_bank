package system.enums;

public enum StatusOperation {
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    ERROR("Error");

    private String name;
    StatusOperation (String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
