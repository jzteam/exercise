package cn.jzteam.work;

public class DataMaker {

    public static void test1(){


        for(int i = 0;i<50;i++){
            StringBuilder sql = new StringBuilder("insert into user_share_profit(user_id,type,rate,amount) values (");
            // 10个用户
            int userId = Double.valueOf(Math.random()*10).intValue();
            // 5个币种
            int type = Double.valueOf(Math.random()*5).intValue();
            // rate 币种类型值/10
            double rate = type/10.0D;
            double amount = Double.valueOf(Math.random()*10);
            sql.append(userId).append(",").append(type).append(",").append(rate).append(",").append(amount).append(");");
            System.out.println(sql.toString());
        }

    }


    public static void main(String[] args) {
        test1();
    }
}
