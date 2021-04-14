package com.example.security.config;

import com.example.security.filter.MyFilter3;
import com.example.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secure 어노테이션 활성화
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final CorsFilter corsFilter;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public class ApiTokenSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
            // BasicAuthenticationFilter(security filter) 적용 이전에 MyFilter1 적용
            // 일반적으로 Spring Security Filter Chain이 우선적으로 실행
            http.csrf().disable();
            // http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
            http
                    .antMatcher("/api/**")
                    .formLogin().disable()
                    .httpBasic().disable()
                    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                    .addFilter(corsFilter) // @CrossOrigin(인증 필요x 경우), 시큐리티 필터에 등록(인증O)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                    .antMatchers("/api/v1/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                    .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll();
        }
    }

    @Configuration
    @Order(2)
    public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/user/**").authenticated() // 인증만 되면 누구나
                    .antMatchers("/manager/**", "/data/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                    .antMatchers("/admin/**", "/info/**").access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll() // 위 세 주소가 아닌 주소는 모두에게 혀용
                    .and()
                    .formLogin()
                    .loginPage("/loginForm")
                    .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인
                    .defaultSuccessUrl("/"); // loginForm 요청해서 로그인 하면 / 으로
        }
    }


}
