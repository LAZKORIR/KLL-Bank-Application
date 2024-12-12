package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;


    @ManyToOne
    @JoinColumn(name = "address_id",referencedColumnName = "addressId")
    private Address address;

}
