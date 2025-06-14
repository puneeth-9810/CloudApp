package com.project.cloudapp.people;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/people")
@AllArgsConstructor
public class PeopleController {

    private  PeopleService peopleService;

    @GetMapping("/fetch")
    public People getPeopleById(@RequestParam String email) {
        return peopleService.getPeopleByEmail(email);
    }

    @PutMapping("/changePassword")
    public void changePassword(@RequestParam Integer id, @RequestParam String password) {
        peopleService.changePassword(id, password);
    }

}
