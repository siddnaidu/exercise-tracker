package com.siddnaidu.exercisetracker.controller;

import com.siddnaidu.exercisetracker.exception.UserNotFoundException;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/api/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/api/users/{id}")
    public EntityModel<User> getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        EntityModel<User> resource = EntityModel.of(user.get());

        WebMvcLinkBuilder apiLinks = linkTo(methodOn(this.getClass()).getAllUsers());

        resource.add(apiLinks.withRel("all-users"));

        return resource;
    }

    @PostMapping(path = "/api/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/api/users/{id}")
    public ResponseEntity<Object> editUser(@RequestBody User newUser, @PathVariable Long id) {
        User userToBeUpdated = userRepository.findById(id) //
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setAge(newUser.getAge());
                    user.setHeightFeet(newUser.getHeightFeet());
                    user.setHeightInches(newUser.getHeightInches());
                    user.setWeight(newUser.getWeight());
                    user.setExercises(newUser.getExercises());
                    return userRepository.save(user);
                }) //
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userToBeUpdated.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/api/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}