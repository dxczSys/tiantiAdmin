package com.jeff.tianti.shiro;

/**
 * 跨域过滤与header设置
 *
 * @author NUGGET
 * @email service@nuggetdata.com
 * @date 2016年11月15日 下午1:23:52
 */

import com.jeff.tianti.util.Constants;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决Rest接口调用过程中，跨域访问问题
 */
public class CorsFilter implements Filter {
    public CorsFilter() {
    }


    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "accept,content-type");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        chain.doFilter(request, response);

    }


    public void init(FilterConfig fConfig) throws ServletException {


    }


    public void destroy() {
    }


}
