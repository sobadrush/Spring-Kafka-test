package com.roger.springkafkatest;

import com.roger.springkafkatest.kafka.entities.UserVO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import static com.roger.springkafkatest.kafka.KafkaProducerConfiguration.MY_TOPIC_1_ROGER;
import static com.roger.springkafkatest.kafka.KafkaProducerConfiguration.MY_TOPIC_2_JUMI;

/**
 * @author RogerLo
 * @date 2024/12/30
 */
@SpringBootTest
public class KafkaTest {

    @Autowired
    @Qualifier("kafkaConsumer_Topic1_ContainerFactory")
    private ConcurrentKafkaListenerContainerFactory<String, UserVO> kafkaListener_Topic1_ContainerFactory;

    @Autowired
    @Qualifier("kafkaConsumer_Topic2_ContainerFactory")
    private ConcurrentKafkaListenerContainerFactory<String, String> kafkaListener_Topic2_ContainerFactory;

    @Test
    @DisplayName("[Test_001] 消費 〔 MY_TOPIC_1_ROGER 〕")
    void test_001() {
        ContainerProperties containerProps = new ContainerProperties(MY_TOPIC_1_ROGER);
        containerProps.setMessageListener(new MessageListener<String, UserVO>() {
            @Override
            public void onMessage(ConsumerRecord<String, UserVO> record) {
                System.out.println(">>> Received: " + record.value());
            }
        });

        // 使用 ConcurrentMessageListenerContainer
        ConcurrentMessageListenerContainer<String, UserVO> container
                = new ConcurrentMessageListenerContainer<>(kafkaListener_Topic1_ContainerFactory.getConsumerFactory(), containerProps);
        container.start();

        // 保持程式運行，持續接收訊息
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        container.stop();
    }

    @Test
    @DisplayName("[Test_002] 消費 〔 MY_TOPIC_2_JUMI 〕")
    void test_002() {
        ContainerProperties containerProps = new ContainerProperties(MY_TOPIC_2_JUMI);
        containerProps.setMessageListener(new MessageListener<String, String>() {
            @Override
            public void onMessage(ConsumerRecord<String, String> record) {
                System.out.println(">>> Received: " + record.value());
            }
        });

        // 使用 ConcurrentMessageListenerContainer
        ConcurrentMessageListenerContainer<String, String> container
                = new ConcurrentMessageListenerContainer<>(kafkaListener_Topic2_ContainerFactory.getConsumerFactory(), containerProps);
        container.start();

        // 保持程式運行，持續接收訊息
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        container.stop();
    }
}
