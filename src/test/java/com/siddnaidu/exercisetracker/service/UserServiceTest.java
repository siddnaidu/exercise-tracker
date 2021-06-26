package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.exception.UserNotFoundException;
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
        User user = new User("Sidd", "Naidu");
        user.setId(10L);
        when(userRepository.findById(10L)).thenReturn(java.util.Optional.of(user));

        User savedUser = userServiceTest.getUserById(10L);

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void testGetUserByIdNotFound(){
        Long id = Long.valueOf(10);

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
    @Disabled
    void testEditUser() {
//        User oldUser = new User("Sidd", "Naidu", 5,
//                9, 140, 26);
        User oldUser = mock(User.class);
//        User newUser = new User("Sidd", "Naidu", 5,
//                9, 145, 26);
        oldUser.setId(0L);
        User newUser = mock(User.class);
//        newUser.setId(10L);
//        newUser.setFirstName("Sidd");
//        newUser.setLastName("Naidu");
//        newUser.setAge(26);
//        newUser.setHeightFeet(5);
//        newUser.setHeightInches(9);
//        newUser.setWeight(145);

        given(userRepository.existsById(anyLong())).willReturn(true);
        doReturn(oldUser).when(userRepository).findById(anyLong());

        // argument capture gets both saves from userRepository
        userServiceTest.editUser(newUser, anyLong());
        verify(newUser).getFirstName();
//        verify(newUser).getLastName();
//        verify(newUser).getWeight();
//        verify(newUser).getAge();
//        verify(newUser).getHeightInches();
//        verify(newUser).getHeightFeet();
        verify(newUser).getId();
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User updatedUser = userArgumentCaptor.getValue();
        System.out.println(updatedUser.getWeight());

        assertThat(updatedUser).isEqualTo(newUser);
    }

    @Test
    void testEditUserCreateNewUser() {
        User newUser = new User("Sidd", "Naidu", 5,
                9, 140, 26);
        newUser.setId(10L);

//        when(userRepository.getById(10L)).thenReturn(null);

        userServiceTest.editUser(newUser, 10L);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User updatedUser = userArgumentCaptor.getValue();

        assertThat(updatedUser).isEqualTo(newUser);
    }

    @Test
    void testDeleteUser() {
        Long id = Long.valueOf(10);

        given(userRepository.existsById(id)).willReturn(true);

        userServiceTest.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void testDeleteUserNotFound() {
        Long id = Long.valueOf(10);

        given(userRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> userServiceTest.deleteUser(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + id + " does not exist");

        verify(userRepository, never()).deleteById(any());
    }
}