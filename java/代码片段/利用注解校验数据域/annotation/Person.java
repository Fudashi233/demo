package annotation;

import java.util.Collection;

public class Person {

    private String name;
    private String email;
    @NumberCheck(min=2,max=5)
    private int age = 7;
    private Integer wrapperAge;
    private Collection hobby;

    public Person() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }

    public Collection getHobby() {
        return hobby;
    }

    public void setHobby(Collection hobby) {
        this.hobby = hobby;
    }

    public Integer getWrapperAge() {
        return wrapperAge;
    }

    public void setWrapperAge(Integer wrapperAge) {
        this.wrapperAge = wrapperAge;
    }
}