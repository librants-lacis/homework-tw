package com.lybrant.homework.repository;

import com.lybrant.homework.model.PersonalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalIdRepository extends JpaRepository<PersonalId, String> {
}
