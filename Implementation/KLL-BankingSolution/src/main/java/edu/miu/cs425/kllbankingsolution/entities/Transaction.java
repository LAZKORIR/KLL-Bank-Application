package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
}
