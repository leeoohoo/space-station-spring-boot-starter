package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.config.SpaceStationAutoConfiguration;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.DeleteManager;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SelectManager;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.UpdateManager;
import com.oohoo.spacestationspringbootstarter.dto.query.mysql.MysqlDtoInserter;
import com.oohoo.spacestationspringbootstarter.dto.query.mysql.MysqlDtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.mysql.MysqlQuery;

import java.util.List;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 11 November 2022
 */
public class EQ {

    /**
     * 链式调用
     * @param clazz 要查询的主表
     * @return
     */
    public static SelectManager find(Class<?> clazz){
        MysqlQuery mysqlQuery = MysqlQuery.init();
        return mysqlQuery.create().from(clazz);
    }

    /**
     * 会根据传入的DtoClass 自动生成 select语句，但是如果写了select 字段则优先select方法中传递的字段
     *
     * @param
     * @return
     */
    public static DtoQuery find(DTO dto) {
        DtoQuery init = MysqlDtoQuery.init(dto);
        init.scan();
        return init;
    }


    public static DtoInserter insert(DTO dto) {
        DtoInserter init = MysqlDtoInserter.init(dto);
        init.scan();
        return init;
    }

    public static DtoInserter insert(List<? extends DTO> dtos) {
        DtoInserter init = MysqlDtoInserter.init(dtos, SpaceStationAutoConfiguration.DEFAULT_BATCH_SIZE);
        init.scan();
        return init;
    }

    public static DtoInserter insert(List<? extends DTO> dtos,Integer batchSize) {
        DtoInserter init = MysqlDtoInserter.init(dtos,batchSize);
        init.scan();
        return init;
    }

    public static<T> UpdateManager update(Class<T> clazz) {
        MysqlQuery init = MysqlQuery.init();
        return init.create().update(clazz);
    }

    public static <T> DeleteManager delete(Class<T> clazz){
        MysqlQuery init = MysqlQuery.init();
        return init.create().delete(clazz);
    }
}
