package system.rest_controller;

public class Operation {
    private long id;
    private String fromAccount;
    private String toAccount;
    private String bic;
    private String currency;
    private String comment;
    private double amount;

    public Operation(){
    }
    public Operation(long id, String fromAccount, String toAccount, String bic, String currency, String comment, double amount) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.bic = bic;
        this.comment = comment;
        this.currency = currency;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

