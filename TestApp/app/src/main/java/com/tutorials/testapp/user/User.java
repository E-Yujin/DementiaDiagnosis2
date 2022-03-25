package com.tutorials.testapp.user;

public class User {

    private int id;
    private String name;
    private String birth;
    private String sex;
    private String edu;

    public User() {}

    public User(String name, String birth, String sex, String edu) {
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.edu = edu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }
}
