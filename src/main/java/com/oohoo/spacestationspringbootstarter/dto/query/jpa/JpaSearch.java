package com.oohoo.spacestationspringbootstarter.dto.query.jpa;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractSearch;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

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
    public <T> T findOne(DtoQuery dtoQuery, Class<T> result) {
        Query init = this.init(dtoQuery.getSql(), dtoQuery.getParams(), result);
        Object singleResult = init.getSingleResult();
        this.em.close();
        return ClassUtils.mapToObj((Map) singleResult, result);
    }

    @Override
    public <T> T findOne(SqlManager sqlManager, Class<T> result) {
        Query init = this.init(sqlManager.getSql(), sqlManager.getParams(), result);
        Object singleResult = init.getSingleResult();
        this.em.close();
        return ClassUtils.mapToObj((Map) singleResult, result);
    }

    private <T> Query init(String sql, List<Object> params, Class<T> result) {
        Query nativeQuery = em.createNativeQuery(sql);
        if (null != params) {
            for (int i = 0; i < params.size(); i++) {
                nativeQuery.setParameter(i + 1, params.get(i));
            }
        }
        nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return nativeQuery;
    }
}
