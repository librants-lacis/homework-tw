package com.lybrant.homework.controller;

import com.lybrant.homework.exception.PersonalIdNotFoundException;
import com.lybrant.homework.model.PersonalId;
import com.lybrant.homework.repository.PersonalIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blacklist")
public class BlacklistedPersonalIdController {
    @Autowired
    private PersonalIdRepository personalIdRepository;

    @GetMapping("/personalIds")
    public List<PersonalId> getPersonalIdList() {
        return personalIdRepository.findAll();
    }

    @PostMapping("/personalId/{id}")
    public PersonalId addPersonalId(@PathVariable String id) {
        return personalIdRepository.save(new PersonalId(id));
    }

    @GetMapping("/personalId/{id}")
    public PersonalId getPersonalId(@PathVariable String id) throws PersonalIdNotFoundException {
        return personalIdRepository.findById(id)
                .orElseThrow(() -> new PersonalIdNotFoundException(id));
    }

    @DeleteMapping("/personalId/{id}")
    public ResponseEntity<PersonalId> deletePersonalId(@PathVariable String id) throws PersonalIdNotFoundException {
        PersonalId personalId = personalIdRepository.findById(id)
                .orElseThrow(() -> new PersonalIdNotFoundException(id));
        personalIdRepository.delete(personalId);

        return ResponseEntity.ok().body(personalId);
    }

}
