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

//    @Test
//    @Disabled
//    void testGetAllExercisesForUserNotFound() {
//        Long userId = 10L;
//        when(userServiceTest.getUserById(userId)).thenReturn(null);
//
//        List<Exercise> savedExercises = exerciseServiceTest.getAllExercisesForUser(userId);
////        Optional<User> user = userRepository.findById(id);
//        assertThatThrownBy(() -> exerciseServiceTest.getAllExercisesForUser(userId))
//                .isInstanceOf(UserNotFoundException.class)
//                .hasMessageContaining("User with id " + userId + " does not exist");
//
////        assertThat(user.isPresent()).isEqualTo(false);
//
//    }

    @Test
    void testGetExerciseByIdForUser() {
        Long userId = 10L;
        Long exerciseId = 100L;
        User user = new User("Sidd", "Naidu");
        user.setId(userId);
        Exercise exercise = new Exercise("jogging");
        exercise.setId(exerciseId);
        exercise.setUser(user);
        when(userServiceTest.getUserById(userId)).thenReturn(user);
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));

        Exercise savedExercise = exerciseServiceTest.getExerciseByIdForUser(userId, exerciseId);

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
        Long userId = 10L;
        Long exerciseId = 100L;
        User user = new User("Sidd", "Naidu");
        user.setId(userId);
        Exercise exercise = new Exercise("jogging");
        exercise.setId(exerciseId);
        exercise.setUser(user);

        exerciseServiceTest.createExerciseForUser(userId, exercise);
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());

        Exercise savedExercise = exerciseArgumentCaptor.getValue();

        assertThat(savedExercise).isEqualTo(exercise);
    }

    @Test
    void testEditExerciseForUser() {
        Long userId = 10L;
        Long exerciseId = 100L;
        User user = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        user.setId(userId);
        Exercise oldExercise = new Exercise("chest press", 3, 10,
                "barbells", 50);
        oldExercise.setId(exerciseId);
        oldExercise.setUser(user);
        String oldExerciseType = oldExercise.getExerciseType();
        Exercise newExercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);
        newExercise.setId(exerciseId);
        newExercise.setUser(user);
        Exercise mockExercise = mock(Exercise.class);

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(oldExercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(newExercise);
        when(mockExercise.getExerciseType()).thenReturn(newExercise.getExerciseType());
        when(mockExercise.getRepCount()).thenReturn(newExercise.getRepCount());
        when(mockExercise.getSetCount()).thenReturn(newExercise.getSetCount());
        when(mockExercise.getEquipment()).thenReturn(newExercise.getEquipment());
        when(mockExercise.getWeight()).thenReturn(newExercise.getWeight());

        exerciseServiceTest.editExerciseForUser(userId, exerciseId, mockExercise);
        verify(mockExercise).getExerciseType();
        verify(mockExercise).getEquipment();
        verify(mockExercise).getWeight();
        verify(mockExercise).getRepCount();
        verify(mockExercise).getSetCount();
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());

        Exercise updatedExercise = exerciseArgumentCaptor.getValue();

        assertThat(updatedExercise.getId()).isEqualTo(newExercise.getId());
        assertThat(updatedExercise.getId()).isEqualTo(oldExercise.getId());
        assertThat(updatedExercise.getEquipment()).isNotEqualTo(oldExerciseType);
    }

    @Test
    void testEditExerciseCreateNewExerciseForUser() {
        Long userId = 10L;
        Long exerciseId = 100L;
        User user = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        user.setId(userId);
        Exercise newExercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);
        newExercise.setId(exerciseId);
        newExercise.setUser(user);

//        when(userRepository.getById(10L)).thenReturn(null);

        exerciseServiceTest.editExerciseForUser(userId, exerciseId, newExercise);
        ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseArgumentCaptor.capture());

        Exercise updatedExercise = exerciseArgumentCaptor.getValue();

        assertThat(updatedExercise).isEqualTo(newExercise);
    }

    @Test
    void testDeleteExerciseForUser() {
        Long userId = 10L;
        Long exerciseId = 100L;

        given(exerciseRepository.existsById(exerciseId)).willReturn(true);

        exerciseServiceTest.deleteExerciseForUser(userId, exerciseId);

        verify(exerciseRepository).deleteById(exerciseId);
    }

    @Test
    void testDeleteExerciseNotFoundForUser() {
        Long userId = 10L;
        Long exerciseId = 100L;

        given(exerciseRepository.existsById(exerciseId)).willReturn(false);

        assertThatThrownBy(() -> exerciseServiceTest.deleteExerciseForUser(userId, exerciseId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Exercise with id " + exerciseId + " does not exist");

        verify(exerciseRepository, never()).deleteById(any());
    }
}