package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.entities.Account;
import edu.miu.cs425.kllbankingsolution.entities.Customer;
import edu.miu.cs425.kllbankingsolution.service.AccountService;
import edu.miu.cs425.kllbankingsolution.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    // Show Deposit Form
    @GetMapping("/deposit")
    public String showDepositForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        return "transactions/deposit-page"; // Thymeleaf template for deposit
    }

    // Perform Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestParam Long accountId,
                          @RequestParam double amount,
                          @RequestParam String accountType,
                          @RequestParam String description,
                          Model model) {
        accountService.deposit(accountId, amount,accountType, description);
        model.addAttribute("message", "Deposit successful!");
        return "redirect:/transactions/deposit";
    }

    // Show Withdraw Form
    @GetMapping("/withdraw")
    public String showWithdrawForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        return "transactions/withdraw-page"; // Thymeleaf template for withdraw
    }

    // Perform Withdrawal
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId,
                           @RequestParam double amount,
                           @RequestParam String accountType,
                           @RequestParam String description,
                           Model model) {
        try {
            accountService.withdraw(accountId, amount,accountType, description);
            model.addAttribute("message", "Withdrawal successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/transactions/withdraw";
    }

    // Show Transfer Form
    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        return "transactions/transfer-funds-page"; // Thymeleaf template for transfer
    }

    // Perform Transfer
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount,
                           @RequestParam String targetAccount,
                           @RequestParam double amount,
                           @RequestParam Long customerId,
                           @RequestParam String description,
                           Model model) {
        try {
            accountService.transfer(fromAccount, targetAccount, amount, customerId,1L,description);
            model.addAttribute("message", "Transfer successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/transactions/transfer";
    }

}
