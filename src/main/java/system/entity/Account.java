package system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private long id;
    private double account_balance;
    private long account_number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> sendList;

    @OneToMany(mappedBy = "reciever", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> recieveList;

    public Account(){

    }

    public Account(double account_balance, long account_number, User user) {
        this.account_balance = account_balance;
        this.account_number = account_number;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(double account_balance) {
        this.account_balance = account_balance;
    }

    public long getAccount_number() {
        return account_number;
    }

    public void setAccount_number(long account_number) {
        this.account_number = account_number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getSendList() {
        return sendList;
    }

    public void setSendList(List<Transaction> sendList) {
        this.sendList = sendList;
    }

    public List<Transaction> getRecieveList() {
        return recieveList;
    }

    public void setRecieveList(List<Transaction> recieveList) {
        this.recieveList = recieveList;
    }
}
