package com.roger.springkafkatest;

import com.roger.springkafkatest.kafka.entities.UserVO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.roger.springkafkatest.kafka.KafkaConsumerConfiguration.GROUP_A;
import static com.roger.springkafkatest.kafka.KafkaProducerConfiguration.MY_TOPIC_1_ROGER;

/**
 * @author RogerLo
 * @date 2024/12/30
 */
@SpringBootTest
public class KafkaConsumerTest {

    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_SERVER_URI;

    @Test
    @DisplayName("[Test_001] 消費 〔 MY_TOPIC_1_ROGER 〕")
    public void test_001(TestInfo testInfo) {
        System.out.println(" === " + testInfo.getDisplayName() + " === ");

        String bootstrapServers = KAFKA_SERVER_URI;
        String groupId = GROUP_A;
        String topic1 = MY_TOPIC_1_ROGER;

        // 配置消費者屬性
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserVO.class.getName());

        // - latest：將消費者的偏移量設置為最新的記錄，從新記錄開始消費。
        // - earliest：將消費者的偏移量設置為最早的記錄，從主題的開頭開始消費。
        // - none：如果找不到初始偏移量，則會引發錯誤並停止消費。
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 決定了當消費者組在 Kafka 主題中，沒有初始偏移量或當當前偏移量不存在時（例如，因為數據已經被刪除），該如何處理

        // 創建消費者
        try (KafkaConsumer<String, UserVO> consumer = new KafkaConsumer<>(properties)) {

            // 訂閱主題
            consumer.subscribe(Collections.singletonList(topic1));

            // 持續輪詢消息
            while (true) {
                ConsumerRecords<String, UserVO> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, UserVO> record : records) {
                    System.out.printf(">>> Received message: key = %s, value = %s, offset = %d%n",
                            record.key(), record.value(), record.offset());
                }
            }
        }

    }

}
