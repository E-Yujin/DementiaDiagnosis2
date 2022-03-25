package user;

public class EducationAge {

    private int age; // 나이
    private int illiterate; // 비문해
    private int uneducated; // 무학
    private int elementarySchool; // 초등졸업
    private int secondarySchool; // 중등졸업
    private int highSchool; // 고등졸업
    private int university; // 대학졸업

    public EducationAge() { }

    public EducationAge(int age, int illiterate, int uneducated, int elementarySchool, int secondarySchool, int highSchool, int university) {
        this.age = age;
        this.illiterate = illiterate;
        this.uneducated = uneducated;
        this.elementarySchool = elementarySchool;
        this.secondarySchool = secondarySchool;
        this.highSchool = highSchool;
        this.university = university;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getIlliterate() {
        return illiterate;
    }

    public void setIlliterate(int illiterate) {
        this.illiterate = illiterate;
    }

    public int getUneducated() {
        return uneducated;
    }

    public void setUneducated(int uneducated) {
        this.uneducated = uneducated;
    }

    public int getElementarySchool() {
        return elementarySchool;
    }

    public void setElementarySchool(int elementarySchool) {
        this.elementarySchool = elementarySchool;
    }

    public int getSecondarySchool() {
        return secondarySchool;
    }

    public void setSecondarySchool(int secondarySchool) {
        this.secondarySchool = secondarySchool;
    }

    public int getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(int highSchool) {
        this.highSchool = highSchool;
    }

    public int getUniversity() {
        return university;
    }

    public void setUniversity(int university) {
        this.university = university;
    }
}
