package com.roger.springkafkatest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author RogerLo
 * @date 2024/12/29
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping(path = "/sayHello", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> sayHello() {
        return Map.of("message", "Hello, World!");
    }

}
