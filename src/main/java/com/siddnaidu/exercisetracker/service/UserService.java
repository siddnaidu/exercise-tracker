package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.exception.UserNotFoundException;
import com.siddnaidu.exercisetracker.model.User;
import com.siddnaidu.exercisetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        return user.get();
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User editUser(User newUser, Long id) {
        User userToBeUpdated = userRepository.findById(id) //
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setAge(newUser.getAge());
                    user.setHeightFeet(newUser.getHeightFeet());
                    user.setHeightInches(newUser.getHeightInches());
                    user.setWeight(newUser.getWeight());
                    user.setExercises(newUser.getExercises());
                    return userRepository.save(user);
                }) //
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
        return userToBeUpdated;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
