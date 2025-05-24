package ru.otus.hw.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@EnableCaching
@Configuration
public class AclConfiguration {

    @Bean
    public MutableAclService aclService(DataSource dataSource, CacheManager cacheManager) {
        var aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
        var permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());

        var aclCache = new SpringCacheBasedAclCache(cacheManager.getCache("acl_cache"),
                permissionGrantingStrategy,
                aclAuthorizationStrategy);

        return new JdbcMutableAclService(dataSource,
                new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, permissionGrantingStrategy),
                aclCache);
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler(MutableAclService aclService) {
        var expressionHandler = new AclMethodSecurityExpressionHandler();
        var permissionEvaluator = new AclPermissionEvaluator(aclService);
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService));

        return expressionHandler;
    }
}