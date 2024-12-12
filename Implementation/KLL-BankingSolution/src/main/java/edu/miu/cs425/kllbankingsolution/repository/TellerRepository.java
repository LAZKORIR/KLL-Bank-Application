package edu.miu.cs425.kllbankingsolution.repository;

import edu.miu.cs425.kllbankingsolution.entities.Teller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TellerRepository extends JpaRepository<Teller, Integer> {
}
