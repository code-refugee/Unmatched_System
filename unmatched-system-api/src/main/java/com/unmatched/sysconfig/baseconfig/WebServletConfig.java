package com.unmatched.sysconfig.baseconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.unmatched.controller")
public class WebServletConfig extends WebMvcConfigurerAdapter {

//    //配置thymeleaf（改用html5展示页面不用jsp）
//    public ViewResolver viewResolver(){
//    }
//
//    //模板解析器
//    public ITemplateResolver iTemplateResolver(){
//        ITemplateResolver iTemplateResolver=new ServletContextTemplateResolver();
//    }

//    @Bean
//    public ViewResolver viewResolver(){
//        InternalResourceViewResolver viewResolver=new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/classes/views/");
//        viewResolver.setSuffix(".jsp");
//        //可以在jsp页面中通过${}访问beans
//        viewResolver.setExposeContextBeansAsAttributes(true);
//        return viewResolver;
//    }

    //配置multipart解析器，servlet只负责接收不会解析
    @Bean
    public MultipartResolver multipartResolver(){
        //不支持非servlet3.0的容器
        return new StandardServletMultipartResolver();
    }


    //自定义消息转换器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter converter=(StringHttpMessageConverter)converters.get(1);
        //用于解决页面中文显示乱码乱码的原因（http header中的Content-Type不是utf-8）
        //虽然我们返回的文字通过filter转码成了utf-8格式的但http header中的Content-Type为ISO-8859-1
        //所以如果没有这行代码，只设置filter前台页面还是显示乱码
        converter.setDefaultCharset(Charset.forName("utf-8"));
    }

    /** 解决跨域问题 **/
    public void addCorsMappings(CorsRegistry registry){}
    /** 添加拦截器 **/
    public void addInterceptors(InterceptorRegistry registry){}
    /** 这里配置视图解析器 **/
    public void configureViewResolvers(ViewResolverRegistry registry){
        registry.jsp("/WEB-INF/classes/views/",".jsp");
    }
    /** 配置内容裁决的一些选项 **/
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer){}
    /** 视图跳转控制器 **/
    public void addViewControllers(ViewControllerRegistry registry){}
    /** 静态资源处理 **/
    public void addResourceHandlers(ResourceHandlerRegistry registry){}
    /** 默认静态资源处理器 **/
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){}
}
