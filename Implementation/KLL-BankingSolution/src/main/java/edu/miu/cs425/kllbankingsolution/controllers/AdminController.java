package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.entities.Customer;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import edu.miu.cs425.kllbankingsolution.service.AccountService;
import edu.miu.cs425.kllbankingsolution.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/admin-home-page";
    }

    // Dashboard Home
    @GetMapping("/customers")
    public String getAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customer-list";
    }

    // Search Customers
    @GetMapping("/customers/search")
    public String searchCustomers(@RequestParam("keyword") String keyword, Model model) {
        List<Customer> customers = customerService.searchCustomers(keyword);
        model.addAttribute("customers", customers);
        return "admin/customer-list";
    }

    // View All Users
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        // Add a service call if necessary to retrieve user details
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users-list"; // Adjust to the appropriate Thymeleaf template
    }

}
