package cn.jzteam.core.lambda;

import java.util.List;

public class StreamTest {

    public void result(List<TransOrder> list){



    }
}
class TransOrder{
    private Long userId;
    private Double btcAmount;
    private Double ethAmount;
    private Double bchAmount;

    public Double getBtcAmount() {
        return btcAmount;
    }

    public void setBtcAmount(Double btcAmount) {
        this.btcAmount = btcAmount;
    }

    public Double getEthAmount() {
        return ethAmount;
    }

    public void setEthAmount(Double ethAmount) {
        this.ethAmount = ethAmount;
    }

    public Double getBchAmount() {
        return bchAmount;
    }

    public void setBchAmount(Double bchAmount) {
        this.bchAmount = bchAmount;
    }
}