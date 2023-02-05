package cn.jzteam.core.grammar;
class Base {
    private int i;
    private void amethod(int iBase) {
        System.out.println("Base.amethod");
    }
}
class Over extends Base {
    private int i;
    public static void main(String args[]) {
        Over o = new Over();
        int iBase = 0 ;
        o.amethod(iBase) ;
    }
    private void amethod(int iOver) {
        System.out.println("Over.amethod");
    }
}
