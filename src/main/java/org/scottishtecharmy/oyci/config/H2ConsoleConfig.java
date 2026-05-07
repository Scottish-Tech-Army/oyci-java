package org.scottishtecharmy.oyci.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Manually registers the H2 console servlet so it is served directly by Tomcat,
 * bypassing Spring MVC's DispatcherServlet entirely.
 *
 * This is required in Spring Boot 4.x where the auto-registered H2 console
 * conflicts with the DispatcherServlet, causing requests like /h2-console/login.do
 * to be intercepted as static resources and returning 404.
 *
 * Only active when spring.h2.console.enabled=true.
 */
@Configuration
@ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
public class H2ConsoleConfig {

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        ServletRegistrationBean<JakartaWebServlet> registration =
                new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
        registration.setName("H2Console");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
