package we.cod.bnz.account.config;

import org.springframework.security.config.http.SessionCreationPolicy;
import we.cod.bnz.account.auth.JwtExceptionFilter;
import we.cod.bnz.account.auth.JwtFilter;
import we.cod.bnz.account.oauth.OAuthService;
import we.cod.bnz.account.oauth.OAuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter filterJWT;
  private final JwtExceptionFilter filterException;
  private final OAuthHandler handler;
  private final OAuthService service;

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  CorsConfigurationSource cors() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000"));
      //== ㅅㄷㅎ oauth2 동작
      config.setExposedHeaders(Collections.singletonList("Set-Cookie"));
      config.setExposedHeaders(Collections.singletonList("Authorization"));
      config.setAllowCredentials(true);
      return config;
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .httpBasic(HttpBasicConfigurer::disable) //  로그인창
            .cors(cors -> cors.configurationSource(cors()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").authenticated()  // AuthTest 실행
                    .requestMatchers("/account/**").permitAll()   // AuthTest 실행X
                    .requestMatchers("/my/**").permitAll()        // AuthTest 실행X
                    .requestMatchers("/member/**").permitAll()    // AuthTest 실행X
                    .requestMatchers("/team/**").permitAll()      // AuthTest 실행X
                    .requestMatchers("/talk/**").permitAll()      // AuthTest 실행X
                    .requestMatchers("/mate/**").permitAll()      // AuthTest 실행X
                    .requestMatchers("/today/**").permitAll()      // AuthTest 실행X
                    .requestMatchers("/plan/**").permitAll())
            .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(config -> config.userService(service))
                    .successHandler(handler))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(filterJWT, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(filterException, JwtFilter.class);
    return http.build();
  }
}