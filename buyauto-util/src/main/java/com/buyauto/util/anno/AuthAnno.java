package com.buyauto.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buyauto.util.method.AuthBackend;


@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAnno {
	AuthBackend[] rules() default {};
}
