package com.unmatched.sysconfig.baseconfig;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/**
* Description: 这个类的作用相当于web.xml
* @author: yuhang tao
* @date: 2020/1/18
* @version: v1.0
*/
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{WebRootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebServletConfig.class};
    }

    /*定义了Spring-mvc的起作用的url模式*/
    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.do"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        //在system.properties中定义了spring.profiles.default=qa
//        registration.setInitParameter("spring.profiles.default","qa");
        registration.setLoadOnStartup(1);
        //启用multipart请求
        registration.setMultipartConfig(
                new MultipartConfigElement("G:/test/upload",2097152,
                        4194304,0));
    }

    //添加的每个filter都有一个默认的name -- 基于其类型，且会被自动映射到DispatcherServlet。
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter=new CharacterEncodingFilter("utf-8",true,true);
        return new Filter[]{encodingFilter};
    }
}
