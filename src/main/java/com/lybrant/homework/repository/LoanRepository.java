package com.lybrant.homework.repository;

import com.lybrant.homework.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, BigInteger>{

    @Query("SELECT l FROM Loan l WHERE l.personalId = :personalId and l.accepted = true")
    public List<Loan> findAcceptedByPersonalId(@Param("personalId") String personalId);

}
