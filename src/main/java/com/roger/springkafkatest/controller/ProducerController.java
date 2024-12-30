package com.roger.springkafkatest.controller;

import com.roger.springkafkatest.kafka.KafkaProducerConfiguration;
import com.roger.springkafkatest.kafka.entities.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author RogerLo
 * @date 2024/12/29
 */
@RestController
@RequestMapping("/my-kafka")
@Slf4j
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, UserVO> kafkaTemplate;

    @GetMapping("/publish/{dept}/{userId}")
    public ResponseEntity<String> sendMessage(@PathVariable("dept") String dept, @PathVariable("userId") String userId) throws ExecutionException, InterruptedException {
        log.info("Publishing message to Kafka: dept={}, userId={}", dept, userId);

        CompletableFuture<SendResult<String, UserVO>> sentFuture =
                kafkaTemplate.send(KafkaProducerConfiguration.MY_TOPIC_1_ROGER, UserVO.builder().dept(dept).userId(userId).build());

        // 阻塞 - 直到 Kafka 發送完成
        SendResult<String, UserVO> rs = sentFuture.get();
        log.info("Message sent to Kafka successfully: topic={}, partition={}, offset={}",
                rs.getRecordMetadata().topic(),
                rs.getRecordMetadata().partition(),
                rs.getRecordMetadata().offset());

        // // 非阻塞 - 完成後 callback
        // sentFuture.whenComplete((result, ex) -> {
        //     if (ex != null) {
        //         log.error("Failed to send message to Kafka", ex);
        //     } else {
        //         log.info("Message sent to Kafka: {}", result);
        //     }
        // });

        return ResponseEntity.ok("生產者-發送成功");
    }

}
