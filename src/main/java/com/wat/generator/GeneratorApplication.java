package com.wat.generator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.wat.generator.Service.GeneratorService;
import com.wat.generator.Config.IniConfig;

@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GeneratorApplication.class);
        application.addInitializers(new IniConfig());
        application.run(args);
    }

    @Bean
    CommandLineRunner run(GeneratorService generatorService) {
        return args -> {
            try {
                generatorService.startDataGeneration();
            } catch (Exception e) {
                // 예외 로깅
                System.err.println("Error starting data generation: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
