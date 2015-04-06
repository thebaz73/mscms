package ms.cms.registration.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Arrays;
import java.util.Collections;

/**
 * Application
 * Created by thebaz on 06/04/15.
 */
@Configuration
@ComponentScan({"ms.cms"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class Application extends WebMvcConfigurerAdapter implements CommandLineRunner {
    private static final String[] RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};
//    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/home").setViewName("home");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    RESOURCE_LOCATIONS);
        }
    }

    @Bean
    public SimpleUrlHandlerMapping myFaviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap("/favicon.ico", myFaviconRequestHandler()));
        return mapping;
    }

    @Bean
    protected ResourceHttpRequestHandler myFaviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        requestHandler.setLocations(Arrays.<Resource>asList(new ClassPathResource("/")));
        requestHandler.setCacheSeconds(0);
        return requestHandler;
    }

    @Bean
    public ApplicationSecurity applicationSecurity() {
        return new ApplicationSecurity();
    }

    @Override
    public void run(String... args) throws Exception {
    }

    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected class ApplicationSecurity extends WebSecurityConfigurerAdapter {

//        @Autowired
//        private SecurityProperties security;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated();
//            http
//                    .formLogin()
//                    .loginPage("/login")
//                    .failureUrl("/login?error")
//                    .permitAll()
//                    .and()
//                    .logout()
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                    .logoutSuccessUrl("/login")
//                    .and()
//                    .rememberMe();
        }
    }

//    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
//    protected class AuthenticationSecurity extends
//            GlobalAuthenticationConfigurerAdapter {
//
//        @Override
//        public void init(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userManager);//.passwordEncoder(userManager.getEncoder());
//        }
//    }
}
