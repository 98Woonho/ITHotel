package com.whl.hotelService.config;

import com.whl.hotelService.config.auth.exceptionHandler.CustomAccessDeniedHandler;
import com.whl.hotelService.config.auth.exceptionHandler.CustomAuthenticationEntryPoint;
import com.whl.hotelService.config.auth.jwt.JwtAuthorizationFilter;
import com.whl.hotelService.config.auth.jwt.JwtProperties;
import com.whl.hotelService.config.auth.jwt.JwtTokenProvider;
import com.whl.hotelService.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import com.whl.hotelService.config.auth.loginHandler.CustomLoginSuccessHandler;
import com.whl.hotelService.config.auth.logoutHandler.CustomLogoutHandler;
import com.whl.hotelService.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import com.whl.hotelService.domain.user.repository.UserRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private HikariDataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        //csrf비활성화
        http.csrf(
                (config)->{config.disable();}
        );

        //요청 URL별 접근 제한
        http.authorizeHttpRequests(
                authorize->{
                    authorize.requestMatchers("/js/**","/css/**","/images/**","/templates").permitAll();
                    authorize.requestMatchers("/**").permitAll();
                    authorize.anyRequest().authenticated();
                }
        );

        //로그인
        http.formLogin(login->{
            login.permitAll();
            login.loginPage("/user/login");
            login.successHandler(customLoginSuccessHandler());
            login.failureHandler(new CustomAuthenticationFailureHandler());
        });

        //로그아웃
        http.logout(
                (logout)->{
                    logout.permitAll();
                    logout.logoutUrl("/logout");
                    logout.addLogoutHandler(customLogoutHandler());
                    logout.logoutSuccessHandler(customLogoutSuccessHandler());
                    // 로그아웃(JWT토큰, 세션 제거)
                    logout.deleteCookies("JSESSIONID", JwtProperties.COOKIE_NAME);
                    // 테스트 코드(2024-01-18)
                    logout.deleteCookies("JSESSIONID", JwtProperties.ADMIN_COOKIE_NAME);
                    logout.invalidateHttpSession(true);
                }
        );

        //예외처리
        http.exceptionHandling(
                ex->{
                    ex.accessDeniedHandler(new CustomAccessDeniedHandler());
                    ex.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                }
        );

        //RememberMe
        http.rememberMe(
                rm->{
                    rm.key("rememberMeKey");
                    rm.rememberMeParameter("remember-me");
                    rm.alwaysRemember(false);
                    rm.tokenValiditySeconds(3600);  //60*60
                    rm.tokenRepository(tokenRepository());
                }
        );

        //Oauth2
        http.oauth2Login(
                oauth2 ->{
                    oauth2.loginPage("/user/login");
                    oauth2.successHandler(new CustomLoginSuccessHandler());
                }
        );

        //SESSION INVALIDATE..
        http.sessionManagement(
                httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
        );

        //JWT ADDED
        http.addFilterBefore(
                new JwtAuthorizationFilter(userRepository,jwtTokenProvider),
                BasicAuthenticationFilter.class

        );

        return http.build();
    }

    //Remember me 처리
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    //CUSTOMLOGOUTSUCCESS BEAN
    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    //CUSTOMLOGOUTHANDLER BEAN
    @Bean
    public CustomLogoutHandler customLogoutHandler(){
        return new CustomLogoutHandler();
    }

    //CUSTOMLOGINSUCCESSHANDLER BEAN
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
