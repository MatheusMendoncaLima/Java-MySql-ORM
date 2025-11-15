package me.texels.mySqlOrm.annotations;

import me.texels.mySqlOrm.column.ColumnTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String columnName() default "";
    boolean unsigned() default  false;
    boolean primaryKey() default false;
    boolean allowNull() default true;
    String defaultValue() default "";
    String onUpdate() default "";
    ColumnTypes type();
    int length() default -1;
    String values() default "";
    int precision() default -1;
    int scale() default -1;
    boolean autoIncrement() default false;

}

