package thisisexercise.exercise.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import thisisexercise.exercise.security.exception.CustomAuthenticationEntryPoint;
import thisisexercise.exercise.security.filter.JwtAuthenticationFilter;
import thisisexercise.exercise.security.provider.JwtAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/join", "/join/idCheck", "/login", "/auth/sms", "/auth/smsCheck", "/refresh").permitAll()
                        .requestMatchers("/hi").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated());

        http
                .logout((auth) -> auth
                        .disable());                //로그아웃은 disable로 해줘야 한다. 안그러면 member controller에 있는 /logout url이 통하지 않는다.


        http
                .exceptionHandling((auth) -> auth
                        .authenticationEntryPoint(customAuthenticationEntryPoint));

        http
                .authenticationProvider(jwtAuthenticationProvider);

        http
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

        http
                .csrf((auth) -> auth
                        .disable());

        http
                .httpBasic((auth) -> auth
                        .disable());

        http
                .formLogin((auth) -> auth
                        .disable());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();

    }
}
