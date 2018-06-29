package com.xinaml.frame.common.utils;

import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.entity.dynamic.Field;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.List;
import java.util.UUID;

public class BeanUtil {
    public static Object createObj(List<Field> fields) throws SerException {
        try {
            BeanGenerator generator = new BeanGenerator();
            generator.addProperty("id", String.class);
            for (Field field : fields) {
                generator.addProperty(field.getName(), Class.forName(field.getType().getCode()));
            }
            Object object = generator.create();
            BeanMap beanMap = BeanMap.create(object);
            beanMap.put("id", UUID.randomUUID().toString());
            for (Field field : fields) {
                beanMap.put(field.getName(), field.getVal());
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException("构建对象错误");
        }

    }


}
