package FindAJob;

import FindAJob.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // INSTANCIAS
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio).
                passwordEncoder(new BCryptPasswordEncoder());
    }

    // METODOS
    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {

        //httpSec.httpBasic().disable();
        //COMANDO CREADO PARA DESHABILITAR MENSAJE DE LOGIN AUTOMATICO DE SPRING AL TIPEAR LA URL EN EL EXPLORADOR
        //mas info >>> https://stackoverflow.com/questions/23636368/how-to-disable-spring-security-login-screen        
        
        httpSec.headers().frameOptions().sameOrigin()
                .and().authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**")
                .permitAll()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProcessing")
                .usernameParameter("mail")
                .passwordParameter("clave")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=error")
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and().csrf().disable();
    }
}
