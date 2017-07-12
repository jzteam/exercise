package cn.jzteam.test;

public class Son extends Parent {

    private String school;
    private Integer score;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public static void main(String[] args) {
        Parent son = null;
        son.test1();// invokestatic不需要实例
    }
    
    public static void test1(){
        System.out.println("我是son，super.getClass=");
    }
}
