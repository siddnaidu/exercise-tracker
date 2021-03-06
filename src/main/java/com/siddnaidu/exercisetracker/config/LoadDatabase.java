package com.siddnaidu.exercisetracker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    @Bean
//    CommandLineRunner initDatabase(UserRepository userRepository, ExerciseRepository exerciseRepository) {
//
//        System.out.println("Test1");
////        return args -> {
////
////            userRepository.save(new User("Sidd", "Naidu", "siddnaidu@gmail.com",
////                    passwordEncoder.encode("password1234"), 5,
////                    9, 140, 26,
////                    Arrays.asList(new Role("ADMIN"))));
////
////            userRepository.findAll().forEach(user -> log.info("Preloaded " + user));
//
//
////            exerciseRepository.save(new Exercise("Push Ups", 3, 10));
////            exerciseRepository.save(new Exercise("Pull Ups", 3, 10));
////
////            exerciseRepository.findAll().forEach(exercise -> {
////                log.info("Preloaded " + exercise);
////            });
//
////            System.out.println("Test");
////
////        };
//    }
}