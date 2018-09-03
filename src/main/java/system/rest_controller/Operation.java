package system.rest_controller;

public class Operation {
    private long id;
    private String from;
    private String to;
    private double amount;

    public Operation(){
        id=0;
    }
    public Operation(long id, String from, String to, double amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
