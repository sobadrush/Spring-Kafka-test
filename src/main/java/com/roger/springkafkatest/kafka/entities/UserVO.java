package com.roger.springkafkatest.kafka.entities;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author RogerLo
 * @date 2024/12/29
 */
@Data
@Builder
@Accessors(chain = true)
public class UserVO {
    private String userId;
    private String dept;
}
