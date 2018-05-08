package com.lybrant.homework.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
public class Loan {
    @Id
    @GeneratedValue
    private BigInteger id;
    @NotBlank
    private String personalId;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Positive
    private BigDecimal amount;
    @Positive
    private int term;
    private boolean accepted=false;
    private String country;

    public Loan() {}
    public Loan(BigInteger id, String personalId, String name, String surname, BigDecimal amount, int term, boolean accepted, String country) {
        this.id = id;
        this.personalId = personalId;
        this.name = name;
        this.surname = surname;
        this.amount = amount;
        this.term = term;
        this.accepted = accepted;
        this.country = country;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
