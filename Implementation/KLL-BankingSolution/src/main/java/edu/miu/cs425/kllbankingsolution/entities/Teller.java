package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "tbl_teller")

public class Teller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tellerId;

    private String tellerName;
    private String branchName;

}
