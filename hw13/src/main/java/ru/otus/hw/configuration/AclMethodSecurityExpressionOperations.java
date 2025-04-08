package ru.otus.hw.configuration;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

public interface AclMethodSecurityExpressionOperations extends MethodSecurityExpressionOperations {
    boolean isAdministrator(Object targetId, Class<?> targetClass);

    boolean canRead(Object targetId, Class<?> targetClass);

    boolean canWrite(Object target, Class<?> targetClass);

    boolean canDelete(Object targetId, Class<?> targetClass);
}