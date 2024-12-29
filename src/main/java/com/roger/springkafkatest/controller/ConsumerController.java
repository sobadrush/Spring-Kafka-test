package com.roger.springkafkatest.controller;

import com.roger.springkafkatest.kafka.service.MessageConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RogerLo
 * @date 2024/12/29
 */
@RestController
@RequestMapping("/my-kafka")
@Slf4j
public class ConsumerController {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @GetMapping("/getConsumerRecords")
    public ResponseEntity<String> get() {
        log.info(">>> Get consumer records from Kafka");
        System.out.println(">>> Partition: " + messageConsumerService.getConsumedPartitions());
        System.out.println(">>> 消費的資料: " + messageConsumerService.getConsumedRecords());
        return ResponseEntity.ok().build();
    }

}
