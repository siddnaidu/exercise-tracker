package com.siddnaidu.exercisetracker.controller;

import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/api/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/api/users/{id}")
    public EntityModel<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);

        EntityModel<User> resource = EntityModel.of(user);
        WebMvcLinkBuilder apiLinks = linkTo(methodOn(this.getClass()).getAllUsers());
        resource.add(apiLinks.withRel("all-users"));

        return resource;
    }

    @PostMapping(path = "/api/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = userService.createUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/api/users/{id}")
    public ResponseEntity<Object> editUser(@RequestBody User newUser, @PathVariable Long id) {
        User userToBeUpdated = userService.editUser(newUser, id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userToBeUpdated.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/api/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}