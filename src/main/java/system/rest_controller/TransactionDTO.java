package system.rest_controller;

/**
 * DTO - объект Операции. Необходим ля перевода и обработки переводов средств сторонних банков.
 */
public class TransactionDTO {
    private  String fromAccount;
    private  String toAccount;
    private  String currency;
    private  String comment;
    private  double amount;

    public TransactionDTO(String fromAccount,
                          String toAccount,
                          String currency,
                          String comment,
                          double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.comment = comment;
        this.currency = currency;
        this.amount = amount;
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

