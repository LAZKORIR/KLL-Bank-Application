package edu.miu.cs425.kllbankingsolution.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_teller")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tellerId;

    private String tellerName;
    private String branchName;

}
