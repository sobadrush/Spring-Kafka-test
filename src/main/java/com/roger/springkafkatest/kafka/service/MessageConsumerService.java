package com.roger.springkafkatest.kafka.service;

import com.google.gson.Gson;
import com.roger.springkafkatest.kafka.KafkaProducerConfiguration;
import com.roger.springkafkatest.kafka.entities.UserVO;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author RogerLo
 * @date 2024/12/29
 */
@Service
@Getter
public class MessageConsumerService {

    // 消費的 Partition
    private Map<String, Set<String>> consumedPartitions = new ConcurrentHashMap<>();

    // 消費的記錄: value
    private List<UserVO> consumedRecords = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    private Gson gson;

    @KafkaListener(topics = KafkaProducerConfiguration.MY_TOPIC_1, groupId = "group-1")
    public void consumer0(ConsumerRecord<?, ?> consumerRecord) {
        this.trackConsumedPartitions("consumer-0", consumerRecord);
        this.consumedRecords.add(gson.fromJson((String) consumerRecord.value(), UserVO.class));
    }

    @KafkaListener(topics = KafkaProducerConfiguration.MY_TOPIC_1, groupId = "group-1")
    public void consumer1(ConsumerRecord<?, ?> consumerRecord) {
        this.trackConsumedPartitions("consumer-1", consumerRecord);
        this.consumedRecords.add((UserVO) consumerRecord.value());
        this.consumedRecords.add(gson.fromJson((String) consumerRecord.value(), UserVO.class));
    }

    /**
     * 追蹤消費的 Partition，記錄到 Map 中
     *
     * @param key 消費者的 partition key
     * @param record 消費的記錄
     */
    private void trackConsumedPartitions(String key, ConsumerRecord<?, ?> record) {
        consumedPartitions.computeIfAbsent(key, k -> new HashSet<>());
        consumedPartitions.computeIfPresent(key, (k, v) -> {
            v.add("資料由 Partition " + record.partition() + " 讀取");
            return v;
        });
    }

}
