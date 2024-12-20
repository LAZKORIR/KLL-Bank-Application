package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.entities.*;
import edu.miu.cs425.kllbankingsolution.repository.RoleRepository;
import edu.miu.cs425.kllbankingsolution.service.AccountService;
import edu.miu.cs425.kllbankingsolution.service.CustomerService;
import edu.miu.cs425.kllbankingsolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teller")
public class TellerController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String tellerDashboard(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("teller", username);

        return "teller/teller-home-page";
    }

    @GetMapping("/add-customer")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
        return "teller/add-customer";
    }

    @PostMapping("/addCustomer")
    public String addCustomer(
            Customer customer,
            String accountType,
            String street,
            String city,
            String state,
            String zip,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Save address
        Address address = new Address(street, city, state, zip);
        Address savedAddress = customerService.saveAddress(address);

        // Assign address to customer
        customer.setAddress(savedAddress);

        // Create a new user
        Role customerRole = getOrCreateRole("ROLE_CUSTOMER");
        User user = new User();
        user.setUsername(customer.getFirstName());
        user.setPassword("1234");

        Optional.of(user)
                .ifPresent(u -> u.setPassword(passwordEncoder.encode(u.getPassword())));
        user.addRole(customerRole);

        User savedUser = userService.addUser(user);

        // Link user to customer
        customer.setUser(savedUser);

        // Save customer
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Create an account for the customer
        Account account = accountService.createAccount(
                savedCustomer.getCustomerId(),
                savedCustomer.getFirstName() + " " + savedCustomer.getLastName(),
                "ACC" + System.currentTimeMillis(),
                accountType,
                0.0);

        redirectAttributes.addAttribute("customer", savedCustomer);
        redirectAttributes.addAttribute("account", account);

        return "redirect:/teller/customer-details/" + savedCustomer.getCustomerId();
    }

    @GetMapping("customer-details/{customerId}")
    public String showCustomerDetails(@PathVariable Long customerId, Model model) {
        // Fetch the customer by ID
        Customer customer = customerService.findCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        // Add customer details to the model
        model.addAttribute("customer", customer);

        // Add the customer's accounts to the model
        model.addAttribute("accounts", customer.getAccounts());

        return "teller/customer-details";
    }


//    @GetMapping("customer-details")
//    public String showCustomerDetails(Model model) {
//        model.addAttribute("customer", new Customer());
//        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
//        return "teller/customer-details";
//    }

    @GetMapping("/add-account")
    public String showAddAccountForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
        return "teller/add-account";
    }

    @PostMapping("/addAccount")
    public String addAccountToCustomer(
            @RequestParam Long customerId,
            @RequestParam String accountType,
            @RequestParam double initialBalance,
            RedirectAttributes redirectAttributes) {

        String accountNumber = "ACC" + System.currentTimeMillis();

        // Call service to create the account
        Account newAccount = accountService.createAccountForExistingCustomer(
                customerId, accountNumber, accountType, initialBalance);
        //redirectAttributes.addAttribute("customer", savedCustomer);
        redirectAttributes.addAttribute("account", newAccount);

        return "redirect:/teller/customer-details/" + customerId;
    }

     // Show Deposit Form
    @GetMapping("/deposit")
    public String showDepositForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
        return "transactions/deposit-page"; // Thymeleaf template for deposit
    }

    // Perform Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestParam Long customerId,
                          @RequestParam double amount,
                          @RequestParam String accountType,
                          @RequestParam String description,
                          Model model) {
        try{
        accountService.deposit(customerId, amount,accountType, description);
            // Add success flag and message
            model.addAttribute("showModal", true);
            model.addAttribute("modalType", "success");

        model.addAttribute("successMessage", "Deposit successful!");
        return "redirect:/transactions/deposit";
        } catch (Exception e) {
            // Add failure flag and message
            model.addAttribute("showModal", true);
            model.addAttribute("modalType", "failure");
            model.addAttribute("failureMessage", "Deposit failed: " + e.getMessage());
            return "redirect:/transactions/deposit";
        }
    }

    // Show Withdraw Form
    @GetMapping("/withdraw")
    public String showWithdrawForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
        return "transactions/withdraw-page"; // Thymeleaf template for withdraw
    }

    // Perform Withdrawal
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long customerId,
                           @RequestParam double amount,
                           @RequestParam String accountType,
                           @RequestParam String description,
                           Model model) {
        try {
            accountService.withdraw(customerId, amount,accountType, description);
            model.addAttribute("message", "Withdrawal successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/transactions/withdraw";
    }

    // Show Transfer Form
    @GetMapping("/intra/transfer")
    public String showIntraTransferForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("customers", customers);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountTypes", List.of("Savings", "Checking", "Business"));
        return "transactions/transfer-funds-intra-page"; // Thymeleaf template for transfer
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
        return "redirect:/transactions/intra/transfer";
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    // Show Transfer Form
    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "transactions/transfer-funds-page"; // Thymeleaf template for transfer
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

            accountService.transfer(fromAccountType, targetAccountType, amount, customerId,targetCustomerId,description);
            model.addAttribute("message", "Transfer successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/transactions/transfer";
    }
}

