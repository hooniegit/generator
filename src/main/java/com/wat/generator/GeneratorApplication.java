package com.wat.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wat.generator.Config.IniConfig;

@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GeneratorApplication.class);
        application.addInitializers(new IniConfig());
        application.run(args);
    }
}
