package ru.otus.hw13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw13.entity.User;
import ru.otus.hw13.exception.DbServiceException;
import ru.otus.hw13.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> users() throws DbServiceException {
        return userService.findAll();
    }

    /**
     * @param user I don't use DTO for simplicity
     * @return create operation successful
     * @throws DbServiceException create operation unsuccessful
     */
    @PostMapping("/users")
    public Boolean create(@RequestBody User user) throws DbServiceException {
        userService.save(user);

        return true;
    }
}
