package user;

public class User {

    private String serial_code;
    private String name;
    private String birth;
    private int age;
    private String sex;
    private String edu;
    private int score;

    public User() {}

    public User(String serial_code, String name, String birth, int age, String sex, String edu, int score) {
        this.serial_code = serial_code;
        this.name = name;
        this.birth = birth;
        this.age = age;
        this.sex = sex;
        this.edu = edu;
        this.score = score;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
