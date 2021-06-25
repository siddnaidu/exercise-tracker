package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.exception.ExerciseNotFoundException;
import com.siddnaidu.exercisetracker.exception.UserNotFoundException;
import com.siddnaidu.exercisetracker.model.Exercise;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserService userServiceTest;
    private ExerciseService exerciseServiceTest;

    @BeforeEach
    void setUp() {
        exerciseServiceTest = new ExerciseService(exerciseRepository, userServiceTest);
    }

    @Test
    void testGetAllExercisesForUser() {
        Long userId = 10L;
        User user = new User("Sidd", "Naidu");
        user.setId(userId);
        List<Exercise> exerciseList = new ArrayList();
        exerciseList.add(new Exercise("push ups", 3, 10));
        exerciseList.add(new Exercise("jogging"));
        user.setExercises(exerciseList);
        when(userServiceTest.getUserById(userId)).thenReturn(user);

        List<Exercise> savedExercises = exerciseServiceTest.getAllExercisesForUser(userId);

        assertThat(savedExercises).isEqualTo(exerciseList);
    }

    @Test
    @Disabled
    void testGetAllExercisesForUserNotFound() {
        Long userId = 10L;
        when(userServiceTest.getUserById(userId)).thenReturn(null);

        List<Exercise> savedExercises = exerciseServiceTest.getAllExercisesForUser(userId);
//        Optional<User> user = userRepository.findById(id);
        assertThatThrownBy(() -> exerciseServiceTest.getAllExercisesForUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + userId + " does not exist");

//        assertThat(user.isPresent()).isEqualTo(false);

    }

    @Test
    void testGetExerciseByIdForUser() {
        User user = new User("Sidd", "Naidu");
        user.setId(10L);
        Exercise exercise = new Exercise("jogging");
        exercise.setId(100L);
        exercise.setUser(user);
        when(userServiceTest.getUserById(10L)).thenReturn(user);
        when(exerciseRepository.findById(100L)).thenReturn(Optional.of(exercise));

        Exercise savedExercise = exerciseServiceTest.getExerciseByIdForUser(10L, 100L);

        assertThat(savedExercise).isEqualTo(exercise);
    }

    @Test
    void testGetExerciseByIdNotFoundForUser() {
        User user = new User("Sidd", "Naidu");
        user.setId(10L);
        when(userServiceTest.getUserById(10L)).thenReturn(user);

        Optional<Exercise> exercise = exerciseRepository.findById(100L);
        assertThatThrownBy(() -> exerciseServiceTest.getExerciseByIdForUser(10L, 100L))
                .isInstanceOf(ExerciseNotFoundException.class)
                .hasMessageContaining("Exercise with id " + 100L + " does not exist");

        assertThat(exercise.isPresent()).isEqualTo(false);
    }

    @Test
    void testCreateExerciseForUser() {
        User user = new User("Sidd", "Naidu");
        user.setId(10L);
        Exercise exercise = new Exercise("jogging");
        exercise.setId(100L);
        exercise.setUser(user);

        exerciseServiceTest.createExerciseForUser(10L, exercise);
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());

        Exercise savedExercise = exerciseArgumentCaptor.getValue();

        assertThat(savedExercise).isEqualTo(exercise);
    }

    @Test
    void testEditExerciseForUser() {
        User user = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        user.setId(10L);
        Exercise oldExercise = new Exercise("chest press", 3, 10,
                "barbells", 50);
        oldExercise.setId(100L);
        oldExercise.setUser(user);
        Exercise newExercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);
        newExercise.setId(100L);
        newExercise.setUser(user);

        when(exerciseRepository.save(newExercise)).thenReturn(newExercise);

        // argument capture gets both saves from exerciseRepository
        exerciseServiceTest.editExerciseForUser(10L, 100L, newExercise);
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());

        Exercise updatedExercise = exerciseArgumentCaptor.getValue();

        assertThat(updatedExercise.getId()).isEqualTo(oldExercise.getId());
        assertThat(updatedExercise.getEquipment()).isNotEqualTo(oldExercise.getEquipment());
    }

    @Test
    void testEditExerciseCreateNewExerciseForUser() {
        User user = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        user.setId(10L);
        Exercise newExercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);
        newExercise.setId(100L);
        newExercise.setUser(user);

//        when(userRepository.getById(10L)).thenReturn(null);

        exerciseServiceTest.editExerciseForUser(10L, 100L, newExercise);
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());

        Exercise updatedExercise = exerciseArgumentCaptor.getValue();

        assertThat(updatedExercise).isEqualTo(newExercise);
    }

    @Test
    void testDeleteExerciseForUser() {
        Long userId = Long.valueOf(10);
        Long exerciseId = 100L;

        given(exerciseRepository.existsById(exerciseId)).willReturn(true);

        exerciseServiceTest.deleteExerciseForUser(userId, exerciseId);

        verify(exerciseRepository).deleteById(exerciseId);
    }

    @Test
    void testDeleteExerciseNotFoundForUser() {
        Long userId = Long.valueOf(10);
        Long exerciseId = 100L;

        given(exerciseRepository.existsById(exerciseId)).willReturn(false);

        assertThatThrownBy(() -> exerciseServiceTest.deleteExerciseForUser(userId, exerciseId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Exercise with id " + exerciseId + " does not exist");

        verify(exerciseRepository, never()).deleteById(any());
    }
}