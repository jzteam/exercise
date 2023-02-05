package cn.jzteam.test;

public class Parent {

    private String name;
    private Integer age;
    private String school;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    public static synchronized void test1(){
        System.out.println("我是parent，super.getClass=");
    }

}
