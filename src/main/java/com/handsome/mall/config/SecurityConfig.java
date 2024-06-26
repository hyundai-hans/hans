package com.handsome.mall.config;

import com.handsome.mall.handler.SystemAuthenticationSuccessHandler;
import com.handsome.mall.handler.TokenHandler;
import com.handsome.mall.http.auth.UserAuthenticationManager;
import com.handsome.mall.http.filter.JwtAuthenticationFilter;
import com.handsome.mall.http.filter.UserAuthenticationFilter;
import com.handsome.mall.repository.primary.MemberRepository;
import com.handsome.mall.service.JwtAccessTokenProcessor;
import com.handsome.mall.service.RegisterTokenInvalidationAsBlackListAtSession;
import com.handsome.mall.service.TokenInvalidationStrategy;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {


  private final HttpSession httpSession;
  @Value("${application.domain}")
  public String domain;
  @Value("${encrypt.key.life-time}")
  public long accessKeyLifeTime;
  private final MemberRepository memberRepository;
  @Value("${encrypt.key.access}")
  public String accessKey;


  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public TokenInvalidationStrategy tokenInvalidationStrategy() {
    return new RegisterTokenInvalidationAsBlackListAtSession(httpSession);
  }

  @Bean
  public JwtAccessTokenProcessor jwtTokenProcessor() {
    return new JwtAccessTokenProcessor(accessKey, accessKeyLifeTime, tokenInvalidationStrategy());
  }

  @Bean
  public TokenHandler tokenHandler() {
    return new TokenHandler(jwtTokenProcessor());
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(Collections.singletonList(domain));
          config.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS", "DELETE", "PUT"));
          config.setAllowCredentials(false);
          config.setAllowedHeaders(List.of(
              "Host",
              "User-Agent",
              "Accept",
              "Accept-Language",
              "Accept-Encoding",
              "Connection",
              "Origin"
          ));
          config.addExposedHeader("Authorization");
          return config;
        }));

    http.csrf().disable().formLogin().disable().logout().disable();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests().
        antMatchers(HttpMethod.POST, "/**/login").permitAll().
        antMatchers(HttpMethod.POST, "/users").permitAll().
        antMatchers(HttpMethod.POST, "/users/email").permitAll().
        antMatchers(HttpMethod.POST, "/users/nickname").permitAll().
        antMatchers(HttpMethod.POST,"/users/products").permitAll().
        antMatchers(HttpMethod.GET, "/posts").permitAll().
        antMatchers(HttpMethod.GET, "/posts/*").permitAll().
        antMatchers(HttpMethod.GET, "/products/*").permitAll().
        antMatchers(HttpMethod.GET, "/tags/*").permitAll().


        anyRequest().authenticated();

    http
        .addFilterBefore(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenHandler());
  }

  @Bean
  public UsernamePasswordAuthenticationFilter userAuthenticationFilter() {
    UserAuthenticationFilter authenticationFilter = new UserAuthenticationFilter(
        successHandler(), userAuthenticationManager());
    authenticationFilter.setAuthenticationSuccessHandler(successHandler());
    authenticationFilter.setFilterProcessesUrl("/**/login");
    return authenticationFilter;
  }



  @Bean
  public AuthenticationManager userAuthenticationManager() {
    return new UserAuthenticationManager(memberRepository,bCryptPasswordEncoder());
  }

  @Bean
  public AuthenticationSuccessHandler successHandler() {
    return new SystemAuthenticationSuccessHandler(tokenHandler());
  }
}
