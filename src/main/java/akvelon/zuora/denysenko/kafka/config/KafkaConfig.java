package akvelon.zuora.denysenko.kafka.config;

import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

/**
 * Kafka standard config.
 *
 * @author Denysenko Stanislav
 */
@Configuration
public class KafkaConfig {

    private final KafkaProperties properties;

    @Autowired
    public KafkaConfig(KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    ProducerFactory<String, AbstractKafkaEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
    }

    @Bean
    KafkaTemplate<String, AbstractKafkaEvent> kafkaTemplate(ProducerFactory<String, AbstractKafkaEvent> factory) {
        return new KafkaTemplate<>(factory);
    }


    @Bean
    public ConsumerFactory<String, AbstractKafkaEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
    }

    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, AbstractKafkaEvent> kafkaListenerContainerFactory(ConsumerFactory<String, AbstractKafkaEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, AbstractKafkaEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}

