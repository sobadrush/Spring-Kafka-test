package com.roger.springkafkatest;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.util.Random;

@Slf4j
@SpringBootApplication
public class SpringKafkaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaTestApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterSpringBootStarted(ApplicationReadyEvent event) throws InterruptedException {
        Random rand = new Random();
        while (true) {
            int nn = rand.nextInt(6) + 5;
            switch (nn % 2) {
                case 0 -> log.info(">>> 我是偶數 ... ");
                case 1 -> log.info(">>> 我是奇數 ... ");
            }
            Thread.sleep(100);
        }
    }

    @Bean
    public Gson gson() {

        return new Gson();
    }
}
