package cn.jzteam.tools.json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class JsonTest implements Serializable {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        System.out.println(JsonTest.class.getName());
        JsonTest jsonTest = new JsonTest();
        // ObjectOutputStream oos = new ObjectOutputStream(new
        // FileOutputStream(new File()));

        BigDecimal divide = BigDecimal.valueOf(3.28).divide(BigDecimal.valueOf(1), 1, RoundingMode.HALF_UP);
        System.out.println("divide=" + divide.doubleValue());

        BigDecimal valueOf = BigDecimal.valueOf(3.3f).setScale(3, RoundingMode.HALF_UP);
        System.out.println("valueOf=" + valueOf.toString());
        int compareTo = divide.compareTo(valueOf);
        System.out.println(compareTo);

    }
}
