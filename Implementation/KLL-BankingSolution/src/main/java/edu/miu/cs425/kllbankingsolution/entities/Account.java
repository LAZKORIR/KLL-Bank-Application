package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String accountNumber;
    private String accountType;
    private String accountName;
    private double balance;




    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Account(String accountNumber, String accountType, String accountName, double balance, Customer customer) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountName = accountName;
        this.balance = balance;
        this.customer = customer;
    }
    public Account() {}

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
