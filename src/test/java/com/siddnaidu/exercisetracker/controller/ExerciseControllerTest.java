package com.siddnaidu.exercisetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siddnaidu.exercisetracker.model.Exercise;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.service.ExerciseService;
import com.siddnaidu.exercisetracker.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExerciseService exerciseServiceTest;

    @Test
    void testGetAllExercisesForUser() throws Exception {
        Long userId = 10L;
        List<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise("pullups"));
        exerciseList.add(new Exercise("chest press", 3, 10,
                "dumbbells", 50));

        when(exerciseServiceTest.getAllExercisesForUser(userId)).thenReturn(exerciseList);

        String url = "/api/users/{userId}/exercises";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url, userId))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        String jsonResponseFromAPI = mvcResult.getResponse().getContentAsString();
        String jsonResponseCreatedForTest = objectMapper.writeValueAsString(exerciseList);

        assertThat(jsonResponseFromAPI).isEqualTo(jsonResponseCreatedForTest);
    }

    @Test
    void testGetAllExerciseByIdForUser() throws Exception {
        Long userId = 10L;
        Long exerciseId = 100L;
        Exercise exercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);
        exercise.setId(exerciseId);

        when(exerciseServiceTest.getExerciseByIdForUser(userId, exerciseId)).thenReturn(exercise);

        String url = "/api/users/{userId}/exercises/{exerciseId}";

        mockMvc.perform(MockMvcRequestBuilders.get(url, userId, exerciseId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(exerciseId));
    }

    @Test
    void testCreateExerciseForUser() throws Exception {
        Long userId = 10L;
        User user = new User("Sidd", "Naidu");
        user.setId(userId);
        Exercise exercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);

        String url = "/api/users/{userId}/exercises";

        when(exerciseServiceTest.createExerciseForUser(eq(userId), any(Exercise.class)))
                .thenReturn(exercise);

        mockMvc.perform(MockMvcRequestBuilders
                .post(url, userId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(exercise)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testEditExerciseForUser() throws Exception {
        Long userId = 10L;
        Long exerciseId = 100L;
        Exercise newExercise = new Exercise("chest press", 3, 10,
                "dumbbells", 50);
        newExercise.setId(exerciseId);

        when(exerciseServiceTest.editExerciseForUser(eq(userId), eq(exerciseId), any(Exercise.class)))
                .thenReturn(newExercise);

        String url = "/api/users/{userId}/exercises/{exerciseId}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(url, userId, exerciseId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newExercise)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteExerciseForUser() throws Exception{
        Long userId = 10L;
        Long exerciseId = 100L;

        String url = "/api/users/{userId}/exercises/{exerciseId}";

        mockMvc.perform(MockMvcRequestBuilders
                .delete(url, userId, exerciseId))
                .andExpect(status().isOk());
    }
}
