package com.connotationjoke.qingguoguo.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:qingguoguo
 * 创建日期：2018/3/18 on 14:15
 * 描述:View注解的Annotation
 *
 * Target:表示View注解的Annotation位置
 * Retention:表示什么时候生效
 * SOURCE  源码
 * CLASS   编译
 * RUNTIME 运行时
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    int value();
}
