package com.siddnaidu.exercisetracker.repository;

import com.siddnaidu.exercisetracker.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

}
