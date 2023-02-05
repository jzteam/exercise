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

//        String phone = Base64.getEncoder().encodeToString("18810246073".getBytes());
//        String email = Base64.getEncoder().encodeToString("zhujizhi1012@163.com".getBytes());
//        String userId = Base64.getEncoder().encodeToString("2006635".getBytes());
//        System.out.println("phone="+phone+",email="+email);
//        System.out.println("userId="+userId);

        int s = (int)(Math.random()*26) + 65;
//        System.out.println("a="+(int)'a');
//        System.out.println("A="+(int)'A');
        for(int i = 65;i<65+52;i++){

            System.out.println("s="+i+", result="+(char)i);
        }

    }
    
    public static void test1(){
        System.out.println("我是son，super.getClass=");
    }
}
