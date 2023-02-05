package cn.jzteam.work;

import java.util.List;

@FunctionalInterface
public interface FunctionListById<E, PK> {
    List<E> listEntity(List<PK> idList);
}
