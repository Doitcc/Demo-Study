package com.zhengqing.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import java.util.Properties;

/**
 * <p> 生产者发送消息 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/7/4 15:48
 */
public class KafkaProducerTest_03_sync {

    @Test
    public void test() throws Exception {
        this.updateLogLevel();
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息：bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        // 4. 调用 send 方法,发送消息
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("my-topic", "zhengqingya: " + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        // 没有异常,输出信息到控制台
                        System.out.println("主题：" + recordMetadata.topic() + "->" + "分区：" + recordMetadata.partition());
                    } else {
                        // 出现异常打印
                        e.printStackTrace();
                    }
                }
            });
        }

        // 同步发送
        kafkaProducer.send(new ProducerRecord<>("my-topic", "zhengqingya: sync")).get();

        // 5. 关闭资源
        kafkaProducer.close();
    }

    private void updateLogLevel() {
        LoggingSystem loggingSystem = LoggingSystem.get(LoggingSystem.class.getClassLoader());
        loggingSystem.setLogLevel("org.apache.kafka", LogLevel.WARN);
    }

}
