package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 20 October 2022
 */
public class ExceptionUtils {
    private ExceptionUtils() {
    }

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static DtoQueryException mpe(String msg, Throwable t, Object... params) {
        return new DtoQueryException(String.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static DtoQueryException mpe(String msg, Object... params) {
        return new DtoQueryException(String.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t 异常
     * @return 返回异常
     */
    public static DtoQueryException mpe(Throwable t) {
        return new DtoQueryException(t);
    }
}
