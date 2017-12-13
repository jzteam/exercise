package cn.jzteam.work;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldFillUtil {

    public static <F, E> void convertList(List<F> formList, String idField,FunctionListById<E, Long> listFunction, Convert<F, E> convert) {

        List<Long> idList = getIdsFromList(formList, idField);

        List<E> entityList = listFunction.listEntity(idList);

        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }

        Map<Long, E> map = new HashMap<>();
        entityList.forEach(x -> {
            // 默认所有entity的主键字段名称都是"id"
            Long id = getFieldValueFromObj("id", x);
            map.put(id, x);
        });

        for (F form : formList) {
            Long fieldValueFromObj = getFieldValueFromObj(idField, form);
            if (fieldValueFromObj == null) {
                continue;
            }

            E entity = map.get(fieldValueFromObj);
            if (entity == null) {
                continue;
            }

            // 回调封装规则
            convert.convert(form, entity);
        }
    }

    /**
     * 从对象集合List中抽出指定字段的集合List
     *
     * @param list
     * @param idField
     * @param <T>
     * @return
     */
    public static <T> List<Long> getIdsFromList(List<T> list, String idField) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        List<Long> idList = new ArrayList<>();
        for (T obj : list) {
            Long fieldValueFromObj = getFieldValueFromObj(idField, obj);
            if (fieldValueFromObj != null) {
                idList.add(fieldValueFromObj);
            }
        }

        return idList;
    }

    /**
     * 根据字段名从对象实例中获取数据value
     *
     * @param fieldName
     * @param obj
     * @return
     */
    public static Long getFieldValueFromObj(String fieldName, Object obj) {
        if (StringUtils.isEmpty(fieldName) || obj == null) {
            return null;
        }
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object id = field.get(obj);
            if (id != null) {
                return Long.valueOf(id.toString());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
