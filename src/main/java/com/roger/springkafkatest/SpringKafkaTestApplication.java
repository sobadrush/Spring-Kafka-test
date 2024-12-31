package com.roger.springkafkatest;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class SpringKafkaTestApplication {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Autowired
    private Environment springEnv;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaTestApplication.class, args);
    }

    // 會比 KafkaProducerConfiguration 中的 @EventListener 早執行
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterSpringBootStarted(ApplicationReadyEvent event) {
        log.info(">>> activeProfile = {}", activeProfile);
        log.info(">>> emp.name = {}", springEnv.getProperty("emp.name"));
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
