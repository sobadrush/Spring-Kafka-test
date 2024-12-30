# Spring Kafka Test Project

## 專案簡介
這是一個使用 Spring Boot 和 Apache Kafka 的範例專案，展示了如何建立 Kafka 生產者和消費者。

## How to Run?
1. 啟動 Kafka (參考 Notion: 【Apache Kafka 安裝 & 運行】)
2. 可搭配 Notion:【Apache Kafka UI 管理工具】測試
3. 啟動 Spring Boot 專案
4. 使用 test-kafka.http: [測試 API: 生產者] 發送訊息
5. 使用 test-kafka.http: [測試 API: 消費者] 接收訊息

## Branches
- `main`: 基本的 Kafka 生產者和消費者(使用 @KafkaListener)
- `feature/kafka_直接寫consumer測試`: 各種 unit test

## 參考資料
| # |                   說明                    |                          URL                          |
|:-:|:---------------------------------------:|:-----------------------------------------------------:|
| 1 |    [昕力] Spring 整合 Apache Kafka 處理事件流    |  https://www.tpisoftware.com/tpu/articleDetails/2518  |
| 2 | [Baeldung] Manage Kafka Consumer Groups | https://www.baeldung.com/kafka-manage-consumer-groups |