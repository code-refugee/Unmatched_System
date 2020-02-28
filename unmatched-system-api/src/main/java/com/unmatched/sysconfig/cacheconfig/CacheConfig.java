package com.unmatched.sysconfig.cacheconfig;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
* Description: 缓存配置类
* @author: yuhang tao
* @date: 2019/12/26
* @version: v1.0
*/
@Configuration
//开启基于注解的缓存
@EnableCaching
public class CacheConfig {

    /**
    * Description: 用于组装多个CacheManager
     * 这里我们只使用了EhCache，在实际项目中
     * 我们还可以配置redisCache
    * @date: 2019/12/26
    */
    @Bean
    public CompositeCacheManager compositeCacheManager(CacheManager ehCacheManager){
        //CompositeCacheManager会迭代这些缓存管理器，以查找之前所缓存的值
        CompositeCacheManager compositeCacheManager=new CompositeCacheManager();
        HashSet<org.springframework.cache.CacheManager> cacheManagers=new HashSet<>(2);
        //我们还可以add RedisCacheManager
        cacheManagers.add(new EhCacheCacheManager(ehCacheManager));
        compositeCacheManager.setCacheManagers(cacheManagers);
        return compositeCacheManager;
    }

    /**
    * Description: 用于创建EhCacheManager
    * @date: 2019/12/26
    */
    @Bean
    public EhCacheManagerFactoryBean ehcache(){
        EhCacheManagerFactoryBean factoryBean=new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("/xmls/ehcache.xml"));
        return factoryBean;
    }

    /**
    * Description: 自定义缓存key生成器
     * 如何使用：在缓存注解里指定属性 keyGenerator=“KeyGenerator”即可
     * keyGenerator属性与key是互斥的
    * @date: 2019/12/27
    */
    @Bean
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return null;
            }
        };
    }
}
