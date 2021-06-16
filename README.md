# Read Me First
This project is an exercise tracker created
using Java and the Spring Boot Framework.
Currently, there is only a backend created with
rest points for Users and Exercises.

# Getting Started

Users and Exercises can be created, updated,
deleted, and received from the server.

### Rest Points:

* api/users
* api/users/{userId}
* api/users/{userId}/exercises
* api/users/{userId}/exercises/{exerciseId}

### User Attributes

* id (Auto-generated)
* firstName
* lastName
* heightFeet
* heightInches
* weight
* age
* exercises (mapped in database)

### Exercise Attributes

* id (auto-generated)
* exerciseType
* setCount
* repCount
* equipment
* weight (weight of equipment used, Ex: Dumbbells)
* user (mapped in database)