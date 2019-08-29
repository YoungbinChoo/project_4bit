package com.bitcamp.project.project_4bit;

import com.bitcamp.project.project_4bit.util.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
        FileProperties.class
})
public class Project4bitApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Project4bitApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(Project4bitApplication.class, args);
    }

}
