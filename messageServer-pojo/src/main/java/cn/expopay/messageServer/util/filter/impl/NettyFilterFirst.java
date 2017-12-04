package cn.expopay.messageServer.util.filter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NettyFilterFirst implements Filter {

    private Logger logger = LoggerFactory.getLogger(NettyFilterFirst.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("NettyFilterFirst doFilter Running");

        //使用流
//        InputStream reader = request.getInputStream();
//        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(100);
//        int i =0;
//        byte [] b = new byte[100];
//        while((i = reader.read(b))!= -1){
//            byteOutput.write(b, 0, i);
//        }
////        request.setAttribute("post", new String(byteOutput.toByteArray()));
//        String temp = new String(byteOutput.toByteArray());
//        logger.info("NettyFilterFirst Read InputStream is " + temp);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
