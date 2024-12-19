package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.entities.Transaction;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import edu.miu.cs425.kllbankingsolution.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userRepository.findByUsername(username)
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double balance = accountService.checkBalance(userId);
        List<Transaction> transactions = accountService.getRecentTransactions(userId);

        model.addAttribute("balance", balance);
        model.addAttribute("transactions", transactions);
        model.addAttribute("customer", username);

        return "customer/customer-home-page";
    }


}

