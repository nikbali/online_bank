package system.enums;

public enum TypeOperation {
    DEPOSIT("Deposit"),
    TRANSFER("Transfer"),
    TRANSFER_TO_ANOTHER_BANK("Transfer to another bank"),
    TRANSFER_FROM_ANOTHER_BANK("Transfer from another bank");

    private String name;
    TypeOperation(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
