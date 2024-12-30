package com.roger.springkafkatest;

import com.roger.springkafkatest.kafka.entities.UserVO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;

import java.time.Duration;
import java.util.Collections;

import static com.roger.springkafkatest.kafka.KafkaProducerConfiguration.MY_TOPIC_1_ROGER;

@SpringBootTest
class KafkaConsumerFactoryTest {

    @Autowired
    @Qualifier("kafkaConsumer_Topic1_Factory")
    private ConsumerFactory<String, UserVO> kafkaConsumer_Topic1_Factory;

    @Test
    @DisplayName("[Test_001] 使用 ConsumerFactory 建立消費者後，取得「消費者」的紀錄")
    // @Disabled
    void test_002(TestInfo testInfo) {
        System.out.println(" === " + testInfo.getDisplayName() + " === " );

        try (Consumer<String, UserVO> consumer = kafkaConsumer_Topic1_Factory.createConsumer()) {
            consumer.subscribe(Collections.singleton(MY_TOPIC_1_ROGER));

            // 將消費者的偏移量設置到分配的分區的開始位置，這樣消費者可以從分區的最開始位置重新開始消費消息
            // consumer.seekToBeginning(consumer.assignment());

            while (true) {
                try {
                    consumer.poll(Duration.ofMillis(1000)).forEach(record -> {
                        System.out.println(">>> Partition: " + record.partition());
                        System.out.println(">>> Offset: " + record.offset());
                        System.out.println(">>> Key: " + record.key());
                        System.out.println(">>> Value: " + record.value());
                    });
                } catch (RecordDeserializationException e) {
                    // 消費者在遇到反序列化錯誤時，跳過有問題的訊息，並繼續處理其他訊息
                    System.err.println("Error deserializing record: " + e.getMessage());
                    consumer.seek(consumer.assignment().iterator().next(), consumer.position(consumer.assignment().iterator().next()) + 1);
                }
            }

        }
    }

}
