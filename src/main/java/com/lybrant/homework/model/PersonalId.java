package com.lybrant.homework.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonalId {
    @Id
    private String personalId;

    public PersonalId() {}
    public PersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
