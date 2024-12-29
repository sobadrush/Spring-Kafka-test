package com.roger.springkafkatest.kafka;

import com.roger.springkafkatest.kafka.entities.UserVO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka 設定連線設定相關設定
 *
 * @author RogerLo
 * @date 2024/12/29
 */
@Configuration
public class KafkaProducerConfiguration {

    public static final String MY_TOPIC_1 = "testRoger";

    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_SERVER_URI;

    @Bean
    public ProducerFactory<String, UserVO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URI);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UserVO> kafkaTemplate(ProducerFactory<String, UserVO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
