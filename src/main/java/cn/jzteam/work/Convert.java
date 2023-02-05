package cn.jzteam.work;

@FunctionalInterface
public interface Convert<F, E> {
    void convert(F from, E entity);
}
