package com.lybrant.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lybrant.homework.model.PersonalId;
import com.lybrant.homework.repository.PersonalIdRepository;
import com.lybrant.homework.model.Loan;
import com.lybrant.homework.repository.LoanRepository;
import com.lybrant.homework.service.IpDataService;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private PersonalIdRepository personalIdRepository;
    @Mock
    private RequestRateLimiter rateLimiter;
    @Mock
    private IpDataService ipDataService;

    private List<Loan> loans = new LinkedList<>();

    @Before
    public void setUp() {
        loans.add(new Loan(new BigInteger("10001"), "pk1", "Name1", "Surname1", new BigDecimal("1000.01"), 12, false, "LV"));
        loans.add(new Loan(new BigInteger("20001"), "pk2", "Name2", "Surname2", new BigDecimal("2000.02"), 24, false, "LV"));
        when(loanRepository.findAll()).thenReturn(loans);
        when(loanRepository.findAcceptedByPersonalId("pk1")).thenReturn(Collections.singletonList(loans.get(0)));
        loans.forEach(loan -> {
            when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        });

        when(loanRepository.save(any(Loan.class))).thenReturn(
                new Loan(new BigInteger("1"), "PersonalId", "Name", "Surname", new BigDecimal("3000"), 36, false, "LV")
        );

        when(rateLimiter.overLimitWhenIncremented(anyString())).thenReturn(false);

        when(ipDataService.getCountryByIp(anyString())).thenReturn("LV");

        RestAssuredMockMvc.standaloneSetup(
                new LoanController(loanRepository, personalIdRepository, rateLimiter, ipDataService)
        );
    }

    @Test
    public void getLoanList() throws Exception {
        RestAssuredMockMvc.given()
                .when()
                .get("/loans")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("get(0).id", is(10001))
                .body("get(0).personalId", is("pk1"))
                .body("get(0).name", is("Name1"))
                .body("get(0).surname", is("Surname1"))
                .body("get(0).amount", is(1000.01f))
                .body("get(0).term", is(12))
                .body("get(1).id", is(20001))
                .body("get(1).personalId", is("pk2"))
                .body("get(1).name", is("Name2"))
                .body("get(1).surname", is("Surname2"))
                .body("get(1).amount", is(2000.02f))
                .body("get(1).term", is(24))
        ;
    }

    @Test
    public void findAcceptedByPersonalId() throws Exception {
        RestAssuredMockMvc.given()
                .when()
                .get("/loans/pk1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("get(0).id", is(10001))
                .body("get(0).personalId", is("pk1"))
                .body("get(0).name", is("Name1"))
                .body("get(0).surname", is("Surname1"))
                .body("get(0).amount", is(1000.01f))
                .body("get(0).term", is(12))
        ;
    }


    @Test
    public void findNoAcceptedByPersonalId() throws Exception {
        RestAssuredMockMvc.given()
                .when()
                .get("/loans/pk2")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(0))
        ;
    }

    @Test
    public void createLoan() throws Exception {

        Loan loan = loans.get(0);
        String loanString = new ObjectMapper().writeValueAsString(loan);

        RestAssuredMockMvc.given()
                .body(loanString)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .post("/loan")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(1))
                .body("personalId", is("PersonalId"))
                .body("name", is("Name"))
                .body("surname", is("Surname"))
                .body("amount", is(3000))
                .body("term", is(36))
                .body("accepted", is(false))
                .body("country", is("LV"))
        ;
    }

    @Test
    public void createLoanShouldThrowRateLimitExceededException() throws Exception {

        when(rateLimiter.overLimitWhenIncremented(anyString())).thenReturn(true);

        Loan loan = loans.get(0);
        String loanString = new ObjectMapper().writeValueAsString(loan);

        RestAssuredMockMvc.given()
                .body(loanString)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .post("/loan")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
        ;
    }

    @Test
    public void createLoanShouldThrowPersonalIdBlacklistedException() throws Exception {

        when(personalIdRepository.findById("pk1")).thenReturn(Optional.of(new PersonalId("pk1")));

        Loan loan = loans.get(0);
        String loanString = new ObjectMapper().writeValueAsString(loan);

        RestAssuredMockMvc.given()
                .body(loanString)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .post("/loan")
                .then()
                .statusCode(HttpStatus.LOCKED.value())
        ;
    }

    @Test
    public void getLoan() throws Exception {
        for(Loan loan: loans) {
            RestAssuredMockMvc.given()
                    .when()
                    .get("/loan/".concat(loan.getId().toString()))
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", is(loan.getId().intValue()))
                    .body("personalId", is(loan.getPersonalId()))
                    .body("name", is(loan.getName()))
                    .body("surname", is(loan.getSurname()))
                    .body("amount", is(loan.getAmount().floatValue()))
                    .body("term", is(loan.getTerm()))
            ;
        }
    }
}