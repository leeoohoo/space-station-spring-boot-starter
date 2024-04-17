package com.oohoo.spacestationspringbootstarter.dto.query.jpa;

import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import org.springframework.util.CollectionUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/12/15
 */
public class JpaUtils {

    public static final Field getIdField(Class<?> clazz){
        List<Field> collect = Arrays.stream(clazz.getDeclaredFields()).filter(
                it -> null != it.getDeclaredAnnotation(Id.class)).collect(Collectors.toList()
        );
        if(CollectionUtils.isEmpty(collect)) {
            throw new DtoQueryException("实体类中缺少Id，请检查实体类注解。clazz:"+clazz.getName());
        }
        if(collect.size() > 1) {
            throw new DtoQueryException("实体类中存在多个主键，请检查实体类。clazz:"+clazz.getName());
        }
        return collect.get(0);
    }
}
