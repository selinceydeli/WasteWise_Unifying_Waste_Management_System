package wastewise.form.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wastewise.form.authentication.JwtAuthenticationEntryPoint;
import wastewise.form.authentication.JwtRequestFilter;

/**
 * The type Web security config.
 */
@Configuration
public class RequestAuthenticationConfig extends WebSecurityConfigurerAdapter {
    private final transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final transient JwtRequestFilter jwtRequestFilter;

    public RequestAuthenticationConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                       JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/form/hello", "/form/add").permitAll() //allow users without authentication
                .anyRequest().authenticated() // anyone else needs to have an account
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
