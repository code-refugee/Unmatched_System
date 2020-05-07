package com.unmatched.function;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(dispatcherTypes = {DispatcherType.REQUEST,DispatcherType.FORWARD},urlPatterns = {"*.do"})
public class CharSetFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("--我是一个Filter!我被创建了");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        request.setCharacterEncoding("GBK");
        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
