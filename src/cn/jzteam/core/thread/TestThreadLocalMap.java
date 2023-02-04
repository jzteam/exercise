package cn.jzteam.core.thread;

/**
 * 验证 ThreadLocalMap 的位置，
 *
 */
public class TestThreadLocalMap {
    public static void main(String[] args) {
        // 当前主线程放入对象到ThreadLocalMap中
        ThreadLocal<Object> mainThreadLocal = new ThreadLocal<>();
        mainThreadLocal.get();
    }
}
