package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.entities.*;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import edu.miu.cs425.kllbankingsolution.service.AccountService;
import edu.miu.cs425.kllbankingsolution.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CustomerService customerService;


    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userRepository.findByUsername(username)
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all accounts dynamically
        List<Account> accounts = accountService.getAccountsByCustomerId(userId);

        List<Transaction> transactions = accountService.getRecentTransactions(userId);

        // Add accounts and transactions to the model
        model.addAttribute("accounts", accounts);
        model.addAttribute("transactions", transactions);
        model.addAttribute("customer", username);

        return "customer/customer-home-page";
    }


    @GetMapping("/{customerId}/accounts")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable Long customerId) {
        System.out.println("Fetching accounts for customerId: " + customerId);
        List<Account> accounts = accountService.findByCustomerId(customerId);
        System.out.println("Accounts fetched: " + accounts);
        return ResponseEntity.ok(accounts);
    }


    // Show Transfer Form
    @GetMapping("/intra/transfer")
    public String showIntraTransferForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
        return "customer/transfer-funds-intra-page"; // Thymeleaf template for transfer
    }

    // Perform Transfer
    @PostMapping("/intra/transfer")
    public String intraTransfer(@RequestParam String fromAccountType,
                                @RequestParam String targetAccountType,
                                @RequestParam double amount,
                                @RequestParam Long customerId,
                                @RequestParam String description,
                                Model model) {
        try {
            accountService.intraTransfer(fromAccountType, targetAccountType, amount, customerId,description);
            model.addAttribute("message", "Transfer successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/customer/intra/transfer";
    }

    // Show Transfer Form
    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/transfer-funds-page"; // Thymeleaf template for transfer
    }



    // Perform Transfer
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccountType,
                           @RequestParam String targetAccountType,
                           @RequestParam double amount,
                           @RequestParam Long customerId,
                           @RequestParam Long targetCustomerId,
                           @RequestParam String description,
                           Model model) {
        try {
            System.out.println(fromAccountType);
            System.out.println(targetAccountType);
            System.out.println(amount);
            System.out.println(customerId);
            System.out.println(targetCustomerId);

            accountService.transfer(fromAccountType, targetAccountType, amount, customerId,targetCustomerId,description);
            model.addAttribute("message", "Transfer successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/customer/transfer";
    }


}

