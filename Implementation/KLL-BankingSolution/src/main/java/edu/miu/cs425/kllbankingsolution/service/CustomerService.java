package edu.miu.cs425.kllbankingsolution.service;

import edu.miu.cs425.kllbankingsolution.dto.CustomerRequestDTO;
import edu.miu.cs425.kllbankingsolution.dto.Response;
import edu.miu.cs425.kllbankingsolution.entities.Account;
import edu.miu.cs425.kllbankingsolution.entities.Address;
import edu.miu.cs425.kllbankingsolution.entities.Customer;
import edu.miu.cs425.kllbankingsolution.entities.User;
import edu.miu.cs425.kllbankingsolution.repository.AccountRepository;
import edu.miu.cs425.kllbankingsolution.repository.AddressRepository;
import edu.miu.cs425.kllbankingsolution.repository.CustomerRepository;
import edu.miu.cs425.kllbankingsolution.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    AccountService accountService;


    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public Optional<Customer> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.searchByKeyword(keyword);
    }

    public Response createCustomer(CustomerRequestDTO customerRequestDTO) {
        Response response = new Response();
        try {
            // Create Customer entity
            Customer customer = new Customer();
            customer.setFirstName(customerRequestDTO.getFirstName());
            customer.setLastName(customerRequestDTO.getLastName());
            customer.setEmail(customerRequestDTO.getEmail());
            customer.setPhone(customerRequestDTO.getPhone());

            // Create and save Address
            Address address = new Address(customerRequestDTO.getStreet(), customerRequestDTO.getCity(), customerRequestDTO.getState(), customerRequestDTO.getZip());
            Address savedAddress = addressRepository.save(address);
            customer.setAddress(savedAddress);

            // Save Customer
            Customer savedCustomer = customerRepository.save(customer);

            // Create Account using AccountService
            Account account = accountService.createAccount(savedCustomer.getCustomerId(),"", "ACC" + System.currentTimeMillis(), customerRequestDTO.getAccountType(), 0.0);

            // Create User and save
            User user = new User();
            user.setUsername(customerRequestDTO.getUsername());
            user.setPassword(customerRequestDTO.getPassword());
            userRepository.save(user);

            response.setResponseCode("200");
            response.setResponseMessage(user.getUsername() + " Account has been created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage("Failed to create Customer");
        }
        return response;
    }

    public Response updateCustomer(Long customerId, CustomerRequestDTO customerRequestDTO) {
        Response response = new Response();
        try {
            // Fetch the existing customer by ID
            Customer existingCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Update Customer fields if provided
            if (customerRequestDTO.getFirstName() != null && !customerRequestDTO.getFirstName().isEmpty()) {
                existingCustomer.setFirstName(customerRequestDTO.getFirstName());
            }
            if (customerRequestDTO.getLastName() != null && !customerRequestDTO.getLastName().isEmpty()) {
                existingCustomer.setLastName(customerRequestDTO.getLastName());
            }
            if (customerRequestDTO.getEmail() != null && !customerRequestDTO.getEmail().isEmpty()) {
                existingCustomer.setEmail(customerRequestDTO.getEmail());
            }
            if (customerRequestDTO.getPhone() != null && !customerRequestDTO.getPhone().isEmpty()) {
                existingCustomer.setPhone(customerRequestDTO.getPhone());
            }

            // Update Address fields if provided
            Address existingAddress = existingCustomer.getAddress();
            if (customerRequestDTO.getStreet() != null && !customerRequestDTO.getStreet().isEmpty()) {
                existingAddress.setStreet(customerRequestDTO.getStreet());
            }
            if (customerRequestDTO.getCity() != null && !customerRequestDTO.getCity().isEmpty()) {
                existingAddress.setCity(customerRequestDTO.getCity());
            }
            if (customerRequestDTO.getState() != null && !customerRequestDTO.getState().isEmpty()) {
                existingAddress.setState(customerRequestDTO.getState());
            }
            if (customerRequestDTO.getZip() != null && !customerRequestDTO.getZip().isEmpty()) {
                existingAddress.setZip(customerRequestDTO.getZip());
            }
            addressRepository.save(existingAddress);

            // Update Account fields if provided
            List<Account> accounts = existingCustomer.getAccounts();
            for (Account account : accounts) {
                if (customerRequestDTO.getAccountNumber() != null && !customerRequestDTO.getAccountNumber().isEmpty()) {
                    account.setAccountNumber(customerRequestDTO.getAccountNumber());
                }
                if (customerRequestDTO.getAccountType() != null && !customerRequestDTO.getAccountType().isEmpty()) {
                    account.setAccountType(customerRequestDTO.getAccountType());
                }
                if (customerRequestDTO.getAccountName() != null && !customerRequestDTO.getAccountName().isEmpty()) {
                    account.setAccountName(customerRequestDTO.getAccountName());
                }
                accountRepository.save(account);
            }

            // Save the updated customer
            customerRepository.save(existingCustomer);

            response.setResponseCode("200");
            response.setResponseMessage("Customer updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage("Failed to update Customer");
        }
        return response;
    }
    public Response deleteCustomer(Long customerId) {
        Response response = new Response();
        try {
            // Fetch the existing customer by ID
            Customer existingCustomer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Delete associated accounts
            List<Account> accounts = existingCustomer.getAccounts();
            accountRepository.deleteAll(accounts);

            // Delete associated address
            Address address = existingCustomer.getAddress();
            addressRepository.delete(address);

            // Delete the customer
            customerRepository.delete(existingCustomer);

            response.setResponseCode("200");
            response.setResponseMessage("Customer deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage("Failed to delete Customer");
        }
        return response;
    }

}
