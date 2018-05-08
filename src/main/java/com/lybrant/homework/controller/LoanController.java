package com.lybrant.homework.controller;

import com.lybrant.homework.model.PersonalId;
import com.lybrant.homework.repository.PersonalIdRepository;
import com.lybrant.homework.exception.LoanNotFoundException;
import com.lybrant.homework.exception.PersonalIdBlacklistedException;
import com.lybrant.homework.exception.RateLimitExceededException;
import com.lybrant.homework.model.Loan;
import com.lybrant.homework.repository.LoanRepository;
import com.lybrant.homework.service.IpDataService;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigInteger;
import java.net.URI;
import java.util.*;

@RestController
public class LoanController {

    private LoanRepository loanRepository;
    private PersonalIdRepository personalIdRepository;
    private RequestRateLimiter rateLimiter;
    private IpDataService ipDataService;

    @Autowired
    public LoanController(
            LoanRepository loanRepository,
            PersonalIdRepository personalIdRepository,
            RequestRateLimiter rateLimiter,
            IpDataService ipDataService) {
        this.loanRepository = loanRepository;
        this.personalIdRepository = personalIdRepository;
        this.rateLimiter = rateLimiter;
        this.ipDataService = ipDataService;
    }

    @GetMapping("/loans")
    public List<Loan> getLoanList() {
        return loanRepository.findAll();
    }

    @GetMapping("/loans/{personalId}")
    public List<Loan> findAcceptedByPersonalId(@PathVariable String personalId) {
        return loanRepository.findAcceptedByPersonalId(personalId);
    }

    @PostMapping("/loan")
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody Loan loan, HttpServletRequest request) {
        String countryCode = ipDataService.getCountryByIp(request.getRemoteAddr());
        if (rateLimiter.overLimitWhenIncremented(countryCode)) {
            throw new RateLimitExceededException(countryCode);
        }

        validateLoan(loan);

        loan.setCountry(countryCode);

        Loan loanSaved = loanRepository.save(loan);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(loanSaved.getId()).toUri();

        return ResponseEntity.created(location).body(loanSaved);
    }



    @GetMapping("/loan/{id}")
    public Loan getLoan(@PathVariable BigInteger id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    @PutMapping("/loan/{id}")
    public ResponseEntity<Object> updateLoan(@Valid @RequestBody Loan loan, @PathVariable BigInteger id) {

        Loan loanFound = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));

        loan.setId(id);

        loanRepository.save(loan);

        return ResponseEntity.ok().body(loan);
    }


    @DeleteMapping("/loan/{id}")
    public ResponseEntity<Loan> deleteLoan(@PathVariable BigInteger id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));

        loanRepository.delete(loan);

        return ResponseEntity.ok().body(loan);
    }

    private void validateLoan(Loan loan) {
        Optional<PersonalId> personalIdFound = personalIdRepository.findById(loan.getPersonalId());
        if(personalIdFound.isPresent()) {
            throw new PersonalIdBlacklistedException(loan.getPersonalId());
        }
    }

}
