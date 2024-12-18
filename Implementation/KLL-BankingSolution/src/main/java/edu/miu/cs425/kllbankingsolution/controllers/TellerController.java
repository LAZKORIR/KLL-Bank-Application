package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/teller")
public class TellerController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/deposit")
    public String deposit(@RequestParam Long accountId, @RequestParam double amount) {
        accountService.deposit(accountId, amount);
        return "redirect:/teller/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accountId, @RequestParam double amount) {
        accountService.withdraw(accountId, amount);
        return "redirect:/teller/dashboard";
    }
}

