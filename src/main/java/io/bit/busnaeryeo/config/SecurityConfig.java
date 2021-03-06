package io.bit.busnaeryeo.config;

import io.bit.busnaeryeo.jwt.JwtAccessDeniedHandler;
import io.bit.busnaeryeo.jwt.JwtAuthenticationEntryPoint;
import io.bit.busnaeryeo.jwt.JwtAuthenticationFilter;
import io.bit.busnaeryeo.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RedisTemplate redisTemplate;
//    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

    // AuthenticationManager를 Bean으로 등록합니다.

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public CorsConfigurationSource configurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
////        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedOriginPattern("/**");
//        corsConfiguration.setMaxAge(3600L); //preflight 결과를 1시간동안 캐시에 저장
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()

                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) ///엔트리포인트 공부
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 X

                .and()
                .authorizeRequests()
                .antMatchers("/","/join", "/login", "/join/admin").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/driver/**").hasRole("DRIVER")
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }

}