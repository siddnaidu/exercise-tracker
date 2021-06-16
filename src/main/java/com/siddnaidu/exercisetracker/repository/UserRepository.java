package com.siddnaidu.exercisetracker.repository;

import com.siddnaidu.exercisetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
