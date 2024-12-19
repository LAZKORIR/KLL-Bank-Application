package edu.miu.cs425.kllbankingsolution.service;

import edu.miu.cs425.kllbankingsolution.entities.Account;
import edu.miu.cs425.kllbankingsolution.entities.Customer;
import edu.miu.cs425.kllbankingsolution.entities.Transaction;
import edu.miu.cs425.kllbankingsolution.repository.AccountRepository;
import edu.miu.cs425.kllbankingsolution.repository.CustomerRepository;
import edu.miu.cs425.kllbankingsolution.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    CustomerRepository customerRepository;

    // Method to create an account
    public Account createAccount(Long customerId,String customerName, String accountNumber, String accountType, double initialBalance) {
        // Fetch the customer from the database (this assumes the customer exists)
        // You can also add validation or error handling in case the customer doesn't exist
        Customer customer = new Customer();
        customer.setCustomerId(customerId);  // In actual code, this should come from database

        Account account = new Account(accountNumber, accountType,  customerName, initialBalance, customer);
        return accountRepository.save(account);
    }

    // Check account balance
    public double checkBalance(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            throw new RuntimeException("No accounts found for the user");
        }

        // Assuming you want to sum all balances; otherwise, you can return balances individually
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }


    // Deposit money
    public void deposit(Long customerId, double amount, String accountType, String description) {

        Account account = accountRepository.findByAccountTypeAndCustomer(accountType,customerId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction(LocalDateTime.now(), amount, "DEPOSIT", description, account);
        transactionRepository.save(transaction);
    }

    // Withdraw money
    public void withdraw(Long customerId, double amount, String accountType, String description) {
        Account account = accountRepository.findByAccountTypeAndCustomer(accountType,customerId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            Transaction transaction = new Transaction(LocalDateTime.now(), amount, "WITHDRAW", description, account);
            transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Insufficient balance.");
        }
    }

    // Transfer money
    public void transfer(String fromAccount, String targetAccount, double amount, Long customerId, String description) {
        Account fromAcc = accountRepository.findByAccountNumberAndCustomer(fromAccount,customerId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumberAndCustomer(targetAccount,customerId)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (fromAcc.getBalance() >= amount) {
            fromAcc.setBalance(fromAcc.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAcc);
            accountRepository.save(toAccount);

            Transaction transaction = new Transaction(LocalDateTime.now(), amount, "TRANSFER", description, fromAcc);
            transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Insufficient balance.");
        }
    }

    // Fetch recent transactions
    public List<Transaction> getRecentTransactions(Long userId) {
        return transactionRepository.findByAccount_Customer_Id(userId)
                .stream()
                .sorted((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
