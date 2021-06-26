package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.exception.UserNotFoundException;
import com.siddnaidu.exercisetracker.model.Exercise;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.UserRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userServiceTest;

    @BeforeEach
    void setUp() {
        userServiceTest = new UserService(userRepository);
    }

    @Test
    void testGetAllUsers() {
        userServiceTest.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById() {
        Long id = 10L;
        User user = new User("Sidd", "Naidu");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(java.util.Optional.of(user));

        User savedUser = userServiceTest.getUserById(id);

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void testGetUserByIdNotFound(){
        Long id = 10L;

        Optional<User> user = userRepository.findById(id);
        assertThatThrownBy(() -> userServiceTest.getUserById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + id + " does not exist");

        assertThat(user.isPresent()).isEqualTo(false);
    }

    @Test
    void testCreateUser() {
        User user = new User("Sidd", "Naidu");

        userServiceTest.createUser(user);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void testEditUser() {
        Long id = 10L;
        User oldUser = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        oldUser.setId(id);
        float oldWeight = oldUser.getWeight();
        User newUser = new User("Sidd", "Naidu", 5,
                9, 145, 26);
        newUser.setId(id);
        User mockUser = mock(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(mockUser.getFirstName()).thenReturn(newUser.getFirstName());
        when(mockUser.getLastName()).thenReturn(newUser.getLastName());
        when(mockUser.getHeightFeet()).thenReturn(newUser.getHeightFeet());
        when(mockUser.getHeightInches()).thenReturn(newUser.getHeightInches());
        when(mockUser.getWeight()).thenReturn(newUser.getWeight());
        when(mockUser.getAge()).thenReturn(newUser.getAge());

        userServiceTest.editUser(mockUser, id);
        verify(mockUser).getFirstName();
        verify(mockUser).getLastName();
        verify(mockUser).getWeight();
        verify(mockUser).getAge();
        verify(mockUser).getHeightInches();
        verify(mockUser).getHeightFeet();
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User updatedUser = userArgumentCaptor.getValue();

        assertThat(updatedUser.getId()).isEqualTo(newUser.getId());
        assertThat(updatedUser.getId()).isEqualTo(oldUser.getId());
        assertThat(updatedUser.getWeight()).isNotEqualTo(oldWeight);
    }

    @Test
    void testEditUserCreateNewUser() {
        Long id = 10L;
        User newUser = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        newUser.setId(id);

//        when(userRepository.getById(10L)).thenReturn(null);

        userServiceTest.editUser(newUser, id);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User updatedUser = userArgumentCaptor.getValue();
        assertThat(updatedUser).isEqualTo(newUser);
    }

    @Test
    void testDeleteUser() {
        Long id = 10L;

        given(userRepository.existsById(id)).willReturn(true);

        userServiceTest.deleteUser(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void testDeleteUserNotFound() {
        Long id = 10L;

        given(userRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> userServiceTest.deleteUser(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + id + " does not exist");
        verify(userRepository, never()).deleteById(any());
    }
}