package com.unmatched.function;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
* Description: 使用了该注解会在容器启动时调用contextInitialized方法
 * 在容器销毁时调用contextDestroyed方法
* @author: yuhang tao
* @date: 2019/10/6
* @version: v1.0
*/
@WebListener
public class CommunicationFunction implements ServletContextListener {
    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("注意！注意！");
        System.out.println("---这是一个系统通信的类，现在已经初始化了!---");
    }

    /**
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("---我随着容器去了！---");
    }
}
