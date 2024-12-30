package com.roger.springkafkatest.kafka;

import com.roger.springkafkatest.kafka.entities.UserVO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author RogerLo
 * @date 2024/12/29
 */
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_SERVER_URI;

    public static final String GROUP_A = "consumer_group_A"; // 讀取 Topic1, 接收 UserVO 型別資料
    public static final String GROUP_B = "consumer_group_B"; // 讀取 Topic2, 接收 String 型別資料

    @Bean
    public ConsumerFactory<String, UserVO> kafkaConsumer_Topic1_Factory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URI);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_A);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 因為資料是 JSON 格式，所以使用 JsonDeserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserVO> kafkaConsumer_Topic1_ContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserVO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.kafkaConsumer_Topic1_Factory());
        factory.setConcurrency(3); // 設定併發消費者數量
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> kafkaConsumer_Topic2_Factory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URI);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_B);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaConsumer_Topic2_ContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.kafkaConsumer_Topic2_Factory());
        return factory;
    }

    // public static final String MY_TOPIC_1 = "test-Roger";
    // public static final String MY_TOPIC_2 = "test-James";
    //
    // public static final String GROUP_1 = "consumer_group_1";
    // public static final String GROUP_2 = "consumer_group_2";
    //
    // @Bean
    // public ConsumerFactory<String, String> kafkaConsumer1() {
    //     Map<String, Object> configProps = new HashMap<>();
    //     configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URI);
    //     configProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_1);
    //     configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    //     configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    //     return new DefaultKafkaConsumerFactory<>(configProps);
    // }
    //
    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, String> kafkaConsumerContainerFactory() {
    //     ConcurrentKafkaListenerContainerFactory<String, String> factory
    //                             = new ConcurrentKafkaListenerContainerFactory<>();
    //     factory.setConsumerFactory(this.kafkaConsumer1());
    //     return factory;
    // }
    //
    // @Bean
    // public ConsumerFactory<String, UserVO> kafkaConsumer2() {
    //     Map<String, Object> configProps = new HashMap<>();
    //     configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URI);
    //     configProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_2);
    //     configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    //     configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    //     return new DefaultKafkaConsumerFactory<>(configProps);
    // }
    //
    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, UserVO> userKafkaListenerFactory() {
    //     ConcurrentKafkaListenerContainerFactory<String, UserVO> factory =
    //             new ConcurrentKafkaListenerContainerFactory<>();
    //     factory.setConsumerFactory(this.kafkaConsumer2());
    //     return factory;
    // }
}
