package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_admin")
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int adminId;

    @Column(name = "name")
    private String adminName;

}
