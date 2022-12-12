package com.oohoo.spacestationspringbootstarter.dto.query.jpa;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractSearch;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.EPage;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
public class JpaSearch extends AbstractSearch {

    private final EntityManager em;


    public JpaSearch(EntityManager em) {
        this.em = em;
    }


    @Override
    public <T> EPage<T> findPage(DtoQuery dtoQuery, Class<T> resultClazz,Integer pageNo,int pageSize) {
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams(), resultClazz);
        EPage<T> page = findPage(init, resultClazz, pageNo, pageSize);
        return page;
    }

    @Override
    public <T> EPage<T> findPage(SqlManager sqlManager, Class<T> resultClazz,Integer pageNo,int pageSize) {
        Query init = this.init(sqlManager.getSql(), sqlManager.getParams(), resultClazz);
        EPage<T> page = findPage(init, resultClazz, pageNo, pageSize);
        return page;
    }

    @Override
    public <T> List<T> findList(DtoQuery dtoQuery, Class<T> resultClazz) {
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams(), resultClazz);
        return this.findList(init,resultClazz);
    }

    @Override
    public <T> List<T> findList(SqlManager sqlManager, Class<T> resultClazz) {
        Query init = this.init(sqlManager.getSql(), sqlManager.getParams(), resultClazz);
        return this.findList(init,resultClazz);
    }

    @Override
    public <T> T findOne(DtoQuery dtoQuery, Class<T> result) {
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams(), result);
        Map<String, Object> one = this.findOne(init);
        if (null == one) {
            return null;
        }
        return ClassUtils.mapToObj(one, result);
    }

    @Override
    public <T> T findOne(SqlManager sqlManager, Class<T> result) {
        Query init = this.init(sqlManager.getSql(), sqlManager.getParams(), result);
        Map<String, Object> one = this.findOne(init);
        if (null == one) {
            return null;
        }
        return ClassUtils.mapToObj(one, result);
    }

    private <T> EPage<T> findPage(Query query,Class<T> resultClazz,Integer pageNo,Integer pageSize) {
        EPage<T> ePage = new EPage<>();
        ePage.setCurrentPage(pageNo);
        ePage.setPageSize(pageSize);
        query.setMaxResults(pageSize);
        query.setFirstResult((pageNo-1) * pageSize );
        List<Map<String,Object>> resultList = query.getResultList();
        if(CollectionUtils.isEmpty(resultList)) {
            ePage.setList(null);
        }
        List<T> list = resultList.stream().map(it -> {
            return ClassUtils.mapToObj(it, resultClazz);
        }).collect(Collectors.toList());
        ePage.setList(list);
        return ePage;
    }

    private <T> List<T> findList(Query query,Class<T> resultClazz) {
        try{
            List<T> result = new ArrayList<>();
            List<Map<String, Object>> list = query.getResultList();
            if(CollectionUtils.isEmpty(list)) {
                return result;
            }
            result = list.stream().map(it -> {
                return ClassUtils.mapToObj(it, resultClazz);
            }).collect(Collectors.toList());
            return result;
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new DtoQueryException("查询发生了其他异常,e:[" + exception.getMessage() + "]");
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


    private <T> Query init(String sql, List<Object> params, Class<T> result) {
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
}
