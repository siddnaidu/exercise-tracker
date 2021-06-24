package com.siddnaidu.exercisetracker.controller;

import com.siddnaidu.exercisetracker.model.Exercise;
import com.siddnaidu.exercisetracker.service.ExerciseService;
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
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping(path = "/api/users/{userId}/exercises")
    public List<Exercise> getAllExercises(@PathVariable Long userId){
        return exerciseService.getAllExercisesForUser(userId);
    }

    @GetMapping(path = "/api/users/{userId}/exercises/{exerciseId}")
    public EntityModel<Exercise> getExercise(@PathVariable Long userId, @PathVariable Long exerciseId) {
        Exercise exercise = exerciseService.getExerciseByIdForUser(userId, exerciseId);

        EntityModel<Exercise> resource = EntityModel.of(exercise);

        WebMvcLinkBuilder apiLinks = linkTo(methodOn(this.getClass()).getAllExercises(userId));

        resource.add(apiLinks.withRel("all-exercises for user-" + userId));

        return resource;
    }

    @PostMapping(path = "/api/users/{userId}/exercises")
    public ResponseEntity<Object> createExercise(@Valid @PathVariable Long userId,
                                                 @RequestBody Exercise exercise) {
        Exercise savedExercise = exerciseService.createExerciseForUser(userId, exercise);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
                .buildAndExpand(savedExercise.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/api/users/{userId}/exercises/{exerciseId}")
    public ResponseEntity<Exercise> editExercise(@RequestBody Exercise newExercise,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long exerciseId) {
        Exercise exerciseToBeUpdated = exerciseService.
                editExerciseForUser(userId, exerciseId, newExercise);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
                .buildAndExpand(exerciseToBeUpdated.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/api/users/{userId}/exercises/{exerciseId}")
    public void deleteExercise(@PathVariable Long userId, @PathVariable Long exerciseId) {
        exerciseService.deleteExerciseForUser(userId, exerciseId);
    }
}
