package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/manage")
    public String manageAccounts() {
        return "admin_manage_accounts";
    }

    @GetMapping("/logs")
    public String viewLogs() {
        // Logic to view system logs
        return "admin_logs";
    }
}
