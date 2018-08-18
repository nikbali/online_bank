package system.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private long id;
    private double amount;
    private Date date;
    private String description;
    private String status;
    private String type;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account reciever;

    public Transaction(){}

    public Transaction(double amount, Date date, String description, String status, String type, Account sender, Account reciever) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.status = status;
        this.type = type;
        this.sender = sender;
        this.reciever = reciever;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReciever() {
        return reciever;
    }

    public void setReciever(Account reciever) {
        this.reciever = reciever;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", sender=" + sender.getAccountNumber() +
                ", reciever=" + reciever.getAccountNumber() +
                '}';
    }
}
