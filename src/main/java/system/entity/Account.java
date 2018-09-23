package system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import system.enums.AccountType;
import system.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @JsonIgnore
    private BigDecimal account_balance;

    private long accountNumber;

    @JsonIgnore
    private int bic;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> sendList;

    @JsonIgnore
    @OneToMany(mappedBy = "reciever", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> recieveList;

    public Account(){

    }

    public Account(BigDecimal account_balance, long accountNumber, User user, Currency currency, AccountType type, int bic) {
        this.account_balance = account_balance;
        this.accountNumber = accountNumber;
        this.user = user;
        this.currency = currency;
        this.bic = bic;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(BigDecimal account_balance) {
        this.account_balance = account_balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getBic() {
        return bic;
    }

    public void setBic(int bic) {
        this.bic = bic;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", account_balance=" + account_balance +
                ", accountNumber=" + accountNumber +
                ", user=" + user +
                ", BIC=" + bic +
                ", currency=" + currency +
                ", type=" + type +
                '}';
    }
}
