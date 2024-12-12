package edu.miu.cs425.kllbankingsolution.controllers;

import edu.miu.cs425.kllbankingsolution.dto.CustomerRequestDTO;
import edu.miu.cs425.kllbankingsolution.dto.Response;
import edu.miu.cs425.kllbankingsolution.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teller")
public class TellerController {

    @Autowired
    private CustomerService customerService;
    //create customer
    @PostMapping("/create-customer")
    public ResponseEntity<Response> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {

        return new ResponseEntity<>(customerService.createCustomer(customerRequestDTO), HttpStatus.OK);
    }
}
