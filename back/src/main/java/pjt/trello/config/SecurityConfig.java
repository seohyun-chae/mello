package pjt.trello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.AuthProvider;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
////    @Override
////    public void configure (WebSecurity web) throws Exception {
////        web.ignoring().antMatchers("/css/**", "/js/**", "/static/**",
////                "/v2/api-docs", "/configuration/ui", "/swagger-resoureces",
////                "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger/**");
////    }
//
//    /**
//     * Spring Security Rules
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http.cors()
//            .and()
//                .csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
//            .and()
//                .authorizeRequests()
//                .antMatchers("/main/*").hasAuthority("ROLE_USER")
//                .antMatchers("/loginForm").permitAll()
//                .anyRequest().authenticated()
//            .and()
//                .formLogin()
//                .loginPage("/loginForm")
//                .defaultSuccessUrl("/main")
//                .usernameParameter("id")
//                .passwordParameter("password")
//            .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/loginForm")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//            .and()
//                .httpBasic();
//
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable()			//cors 방지
                .csrf().disable()			//csrf 방지
                .formLogin().disable()		//기본 로그인페이지 없애기
                .headers().frameOptions().disable();

        return http.build();
    }
}
