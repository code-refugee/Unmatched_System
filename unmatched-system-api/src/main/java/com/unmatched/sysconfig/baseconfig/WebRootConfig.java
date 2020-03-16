package com.unmatched.sysconfig.baseconfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.io.Resources;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.dao.annotation.PersistenceExceptionTranslationAdvisor;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

/**
* Description:这里不使用@PropertySource注解也行，因为在applicationContext.xml中已经指定了
 * 但不使用@PropertySource会导致@Profile的设置无效
* @author: yuhang tao
* @date: 2019/12/20
* @version: v1.0
*/
@Configuration
@ComponentScan(basePackages = {"com.unmatched"},
               excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
               classes = {EnableWebMvc.class, Controller.class})} )
@MapperScan(basePackages = {"com.unmatched.mapper","com.unmatched.common.messageTransform.mapper"})
@PropertySource("classpath:/properties/system.properties")
@ImportResource("classpath:/xmls/applicationContext.xml")
@EnableJms
@EnableAspectJAutoProxy
//@Import(UserConfig.class)
public class WebRootConfig {

    /**
     * 看源码可知@Profile会调用PropertySourcesPropertyResolver去配置文件里读取
     * 哪个Profile被启用，这里我们指定了PropertySource为自己的properties，所以
     * 我们在WebAppInitializer中设置的registration.setInitParameter("spring.profiles.default","qa");
     * 无效了，我们需要在properties定义我们需要启用的配置
     * */
//    @Bean
//    @Profile("qa")
//    public DataSource getQaDataSource(@Value("${qa_Mysql_DriverClassName}") String driverClassName,
//                                 @Value("${qa_Mysql_Url}") String url,
//                                 @Value("${qa_Mysql_UserName}")String username,
//                                 @Value("${qa_Mysql_PassWord}")String password){
//        DruidDataSource dataSource=new DruidDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        //其他属性
//        //初始大小
//        dataSource.setInitialSize(10);
//        //最大大小
//        dataSource.setMaxActive(50);
//        //最小大小
//        dataSource.setMinIdle(10);
//        //检查时间
//        dataSource.setMaxWait(5000);
//        return dataSource;
//    }

//    @Bean
//    @Profile("production")
//    public DataSource getProDataSource(){
//        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
//        factoryBean.setJndiName("jdbc/oracle");
//        factoryBean.setResourceRef(true);
//        factoryBean.setProxyInterface(javax.sql.DataSource.class);
//        return (DataSource) factoryBean.getObject();
//    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
    * Description:是一个后置处理器，会在所有拥有@Repository注解的
     * 类上添加一个通知器(advisor)，这样就会捕获任何平台相关的异常
     * 并以Spring非检查型数据访问异常的形式重新抛出
    * @date: 2019/12/22
    */
    @Bean
    public BeanPostProcessor translationAdvisor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }



//    //要配值一个SqlSessionFactoryBean来充当SqlSessionFactory
//    //注意SqlSessionFactoryBean实现了Spring的FactoryBean接口
//    //这意味着Spring最终创建的bean不是SqlSessionFactoryBean自身,而是SqlSessionFactory
//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,
//                                                   @Value("${mapperLocation}") String mapperLocation){
//        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
//        String[] mapperLocations=mapperLocation.split("\\|");
//        ClassPathResource[] resources=new ClassPathResource[mapperLocations.length];
//        for (int i = 0; i < mapperLocations.length; i++) {
//            ClassPathResource resource=new ClassPathResource(mapperLocations[i]);
//            resources[i]=resource;
//        }
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(resources);
//        return bean;
//    }

}
