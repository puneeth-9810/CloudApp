package com.project.cloudapp.people;

import com.project.cloudapp.people.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeopleRepo extends JpaRepository<People, Integer> {

    Optional<People> findByEmail(String email);

}
