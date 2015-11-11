package com.wcrs.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
/**
 * 实体显示用名称(中文名)
 */
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityDisplayName {
     
    String value();
 
}