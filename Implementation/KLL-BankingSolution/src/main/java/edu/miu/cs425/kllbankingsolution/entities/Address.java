package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String street;
    private String city;
    private String state;
    private String zip;

}
