package com.project.cloudapp.people;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PeopleService {

    private final PeopleRepo peopleRepo;


    public People getPeopleByEmail(String email)
    {
        return peopleRepo.findByEmail(email).orElseThrow();
    }

    public void changePassword(Integer id, String password)
    {
        peopleRepo.findById(id).ifPresent(people -> people.setPassword(password));
    }

}
