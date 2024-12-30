package com.roger.springkafkatest.kafka;

import com.roger.springkafkatest.kafka.entities.UserVO;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Kafka 設定連線設定相關設定
 *
 * @author RogerLo
 * @date 2024/12/29
 */
@Configuration
public class KafkaProducerConfiguration {

    public static final String MY_TOPIC_1_ROGER = "testRoger"; // UserVO
    public static final String MY_TOPIC_2_JUMI = "testJuMi"; // String

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
    public KafkaTemplate<String, UserVO> kafkaTemplate() {
        return new KafkaTemplate<>(this.producerFactory());
    }

    @Bean
    public AdminClient kafkaAdminClient() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URI);
        return AdminClient.create(config);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterSpringBootStarted(ApplicationReadyEvent event) throws ExecutionException, InterruptedException {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        AdminClient adminClient = applicationContext.getBean("kafkaAdminClient", AdminClient.class);
        NewTopic topic1 = applicationContext.getBean("topic1", NewTopic.class);
        NewTopic topic2 = applicationContext.getBean("topic2", NewTopic.class);
        this.createKafkaTopic(adminClient, topic1, topic2);
    }

    /**
     * 建立 Topic1, 3 個 Partition, 1 個 Replication
     * 因程式中有 @KafkaListener，故在程式啟動時將 NewTopic 建立為 Bean，
     * 搭配 @DependsOn(...) 來確保 Topic 是根據此處的 partition 與 replication 初始化的
     */
    @Bean
    public NewTopic topic1() {
        return new NewTopic(MY_TOPIC_1_ROGER, 3, (short) 1);
    }

    /**
     * 建立 Topic2, 5 個 Partition, 1 個 Replication
     * 因程式中有 @KafkaListener，故在程式啟動時將 NewTopic 建立為 Bean，
     * 搭配 @DependsOn(...) 來確保 Topic 是根據此處的 partition 與 replication 初始化的
     */
    @Bean
    public NewTopic topic2() {
        return new NewTopic(MY_TOPIC_2_JUMI, 5, (short) 1);
    }

    /**
     * 建立 Kafka Topic
     * @param adminClient
     * @param topics
     */
    private void createKafkaTopic(AdminClient adminClient, NewTopic... topics) throws ExecutionException, InterruptedException {
        for (NewTopic topic : topics) {
            if (!this.topicExists(topic.name(), adminClient)) {
                adminClient.createTopics(Collections.singleton(topic));
            }
        }
    }

    /**
     * 檢查 Topic 是否存在
     *
     * @param topicName Topic 名稱
     * @param client    AdminClient
     * @return 是否存在
     */
    private boolean topicExists(String topicName, AdminClient client) throws ExecutionException, InterruptedException {
        return client.listTopics().names().get().contains(topicName);
    }
}
