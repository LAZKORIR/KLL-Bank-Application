package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.entities.Customer;
import edu.miu.cs425.kllbankingsolution.entities.Role;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.RoleRepository;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import edu.miu.cs425.kllbankingsolution.service.AccountService;
import edu.miu.cs425.kllbankingsolution.service.CustomerService;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model,Principal principal) {
        String username = principal.getName();
        model.addAttribute("admin", username);
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

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Role> allRoles = roleRepository.findAll();
        System.out.println("******");
        System.out.println(allRoles);// Fetch all roles from DB
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/user-edit"; // Template for editing user
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User updatedUser) {
        userService.updateUser(id, updatedUser); // Handle update logic
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); // Handle delete logic
        return "redirect:/admin/users";
    }

}
