package com.SAP.studyd8;

public class User {

    private String firstName, lastName, major, studyHabits, university, userId, username;

    public User(){}

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public void setFirstName(String id) {
        firstName = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUniversity() {
        return university;
    }

    public String getMajor() {
        return major;
    }

    public String getStudyHabits() {
        return studyHabits;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setStudyHabits(String studyHabits) {
        this.studyHabits = studyHabits;
    }

    public void setUniversity(String university) {
        this.university = university;
    }


}
