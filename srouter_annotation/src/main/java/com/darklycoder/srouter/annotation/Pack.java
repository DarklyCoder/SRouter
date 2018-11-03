package com.darklycoder.srouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 包名注解
 *
 * @author DarklyCoder 2018/10/26
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Pack {

    /**
     * 包名
     */
    String value();

    /**
     * 路由表名
     */
    String table();

    /**
     * 主assets路径
     * <p>默认为"/app/src/main/assets/srouter/"</p>
     */
    String assetsPath() default "/app/src/main/assets/srouter/";
}
