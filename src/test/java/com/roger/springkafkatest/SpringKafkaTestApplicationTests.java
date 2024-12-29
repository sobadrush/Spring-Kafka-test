package com.roger.springkafkatest;

import com.roger.springkafkatest.kafka.service.MessageConsumerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringKafkaTestApplicationTests {

    @Autowired
    private MessageConsumerService messageConsumerService;

    @Test
    @DisplayName("[Test_001] 取得消費 Kafka 的紀錄")
    // @Disabled
    void test_001(TestInfo testInfo) {
        System.out.println(" === " + testInfo.getDisplayName() + " === " );
        System.out.println(messageConsumerService.getConsumedPartitions());
    }

}
