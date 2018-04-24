package com.config;

import com.service.IUserService;
import com.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private static final String KEY = "shujia";
    @Autowired
    private IUserService userDetailsService;
    @Autowired
    //private PasswordEncoder passwordEncoder;
    private Md5PasswordEncoder md5PasswordEncoder;

    @Bean
    public Md5PasswordEncoder md5PasswordEncoder(){
        return new Md5PasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(md5PasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    /**
     *
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
    /**
     *  对http请求进行过滤认证
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css*//**", "/js*//**", "/fonts*//**","/images*//**", "/index").permitAll() // 都可以访问
                .antMatchers("/admin*//**").hasRole("ADMIN") // 需要相应的角色才能访问
                .and()
                .formLogin()
                .loginPage("/api/login").failureUrl("/api/loginError")
                .successForwardUrl("/api/index")
                .and()
                .rememberMe().key(KEY)
                .and().exceptionHandling().accessDeniedPage("/403")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
                /*.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()*/;

        http.csrf().disable();//禁用H2控制台的防护

    }

    /*
    * @param auth the {@link AuthenticationManagerBuilder} to use
    * @thr ows Exception
    * 用户认证
    */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
}
