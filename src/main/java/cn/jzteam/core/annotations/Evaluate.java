package cn.jzteam.core.annotations;


import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Evaluate {
	String name();
	boolean required() default true;
}
