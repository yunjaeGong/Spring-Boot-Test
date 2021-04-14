package com.example.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("필터3");

        // 임의로 Authorization 토큰 생성
        if(req.getMethod().equals("POST")) {
            System.out.println("    filter3: " + req.getMethod());

            String headerAuth = req.getHeader("Authorization");

            System.out.println("    filter3 auth header: " + headerAuth);

            if (headerAuth.equals("cos")) { // 임의생성 token
                // TODO: id, password가 들어와 정상적으로 로그인이 되면 토큰을 생성하고 요청에 대한 응답을 한다.
                // 발급된 토큰은 매 요청마다 header의 Authorization value로 전달되고, 토큰 검증만 하면 된다.
                chain.doFilter(req, res); // authorization 일치할 때만 filter chain에 잔류
            } else {
                PrintWriter out = res.getWriter();
                out.println("   인증안됨");
            }
        }
        chain.doFilter(request, response);
    }
}
