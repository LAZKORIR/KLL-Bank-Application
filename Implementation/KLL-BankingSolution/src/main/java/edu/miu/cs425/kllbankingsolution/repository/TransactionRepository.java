package edu.miu.cs425.kllbankingsolution.repository;

import edu.miu.cs425.kllbankingsolution.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.account.customer.user.userId = :userId")
    List<Transaction> findByAccount_Customer_Id(Long userId);

}
