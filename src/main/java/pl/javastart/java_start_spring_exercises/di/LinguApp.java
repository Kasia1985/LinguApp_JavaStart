package pl.javastart.java_start_spring_exercises.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class LinguApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext lingu = SpringApplication.run(LinguApp.class);
        LinguController linguController = lingu.getBean(LinguController.class);
        linguController.mainLoop();
}

@Bean
Scanner scanner() {
    return new Scanner(System.in);
}
}
