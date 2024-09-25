package io.github.xisabla.tavern.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class BackApplication {

    /**
     * Main method to run the application.
     *
     * @param args Spring Boot arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }

    /**
     * Add various properties files to the application context.
     *
     * @return The configurer created for the properties files.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();

        configurer.setLocations(new ClassPathResource("git.properties"));

        configurer.setIgnoreResourceNotFound(true);
        configurer.setIgnoreUnresolvablePlaceholders(true);

        return configurer;
    }

}
