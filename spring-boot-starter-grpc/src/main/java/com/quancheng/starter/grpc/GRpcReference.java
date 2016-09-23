package com.quancheng.starter.grpc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface GRpcReference {

    String group() default GrpcConstants.DEFAULT_GROUP;

    String version() default GrpcConstants.DEFAULT_VERSION;

    // blocking async future
    String callType() default "future";

}
