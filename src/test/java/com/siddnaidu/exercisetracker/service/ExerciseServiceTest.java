package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserService userService;
    private ExerciseService exerciseServiceTest;

    @BeforeEach
    void setUp() {
        exerciseServiceTest = new ExerciseService(exerciseRepository, userService);
    }

    @Test
    @Disabled
    void testGetAllExercisesForUser() {
        Long id = 10L;

    }

    @Test
    @Disabled
    void testGetExerciseByIdForUser() {
    }

    @Test
    @Disabled
    void createExerciseForUser() {
    }

    @Test
    @Disabled
    void testEditExerciseForUser() {
    }

    @Test
    @Disabled
    void testDeleteExerciseForUser() {
    }
}