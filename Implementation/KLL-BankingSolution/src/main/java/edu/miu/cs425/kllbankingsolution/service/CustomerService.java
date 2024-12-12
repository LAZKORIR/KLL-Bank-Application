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

    public Response createCustomer(CustomerRequestDTO customerRequestDTO) {
        Response response = new Response();
        try {
            Customer customer = new Customer();
            customer.setFirstName(customerRequestDTO.getFirstName());
            customer.setLastName(customerRequestDTO.getLastName());
            customer.setEmail(customerRequestDTO.getEmail());
            customer.setPhone(customerRequestDTO.getPhone());

            Address address = new Address(customerRequestDTO.getStreet(),customerRequestDTO.getCity(),customerRequestDTO.getState(),customerRequestDTO.getZip());

            Address savedAddress=addressRepository.save(address);
            customer.setAddress(savedAddress);
            Customer savedCustomer=customerRepository.save(customer);
            Account account = new Account(customerRequestDTO.getAccountNumber(), customerRequestDTO.getAccountType(), customerRequestDTO.getAccountName(), 100.0,savedCustomer);

            accountRepository.save(account);

            User user = new User();
            user.setUsername(customerRequestDTO.getUsername());
            user.setPassword(customerRequestDTO.getPassword());
            User savedUser=userRepository.save(user);

            response.setResponseCode("200");
            response.setResponseMessage(savedUser.getUsername()+" Account has been created successfully");

        }catch(Exception e){
            e.printStackTrace();
            response.setResponseCode("500");
            response.setResponseMessage("Failed to create Customer");

        }
        return response;
    }

    public Response getAllCustomers() {
        Response response = new Response();
        List<Customer> customers= customerRepository.findAll();
        response.setResponseCode("200");
        response.setResponseMessage(customers.size()+" Customer found");
        response.setResponseObject(customers);

        return response;
    }

}
