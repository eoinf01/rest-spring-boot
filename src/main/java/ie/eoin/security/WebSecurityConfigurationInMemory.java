package ie.eoin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfigurationInMemory {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests().
                antMatchers(HttpMethod.POST,"/departments/","/departments").hasRole("HOS")
                .antMatchers(HttpMethod.DELETE,"/departments/{id}","/departments/{id}/").hasRole("HOS")
                .antMatchers(HttpMethod.PATCH,"offices/{id}/move","offices/{id}/move/").hasRole("HOS")
                .antMatchers(HttpMethod.POST,"/offices/","/offices").hasAnyRole("HOS","HOD")
                .antMatchers(HttpMethod.DELETE,"/offices/{id}","/offices/{id}").hasAnyRole("HOS","HOD")
                .antMatchers(HttpMethod.PATCH,"/offices/{id}/occupancy/{newSpace}","/offices/{id}/occupanacy/{newSpace}/").hasAnyRole("HOS","HOD")
                .anyRequest().permitAll()
                .and()
                .httpBasic().and().formLogin().and().csrf().disable();
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users(){
        String encoded = passwordEncoder().encode("secret");
        UserDetails hos = User.builder()
                .username("HOS")
                .password(
                        encoded
                ).roles("HOS").build();
        UserDetails hod = User.builder()
                .username("HOD")
                .password(encoded)
                .roles("HOD")
                .build();
        return new InMemoryUserDetailsManager(hos,hod);
    }


}
