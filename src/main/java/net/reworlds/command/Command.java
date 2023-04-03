package net.reworlds.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для сбора методов в <code>CommandDispatcher</code>.
 * Позволяет кроме команды собирать и псевдонимы (aliases) команд.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value() default "";

    String[] aliases() default {};
}
