package fpt.aptech.day5loginform.security;

import fpt.aptech.day5loginform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class LoginConfig extends WebSecurityConfigurerAdapter{
    //NOTE: IMPORTANT
//    Here in the configure() method,
//    a user must login to see the list users page (URL /users) and other pages do not require authentication.
//    We also configure the default login page (generated by Spring Security) with the parameter name of the username field is email and the default success URL is /users – that means after successful login, the user will be redirected to the list users page.
    //MUST CODING EXACTLY @override of these methods below

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        CustomUserDetailService userDetailService = new CustomUserDetailService();
        return userDetailService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/showAll", "/register").authenticated()// require login when access this page
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login") //use a page login dc chi dinh hơn dùng page mặc định
                .usernameParameter("username") //follow name of column username in model User
                .defaultSuccessUrl("/showAll") //redirect to showAll page after login success
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")//neu them .logoutSuccessUrl("/") thi logout thanh cong tra ve trang co route "/" (vd: index.html)
                .permitAll();
    }
}
