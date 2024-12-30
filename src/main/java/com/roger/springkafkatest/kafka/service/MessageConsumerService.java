package com.roger.springkafkatest.kafka.service;

import com.roger.springkafkatest.kafka.entities.UserVO;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.roger.springkafkatest.kafka.KafkaConsumerConfiguration.GROUP_A;
import static com.roger.springkafkatest.kafka.KafkaConsumerConfiguration.GROUP_B;
import static com.roger.springkafkatest.kafka.KafkaProducerConfiguration.MY_TOPIC_1_ROGER;
import static com.roger.springkafkatest.kafka.KafkaProducerConfiguration.MY_TOPIC_2_JUMI;

/**
 * ‼️注意：因 @KafkaListener 在設定：missing-topics-fatal: false 的前提下
 * 當 Topic 不存在時，會自動建立 Topic，故此處設定 @DependsOn(value = { "topic1", "topic2" })
 * 由 doSomethingAfterSpringBootStarted() 方法建立 Topic
 *
 * @author RogerLo
 * @date 2024/12/29
 */
@DependsOn(value = { "topic1", "topic2" })
@Service
@Getter
public class MessageConsumerService {

    // 消費的 Partition
    private final Map<String, Set<String>> consumedPartitions = new ConcurrentHashMap<>();

    // 消費的記錄: Topic1
    private final List<UserVO> consumedRecordsTopic1 = Collections.synchronizedList(new ArrayList<>());

    // 消費的記錄: Topic2
    private final List<String> consumedRecordsTopic2 = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = MY_TOPIC_1_ROGER, groupId = GROUP_A /* 讀取 Topic1 */, containerFactory = "kafkaConsumer_Topic1_ContainerFactory")
    public void consumer_to_Topic1(ConsumerRecord<?, ?> consumerRecord, UserVO userVO /* 配置 kafkaConsumer_Topic1_ContainerFactory, 故可自動反序列化資料為 UserVO */) {
        this.trackConsumedPartitions("listener-consumer-topic1", consumerRecord);
        System.out.println(">>> userVO = " + userVO);
        this.consumedRecordsTopic1.add(userVO);
    }

    @KafkaListener(topics = MY_TOPIC_2_JUMI, groupId = GROUP_B /* 讀取 Topic2 */)
    public void consumer_to_Topic2(ConsumerRecord<?, ?> consumerRecord) {
        this.trackConsumedPartitions("listener-consumer-topic2", consumerRecord);
        // this.consumedRecords.add((UserVO) consumerRecord.value());
        this.consumedRecordsTopic2.add(consumerRecord.value().toString());
    }

    /**
     * 追蹤消費的 Partition，記錄到 Map 中
     *
     * @param key 消費者的 partition key
     * @param record 消費的記錄
     */
    private void trackConsumedPartitions(String key, ConsumerRecord<?, ?> record) {
        consumedPartitions.computeIfAbsent(key, k -> new HashSet<>());
        consumedPartitions.computeIfPresent(key, (kk, vv) -> {
            vv.add("資料由 Partition " + record.partition() + " 讀取");
            System.out.println("kk = " + kk + ", vv = " + vv);
            return vv;
        });
    }

}