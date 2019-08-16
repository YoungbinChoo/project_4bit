//package com.example.spring.todo.config;
//
//import com.example.spring.todo.service.LocalUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    private static final String RESOURCE_ID = "todo-server-rest-api";
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
////                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(
//                        "/v2/api-docs",
//                        "/configuration/ui",
//                        "/swagger-resources",
//                        "/configuration/security",
//                        "/swagger-ui.html",
//                        "/webjars/**",
//                        "/swagger-resources/configuration/ui",
//                        "/swagger-ui.html",
//                        "/swagger-resources/configuration/security"
//                ).permitAll()
//                .antMatchers("/**").authenticated();
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId(RESOURCE_ID);
//    }
//}
package com.bitcamp.project.project_4bit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String CLIENT_ID = "762f6bbb-a257-11e9-9b39-0242ac120002";
    private static final String CLIENT_SECRET = "c16b2a8b36678a7440caeda356534ef2fa75699098bb7d58d499541024e53a51";
    private static final String ENDPOINT_URL = "http://localhost:8080/oauth/check_token";
    private static final String RESOURCE_ID = "project-4bit-server";
//    private static final String RESOURCE_ID = "todo-server-rest-api";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId(RESOURCE_ID);
    }
}
