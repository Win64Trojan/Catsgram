package ru.yandex.practicum.catsgram.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUserList(@RequestParam(value = "page", defaultValue = "0", required = false) Long page,
                                        @RequestParam(value = "size", defaultValue = "10", required = false) Long size,
                                        @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {

        Long from = page * size;
        if (!(sort.equals("asc") || sort.equals("desc"))) {
            throw new IllegalArgumentException();
        }
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException();
        }
        return userService.getUserList(size, from, sort);
    }

    @GetMapping("{id}")
    public Optional<User> findUserById(@PathVariable("id") Long userById) {
        return userService.findUserById(userById);
    }

    @PostMapping
    public ResponseEntity <User> addUser(@RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        return userService.updateUser(user);
    }
}
