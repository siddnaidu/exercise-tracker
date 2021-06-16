package com.siddnaidu.exercisetracker.controller;

import com.siddnaidu.exercisetracker.exception.ExerciseNotFoundException;
import com.siddnaidu.exercisetracker.exception.UserNotFoundException;
import com.siddnaidu.exercisetracker.model.Exercise;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.ExerciseRepository;
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
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/api/users/{userId}/exercises")
    public List<Exercise> getAllExercises(@PathVariable Long userId){
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("user-id:" + userId);
        }

        return userOptional.get().getExercises();
    }

    @GetMapping(path = "/api/users/{userId}/exercises/{exerciseId}")
    public EntityModel<Exercise> getExercise(@PathVariable Long userId, @PathVariable Long exerciseId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("user-id:" + userId);
        }

        User user = userOptional.get();

        Optional<Exercise> exerciseOptional = exerciseRepository.findById(exerciseId);

        if(!exerciseOptional.isPresent()) {
            throw new ExerciseNotFoundException("exercise-id: " + exerciseId);
        }

        Exercise exercise = exerciseOptional.get();

        if(exercise.getUser() != user) {
            throw new ExerciseNotFoundException("Excersize: " + exerciseId
                    + "does not belong to User: " + userId);
        }

        EntityModel<Exercise> resource = EntityModel.of(exercise);

        WebMvcLinkBuilder apiLinks = linkTo(methodOn(this.getClass()).getAllExercises(userId));

        resource.add(apiLinks.withRel("all-exercises for user: " + userId));

        return resource;
    }

    @PostMapping(path = "/api/users/{userId}/exercises")
    public ResponseEntity<Object> createExercise(@Valid @PathVariable Long userId,
                                                 @RequestBody Exercise exercise) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("user-id:" + userId);
        }

        User user = userOptional.get();

        exercise.setUser(user);

        exerciseRepository.save(exercise);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
                .buildAndExpand(exercise.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/api/users/{userId}/exercises")
    public ResponseEntity<Exercise> editExercise(@RequestBody Exercise newExercise,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long exerciseId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("user-id:" + userId);
        }

        User user = userOptional.get();

        Optional<Exercise> exerciseOptional = exerciseRepository.findById(exerciseId);

        if(!exerciseOptional.isPresent()) {
            throw new ExerciseNotFoundException("exercise-id: " + exerciseId);
        }

        if(exerciseOptional.get().getUser() != user) {
            throw new ExerciseNotFoundException("Excersize: " + exerciseId
                    + "does not belong to User: " + userId);
        }

        Exercise exerciseToBeUpdated = exerciseOptional
                .map(exercise -> {
            exercise.setExerciseType(newExercise.getExerciseType());
            exercise.setUser(newExercise.getUser());
            exercise.setRepCount(newExercise.getRepCount());
            exercise.setSetCount(newExercise.getSetCount());
            exercise.setEquipment(newExercise.getEquipment());
            exercise.setWeight(newExercise.getWeight());
            return exerciseRepository.save(exercise);
        }).orElseGet(() -> {
            newExercise.setId(exerciseId);
            newExercise.setUser(user);
            return exerciseRepository.save(newExercise);
        });

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{exerciseId}")
                .buildAndExpand(exerciseToBeUpdated.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/api/users/{userId}/exercises/{exerciseId}")
    public void deleteUser(@PathVariable Long userId, @PathVariable Long exerciseId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent()) {
            throw new UserNotFoundException("user-id:" + userId);
        }

        exerciseRepository.deleteById(exerciseId);
    }
}
