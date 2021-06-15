package com.siddnaidu.exercisetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Exercise {

    @Id
    @GeneratedValue
    private Long id;
    private String exerciseType;
    private int setCount;
    private int repCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Exercise() {}

    public Exercise(String exerciseType, int setCount, int repCount) {
        this.exerciseType = exerciseType;
        this.setCount = setCount;
        this.repCount = repCount;
    }

    public Exercise(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getSetCount() {
        return setCount;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }

    public int getRepCount() {
        return repCount;
    }

    public void setRepCount(int repCount) {
        this.repCount = repCount;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", exerciseType='" + exerciseType + '\'' +
                ", setCount=" + setCount +
                ", repCount=" + repCount +
                '}';
    }
}
