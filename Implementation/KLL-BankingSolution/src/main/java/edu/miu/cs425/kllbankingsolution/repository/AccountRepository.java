package edu.miu.cs425.kllbankingsolution.repository;

import edu.miu.cs425.kllbankingsolution.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    //find by account type and customer id
    @Query("SELECT account FROM Account account WHERE account.accountType=:accountType AND account.customer.customerId=:customerId")
    Optional<Account> findByAccountTypeAndCustomer(String accountType, Long customerId);

    @Query("SELECT account FROM Account account WHERE account.accountNumber=:accountNumber AND account.customer.customerId=:customerId")
    Optional<Account> findByAccountNumberAndCustomer(String accountNumber, Long customerId);

    @Query("SELECT DISTINCT acc FROM Account acc WHERE acc.customer.customerId = :customerId")
    List<Account> findByCustomerId(@Param("customerId") Long customerId);


}
