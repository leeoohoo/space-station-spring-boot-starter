package com.oohoo.spacestationspringbootstarter.dto.query.jpa;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractSearch;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.spi.NativeQueryImplementor;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.transform.Transformer;
import java.util.List;
import java.util.Map;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 01 December 2022
 */
@Component
public class Butler extends AbstractSearch {

    private final EntityManager em;

    private Butler(EntityManager em) {
        this.em = em;
    }

    public static Butler create(EntityManager em) {
        return new Butler(em);
    }


    @Override
    public <T> T findOne(DtoQuery dtoQuery, Class<T> result) {
        String sql = dtoQuery.getSql();
        List<Object> params = dtoQuery.getParams();
        Query nativeQuery = em.createNativeQuery(sql);
        for (int i = 0; i < params.size(); i++) {
            nativeQuery = nativeQuery.setParameter(i + 1, params.get(i));
        }
        ;
        nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object singleResult = nativeQuery.getSingleResult();
        return ClassUtils.mapToObj((Map) singleResult, result);
    }

    @Override
    public <T> T findOne(SqlManager sqlManager, Class<T> result) {
        String sql = sqlManager.getSql();
        List<Object> params = sqlManager.getParams();
        Query nativeQuery = em.createNativeQuery(sql);
        for (int i = 0; i < params.size(); i++) {
            nativeQuery = nativeQuery.setParameter(i + 1, params.get(i));
        }
        ;
        nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object singleResult = nativeQuery.getSingleResult();
        return ClassUtils.mapToObj((Map) singleResult, result);
    }
}
