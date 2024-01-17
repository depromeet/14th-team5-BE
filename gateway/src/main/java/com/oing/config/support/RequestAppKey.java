package com.oing.config.support;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_PARAMETER, ElementType.PARAMETER})
public @interface RequestAppKey {
}
