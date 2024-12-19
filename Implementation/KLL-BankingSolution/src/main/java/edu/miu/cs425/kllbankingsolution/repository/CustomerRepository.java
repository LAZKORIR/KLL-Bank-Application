package edu.miu.cs425.kllbankingsolution.repository;

import edu.miu.cs425.kllbankingsolution.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByUser_UserId(Long userId);

    @Query("SELECT c FROM Customer c " +
            "WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR c.accounts IS NOT EMPTY AND EXISTS (" +
            "SELECT a FROM Account a WHERE a.customer = c AND (LOWER(a.accountNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.accountType) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    List<Customer> searchByKeyword(String keyword);

}
