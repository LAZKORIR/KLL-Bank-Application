package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private LocalDateTime dateTime;
    private double amount;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER

    @ManyToOne
    private Account account;

    public Transaction() {
    }

    public Transaction(LocalDateTime dateTime, double amount, String type, Account account) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String getDateTime() {
        return dateTime.toString();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
