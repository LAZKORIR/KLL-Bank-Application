package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private AccountService accountService;
@GetMapping("/dashboard")
public String dashboard(Model model, Principal principal) {
    model.addAttribute("customer", principal.getName());
    return "customer/customer-home-page";
}
    @GetMapping("/balance")
    public String checkBalance(Model model, Principal principal) {
        Long accountId = Long.parseLong(principal.getName());
        double balance = accountService.checkBalance(accountId);
        model.addAttribute("balance", balance);
        return "customer_balance";
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long toAccountId, @RequestParam double amount, Principal principal) {
        Long fromAccountId = Long.parseLong(principal.getName());
        accountService.transfer(fromAccountId, toAccountId, amount);
        return "redirect:/customer/dashboard";
    }
}

