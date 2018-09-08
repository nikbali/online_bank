package system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import system.enums.StatusOperation;
import system.enums.TypeOperation;
import system.utils.json.CustomDateSerializer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    @JsonIgnore
    private long id;
    private BigDecimal amount;


    @JsonSerialize(using = CustomDateSerializer.class)
    private Instant date;

    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOperation status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeOperation type;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account reciever;

    public static final String[] COLUMNS = new String[]{"amount","date","description","status","type"};
    public static final String[] NAMES = new String[]{"Amount","Date","Description","Status","Type"};

    public Transaction(){}

    public Transaction(BigDecimal amount,
                       Instant date,
                       String description,
                       StatusOperation status,
                       TypeOperation type,
                       Account sender,
                       Account reciever) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.status = status;
        this.type = type;
        this.sender = sender;
        this.reciever = reciever;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusOperation getStatus() {
        return status;
    }

    public void setStatus(StatusOperation status) {
        this.status = status;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
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
                ", date=" + date.toString() +
                ", description='" + description + '\'' +
                ", status='" + status.getName() + '\'' +
                ", type='" + type.getName() + '\'' +
                ", sender=" + sender.getAccountNumber() +
                ", reciever=" + reciever.getAccountNumber() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id &&
                Double.compare(that.amount.doubleValue(), amount.doubleValue()) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description) &&
                Objects.equals(status, that.status) &&
                Objects.equals(type, that.type) &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(reciever, that.reciever);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, description, status, type, sender, reciever);
    }
}
