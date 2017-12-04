package cn.expopay.messageServer.util.filter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class NettyFilerSecond implements Filter {

    private Logger logger = LoggerFactory.getLogger(NettyFilerSecond.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("NettyFilerSecond doFilter Running");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
