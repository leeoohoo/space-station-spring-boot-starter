package com.oohoo.spacestationspringbootstarter.dto.query.init;

import com.oohoo.spacestationspringbootstarter.config.SpringUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.intercept.CreateByInit;
import com.oohoo.spacestationspringbootstarter.dto.query.intercept.IdInit;
import com.oohoo.spacestationspringbootstarter.dto.query.intercept.LastUpdateByInit;
import com.oohoo.spacestationspringbootstarter.dto.query.intercept.SnowFlakeUtil;

import java.util.Map;
import java.util.Optional;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/12/20
 */
public  final class InsertInit {

    public static final IdInit idInit = getBean(IdInit.class);

    public static final SnowFlakeUtil snowFlakeGenerator = new SnowFlakeUtil();

    public static final CreateByInit createByInit = getBean(CreateByInit.class);

    public static final LastUpdateByInit lastUpdateByInit = getBean(LastUpdateByInit.class);



    private static  <T> T getBean(Class<T> clazz){
        Map<String, T> beansOfType = SpringUtils.getBeansOfType(clazz);
        if(beansOfType.size() > 0) {
            Optional<String> first = beansOfType.keySet().stream().findFirst();
            T t = beansOfType.get(first.get());
            return t;
        }
        return null;
    }

}
