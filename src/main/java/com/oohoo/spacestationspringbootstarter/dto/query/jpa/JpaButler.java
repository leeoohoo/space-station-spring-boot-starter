package com.oohoo.spacestationspringbootstarter.dto.query.jpa;

import com.oohoo.spacestationspringbootstarter.config.SpringUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.*;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 01 December 2022
 */
public class JpaButler extends AbstractSearch {

    private final EntityManager em;


    public JpaButler(EntityManager em) {
        this.em = em;
    }


    @Override
    public <T extends DTO> EPage<T> findPage(T dto, Integer pageNo, Integer pageSize) {
        DtoQuery dtoQuery = EQ.find(dto);
        Class<T> aClass = (Class<T>) dto.getClass();
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams());
        return findPage(dtoQuery.getSql(), dtoQuery.getParams(), aClass, init, pageNo, pageSize);


    }

    @Override
    public <T extends DTO> EPage<T> findPage(SqlManager sqlManager, Class<T> resultClazz, Integer pageNo, Integer pageSize) {
        Query init = this.init(sqlManager.getSelectSql(), sqlManager.getParams());
        return findPage(sqlManager.getSelectSql(), sqlManager.getParams(), resultClazz, init, pageNo, pageSize);
    }

    @Override
    public <T extends DTO> List<T> findList(T dto) {
        DtoQuery dtoQuery = EQ.find(dto);
        Class<T> aClass = (Class<T>) dto.getClass();
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams());
        return this.findList(init, aClass);
    }

    @Override
    public <T> List<T> findList(SqlManager sqlManager, Class<T> resultClazz) {
        Query init = this.init(sqlManager.getSelectSql(), sqlManager.getParams());
        return this.findList(init, resultClazz);
    }

    @Override
    public <T extends DTO> T findOne(T dto) {
        DtoQuery dtoQuery = EQ.find(dto);
        Class<T> aClass = (Class<T>) dto.getClass();
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams());
        Map<String, Object> one = this.findOne(init);
        if (null == one) {
            return null;
        }
        return ClassUtils.mapToObj(one, aClass);
    }

    @Override
    public <T> T findOne(SqlManager sqlManager, Class<T> result) {
        Query init = this.init(sqlManager.getSelectSql(), sqlManager.getParams());
        Map<String, Object> one = this.findOne(init);
        if (null == one) {
            return null;
        }
        return ClassUtils.mapToObj(one, result);
    }

    @Override
    public <T> EPage<T> count(String sql, List<Object> params) {
        return this.findCount(sql, params);
    }

    @Override
    public Object insert(DTO dto) {

        DtoInserter insert = EQ.insert(dto);
        this.execute(insert.getInsertOneSql(), insert.getParams());
        return insert.getEntity();


    }

    @Override
    public <T extends DTO> void insertBatch(List<T> dtoList, Integer batchSize) {
        DtoInserter insert = EQ.insert(dtoList, batchSize);
        AbstractPlatformTransactionManager transactionManager = this.getTransactionManager();
        TransactionStatus transactionStatus = this.getTransactionStatus(transactionManager);
        try {
            insert.getBatchInsertContainers().forEach(it -> {
                Query init = this.init(it.getSql(), it.getParams());
                init.executeUpdate();
            });
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new DtoQueryException("批量插入发生异常,e:[" + e.getMessage() + "]");
        } finally {
            this.em.close();
        }

    }


    @Override
    public void update(SqlManager sqlManager) {
        this.execute(sqlManager.getUpdateSql(), sqlManager.getParams());
    }


    @Override
    public void delete(SqlManager sqlManager) {
        this.execute(sqlManager.getDeleteSql(), sqlManager.getParams());
    }


    private void execute(String sql, List<Object> params) {
        try {
            Query init = this.init(sql, params);
            init.executeUpdate();
        } catch (Exception e) {
            throw new DtoQueryException("执行sql 发生异常，e:[" + e.getMessage() + "，sql:" + sql + ",params:" + params + "]");
        } finally {
            this.em.close();
        }
    }

    private <T> EPage<T> findCount(String sql, List<Object> params) {
        EPage<T> ePage = new EPage<T>();
        String selectCountSql = sql.substring(sql.indexOf("select "), sql.indexOf(" from "));
        Integer paramsCount = this.hasParams(selectCountSql, 0);
        if (paramsCount > 0) {
            params = params.subList(paramsCount, params.size() - 1);
        }
        String replace = sql.replace(selectCountSql, "select count(1) as total ");
        Query init = this.init(replace, params);
        Map<String, Object> one = null;
        try {
            one = (Map<String, Object>) init.getSingleResult();
        } catch (Exception e) {
            throw new DtoQueryException("查询总行数时发生异常，e:[" + e.getMessage() + "]");
        }
        if (null == one) {
            ePage.setTotal(0);
        } else {
            ePage.setTotal(Integer.valueOf(String.valueOf(one.get("total"))));
        }
        return ePage;
    }

    private <T extends DTO> EPage<T> findPage(String sql, List<Object> params,
                                              Class<T> resultClazz, Query query,
                                              Integer pageNo, Integer pageSize) {
        AbstractPlatformTransactionManager transactionManager = this.getTransactionManager();
        TransactionStatus transactionStatus = this.getTransactionStatus(transactionManager);
        try {
            EPage<T> page = this.count(sql, params);
            findPage(query, resultClazz, page, pageNo, pageSize);
            transactionManager.commit(transactionStatus);
            return page;
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw new DtoQueryException("查询分页发生异常,e:[" + e.getMessage() + "]");
        } finally {
            this.em.close();
        }
    }

    private <T> void findPage(Query query, Class<T> resultClazz,
                              EPage<T> ePage,
                              Integer pageNo, Integer pageSize) {
        if (null == pageNo || pageNo <= 0) {
            pageNo = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        ePage.setCurrentPage(pageNo);
        ePage.setPageSize(pageSize);
        query.setMaxResults(pageSize);
        query.setFirstResult((pageNo - 1) * pageSize);
        try {
            List<Map<String, Object>> resultList = query.getResultList();
            if (CollectionUtils.isEmpty(resultList)) {
                ePage.setList(null);
            }
            List<T> list = resultList.stream().map(it -> ClassUtils.mapToObj(it, resultClazz))
                    .collect(Collectors.toList());
            ePage.setList(list);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DtoQueryException("查询发生了其他异常,e:[" + exception.getMessage() + "]");
        }

    }

    private <T> List<T> findList(Query query, Class<T> resultClazz) {
        try {
            List<T> result = new ArrayList<>();
            List<Map<String, Object>> list = query.getResultList();
            if (CollectionUtils.isEmpty(list)) {
                return result;
            }
            result = list.stream().map(it -> {
                return ClassUtils.mapToObj(it, resultClazz);
            }).collect(Collectors.toList());
            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DtoQueryException("查询发生了其他异常,e:[" + exception.getMessage() + "]");
        } finally {
            this.em.close();
        }

    }


    private Map<String, Object> findOne(Query query) {
        try {
            return (Map<String, Object>) query.getSingleResult();
        } catch (NonUniqueResultException nonUniqueResultException) {
            throw new DtoQueryException("希望找到一个，但是却又两个结果哦");
        } catch (NoResultException noResultException) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtoQueryException("查询发生了其他异常,e:[" + e.getMessage() + "]");
        } finally {
            this.em.close();
        }
    }


    private <T> Query init(String sql, List<Object> params) {
        Query nativeQuery = em.createNativeQuery(sql);
        if (null != params) {
            for (int i = 0; i < params.size(); i++) {
                nativeQuery.setParameter(i + 1, params.get(i));
            }
        }
        NativeQueryImpl unwrap = nativeQuery.unwrap(NativeQueryImpl.class);
        unwrap.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return unwrap;
    }


    private Integer hasParams(String selectCountSql, Integer counter) {
        if (!StringUtils.hasLength(selectCountSql)) {
            throw new DtoQueryException("生成Count 语句时发生异常，原语句不能为空");
        }
        if (!selectCountSql.contains("?")) {
            return 0;
        } else if (selectCountSql.contains("?")) {
            counter++;
            hasParams(selectCountSql.substring(selectCountSql.indexOf("?") +
                    "?".length()), counter);
            return counter;
        }
        return 0;
    }

    private AbstractPlatformTransactionManager getTransactionManager() {
        AbstractPlatformTransactionManager transactionManager =
                SpringUtils.getBean("transactionManager");
        assert transactionManager != null;
        // 获得事务管理
        return transactionManager;
    }

    private TransactionStatus getTransactionStatus(AbstractPlatformTransactionManager transactionManager) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionManager.getTransaction(def);
    }
}
