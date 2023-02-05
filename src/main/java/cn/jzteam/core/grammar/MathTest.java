package cn.jzteam.core.grammar;

public class MathTest {
    public static void main(String[] args) {
        System.out.println(1<<0);
        System.out.println(1<<1);
        System.out.println(1<<2);
        System.out.println(1<<3);
        System.out.println(1<<4);
        System.out.println(1<<5);
        System.out.println(1<<6);

        System.out.println(1<<16);
        System.out.println(1<<17);
        System.out.println(1<<18);
        System.out.println(1<<19);
        System.out.println(1<<20);
        System.out.println(1<<21);
        System.out.println(1<<22);

        System.out.println((1<<0)
                + (1<<1)
                + (1<<2)
                + (1<<3)
                + (1<<4)
                + (1<<5)
                + (1<<6)
//                + (1<<16)
//                + (1<<17)
//                + (1<<18)
//                + (1<<19)
//                + (1<<20)
//                + (1<<21)
//                + (1<<22)
        );
    }
}
