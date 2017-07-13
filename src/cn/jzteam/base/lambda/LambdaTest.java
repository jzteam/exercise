package cn.jzteam.base.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

import org.junit.Before;
import org.junit.Test;

public class LambdaTest {
    
    private List<Person> list;
    
    {
        list = new ArrayList<>();
        list.add(new Person("jzteam1",22,220));
        list.add(new Person("jzteam2",26,230));
        list.add(new Person("jzteam3",25,240));
        list.add(new Person("jzteam4",23,250));
        list.add(new Person("jzteam5",21,260));
    }
    
    @Test
    public void test2(){
        list.stream()
                    .sorted((x,y)->Integer.compare(x.getAge(), y.getAge()))
                    .forEach(System.out::println);
    }
    
    @Test
    public void test1(){
        list.stream().filter((e)->e.getAge()>23)
        .limit(2).forEach(System.out::println);
        
        new Runnable() {
            
            @Override
            public void run() {
                System.out.println("你好");
                
            }
        }.run();
        
        Runnable r = () -> System.out.println("lambda 真棒");
        r.run();
    }

}
class Person{
    private String name;
    private int age;
    private int score;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    
    public Person(String name, int age, int score) {
        super();
        this.name = name;
        this.age = age;
        this.score = score;
    }
    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", score=" + score + "]";
    }
}
