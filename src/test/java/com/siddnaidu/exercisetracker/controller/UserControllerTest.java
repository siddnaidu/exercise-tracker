package com.siddnaidu.exercisetracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.UserRepository;
import com.siddnaidu.exercisetracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userServiceTest;

    @Test
    void testGetAllUsers() throws Exception{
        List<User> userList = new ArrayList<>();
        userList.add(new User("Sidd", "Naidu"));
        userList.add(new User("John", "Doe"));

        when(userServiceTest.getAllUsers()).thenReturn(userList);

        String url = "/api/users";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();

        String jsonResponseFromAPI = mvcResult.getResponse().getContentAsString();
        String jsonResponseCreatedForTest = objectMapper.writeValueAsString(userList);

        assertThat(jsonResponseFromAPI).isEqualTo(jsonResponseCreatedForTest);
    }

    @Test
    void testGetUserById() throws Exception {
        Long id = 10L;
        User user = new User("Sidd", "Naidu");
        user.setId(id);

        when(userServiceTest.getUserById(id)).thenReturn(user);

        String url = "/api/users/{id}";

        mockMvc.perform(MockMvcRequestBuilders.get(url, id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(id));
    }

    @Test
    void testCreateUser() throws JsonProcessingException, Exception {
        Long id = 10L;
        User user = new User("Sidd", "Naidu");
        User savedUser = new User("Sidd", "Naidu");
        savedUser.setId(id);

        String url = "/api/users";

        when(userServiceTest.createUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    void testEditUser() throws Exception {
        Long id = 10L;
        User newUser = new User("Sidd", "Naidu", 5,
                9, 145, 26);
        newUser.setId(id);

        when(userServiceTest.editUser(any(User.class), eq(id))).thenReturn(newUser);

        String url = "/api/users/{id}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(url, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteUser() throws Exception {
        Long id = 10L;

        String url = "/api/users/{id}";

        mockMvc.perform(MockMvcRequestBuilders
                .delete(url, id))
                .andExpect(status().isOk());
    }
}
