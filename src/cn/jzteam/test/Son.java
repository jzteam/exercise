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
        Class<Object> superclass = Object.class.getSuperclass();
        System.out.println(superclass);
    }
}
