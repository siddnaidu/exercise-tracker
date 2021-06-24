package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.exception.ExerciseNotFoundException;
import com.siddnaidu.exercisetracker.model.Exercise;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.ExerciseRepository;
import com.siddnaidu.exercisetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserService userService;

    public ExerciseService(ExerciseRepository exerciseRepository, UserService userService) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
    }

    public List<Exercise> getAllExercisesForUser(Long userId) {
        User user = userService.getUserById(userId);
        return user.getExercises();
    }

    public Exercise getExerciseByIdForUser(Long userId, Long exerciseId) {
        User user = userService.getUserById(userId);

        Optional<Exercise> exerciseOptional = exerciseRepository.findById(exerciseId);

        if(!exerciseOptional.isPresent()) {
            throw new ExerciseNotFoundException("exercise-id: " + exerciseId);
        }

        Exercise exercise = exerciseOptional.get();

        if(exercise.getUser() != user) {
            throw new ExerciseNotFoundException("Excersize: " + exerciseId
                    + "does not belong to User: " + userId);
        }
        return exercise;
    }

    public Exercise createExerciseForUser(Long userId, Exercise exercise) {
        User user = userService.getUserById(userId);

        exercise.setUser(user);

        return exerciseRepository.save(exercise);
    }

    public Exercise editExerciseForUser(Long userId, Long exerciseId, Exercise newExercise) {
        User user = userService.getUserById(userId);

        Exercise exerciseToBeUpdated = exerciseRepository.findById(exerciseId)
                .map(exercise -> {
                    exercise.setExerciseType(newExercise.getExerciseType());
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

        return exerciseToBeUpdated;
    }

    public void deleteExerciseForUser(Long userId, Long exerciseId) {
        // Check User exists
        User user = userService.getUserById(userId);
        exerciseRepository.deleteById(exerciseId);
    }
}
