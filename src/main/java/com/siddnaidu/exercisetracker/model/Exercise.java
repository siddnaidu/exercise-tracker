package com.siddnaidu.exercisetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Exercise {

    @Id
    @GeneratedValue
    private Long id;
    private String exerciseType;
    private int setCount;
    private int repCount;
    private String equipment;
    private float weight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    public Exercise() {}

    public Exercise(String exerciseType, int setCount, int repCount) {
        this.exerciseType = exerciseType;
        this.setCount = setCount;
        this.repCount = repCount;
    }

    public Exercise(String exerciseType, int setCount, int repCount, String equipment, float weight) {
        this.exerciseType = exerciseType;
        this.setCount = setCount;
        this.repCount = repCount;
        this.equipment = equipment;
        this.weight = weight;
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

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
                ", equipment='" + equipment + '\'' +
                ", weight=" + weight +
                ", user=" + user +
                '}';
    }
}
