package com.company.aggregator.controller;

import com.company.aggregator.model.UserDTO;
import com.company.aggregator.service.UserDataAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {

    private final UserDataAggregator userDataAggregator;

    @GetMapping("/users")
    public List<UserDTO> getUsers(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "surname", required = false) String surname) {

        return userDataAggregator.aggregateUsers(username, name, surname);
    }
}

