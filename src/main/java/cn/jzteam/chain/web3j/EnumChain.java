package cn.jzteam.chain.web3j;

public enum EnumChain {

    OKC(66L, "https://exchainrpc.okex.org/"),
    OKCTEST(65L, "https://exchaintestrpc.okex.org/"),
    ;

    private Long chainID;
    private String url;

    EnumChain (Long chainID, String url) {
        this.chainID = chainID;
        this.url = url;
    }

    public Long getChainID(){
        return this.chainID;
    }

    public String getUrl(){
        return this.url;
    }
}
