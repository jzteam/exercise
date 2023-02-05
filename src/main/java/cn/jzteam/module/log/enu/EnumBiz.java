package cn.jzteam.module.log.enu;

/**
 * the project code under the OKC Dapp
 */
public enum EnumBiz {

    DEFAULT("DEFAULT"),
    VRF("VRF"),
    SUB("SUBGRAPH"),
    LST("LIQUIDITY_STAKING"),
    SWAP("SWAP"),
    BRI("BRIDGE"),
    FAU("FAUCET"),
    NFT("NFT"),
    ORA("ORACLE"),
    DOC("DOC"),
    MKT("MARKET");


    private final String code;

    EnumBiz (String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
