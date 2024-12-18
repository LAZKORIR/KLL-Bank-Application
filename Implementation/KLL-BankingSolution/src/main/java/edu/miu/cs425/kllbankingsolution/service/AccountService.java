package edu.miu.cs425.kllbankingsolution.service;

import edu.miu.cs425.kllbankingsolution.entities.Account;
import edu.miu.cs425.kllbankingsolution.entities.Transaction;
import edu.miu.cs425.kllbankingsolution.repository.AccountRepository;
import edu.miu.cs425.kllbankingsolution.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Check account balance
    public double checkBalance(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow().getBalance();
    }

    // Deposit money
    public void deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        transactionRepository.save(new Transaction(LocalDateTime.now(), amount, "DEPOSIT", account));
    }

    // Withdraw money
    public void withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            transactionRepository.save(new Transaction(LocalDateTime.now(), amount, "WITHDRAW", account));
        } else {
            throw new RuntimeException("Insufficient balance.");
        }
    }

    // Transfer money
    public void transfer(Long fromAccountId, Long toAccountId, double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow();
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow();

        if (fromAccount.getBalance() >= amount) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transactionRepository.save(new Transaction(LocalDateTime.now(), amount, "TRANSFER", fromAccount));
        } else {
            throw new RuntimeException("Insufficient balance.");
        }
    }
}

