package com.project.cloudapp.files;

import com.project.cloudapp.people.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepo extends JpaRepository<File, Integer> {
    List<File> findAllByPeople(People people);
}
